package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.ImageStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @program: forum
 * @description: 文件上传相关接口
 * @author: 王贝强
 * @create: 2024-06-10 12:40
 */

public interface FileService extends IService<ImageStore> {
    String uploadAvatar(MultipartFile file,Integer userId) throws IOException;
    String uploadImage(MultipartFile file,Integer userId) throws IOException;
    void fetchImage(OutputStream outputStream,String imagePath) throws Exception;
}
