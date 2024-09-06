package com.xclhove.xnote.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.interceptor.UserTokenInterceptor;
import com.xclhove.xnote.pojo.form.shareNoteRecord.ShareNoteRecordCreateForm;
import com.xclhove.xnote.pojo.form.shareNoteRecord.ShareNoteRecordUpdateForm;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.vo.PageVO;
import com.xclhove.xnote.pojo.vo.ShareNoteRecordVO;
import com.xclhove.xnote.service.ShareNoteRecordService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 笔记分享记录相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/share-note-record")
@RequiredArgsConstructor
public class ShareNoteRecordController {
    private final ShareNoteRecordService shareNoteRecordService;
    
    /**
     * 分享笔记
     * @return 分享码
     */
    @PostMapping
    @UserTokenInterceptor.UserTokenIntercept
    public Result<String> shareNote(@RequestBody @Validated ShareNoteRecordCreateForm shareNoteRecordCreateForm) {
        User user = ThreadLocalTool.getUser();
        String code = shareNoteRecordService.share(user, shareNoteRecordCreateForm);
        return Result.success(code);
    }
    
    /**
     * 批量删除分享的记录
     */
    @DeleteMapping
    @UserTokenInterceptor.UserTokenIntercept
    public Result<?> deleteShareNoteByIds(@RequestParam List<Integer> shareNoteRecordIds) {
        User user = ThreadLocalTool.getUser();
        shareNoteRecordService.deleteShareNotes(user, shareNoteRecordIds);
        return Result.success();
    }
    
    /**
     * 获取分享的记录
     */
    @GetMapping("me")
    @UserTokenInterceptor.UserTokenIntercept
    public Result<PageVO<ShareNoteRecordVO>> searchShareNote(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        User user = ThreadLocalTool.getUser();
        Page<ShareNoteRecordVO> pageResult = shareNoteRecordService.getShareNoteList(user, new Page<>(current, size));
        PageVO<ShareNoteRecordVO> pageVO = BeanUtil.copyProperties(pageResult, PageVO.class);
        return Result.success(pageVO);
    }
    
    /**
     * 修改笔记分享记录
     */
    @PutMapping
    public Result<?> updateShareNoteRecord(@RequestBody @Validated ShareNoteRecordUpdateForm shareNoteRecordCreateForm) {
        User user = ThreadLocalTool.getUser();
        shareNoteRecordService.update(user, shareNoteRecordCreateForm);
        return Result.success();
    }
}
