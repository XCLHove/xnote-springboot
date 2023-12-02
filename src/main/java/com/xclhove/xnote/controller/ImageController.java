package com.xclhove.xnote.controller;

import com.xclhove.xnote.exception.NoteServiceException;
import com.xclhove.xnote.tool.MinioTool;
import com.xclhove.xnote.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
    @Value("${xnote.image.allow-type}")
    private String allowType; //允许上传的文件格式
    @Value("${xnote.image.allow-size}")
    private Long allowSize; //允许上传的文件大小
    private final MinioTool minio;
    
    @GetMapping("/{imageName}")
    @ApiOperation(value = "下载图片")
    public void downloadImage(HttpServletResponse response, @PathVariable String imageName) {
        minio.downloadFile(imageName, imageName, response);
    }
    
    @PostMapping("/upload")
    @ApiOperation(value = "上传图片")
    public Result<String> upload(MultipartFile uploadImage) {
        if (uploadImage == null) throw new NoteServiceException("不允许上传空文件");
        String contentType = uploadImage.getContentType();
        if (!contentType.matches(allowType)) throw new NoteServiceException("不允许上传该格式的图片！");
        if (uploadImage.getSize() > allowSize) throw new NoteServiceException("上传的文件大小不能超过" + allowSize + "字节！");
        String fileName = minio.upload(uploadImage);
        return Result.success(fileName);
    }
}