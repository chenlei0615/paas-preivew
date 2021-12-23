package com.meicloud.ship.preview.core.processor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlei140
 * @className ExcelAssembler
 * @description excel配置装配
 * @date 2021/12/23 10:11
 */
public class ExcelAssembler {

    private static final String FilterData = "FilterData";
    private static final String ExportBookmarks = "ExportBookmarks";
    private static final String ExportNotes = "ExportNotes";

    private ExcelAssembler() {

    }

    public static Map<String, Object> loadProperties() {
        HashMap<String, Object> loadProperties = new HashMap<>(2);
        loadProperties.put(FilterData, getFilterData());
        return loadProperties;
    }

    public static Map<String, Object> getFilterData() {
        HashMap<String, Object> filterDate = new HashMap<>(2);
        filterDate.put(ExportBookmarks, false);
        filterDate.put(ExportNotes, false);
        return filterDate;
    }
}
