package com.xclhove.xnote.tool;

import cn.hutool.core.io.FastByteArrayOutputStream;
import com.xclhove.xnote.config.MinioConfig;
import io.minio.Result;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
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
     */
    public boolean bucketExist() throws Exception {
        boolean bucketExist = false;
        String bucketName = minioConfig.getBucketName();
        BucketExistsArgs args = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        bucketExist = minioClient.bucketExists(args);
        return bucketExist;
    }
    
    /**
     * 创建bucket
     */
    public boolean createBucket() throws Exception {
        String bucketName = minioConfig.getBucketName();
        
        MakeBucketArgs args = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.makeBucket(args);
        return bucketExist();
    }
    
    /**
     * 删除bucket
     */
    public boolean removeBucket() throws Exception {
        String bucketName = minioConfig.getBucketName();
        
        RemoveBucketArgs args = RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.removeBucket(args);
        return !bucketExist();
    }
    
    /**
     * 文件上传
     */
    public void upload(MultipartFile file, String fileNameInBucket) throws Exception {
        if (!bucketExist()) {
            createBucket();
        }
        if (getFile(fileNameInBucket) != null) {
            return;
        }
        
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileNameInBucket)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        //文件名称相同会覆盖
        minioClient.putObject(args);
    }
    
    public String getFileUrl(String fileName) {
        return String.join("/", minioConfig.getRemoteEndpoint(), minioConfig.getBucketName(), fileName);
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
            byte[] buffer = new byte[1024];
            int length;
            try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {
                while ((length = objectResponse.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                byte[] bytes = outputStream.toByteArray();
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
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build();
        Iterable<io.minio.Result<Item>> results = minioClient.listObjects(args);
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
        try {
            StatObjectArgs args = StatObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .build();
            StatObjectResponse file = minioClient.statObject(args);
            return file;
        } catch (ErrorResponseException e) {
            if (e.getMessage().contains("Object does not exist")) {
                return null;
            }
            throw e;
        }
    }
    
    /**
     * 通过文件名删除文件
     *
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public boolean deleteFile(String fileName) throws Exception {
        
        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(minioConfig.getBucketName()).object(fileName)
                        .build()
        );
        return getFile(fileName) == null;
        
    }
}
