package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.dto.Account;
import com.example.mapper.AccountMapper;
import com.example.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @program: forum
 * @description: 文件上传相关接口实现类
 * @author: 王贝强
 * @create: 2024-06-10 12:42
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${minio.bucket-name}")
    String bucketName;
    @Resource
    MinioClient minioClient;
    @Resource
    AccountMapper accountMapper;
    @Override
    public String uploadAvatar(MultipartFile file, Integer userId) throws IOException {
        String imageName= "/avatar/"+UUID.randomUUID().toString().replace("-","");
        PutObjectArgs objectArgs= PutObjectArgs.builder()
                .bucket(bucketName)
                .stream(file.getInputStream(),file.getSize(),-1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(objectArgs);
            if(accountMapper.update(null, Wrappers.<Account>update()
                    .eq("id",userId).set("avatar",imageName))>0){
                return imageName;
            }else
                return null;
        }catch (Exception e){
            log.error("图片上传失败:{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void fetchImage(OutputStream outputStream, String imagePath) throws Exception {
        GetObjectArgs args= GetObjectArgs.builder()
                .bucket(bucketName)
                .object(imagePath)
                .build();
        GetObjectResponse object = minioClient.getObject(args);
        IOUtils.copy(object,outputStream);
    }
}
