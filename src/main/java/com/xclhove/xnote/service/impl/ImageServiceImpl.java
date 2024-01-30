package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.dto.ImagePageDTO;
import com.xclhove.xnote.entity.table.Image;
import com.xclhove.xnote.exception.ImageServiceException;
import com.xclhove.xnote.exception.NoteServiceException;
import com.xclhove.xnote.mapper.ImageMapper;
import com.xclhove.xnote.service.ImageService;
import com.xclhove.xnote.tool.MinioTool;
import com.xclhove.xnote.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 图片 服务实现类
 * </p>
 *
 * @author xclhove
 * @since 2023-12-09
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    /** 允许上传的文件格式 */
    @Value("${xnote.image.allow-type}")
    private String allowType;
    
    /** 允许上传的文件大小，默认10MB */
    @Value("${xnote.image.allow-size:10485670}")
    private Long allowSize;
    
    private final MinioTool minio;
    
    @Override
    public Image upload(Integer userId, MultipartFile imageFile) {
        if (imageFile.isEmpty() || imageFile.getSize() == 0) {
            throw new NoteServiceException("不允许上传空文件");
        }
        String contentType = imageFile.getContentType();
        if (!contentType.matches(allowType)) {
            throw new NoteServiceException("不允许上传该格式的图片！");
        }
        if (imageFile.getSize() > allowSize) {
            throw new NoteServiceException("上传的文件大小不能超过" + allowSize + "字节！");
        }
        String fileName = null;
        try {
            fileName = minio.upload(imageFile);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("图片文件上传失败！");
        }
        Image image = new Image();
        image.setUserId(userId);
        image.setAlias(imageFile.getOriginalFilename());
        image.setName(fileName);
        boolean save = false;
        try {
            save = this.save(image);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException(" 出现异常，图片文件上传成功，数据库保存失败！");
        }
        if (!save) {
            throw new ImageServiceException("图片文件上传成功，数据库保存失败！");
        }
        return image;
    }
    
    @Override
    public boolean deleteByIds(Integer userId, List<Integer> imageIds) {
        LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, Image::getUserId, userId)
                .and(qw -> qw.in(Image::getId, imageIds));
        List<Image> images = this.list(queryWrapper);
        for (Image image : images) {
            delete(image);
        }
        return true;
    }
    
    @Override
    public boolean delete(Image image) {
        boolean deleteSuccess = false;
        if (StrUtil.isBlank(image.getName())) throw new ImageServiceException("图片名不能为空！");
        try {
            deleteSuccess = minio.deleteFileByFileName(image.getName());
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("出现异常，删除图片文件失败！");
        }
        if (!deleteSuccess) throw new ImageServiceException("图片删除失败！");
        try {
            deleteSuccess = this.removeById(image.getId());
        } catch (Exception e) {
            throw new ImageServiceException("出现异常，图片删除失败！");
        }
        if (!deleteSuccess) throw new ImageServiceException("图片删除失败！");
        return true;
    }
    
    @Override
    public Image get(Integer userId, Integer imageId) {
        LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, Image::getUserId, userId)
                .and(qw -> qw.eq(Image::getId, imageId));
        try {
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ImageServiceException("出现异常，获取图片信息失败！");
        }
    }
    
    @Override
    public ImagePageDTO page(ImagePageDTO pageDTO) {
        Page<Image> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(pageDTO.getUserId() != null, Image::getUserId, pageDTO.getUserId())
                .like(pageDTO.getSearchAlias() != null, Image::getAlias, pageDTO.getSearchAlias())
                .orderByDesc(Image::getLastDownloadTime);
        try {
            List<Image> images = this.page(page, queryWrapper).getRecords();
            pageDTO.setList(images);
            return pageDTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ImageServiceException("出现异常，分页查询失败！");
        }
    }
    
    @Override
    public boolean change(Image image) {
        LambdaUpdateWrapper<Image> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Image::getId, image.getId())
                .eq(image.getUserId() != null, Image::getUserId, image.getUserId())
                .set(image.getAlias() != null, Image::getAlias, image.getAlias());
        boolean updateSuccess = false;
        try {
            updateSuccess = this.updateById(image);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ImageServiceException("出现异常，修改图片信息失败！");
        }
        if (!updateSuccess) throw new ImageServiceException("修改图片信息失败！");
        return true;
    }
    
    @Override
    public void downloadById(HttpServletResponse response, Integer imageId) {
        Image image = null;
        try {
            image = this.getById(imageId);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("出现异常，获取图片信息失败！");
        }
        download(response, image);
    }
    
    @Override
    public void downloadByName(HttpServletResponse response, String imageName) {
        Image image = null;
        try {
            LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Image::getName, imageName);
            image = this.getOne(queryWrapper);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("出现异常，获取图片信息失败！");
        }
        download(response, image);
    }
    
    @Override
    public void download(HttpServletResponse response, Image image) {
        if (image == null) {
            throw new ImageServiceException("图片不存在或已被删除！");
        }
        
        //更新图片下载时间
        image.setLastDownloadTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            this.updateById(image);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("更新图片下载时间失败！");
        }
        
        byte[] bytes = new byte[0];
        try {
            bytes = minio.downloadFile(image.getName());
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("出现异常，获取图片文件失败！");
        }
        
        // 设置字符编码
        response.setCharacterEncoding("utf-8");
        
        // 设置强制下载不打开
        //servletResponse.setContentType("application/force-download");
        
        // 设置文件名称
        String imageFileName = image.getAlias();
        imageFileName = imageFileName.replaceAll("\\s+", "_");
        // 进行URL编码，防止乱码
        try {
            imageFileName = URLEncoder.encode(imageFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("出现异常，编码图片文件名失败！");
        }
        response.addHeader("Content-Disposition", "attachment;fileName=" + imageFileName);
        response.addHeader("file-name", imageFileName);
        response.setHeader("Access-Control-Expose-Headers", "file-name");
        
        // 传输文件
        try {
            ServletOutputStream stream = response.getOutputStream();
            stream.write(bytes);
            stream.flush();
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new ImageServiceException("出现异常，图片传输失败！");
        }
    }
}
