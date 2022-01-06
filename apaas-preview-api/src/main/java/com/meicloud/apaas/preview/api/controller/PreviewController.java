package com.meicloud.apaas.preview.api.controller;

import java.io.InputStream;
import java.util.Objects;

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
import com.meicloud.apaas.preview.api.service.PreviewService;
import com.meicloud.apaas.preview.api.utils.InputStreamUtil;
import com.meicloud.apaas.preview.core.common.ErrorCodeEnum;
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

    private final PreviewService previewService;

    @Value("${meicloud.apaas.preview.max-file-size}")
    private Integer maxSize;

    @Autowired
    public PreviewController(PreviewService previewService) {
        this.previewService = previewService;
    }

    @ApiOperation("通过文件上传预览")
    @PostMapping("/preview")
    public ResponseEntity<byte[]> preview(MultipartFile file) {
        AssertUtils.isTrue(Objects.nonNull(file) && !file.isEmpty(), ErrorCodeEnum.FILE_NOT_EXIST);
        AssertUtils.isTrue(file.getSize() / 1048576 < maxSize, ErrorCodeEnum.FILE_OVERSIZE);
        logger.info(" \n 上传文件名 ：【{}】，文件大小 【{}】KB", file.getOriginalFilename(), file.getSize() / 1024);
        StopWatch clock = new StopWatch();
        clock.start("文件上传：数据转化任务开始");
        DataTranslator translator = new DataTranslator(file);
        byte[] convertedFileBytes = previewService.convertByFile(file);
        clock.stop();
        logger.info(" \n 文件上传：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        return ResponseEntity.ok().headers(translator.getHttpHeader()).body(convertedFileBytes);
    }

    @ApiOperation("通过url地址预览")
    @GetMapping("/preview/link")
    public ResponseEntity<byte[]> previewByUrl(@RequestParam("url") String url) {
        logger.info(" \n 预览文件地址：【{}】", url);
        AssertUtils.isTrue(StringUtils.isNotBlank(url), ErrorCodeEnum.FILE_URL_NOT_EXIST);
        InputStream inputStream = InputStreamUtil.getInputStreamByUrl(url);
        AssertUtils.notNull(inputStream, ErrorCodeEnum.PARSE_FILE_URL_FAILED);
        StopWatch clock = new StopWatch();
        clock.start("文件链接：数据转化任务开始");
        DataTranslator translator = new DataTranslator(url);
        byte[] convertedFileBytes = previewService.convertByUrl(inputStream, translator.getTargetFilename());
        clock.stop();
        logger.info(" \n 文件链接：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        return ResponseEntity.ok().headers(translator.getHttpHeader()).body(convertedFileBytes);
    }

}
