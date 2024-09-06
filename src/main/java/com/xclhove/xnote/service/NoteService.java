package com.xclhove.xnote.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.EsConstant;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.NoteServiceException;
import com.xclhove.xnote.mapper.NoteMapper;
import com.xclhove.xnote.pojo.enums.NoteIsPublic;
import com.xclhove.xnote.pojo.es.NoteDoc;
import com.xclhove.xnote.pojo.form.note.NoteAddForm;
import com.xclhove.xnote.pojo.form.note.NoteUpdateForm;
import com.xclhove.xnote.pojo.form.note.NoteUpdateTypeForm;
import com.xclhove.xnote.pojo.table.Note;
import com.xclhove.xnote.pojo.table.NoteType;
import com.xclhove.xnote.pojo.table.ShareNoteRecord;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.vo.PageVO;
import com.xclhove.xnote.repository.NoteRepository;
import com.xclhove.xnote.tool.ElasticsearchTool;
import com.xclhove.xnote.tool.RedisTool;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xclhove
 */
@Service
@RequiredArgsConstructor
public class NoteService extends ServiceImpl<NoteMapper, Note> {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTool redisTool;
    private final XnoteConfig xnoteConfig;
    private final NoteRepository noteRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    
    @Resource
    private NoteTypeService noteTypeService;
    @Resource
    private ShareNoteRecordService shareNoteRecordService;
    
    public Note getWithRedis(String redisKey, RedisTool.Getter<Note> getter) {
        return redisTool.getUseStringAntiCachePassThrough(
                redisKey,
                getter,
                JSON::toJSONString,
                jsonString -> JSON.parseObject(jsonString, Note.class),
                30,
                TimeUnit.MINUTES
        );
    }
    
    /**
     * redisKey
     */
    public String getRedisKeyById(int id) {
        return RedisKey.join(RedisKey.Note.PREFIX, String.valueOf(id));
    }
    
    /**
     * （使用redis做缓存，有防缓存穿透）
     */
    public Note getByIdWithRedis(int id) {
        return getWithRedis(getRedisKeyById(id), () -> getById(id));
    }
    
    public int add(User noteOwner, NoteAddForm noteAddForm) {
        NoteType noteType = noteTypeService.getByIdWithRedis(noteAddForm.getTypeId());
        if (noteType == null || !Objects.equals(noteType.getUserId(), noteOwner.getId())) {
            noteAddForm.setTypeId(null);
        }
        
        Note note = BeanUtil.copyProperties(noteAddForm, Note.class);
        note.setUserId(noteOwner.getId());
        
        boolean saveSuccess = this.save(note);
        if (!saveSuccess) {
            throw new NoteServiceException();
        }
        
        noteRepository.save(note.toNoteDoc());
        
        return note.getId();
    }
    
    public Note previewNote(@Nullable User user, int targetNoteId, @Nullable String shareCode) {
        Note note = getByIdWithRedis(targetNoteId);
        if (note == null) {
            throw new NoteServiceException("笔记不存在");
        }
        if (note.getIsPublic() == NoteIsPublic.YES) {
            return note;
        }
        if (user != null && user.getId().equals(note.getUserId())) {
            return note;
        }
        if (StrUtil.isBlank(shareCode)) {
            throw new NoteServiceException("权限不足，无法查看");
        }
        ShareNoteRecord shareNoteRecord = shareNoteRecordService.getByCodeWithRedis(shareCode);
        if (shareNoteRecord == null || !Objects.equals(shareNoteRecord.getNoteId(), note.getId())) {
            throw new NoteServiceException("分享码错误或已失效");
        }
        if (shareNoteRecord.isExpired()) {
            shareNoteRecordService.deleteById(shareNoteRecord);
            throw new NoteServiceException("分享码已失效");
        }
        
        return note;
    }
    
    public PageVO<Note> search(Page<Note> page,
                               @Nullable User user,
                               @Nullable String search,
                               @Nullable String heightLightPreTag,
                               @Nullable String heightLightPostTag
    ) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StrUtil.isNotBlank(search)) {
            boolQuery.must(QueryBuilders.matchQuery(EsConstant.NOTE_QUERY_FIELD, search));
        }
        
        BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
        nestedBoolQuery.should(QueryBuilders.termQuery("isPublic", "YES"));
        if (user != null) {
            nestedBoolQuery.should(QueryBuilders.termQuery("userId", user.getId()));
        }
        boolQuery.must(nestedBoolQuery);
        
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        if (StrUtil.isNotBlank(heightLightPreTag) && StrUtil.isNotBlank(heightLightPostTag)) {
            queryBuilder.withHighlightBuilder(new HighlightBuilder()
                    .field(EsConstant.NOTE_QUERY_FIELD)
                    .preTags(heightLightPreTag)
                    .postTags(heightLightPostTag)
            );
        }
        queryBuilder.withQuery(boolQuery);
        queryBuilder.withPageable(PageRequest.of(Math.toIntExact(page.getCurrent()) - 1, Math.toIntExact(page.getSize())));
        
        NativeSearchQuery searchQuery = queryBuilder.build();
                
                SearchHits<NoteDoc> searchHits = elasticsearchOperations.search(searchQuery, NoteDoc.class);
        
        List<Note> notes = searchHits.getSearchHits()
                .stream()
                .map(searchHit -> {
                    NoteDoc noteDoc = searchHit.getContent();
                    List<String> heightList = searchHit.getHighlightFields().get(EsConstant.NOTE_QUERY_FIELD);
                    final int maxLength = xnoteConfig.search.getHeightLightContentMaxLength();
                    if (heightList == null || heightList.isEmpty()) {
                        Note note = noteDoc.toNote();
                        String content = note.getContent();
                        return note.setContent(content.substring(0, Math.min(content.length(), maxLength)));
                    }
                    
                    StrBuilder content = new StrBuilder();
                    heightList.forEach(heightString -> {
                        int length = content.length();
                        if (length > maxLength) {
                            return;
                        }
                        content.append(heightString);
                        content.append("......");
                    });
                    noteDoc.setContent(content.toString());
                    return noteDoc.toNote();
                })
                .collect(Collectors.toList());
        
        PageVO<Note> pageVO = BeanUtil.copyProperties(page, PageVO.class);
        pageVO.setRecords(notes);
        pageVO.setTotal(searchHits.getTotalHits());
        return pageVO;
    }
    
    public void update(User noteOwner, NoteUpdateForm noteUpdateForm) {
        Note note = getByIdWithRedis(noteUpdateForm.getId());
        if (note == null) {
            throw new NoteServiceException("笔记不存在");
        }
        if(!Objects.equals(note.getUserId(), noteOwner.getId())) {
            throw new NoteServiceException("权限不足，无法修改");
        }
        
        
        Integer typeId = noteUpdateForm.getTypeId();
        NoteType noteType = null;
        if (typeId != null) {
            noteType = noteTypeService.getByIdWithRedis(typeId);
        }
        if (noteType == null || !Objects.equals(noteType.getUserId(), noteOwner.getId())) {
            noteUpdateForm.setTypeId(null);
        }
        
        BeanUtil.copyProperties(noteUpdateForm, note, CopyOptions.create().setIgnoreNullValue(true));
        note.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        boolean updateSuccess = this.updateById(note);
        if (!updateSuccess) {
            throw new NoteServiceException("系统异常，保存失败");
        }
        
        stringRedisTemplate.delete(getRedisKeyById(note.getId()));
        noteRepository.save(note.toNoteDoc());
    }
    
    public void deleteBatchByIds(User noteOwner, List<Integer> noteIds) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Note::getId);
        queryWrapper.eq(Note::getUserId, noteOwner.getId());
        queryWrapper.in(Note::getId, noteIds);
        
        List<Note> deleteNotes = this.list(queryWrapper);
        boolean removeSuccess = this.remove(queryWrapper);
        if (!removeSuccess) {
            throw new NoteServiceException("删除失败");
        }
        
        // 删除 redis 中的缓存
        Set<String> redisKeys = new HashSet<>();
        deleteNotes.forEach(note -> redisKeys.add(getRedisKeyById(note.getId())));
        stringRedisTemplate.delete(redisKeys);
        
        noteRepository.deleteAll(BeanUtil.copyToList(deleteNotes, NoteDoc.class));
    }
    
    public Page<Note> searchUserNote(Page<Note> page,
                                     @Nullable User user,
                                     @NonNull Integer targetUserId,
                                     @Nullable Integer typeId,
                                     @Nullable String search
    )  {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        boolQuery.must(QueryBuilders.termQuery("userId", targetUserId));
        if (user == null || !user.getId().equals(targetUserId)) {
            boolQuery.must(QueryBuilders.termQuery("isPublic", NoteIsPublic.YES));
        }
        
        if (typeId != null) {
            boolQuery.must(QueryBuilders.termQuery("typeId", typeId));
        }
        if (StrUtil.isNotBlank(search)) {
            boolQuery.must(QueryBuilders.matchQuery(EsConstant.NOTE_QUERY_FIELD, search));
        }
        
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(Math.toIntExact(page.getCurrent()) - 1, Math.toIntExact(page.getSize())))
                .build();
        
        SearchHits<NoteDoc> searchHits = elasticsearchOperations.search(searchQuery, NoteDoc.class);
        
        List<Note> notes = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .map(NoteDoc::toNote)
                .collect(Collectors.toList());
        
        page.setRecords(notes);
        page.setTotal(searchHits.getTotalHits());
        return page;
    }
    
    public void updateBatchUserNoteType(User noteOwner, NoteUpdateTypeForm noteUpdateTypeForm) {
        NoteType noteType = noteTypeService.getByIdWithRedis(noteUpdateTypeForm.getTypeId());
        if (noteType == null || !Objects.equals(noteType.getUserId(), noteOwner.getId())) {
            throw new NoteServiceException("类型不存在");
        }
        
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, noteOwner.getId());
        queryWrapper.in(Note::getId, noteUpdateTypeForm.getNoteIds());
        List<Note> updateNotes = this.list(queryWrapper);
        
        updateNotes.forEach(note -> note.setTypeId(noteType.getId()));
        updateBatchByIdWithRedisAndEs(updateNotes);
    }
    
    public void updateBatchByIdWithRedisAndEs(Collection<Note> updateNotes) {
        boolean updateSuccess = this.updateBatchById(updateNotes);
        if (!updateSuccess) {
            throw new NoteServiceException("系统异常，修改失败");
        }
        
        // 删除 redis 中的缓存
        Set<String> redisKeys = new HashSet<>();
        updateNotes.forEach(note -> redisKeys.add(getRedisKeyById(note.getId())));
        stringRedisTemplate.delete(redisKeys);
        
        noteRepository.saveAll(BeanUtil.copyToList(updateNotes, NoteDoc.class));
    }
}
