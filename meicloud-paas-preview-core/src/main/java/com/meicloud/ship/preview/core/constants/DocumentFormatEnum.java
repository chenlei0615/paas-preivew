package com.meicloud.ship.preview.core.constants;

import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;

import java.io.*;

/**
 * @author chenlei140
 * @className DocumentFormatEnum
 * @description TODO
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
    },
    TXT {
        @Override
        public DocumentFormat getFormFormat() {
            return DefaultDocumentFormatRegistry.TXT;
        }

        @Override
        public InputStream getInputStream(InputStream inputStream) throws IOException {
            //因为会出现中文乱码问题，所以先通过字符流进行编码转换，再转换成字节流
            try (final BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "GBK"))) {
                StringBuffer buf = new StringBuffer();
                String temp;
                while ((temp = bis.readLine()) != null) {
                    buf.append(temp).append(System.getProperty("line.separator"));
                }
                return new ByteArrayInputStream(buf.toString().getBytes());
            }
        }
    };

    public InputStream getInputStream(InputStream inputStream) throws IOException {
        return inputStream;
    }

    public abstract DocumentFormat getFormFormat();

    public DocumentFormat getTargetFormat() {
        return DefaultDocumentFormatRegistry.PDF;
    }
}
