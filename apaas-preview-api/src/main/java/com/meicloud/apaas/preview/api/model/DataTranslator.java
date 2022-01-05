package com.meicloud.apaas.preview.api.model;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.meicloud.apaas.preview.api.config.HeaderGenerator;
import com.meicloud.apaas.preview.core.common.ExtensionConstant;

/**
 * @author chenlei140
 * @className DataTranslator
 * @description 数据转化模型
 * @date 2022/1/5 15:50
 */
@Scope(value = "prototype")
public class DataTranslator {
    private final String sourceFileName;
    private final String sourceFileSuffix;

    private String targetFilename;
    private HttpHeaders httpHeader;

    public DataTranslator(String sourceFileName, String sourceFileSuffix) {
        this.sourceFileName = sourceFileName;
        this.sourceFileSuffix = sourceFileSuffix;

        translate();
    }

    private void translate() {
        if (ExtensionConstant.contains(sourceFileSuffix, ExtensionConstant.XLS, ExtensionConstant.XLSX,
            ExtensionConstant.CSV)) {
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

    public void setTargetFilename(String targetFilename) {
        this.targetFilename = targetFilename;
    }

    public HttpHeaders getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(HttpHeaders httpHeader) {
        this.httpHeader = httpHeader;
    }
}
