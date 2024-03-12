package com.xclhove.xnote.controller;

import com.xclhove.xnote.Interceptor.UserJwtInterceptor;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.entity.dto.ImagePageDTO;
import com.xclhove.xnote.entity.table.Image;
import com.xclhove.xnote.service.ImageService;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.ThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 图片相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/image")
@Api(tags = "图片相关接口")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    
    @GetMapping("downloadByName/{imageName}")
    @ApiOperation(value = "下载图片")
    public void downloadByName(HttpServletResponse response, @PathVariable String imageName) {
        imageService.downloadByName(response, imageName);
    }
    
    @GetMapping("downloadById/{imageId}")
    @ApiOperation(value = "下载图片")
    @UserJwtInterceptor.UserJwtIntercept()
    public void downloadById(HttpServletResponse response,
                             @PathVariable
                             @Pattern(regexp = "^\\d+$", message = "图片id必须为数字")
                             Integer imageId) {
        Integer userId = (Integer) ThreadLocalUtil.get(TreadLocalKey.ID);
        imageService.downloadById(response, userId, imageId);
    }
    
    @PutMapping
    @ApiOperation(value = "上传图片")
    @UserJwtInterceptor.UserJwtIntercept
    public Result<Image> upload(MultipartFile uploadImage) {
        Integer id = (Integer) ThreadLocalUtil.get("id");
        Image image = imageService.upload(id, uploadImage);
        return Result.success(image);
    }
    
    @PostMapping("/deleteByIds")
    @ApiOperation(value = "通过id删除图片")
    @UserJwtInterceptor.UserJwtIntercept
    public Result<Object> deleteByIds(@RequestBody List<Integer> ids) {
        Integer userId = (Integer) ThreadLocalUtil.get(TreadLocalKey.ID);
        imageService.deleteByIds(userId, ids);
        return Result.success();
    }
    
    @PostMapping
    @ApiOperation(value = "修改图片")
    @UserJwtInterceptor.UserJwtIntercept
    public Result<Object> change(@RequestBody Image image) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        image.setUserId(userId);
        imageService.change(image);
        return Result.success();
    }
    
    @GetMapping("/{imageId}")
    @ApiOperation(value = "获取图片信息")
    @UserJwtInterceptor.UserJwtIntercept
    public Result<Image> get(@PathVariable
                             @Pattern(regexp = "^\\d+$", message = "图片id必须为数字")
                             Integer imageId) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        Image image = imageService.get(userId, imageId);
        return Result.success(image);
    }
    
    @PostMapping("/page")
    @ApiOperation(value = "分页获取图片")
    @UserJwtInterceptor.UserJwtIntercept
    public Result<ImagePageDTO> page(@RequestBody ImagePageDTO pageDTO) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        pageDTO.setUserId(userId);
        ImagePageDTO imagePageDTO = imageService.page(pageDTO);
        return Result.success(imagePageDTO);
    }
}