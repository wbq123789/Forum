package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.ImageStore;
import com.example.mapper.AccountMapper;
import com.example.mapper.ImageStoreMapper;
import com.example.service.FileService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import io.minio.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @program: forum
 * @description: 文件上传相关接口实现类
 * @author: 王贝强
 * @create: 2024-06-10 12:42
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<ImageStoreMapper , ImageStore> implements FileService {
    @Value("${minio.bucket-name}")
    String bucketName;
    @Resource
    MinioClient minioClient;
    @Resource
    AccountMapper accountMapper;
    @Resource
    FlowUtils flowUtils;
    private final SimpleDateFormat format= new SimpleDateFormat("yyyyMMdd");
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
            String Avatar=accountMapper.selectById(userId).getAvatar();
            this.deleteOldImage(Avatar);
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
    public String uploadImage(MultipartFile file, Integer userId) throws IOException {
        String key= Const.FORUM_IMAGE_COUNTER+userId;
        if(!flowUtils.limitPeriodCountCheck(key,20,3600))
            return null;
        String imageName= UUID.randomUUID().toString().replace("-","");
        Date data=new Date();
        imageName="/cache"+format.format(data)+"/"+imageName;
        PutObjectArgs objectArgs= PutObjectArgs.builder()
                .bucket(bucketName)
                .stream(file.getInputStream(),file.getSize(),-1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(objectArgs);
            if (this.save(new ImageStore(null,imageName,data)))
                return imageName;
            else {
                this.deleteOldImage(imageName);
                return null;
            }
        }catch (Exception e) {
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
    private void deleteOldImage(String avatar){
        if (avatar==null||avatar.isEmpty())
            return ;
        RemoveObjectArgs args= RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(avatar)
                .build();
        try {
            minioClient.removeObject(args);
        }catch (Exception e){
            log.error("图片删除失败:{}", e.getMessage(), e);
        }
    }
}
