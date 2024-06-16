package com.xclhove.xnote.runner;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xclhove.xnote.entity.attribute.NoteKeyword;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Xnote启动初始化类
 *
 * @author xclhove
 */
@Component
@Slf4j
@Order
public class NoteRunner extends AbstractRunner {
    @Resource
    private NoteService noteService;
    
    public NoteRunner(ApplicationContext applicationContext) {
        super(applicationContext);
    }
    
    @Override
    public void doRun(ApplicationArguments args) {
        convertKeywords();
        replaceImageUrl();
    }
    
    /**
     * 转换关键词的存储结构
     */
    private void convertKeywords() {
        if (!runnerConfig.getEnableKeywordsConvertRunner()) {
            return;
        }
        
        try {
            log.info("关键词转化……");
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Note::getId, Note::getKeywords);
            List<Note> notes = noteService.list(queryWrapper);
            List<Note> newNotes = new ArrayList<>();
            
            for (Note note : notes) {
                List<NoteKeyword> keywords = JSON.parseArray(JSON.toJSONString(note.getKeywords()), NoteKeyword.class);
                
                List<String> newKeywords = new ArrayList<>();
                keywords.forEach(noteKeyword -> {
                    newKeywords.add(noteKeyword.getName());
                });
                
                Note newNote = new Note();
                newNote.setKeywords(newKeywords);
                newNote.setId(note.getId());
                
                newNotes.add(newNote);
                
                if (newNotes.size() == 500) {
                    noteService.updateBatchById(newNotes);
                    newNotes.clear();
                }
            }
            
            noteService.updateBatchById(newNotes);
            
            log.info("关键词转化已经完成！");
            System.exit(0);
        } catch (Exception e) {
            log.info("关键词无需转化！");
        }
    }
    
    /**
     * 替换笔记中的图片地址
     */
    private void replaceImageUrl() {
        if (!runnerConfig.getEnableImageUrlReplaceRunner()) {
            return;
        }
        
        List<Note> list = new ArrayList<>();
        noteService.list().forEach(note -> {
            note.setContent(note.getContent().replaceAll("xnote.xclhove.top/api", "api.xclhove.top/xnote"));
            list.add(note);
        });
        noteService.updateBatchById(list);
        log.info("替换图片地址成功！");
    }
}
