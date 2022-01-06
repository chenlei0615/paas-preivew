package com.meicloud.apaas.preview.api.model;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.meicloud.apaas.preview.api.config.HeaderGenerator;
import com.meicloud.apaas.preview.core.common.ExtensionConstant;

/**
 * @author chenlei140
 * @className DataTranslator
 * @description 数据转化模型
 * @date 2022/1/5 15:50
 */
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DataTranslator {

    private String sourceFileName;
    private String sourceFileSuffix;

    private String targetFilename;
    private HttpHeaders httpHeader;

    private DataTranslator() {}

    public DataTranslator(MultipartFile file) {
        sourceFileName = FilenameUtils.getBaseName(file.getOriginalFilename());
        sourceFileSuffix = FilenameUtils.getExtension(file.getOriginalFilename());

        translate();
    }

    public DataTranslator(String url) {
        sourceFileName = url.trim().substring(url.lastIndexOf("/") + 1);
        sourceFileSuffix = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);

        translate();
    }

    private void translate() {
        if (ExtensionConstant.include(sourceFileSuffix, ExtensionConstant.EXCEL_2_HTML)) {
            targetFilename = String.format("%s%s", sourceFileName, ExtensionConstant.HTML_EXTENSION);
            httpHeader = HeaderGenerator.headers(targetFilename, MediaType.valueOf(MediaType.TEXT_HTML_VALUE));
        } else {
            targetFilename = String.format("%s%s", sourceFileName, ExtensionConstant.PDF_EXTENSION);
            httpHeader = HeaderGenerator.headers(targetFilename);
        }
    }

    public String getTargetFilename() {
        return targetFilename;
    }

    public HttpHeaders getHttpHeader() {
        return httpHeader;
    }

}
