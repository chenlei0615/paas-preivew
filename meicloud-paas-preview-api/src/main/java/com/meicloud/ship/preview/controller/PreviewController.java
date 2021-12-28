package com.meicloud.ship.preview.controller;

import com.meicloud.ship.preview.core.common.ErrorCodeEnum;
import com.meicloud.ship.preview.core.common.ExtensionConstant;
import com.meicloud.ship.preview.core.common.HeaderGenerator;
import com.meicloud.ship.preview.core.processor.StreamConverter;
import com.meicloud.ship.preview.core.utils.AssertUtils;
import com.meicloud.ship.preview.core.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author chenlei140
 * @className PreviewController
 * @description 预览控制器
 * @date 2021/11/30 11:23
 */
@Api(tags = {"预览控制器"})
@Slf4j
@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class PreviewController {

    private final StreamConverter streamConverter;

    @Value("${preview.max-file-size}")
    private Integer maxSize;

    @ApiOperation("通过文件上传预览")
    @PostMapping("/preview")
    public ResponseEntity<byte[]> preview(MultipartFile file) {
        AssertUtils.isTrue(Objects.nonNull(file) && !file.isEmpty(), ErrorCodeEnum.FILE_NOT_EXIST);
        AssertUtils.isTrue(file.getSize() / 1024 / 1024 < maxSize, ErrorCodeEnum.FILE_OVERSIZE);
        log.info(" >>> file.getOriginalFilename()：【{}】，file.getSize()：【{}】", file.getOriginalFilename(), file.getSize());
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());
        String targetFilename = String.format("%s%s", fileName, ExtensionConstant.PDF_EXTENSION);
        StopWatch clock = new StopWatch();
        clock.start("文件上传：数据转化任务开始");
        ByteArrayOutputStream convertedFile = null;
        try {
            convertedFile = this.streamConverter.convert(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error(" 文件上传： 获取文件输入流异常 {}", e.getStackTrace());
        }
        clock.stop();
        log.info("文件上传：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        final HttpHeaders headers = HeaderGenerator.pdfHeader(targetFilename);
        return ResponseEntity.ok().headers(headers).body(convertedFile.toByteArray());
    }


    @ApiOperation("通过url地址预览")
    @GetMapping("/preview/link")
    public ResponseEntity<byte[]> previewByUrl(@RequestParam("url") String fileUrl) {
        log.info(" >>> 预览文件地址：【{}】", fileUrl);
        AssertUtils.isTrue(StringUtils.isNotBlank(fileUrl), ErrorCodeEnum.FILE_URL_NOT_EXIST);
        InputStream in = FileUtil.getInputStreamByUrl(fileUrl);
        AssertUtils.notNull(FileUtil.getInputStreamByUrl(fileUrl), ErrorCodeEnum.PARSE_FILE_URL_FAILED);
        StopWatch clock = new StopWatch();
        clock.start("文件链接：数据转化任务开始");
        String fileName = fileUrl.trim().substring(fileUrl.lastIndexOf("/") + 1);
        String targetFilename = String.format("%s%s", FilenameUtils.getBaseName(fileName), ExtensionConstant.PDF_EXTENSION);
        ByteArrayOutputStream bos = this.streamConverter.convert(in, fileName);
        clock.stop();
        log.info("文件链接：任务耗时 【{}】秒", clock.getTotalTimeSeconds());
        final byte[] bytes = bos.toByteArray();
        final HttpHeaders headers = HeaderGenerator.pdfHeader(targetFilename);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }


}
