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
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.meicloud.apaas.preview.api.model.DataTranslator;
import com.meicloud.apaas.preview.api.utils.InputStreamUtil;
import com.meicloud.apaas.preview.core.common.ErrorCodeEnum;
import com.meicloud.apaas.preview.core.processor.StreamConverter;
import com.meicloud.apaas.preview.core.utils.AssertUtils;

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
        String sourceFileName = FilenameUtils.getBaseName(file.getOriginalFilename());
        String sourceFileSuffix = FilenameUtils.getExtension(file.getOriginalFilename());
        DataTranslator translator = new DataTranslator(sourceFileName, sourceFileSuffix);
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
        return ResponseEntity.ok().headers(translator.getHttpHeader()).body(convertedFileBytes);
    }

    @ApiOperation("通过url地址预览")
    @GetMapping("/preview/link")
    public ResponseEntity<byte[]> previewByUrl(@RequestParam("url") String fileUrl) {
        logger.info(" >>> 预览文件地址：【{}】", fileUrl);
        AssertUtils.isTrue(StringUtils.isNotBlank(fileUrl), ErrorCodeEnum.FILE_URL_NOT_EXIST);
        InputStream inputStream = InputStreamUtil.getInputStreamByUrl(fileUrl);
        AssertUtils.notNull(InputStreamUtil.getInputStreamByUrl(fileUrl), ErrorCodeEnum.PARSE_FILE_URL_FAILED);
        StopWatch clock = new StopWatch();
        clock.start("文件链接：数据转化任务开始");
        String sourceFileName = fileUrl.trim().substring(fileUrl.lastIndexOf("/") + 1);
        String sourceFileSuffix = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
        DataTranslator translator = new DataTranslator(sourceFileName, sourceFileSuffix);
        byte[] convertedFileBytes = streamConverter.convert(inputStream, sourceFileName, storePath);
        clock.stop();
        logger.info("文件链接：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        return ResponseEntity.ok().headers(translator.getHttpHeader()).body(convertedFileBytes);
    }

}
