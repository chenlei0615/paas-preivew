package com.meicloud.apaas.preview.api.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenlei140
 * @className PreviewService
 * @description 预览业务处理
 * @date 2022/1/6 10:00
 */
public interface PreviewService {
    byte[] convertByUrl(InputStream inputStream, String sourceFileName);

    byte[] convertByFile(MultipartFile file);

}
