package com.meicloud.ship.preview.core.processor;

import com.meicloud.ship.preview.core.common.ExtensionConstant;
import com.meicloud.ship.preview.core.constants.DocumentFormatEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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
import java.util.HashMap;

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

    private static final String FilterData = "FilterData";
    private static final String ExportBookmarks = "ExportBookmarks";
    private static final String ExportNotes = "ExportNotes";
    private DocumentConverter converter;

    private static volatile boolean isRDInitialized = false;

    @Resource
    private OfficeManager officeManager;

    @Resource
    private ExcelStreamReader excelStreamReader;

    @PostConstruct
    public synchronized void init() {
        if (!isRDInitialized) {
            log.info("Office Manager Init ....");
            this.converter = LocalConverter.builder()
                    .storeProperties(getStoreProperties())
                    .officeManager(officeManager).build();
            log.info("Office Manager End");
            isRDInitialized = true;
        }
    }

    /**
     * @param inputStream    => stream
     * @param sourceFileName => xyz.xls
     * @param targetFileName => xyz.pdf
     */
    public ByteArrayOutputStream doConvert(InputStream inputStream, String sourceFileName, String targetFileName) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (inputStream == null || (sourceFileName == null || sourceFileName.equals("")) || (targetFileName == null || targetFileName.equals(""))) {
            throw new NullPointerException("File Process File Due To Null Value");
        } else {
            if (sourceFileName.contains(ExtensionConstant.XLS_EXTENSION) || sourceFileName.contains(ExtensionConstant.XLSX_EXTENSION)) {
                inputStream = this.excelStreamReader.getExcelStream(inputStream, new ByteArrayOutputStream());
            }
            return convert(inputStream, sourceFileName, targetFileName, outputStream);
        }
    }

    private HashMap<String, Object> getStoreProperties() {
        HashMap<String, Object> loadProperties = new HashMap<>(2);
        loadProperties.put(FilterData, getFilterData());
        return loadProperties;
    }

    private HashMap<String, Object> getFilterData() {
        HashMap<String, Object> filterDate = new HashMap<>(2);
        filterDate.put(ExportBookmarks, false);
        filterDate.put(ExportNotes, false);
        return filterDate;
    }

    private ByteArrayOutputStream convert(InputStream inputStream, String sourceFileName, String targetFileName,
                                          ByteArrayOutputStream byteArrayOutputStream) throws OfficeException, IOException {
        final DocumentFormat sourceFormat = DefaultDocumentFormatRegistry.getFormatByExtension(FilenameUtils.getExtension(sourceFileName));
        if (sourceFormat.getName().equals(DefaultDocumentFormatRegistry.TXT.getName())) {
            inputStream = DocumentFormatEnum.TXT.getInputStream(inputStream);
        }
        log.info(">>> 待转换的文档类型：{}", sourceFormat);
        final DocumentFormat targetFormat = DefaultDocumentFormatRegistry.getFormatByExtension(FilenameUtils.getExtension(targetFileName));
        log.info(">>> 转换的目标文档类型：{}", targetFormat);
        this.converter.convert(inputStream).as(sourceFormat).to(byteArrayOutputStream).as(targetFormat).execute();
        if (inputStream != null) {
            inputStream.close();
        }
        return byteArrayOutputStream;
    }

}
