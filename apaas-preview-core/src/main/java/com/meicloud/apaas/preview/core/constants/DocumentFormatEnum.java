package com.meicloud.apaas.preview.core.constants;

import java.io.InputStream;

import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;

import com.meicloud.apaas.preview.core.streams.ExcelStreamReader;
import com.meicloud.apaas.preview.core.streams.TxtStreamReader;

/**
 * @author chenlei140
 * @className DocumentFormatEnum
 * @description 文档枚举
 * @date 2021/11/30 11:28
 */
public enum DocumentFormatEnum {
    DOC {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.DOC;
        }
    },
    DOCX {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.DOCX;
        }
    },
    PPT {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.PPT;
        }
    },
    PPTX {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.PPTX;
        }
    },
    XLS {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.XLS;
        }

        @Override
        public DocumentFormat getTargetFormat() {
            return DefaultDocumentFormatRegistry.HTML;
        }

        // @Override
        // public InputStream getInputStream(InputStream inputStream) {
        // return ExcelStreamReader.getInputStream(inputStream);
        // }
    },
    XLSX {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.XLSX;
        }

        @Override
        public DocumentFormat getTargetFormat() {
            return DefaultDocumentFormatRegistry.HTML;
        }

        // @Override
        // public InputStream getInputStream(InputStream inputStream) {
        // return ExcelStreamReader.getInputStream(inputStream);
        // }
    },

    TXT {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.TXT;
        }

        @Override
        public InputStream getInputStream(InputStream inputStream) {
            return TxtStreamReader.getInputStream(inputStream);
        }
    },

    CSV {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.CSV;
        }

        @Override
        public InputStream getInputStream(InputStream inputStream) {
            return ExcelStreamReader.getInputStream(inputStream);
        }
    };

    public InputStream getInputStream(InputStream inputStream) {
        return inputStream;
    }

    public abstract DocumentFormat getFormFormat();

    public DocumentFormat getTargetFormat() {
        return DefaultDocumentFormatRegistry.PDF;
    }
}
