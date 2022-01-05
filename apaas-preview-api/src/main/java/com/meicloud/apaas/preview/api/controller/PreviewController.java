package com.meicloud.apaas.preview.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.meicloud.apaas.preview.core.common.ErrorCodeEnum;
import com.meicloud.apaas.preview.core.common.ExtensionConstant;
import com.meicloud.apaas.preview.core.common.HeaderGenerator;
import com.meicloud.apaas.preview.core.processor.StreamConverter;
import com.meicloud.apaas.preview.core.utils.AssertUtils;
import com.meicloud.apaas.preview.core.utils.FileUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author chenlei140
 * @className PreviewController
 * @description 预览控制器
 * @date 2021/11/30 11:23
 */
@Api(tags = {"预览控制器"})
@RestController
@RequestMapping("v1")
public class PreviewController {

    private static final Logger logger = LoggerFactory.getLogger(PreviewController.class);

    private final StreamConverter streamConverter;

    @Value("${meicloud.apaas.preview.max-file-size}")
    private Integer maxSize;

    @Value("${meicloud.apaas.preview.store-path}")
    private String storePath;

    @Autowired
    public PreviewController(StreamConverter streamConverter) {
        this.streamConverter = streamConverter;
    }

    @ApiOperation("通过文件上传预览")
    @PostMapping("/preview")
    public ResponseEntity<byte[]> preview(MultipartFile file) {
        AssertUtils.isTrue(Objects.nonNull(file) && !file.isEmpty(), ErrorCodeEnum.FILE_NOT_EXIST);
        AssertUtils.isTrue(file.getSize() / 1024 / 1024 < maxSize, ErrorCodeEnum.FILE_OVERSIZE);
        logger.info(" >>> 文件名 ：【{}】，文件大小 ：【{}】", file.getOriginalFilename(), file.getSize());
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());
        String fileSuffix = FilenameUtils.getExtension(file.getOriginalFilename());
        String targetFilename = getTargetFilename(fileName, fileSuffix);
        StopWatch clock = new StopWatch();
        clock.start("文件上传：数据转化任务开始");
        byte[] convertedFileBytes = new byte[] {};
        try {
            convertedFileBytes = streamConverter.convert(file.getInputStream(), file.getOriginalFilename(), storePath);
        } catch (IOException e) {
            logger.error(" 文件上传： 获取文件输入流异常 {}", e.getStackTrace());
        }
        clock.stop();
        logger.info(" 文件上传：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        HttpHeaders headers = HeaderGenerator.headers(targetFilename);
        return ResponseEntity.ok().headers(headers).body(convertedFileBytes);
    }

    @ApiOperation("通过url地址预览")
    @GetMapping("/preview/link")
    public ResponseEntity<byte[]> previewByUrl(@RequestParam("url") String fileUrl) {
        logger.info(" >>> 预览文件地址：【{}】", fileUrl);
        AssertUtils.isTrue(StringUtils.isNotBlank(fileUrl), ErrorCodeEnum.FILE_URL_NOT_EXIST);
        InputStream inputStream = FileUtil.getInputStreamByUrl(fileUrl);
        AssertUtils.notNull(FileUtil.getInputStreamByUrl(fileUrl), ErrorCodeEnum.PARSE_FILE_URL_FAILED);
        StopWatch clock = new StopWatch();
        clock.start("文件链接：数据转化任务开始");
        String fileName = fileUrl.trim().substring(fileUrl.lastIndexOf("/") + 1);
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String targetFilename = getTargetFilename(fileName, fileSuffix);
        byte[] convertedFileBytes = streamConverter.convert(inputStream, fileName, storePath);
        clock.stop();
        logger.info("文件链接：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        HttpHeaders headers = HeaderGenerator.headers(targetFilename);
        return ResponseEntity.ok().headers(headers).body(convertedFileBytes);
    }

    private String getTargetFilename(String fileName, String fileSuffix) {
        String targetFilename;
        if (ExtensionConstant.XLS.equalsIgnoreCase(fileSuffix) || ExtensionConstant.XLSX.equalsIgnoreCase(fileSuffix)) {
            targetFilename = String.format("%s%s", fileName, ExtensionConstant.HTML_EXTENSION);
        } else {
            targetFilename = String.format("%s%s", fileName, ExtensionConstant.PDF_EXTENSION);
        }
        return targetFilename;
    }
}
