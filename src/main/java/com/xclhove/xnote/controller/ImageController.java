package com.xclhove.xnote.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.exception.ImageServiceException;
import com.xclhove.xnote.interceptor.UserTokenInterceptor;
import com.xclhove.xnote.interceptor.annotations.UserTokenIntercept;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.vo.PageVO;
import com.xclhove.xnote.pojo.vo.SearchUserImageVO;
import com.xclhove.xnote.resolver.annotations.UserInfoFormToken;
import com.xclhove.xnote.service.ImageService;
import com.xclhove.xnote.service.UserImageService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 图片相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final UserImageService userImageService;
    
    /**
     * 上传图片
     */
    @PostMapping
    @UserTokenIntercept()
    public Result<String> upload(
            @NotNull(message = "图片不能为空") MultipartFile uploadImageFile,
            @UserInfoFormToken User user
    ) {
        String imageName = imageService.upload(user, uploadImageFile);
        return Result.success(imageName);
    }
    
    /**
     * 预览图片
     */
    @GetMapping("/name/{imageName}")
    public void previewImage(HttpServletResponse response, @PathVariable String imageName) {
        String imageUrl = imageService.getImageUrlByNameWithRedis(imageName);
        if (imageUrl == null) {
            throw new ImageServiceException("图片不存在");
        }
        try {
            response.setStatus(302);
            response.sendRedirect(imageUrl);
        } catch (IOException e) {
            log.error("重定向失败", e);
            throw new ImageServiceException("重定向失败");
        }
    }
    
    /**
     * 搜索自己的图片
     */
    @GetMapping("me")
    @UserTokenIntercept()
    public Result<PageVO<SearchUserImageVO>> searchSelfImage(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(required = false) String search,
            @UserInfoFormToken User user
    ) {
        Page<SearchUserImageVO> pageResult = userImageService.searchUserImage(new Page<>(page, size), user, search);
        PageVO<SearchUserImageVO> pageVO = BeanUtil.copyProperties(pageResult, PageVO.class);
        return Result.success(pageVO);
    }
    
    /**
     * 删除图片
     */
    @DeleteMapping
    @UserTokenIntercept
    public Result<?> delete(
            @NotEmpty(message = "用户图片ID不能为空")
            @RequestParam List<Integer> userImageIds,
            @UserInfoFormToken User user
    ) {
        userImageService.removeUserImageByIds(user, userImageIds);
        return Result.success();
    }
}
