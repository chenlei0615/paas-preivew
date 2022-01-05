package com.meicloud.apaas.preview.core.common;

import java.util.Arrays;

/**
 * @author chenlei140
 * @className ExtensionConstant
 * @description 扩展名常量
 * @date 2021/12/22 14:58
 */
public interface ExtensionConstant {

    String UTF8 = "UTF-8";
    String BLANK = " ";
    String ZIP = "zip";

    String PDF = "pdf";
    String PDF_EXTENSION = ".pdf";

    String DOC = "doc";
    String DOC_EXTENSION = ".doc";

    String DOCX = "docx";
    String DOCX_EXTENSION = ".docx";

    String ODT = "odt";
    String ODT_EXTENSION = ".odt";

    String RTF = "rtf";
    String RTF_EXTENSION = ".rtf";

    String TXT = "txt";
    String TXT_EXTENSION = ".txt";

    String PPT = "ppt";
    String PPT_EXTENSION = ".ppt";

    String PPTX = "pptx";
    String PPTX_EXTENSION = ".pptx";

    String XLS = "xls";
    String XLS_EXTENSION = ".xls";

    String XLSX = "xlsx";
    String XLSX_EXTENSION = ".xlsx";

    String DOT = "dot";
    String DOT_EXTENSION = ".dot";

    String DOTX = "dotx";
    String DOTX_EXTENSION = ".dotx";

    String HWP = "hwp";
    String HWP_EXTENSION = ".hwp";

    String CSV = "csv";
    String CSV_EXTENSION = ".csv";

    String XML = "xml";
    String XML_EXTENSION = ".xml";

    String ALL = "all";
    String HTML = "html";
    String HTML_EXTENSION = ".html";

    static boolean contains(String fileSuffix, String... extentions) {
        return Arrays.stream(extentions).anyMatch(s -> s.equals(fileSuffix));
    }
}
