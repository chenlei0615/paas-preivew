package com.meicloud.apaas.preview.core.processor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meicloud.apaas.preview.core.common.ExtensionConstant;
import com.meicloud.apaas.preview.core.constants.DocumentFormatEnum;
import com.meicloud.apaas.preview.core.utils.HtmlParseUtil;

/**
 * @author chenlei140
 * @className StreamConverter
 * @description 数据流转换器
 * @date 2021/12/22 14:29
 */
@Component
public class StreamConverter {

    private static final Logger logger = LoggerFactory.getLogger(StreamConverter.class);
    private static volatile boolean isRDInitialized = false;
    private final OfficeManager officeManager;
    private DocumentConverter converter;

    @Autowired
    public StreamConverter(OfficeManager officeManager) {
        this.officeManager = officeManager;
    }

    @PostConstruct
    public synchronized void init() {
        if (!isRDInitialized) {
            logger.info("Office Manager Init ....");
            converter = LocalConverter.builder().storeProperties(ExcelProperties.loadProperties())
                .officeManager(officeManager).build();
            logger.info("Office Manager End");
            isRDInitialized = true;
        }
    }

    /**
     * @param inputStream => stream
     * @param sourceFileName => xyz.xls
     */
    public byte[] convert(InputStream inputStream, String sourceFileName, String storePath) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (inputStream == null || StringUtils.isBlank(sourceFileName)) {
            throw new NullPointerException("File Process Failed Due To Null Value");
        }
        return convert(inputStream, sourceFileName, outputStream, storePath);
    }

    /**
     * @param inputStream
     * @param sourceFileName
     * @param byteArrayOutputStream
     * @return
     */
    private byte[] convert(InputStream inputStream, String sourceFileName, ByteArrayOutputStream byteArrayOutputStream,
        String storePath) {
        String suffix = FilenameUtils.getExtension(sourceFileName);
        DocumentFormatEnum documentFormatEnum = DocumentFormatEnum.valueOf(suffix.toUpperCase());
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
        if (ExtensionConstant.XLS.equals(suffix) || ExtensionConstant.XLSX.equals(suffix)) {
            String fileName = FilenameUtils.getBaseName(sourceFileName);
            File targetFile = new File(storePath + "/" + fileName + ExtensionConstant.HTML_EXTENSION);
            return convert(inputStream, sourceFormat, targetFormat, targetFile, storePath);
        } else {
            return convert(inputStream, sourceFormat, targetFormat, byteArrayOutputStream);
        }
    }

    /**
     * @param inputStream
     * @param sourceFormat
     * @param targetFormat
     * @return byte[]
     */
    private byte[] convert(InputStream inputStream, DocumentFormat sourceFormat, DocumentFormat targetFormat,
        File targetFile, String storePath) {
        logger.info("  \n   >>> 待转换的文档类型：【{}】   \n   >>> 转换的目标文档类型：【{}】 ", sourceFormat, targetFormat);

        byte[] bytes = new byte[] {};
        try {
            converter.convert(inputStream).as(sourceFormat).to(targetFile).as(targetFormat).execute();
            String editHtml = HtmlParseUtil.editHtml(targetFile, storePath);
            if (StringUtils.isEmpty(editHtml)) {
                bytes = FileUtils.readFileToByteArray(targetFile);
            } else {
                bytes = editHtml.getBytes("GB2312");
            }
        } catch (OfficeException e) {
            logger.error(" 转化流异常: {} ", e.getStackTrace());
        } catch (IOException ex) {
            logger.error(" read File error: {} ", ex.getStackTrace());
        } finally {
            releaseInputStream(inputStream);
        }
        return bytes;
    }

    /**
     * @param inputStream
     * @param sourceFormat
     * @param targetFormat
     * @param byteArrayOutputStream
     * @return
     */
    private byte[] convert(InputStream inputStream, DocumentFormat sourceFormat, DocumentFormat targetFormat,
        ByteArrayOutputStream byteArrayOutputStream) {
        logger.info("  \n   >>> 待转换的文档类型：【{}】   \n   >>> 转换的目标文档类型：【{}】 ", sourceFormat, targetFormat);
        try {
            converter.convert(inputStream).as(sourceFormat).to(byteArrayOutputStream).as(targetFormat).execute();
        } catch (OfficeException e) {
            logger.error(" 转化流异常: {} ", e.getStackTrace());
        } finally {
            releaseInputStream(inputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void releaseInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error(" 流关闭异常: {} ", e.getStackTrace());
            }
        }
    }

}
