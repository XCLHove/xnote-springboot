package com.xclhove.xnote.tool;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.lang.UUID;
import com.xclhove.xnote.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Minio工具类
 *
 * @author xclhove
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioTool {
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;
    
    /**
     * 查看存储bucket是否存在
     *
     * @return 是否存在
     */
    public boolean bucketExist(String bucketName) throws Exception {
        boolean bucketExist = false;
        bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        return bucketExist;
    }
    
    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public boolean creatBucket(String bucketName) throws Exception {
        
        minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
        return bucketExist(bucketName);
        
    }
    
    /**
     * 删除存储bucket
     *
     * @return 是否删除成功
     */
    public boolean deleteBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
        return !bucketExist(bucketName);
    }
    
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件名
     */
    public String upload(MultipartFile file) throws Exception {
        String bucketName = minioConfig.getBucketName();
        if (!bucketExist(bucketName)) creatBucket(bucketName);
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String fileNameInBucket = uuid + fileSuffix;
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileNameInBucket)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        //文件名称相同会覆盖
        minioClient.putObject(objectArgs);
        return fileNameInBucket;
    }
    
    /**
     * 预览文件
     *
     * @param fileName 文件名
     * @return 文件预览地址
     */
    public String preview(String fileName) throws Exception {
        
        new GetPresignedObjectUrlArgs();
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs
                .builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
        String fileUrl = minioClient.getPresignedObjectUrl(build);
        return fileUrl;
    }
    
    /**
     * 文件下载
     *
     * @param fileName        文件名称
     * @param originName      文件原名称
     * @param servletResponse 响应
     */
    public void downloadFile(String fileName, String originName, HttpServletResponse servletResponse) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(minioConfig.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse objectResponse = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = objectResponse.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                servletResponse.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                //servletResponse.setContentType("application/force-download");
                String documentFileName = originName.substring(0, originName.lastIndexOf("."));
                String documentFileType = fileName.substring(fileName.lastIndexOf("."));
                String finalFileName = documentFileName + documentFileType;
                finalFileName = finalFileName.replaceAll("\\s+", "_");
                finalFileName = URLEncoder.encode(finalFileName, "UTF-8");
                servletResponse.addHeader("Content-Disposition", "attachment;fileName=" + finalFileName);
                servletResponse.addHeader("file-name", finalFileName);
                servletResponse.setHeader("Access-Control-Expose-Headers", "file-name");
                try (ServletOutputStream stream = servletResponse.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        }
    }
    
    /**
     * 文件下载
     *
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public byte[] downloadFile(String fileName) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(minioConfig.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse objectResponse = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = objectResponse.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                return bytes;
            }
        }
    }
    
    /**
     * 列出所有文件对象
     *
     * @return 文件对象列表
     */
    public List<Item> listFiles() throws Exception {
        Iterable<io.minio.Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build()
        );
        List<Item> items = new ArrayList<>();
        
        for (Result<Item> result : results) {
            items.add(result.get());
        }
        
        return items;
    }
    
    /**
     * 通过文件名获取文件对象
     *
     * @param fileName 文件名
     * @return 文件对象
     */
    public StatObjectResponse getFile(String fileName) throws Exception {
        
        StatObjectResponse file = minioClient.statObject(
                StatObjectArgs
                        .builder()
                        .bucket(minioConfig.getBucketName()).object(fileName)
                        .build()
        );
        return file;
        
    }
    
    /**
     * 通过文件路径获取文件对象
     *
     * @param filePath 文件路径
     * @return 文件对象
     */
    public StatObjectResponse getFileByFilePath(String filePath) throws Exception {
        
        String fileName = filePath.substring(filePath.lastIndexOf("/"));
        StatObjectResponse file = getFile(fileName);
        return file;
        
    }
    
    /**
     * 通过文件名删除文件
     *
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public boolean deleteFileByFileName(String fileName) throws Exception {
        
        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(minioConfig.getBucketName()).object(fileName)
                        .build()
        );
        return getFile(fileName) == null;
        
    }
}
