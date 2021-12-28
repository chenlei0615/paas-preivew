package com.meicloud.ship.preview.core.constants;

import com.meicloud.ship.preview.core.streams.ExcelStreamReader;
import com.meicloud.ship.preview.core.streams.TxtStreamReader;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;

import java.io.InputStream;

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
            return DefaultDocumentFormatRegistry.PDF;
        }
    },
    XLSX {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.XLSX;
        }

        @Override
        public DocumentFormat getTargetFormat() {
            return DefaultDocumentFormatRegistry.PDF;
        }

        @Override
        public InputStream getInputStream(InputStream inputStream) {
            return ExcelStreamReader.getExcelStream(inputStream);
        }

    },
    TXT {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.TXT;
        }

        @Override
        public InputStream getInputStream(InputStream inputStream) {
            return TxtStreamReader.getExcelStream(inputStream);
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
