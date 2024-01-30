package com.xclhove.xnote.runner;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xclhove.xnote.entity.attribute.NoteKeyword;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Xnote启动初始化类
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class XnoteRunner implements ApplicationRunner {
    private final NoteService noteService;
    @Value("${xnote.keywords.convert-enable: false}")
    private boolean enableKeywordsConvert;
    
    @Override
    public void run(ApplicationArguments args) {
        convertKeywords();
    }
    
    private void convertKeywords() {
        if (!enableKeywordsConvert) return;
        
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
            log.info("无需转化！");
            return;
        }
    }
}
