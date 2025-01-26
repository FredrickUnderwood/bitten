package com.chen.bitten.common.utils;

import com.aliyun.oss.OSS;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class AliyunOSSUtils {

    private static final String LOG_PREFIX = "[AliyunOSSUtils]";

    @Value("${bitten.aliyun.oss.endPoint}")
    private String endPoint;

    @Value("${bitten.aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${bitten.aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${bitten.aliyun.oss.bucketName}")
    private String bucketName;

    public String upload(MultipartFile file) {
        OSS ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        // 获取原生文件名
        String originalFilename = file.getOriginalFilename();
        String folder = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());
        String filename = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uploadFilename = "/user" + folder + "/" + filename + extension;
        // 上传
        try {
            PutObjectResult result = ossClient.putObject(bucketName, uploadFilename, file.getInputStream());
            if (result != null) {
                return "https://" + bucketName + "." + endPoint + "/" + uploadFilename;
            }
        } catch (IOException e) {
            log.error("{}upload fail! e: {}", LOG_PREFIX, e.getStackTrace());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}
