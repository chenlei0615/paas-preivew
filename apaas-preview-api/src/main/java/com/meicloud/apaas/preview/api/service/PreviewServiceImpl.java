package com.meicloud.apaas.preview.api.service;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.meicloud.apaas.preview.core.processor.StreamConverter;

/**
 * @author chenlei140
 * @className PreviewServiceImpl
 * @description 预览业务处理
 * @date 2022/1/6 10:01
 */
@Service
public class PreviewServiceImpl implements PreviewService {
    private static final Logger logger = LoggerFactory.getLogger(PreviewServiceImpl.class);

    private final StreamConverter streamConverter;

    @Value("${meicloud.apaas.preview.store-path}")
    private String storePath;

    @Autowired
    public PreviewServiceImpl(StreamConverter streamConverter) {
        this.streamConverter = streamConverter;
    }

    @Override
    public byte[] convertByFile(MultipartFile file) {
        byte[] convertedFileBytes = new byte[] {};
        try (InputStream inputStream = file.getInputStream()) {
            convertedFileBytes = streamConverter.convert(inputStream, file.getOriginalFilename(), storePath);
        } catch (IOException e) {
            logger.error(" 文件上传： 获取文件输入流异常 {}", e.getStackTrace());
        }
        return convertedFileBytes;
    }

    @Override
    public byte[] convertByUrl(InputStream inputStream, String sourceFileName) {
        return streamConverter.convert(inputStream, sourceFileName, storePath);
    }

}
