package com.xclhove.xnote.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.pojo.table.Note;
import com.xclhove.xnote.pojo.table.NoteType;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.service.NoteService;
import com.xclhove.xnote.service.NoteTypeService;
import com.xclhove.xnote.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xclhove
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Order
public final class NoteRunner implements ApplicationRunner {
    private final XnoteConfig xnoteConfig;
    private final NoteTypeService noteTypeService;
    private final UserService userService;
    private final NoteService noteService;
    
    @Override
    public void run(ApplicationArguments args) {
        if (!xnoteConfig.runner.getCreateDefaultNoteTypeForUserOfNoneType()) {
            log.info("已关闭启动时为用户创建默认笔记类型");
        } else {
            log.info("开始为用户创建默认笔记类型...");
            createDefaultNoteTypeForUserOfNoneType();
            log.info("为用户创建默认笔记类型完成！");
        }
        
        if (!xnoteConfig.runner.getReplaceImageUrlInNote()) {
            log.info("已关闭启动时替换笔记中的图片url");
        } else {
            log.info("开始替换笔记中的图片url...");
            replaceImageUrlInNote();
            log.info("替换笔记中的图片 url 完成！");
        }
    }
    
    private void createDefaultNoteTypeForUserOfNoneType() {
        long total = 0;
        long current = 1;
        long pageSize = 1000L;
        
        LambdaQueryWrapper<NoteType> noteTypeQueryWrapper = new LambdaQueryWrapper<>();
        noteTypeQueryWrapper.select(NoteType::getUserId);
        Set<Integer> userOfHasNoteTypeIds = new HashSet<>();
        noteTypeService.page(new Page<>(current, pageSize), noteTypeQueryWrapper)
                .getRecords()
                .forEach(noteType -> userOfHasNoteTypeIds.add(noteType.getUserId()));
        
        do {
            Set<Integer> userOfNoneNoteTypeIds = new HashSet<>();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.select(User::getId);
            userLambdaQueryWrapper.notIn(!userOfHasNoteTypeIds.isEmpty(), User::getId, userOfHasNoteTypeIds);
            Page<User> page = userService.page(new Page<>(current, pageSize), userLambdaQueryWrapper);
            page.getRecords().forEach(user -> userOfNoneNoteTypeIds.add(user.getId()));
            userOfNoneNoteTypeIds.forEach(noteTypeService::createDefaultAndSetToNoteOfNoneType);
            total = page.getTotal();
            current++;
        } while ((current - 1) * pageSize < total);
    }
    
    private void replaceImageUrlInNote() {
        long total = 0;
        long current = 1;
        long pageSize = 1000L;
        
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        do {
            Page<Note> page = noteService.page(new Page<>(current, pageSize), queryWrapper);
            page.getRecords().forEach(note -> note.setContent(note.getContent()
                    .replaceAll(
                            "https://api.xclhove.top/xnote/image/downloadByName",
                            "/api/image/name"
                    )
            ));
            noteService.updateBatchByIdWithRedisAndEs(page.getRecords());
            
            total = page.getTotal();
            current++;
        } while ((current - 1) * pageSize < total);
    }
}
