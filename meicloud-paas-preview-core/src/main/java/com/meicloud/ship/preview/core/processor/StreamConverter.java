package com.meicloud.ship.preview.core.processor;

import com.meicloud.ship.preview.core.constants.DocumentFormatEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author chenlei140
 * @className StreamConverter
 * @description 数据流转换器
 * @date 2021/12/22 14:29
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class StreamConverter {

    @Resource
    private OfficeManager officeManager;

    private DocumentConverter converter;

    private static volatile boolean isRDInitialized = false;

    @PostConstruct
    public synchronized void init() {
        if (!isRDInitialized) {
            log.info("Office Manager Init ....");
            this.converter = LocalConverter.builder()
                    .storeProperties(ExcelAssembler.loadProperties())
                    .officeManager(officeManager).build();
            log.info("Office Manager End");
            isRDInitialized = true;
        }
    }

    /**
     * @param inputStream    => stream
     * @param sourceFileName => xyz.xls
     */
    public ByteArrayOutputStream convert(InputStream inputStream, String sourceFileName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (inputStream == null || StringUtils.isBlank(sourceFileName)) {
            throw new NullPointerException("File Process Failed Due To Null Value");
        }
        return convert(inputStream, sourceFileName, outputStream);
    }

    /**
     * @param inputStream
     * @param sourceFileName
     * @param byteArrayOutputStream
     * @return
     */
    private ByteArrayOutputStream convert(InputStream inputStream, String sourceFileName,
                                          ByteArrayOutputStream byteArrayOutputStream) {
        final String suffix = FilenameUtils.getExtension(sourceFileName);
        final DocumentFormatEnum documentFormatEnum = DocumentFormatEnum.valueOf(suffix.toUpperCase());
        DocumentFormat sourceFormat;
        DocumentFormat targetFormat;
        if (Objects.isNull(documentFormatEnum)) {
            sourceFormat = DefaultDocumentFormatRegistry.getFormatByExtension(suffix);
            // default pdf
            targetFormat = DefaultDocumentFormatRegistry.PDF;
        } else {
            sourceFormat = documentFormatEnum.getFormFormat();
            targetFormat = documentFormatEnum.getTargetFormat();
            inputStream = documentFormatEnum.getInputStream(inputStream);
        }
        return convert(inputStream, sourceFormat, targetFormat, byteArrayOutputStream);
    }


    /**
     * @param inputStream
     * @param sourceFormat
     * @param targetFormat
     * @param byteArrayOutputStream
     * @return
     */
    private ByteArrayOutputStream convert(InputStream inputStream, DocumentFormat sourceFormat, DocumentFormat targetFormat,
                                          ByteArrayOutputStream byteArrayOutputStream) {
        log.info("  \n   >>> 待转换的文档类型：【{}】   \n   >>> 转换的目标文档类型：【{}】 ", sourceFormat, targetFormat);
        try {
            this.converter.convert(inputStream).as(sourceFormat).to(byteArrayOutputStream).as(targetFormat).execute();
        } catch (OfficeException e) {
            log.error(" 转化流异常: {} ", e.getStackTrace());
        } finally {
            releaseInputStream(inputStream);
        }
        return byteArrayOutputStream;
    }


    private void releaseInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(" 流关闭异常: {} ", e.getStackTrace());
            }
        }
    }
}
