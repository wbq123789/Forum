package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.FileService;
import com.example.utils.Const;
import io.minio.errors.ErrorResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @program: forum
 * @description: 文件上传相关接口
 * @author: 王贝强
 * @create: 2024-06-10 12:28
 */
@Slf4j
@RestController
@Tag(name = "文件上传下载相关", description = "包括头像、图片等文件的上传下载操作。")
@RequestMapping("/api/file")
public class FileController {
    @Resource
    FileService fileService;
    /**
     * 上传头像
     * @param file 头像文件
     * @param userId 用户ID
     * @return 上传结果
     * @throws IOException IO异常
     */
    @PostMapping("/upload/avatar")
    @Operation(summary = "上传头像")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                         @RequestAttribute(Const.ATTR_USER_ID) Integer userId) throws IOException {
        if (file.isEmpty()) {
            return RestBean.failure(400, "头像文件为空");
        } else if (file.getSize() > 100 * 1024) {
            return RestBean.failure(400, "头像不能大于100KB");
        }
        log.info("头像文件：{} 上传中", file.getOriginalFilename());
        String fileName = fileService.uploadAvatar(file, userId);
        if (fileName != null) {
            log.info("头像文件上传成功，大小:{}", file.getSize());
            return RestBean.success(fileName);
        } else {
             return RestBean.failure(400, "头像上传失败，请联系管理员！");
        }
    }
    /**
     * 上传图片
     * @param file 图片文件
     * @param userId 用户ID
     * @return 上传结果
     * @throws IOException IO异常
     */
    @PostMapping("/upload/image")
    @Operation(summary = "上传图片")
    public RestBean<String> uploadImage(@RequestParam("file") MultipartFile file,
                                        @RequestAttribute(Const.ATTR_USER_ID) Integer userId,
                                        HttpServletResponse response) throws IOException {
        if (file.isEmpty()) {
            return RestBean.failure(400, "图片为空");
        } else if (file.getSize() > 5* 1024*1024) {
            return RestBean.failure(400, "图片不能大于5MB");
        }
        log.info("图像文件：{} 上传中", file.getOriginalFilename());
        String fileName = fileService.uploadImage(file, userId);
        if (fileName != null) {
            log.info("图像文件上传成功，大小:{}", file.getSize());
            return RestBean.success(fileName);
        } else {
            response.setStatus(400);
            return RestBean.failure(400, "图片上传失败，请联系管理员！");
        }
    }
    /**
     * 下载头像
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @GetMapping("/download/images/**")
    @Operation(summary = "下载图片")
    public void avatarFetch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type","image/jpeg");
        this.fetchImage(request,response);
    }
    /**
     * 下载图片
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath= request.getServletPath().substring(25);
        ServletOutputStream outputStream = response.getOutputStream();
        if (imagePath.length()<=13){
            response.setStatus(404);
            outputStream.println(RestBean.failure(404,"File Not Found!").toString());
        }else {
            try {
                fileService.fetchImage(outputStream, imagePath);
                response.setHeader("Cache-Control","max-age=2592000");
            }catch (ErrorResponseException e){
                if (e.response().code()==404){
                    response.setStatus(404);
                    outputStream.println(RestBean.failure(404,"File Not Found!").toString());
                }else {
                    log.error("从minIO读取图片出现异常：{}", e.getMessage(), e);
                }
            }
        }
    }
}
