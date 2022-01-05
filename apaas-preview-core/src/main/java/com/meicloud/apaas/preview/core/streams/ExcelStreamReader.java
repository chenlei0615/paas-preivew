package com.meicloud.apaas.preview.core.streams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * @author chenlei140
 * @className ExcelStreamReader
 * @description excel 数据流特殊处理类
 * @date 2021/12/21 17:03
 */
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExcelStreamReader {

    public static final String MAC_TODAY = "May 6, 2000";
    private static final Logger log = LoggerFactory.getLogger(ExcelStreamReader.class);
    private static final String TODAY = "TODAY()";

    private ExcelStreamReader() {}

    public static InputStream getInputStream(InputStream inputStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Iterator<Sheet> iterator = workbook.sheetIterator();
            while (iterator.hasNext()) {
                Sheet sheet = iterator.next();
                disableComments(sheet);
                sheet.setFitToPage(true);
                sheet.setAutobreaks(true);
                PrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setFitWidth((short)1);
                printSetup.setFitHeight((short)0);
            }
            workbook.write(bos);
            inputStream = new ByteArrayInputStream(bos.toByteArray());
        } catch (IOException e) {
            log.error(" deal excel stream error : {} ", e.getStackTrace());
        }
        return inputStream;
    }

    private static void disableComments(Sheet sheet) {
        if (sheet instanceof XSSFSheet || sheet instanceof HSSFSheet) {
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Comment comment = cell.getCellComment();
                    if (comment != null) {
                        log.info("Comment :-  {}", comment.getString());
                        cell.removeCellComment();
                    }
                    if (cell.getCellType() == CellType.FORMULA && cell.getCellFormula().equalsIgnoreCase(TODAY)) {
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(MAC_TODAY);
                    }
                }
            }
        }
    }
}
