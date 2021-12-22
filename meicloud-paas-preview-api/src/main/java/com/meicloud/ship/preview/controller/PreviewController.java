package com.meicloud.ship.preview.controller;

import com.meicloud.ship.preview.core.common.ExtensionConstant;
import com.meicloud.ship.preview.core.common.HeaderGenerator;
import com.meicloud.ship.preview.core.processor.StreamConverter;
import com.meicloud.ship.preview.core.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
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

    @ApiOperation("通过文件上传预览")
    @PostMapping("/preview")
    public ResponseEntity<byte[]> preview(MultipartFile file) throws Exception {
        Assert.isTrue(Objects.nonNull(file) && !file.isEmpty(), "文件不存在或文件为空");
        log.info(">>> file.getOriginalFilename()：【{}】，file.getSize()：【{}】", file.getOriginalFilename(), file.getSize());
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());
        String targetFilename = String.format("%s%s", fileName, ExtensionConstant.PDF_EXTENSION);
        ByteArrayOutputStream convertedFile = this.streamConverter.doConvert(file.getInputStream(), file.getOriginalFilename(), targetFilename);
        final HttpHeaders headers = HeaderGenerator.pdfHeader(targetFilename);
        return ResponseEntity.ok().headers(headers).body(convertedFile.toByteArray());
    }


    @ApiOperation("通过url地址预览")
    @GetMapping("/preview/link")
    public ResponseEntity<byte[]> previewByUrl(@RequestParam("url") String fileUrl) throws Exception {
        log.info(">>> 预览文件地址：【{}】", fileUrl);
        Assert.isTrue(StringUtils.isNotBlank(fileUrl), "请传入文件url地址");
        InputStream in = FileUtil.getInputStreamByUrl(fileUrl);
        Assert.notNull(FileUtil.getInputStreamByUrl(fileUrl), "解析url地址失败");
        String fileExtName = FileUtil.getExtension(in);
        Assert.isTrue(StringUtils.isNotBlank(fileExtName), "解析文件扩展名失败");
        String fileName = fileUrl.trim().substring(fileUrl.lastIndexOf("/") + 1);
        String targetFilename = String.format("%s%s", FilenameUtils.getBaseName(fileName), ExtensionConstant.PDF_EXTENSION);
        ByteArrayOutputStream bos = this.streamConverter.doConvert(in, fileName, targetFilename);
        final byte[] bytes = bos.toByteArray();
        final HttpHeaders headers = HeaderGenerator.pdfHeader(targetFilename);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }


}
