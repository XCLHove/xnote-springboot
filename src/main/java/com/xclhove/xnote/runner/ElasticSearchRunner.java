package com.xclhove.xnote.runner;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.pojo.es.NoteDoc;
import com.xclhove.xnote.pojo.table.Note;
import com.xclhove.xnote.repository.NoteRepository;
import com.xclhove.xnote.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动时检查能否发送邮件
 *
 * @author xclhove
 */
@Component
@Slf4j
@RequiredArgsConstructor
public final class ElasticSearchRunner implements ApplicationRunner {
    private final NoteRepository noteRepository;
    private final NoteService noteService;
    private final XnoteConfig xnoteConfig;
    
    @Override
    public void run(ApplicationArguments args) {
        if (!xnoteConfig.runner.getImportDataFromDatabaseToElasticSearch()) {
            log.info("已关闭启动时导入数据到 ElasticSearch 的功能");
            return;
        }
        log.info("开始导入数据到 ElasticSearch...");
        importDataFromDatabaseToElasticSearch();
        log.info("导入数据到 ElasticSearch 成功！");
    }
    
    private void importDataFromDatabaseToElasticSearch() {
        noteRepository.deleteAll();
        
        int current = 1;
        long pageSize = 1000L;
        long total = 0;
        
        do {
            Page<Note> page = noteService.page(new Page<>(current, pageSize));
            List<NoteDoc> noteDocs = BeanUtil.copyToList(page.getRecords(), NoteDoc.class);
            //noteDocs.forEach(NoteDoc::generateSuggestionField);
            noteRepository.saveAll(noteDocs);
            
            total = page.getTotal();
            current++;
        } while ((current - 1) * pageSize < total);
    }
}
