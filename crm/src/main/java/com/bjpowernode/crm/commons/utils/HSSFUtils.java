package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 关于操作Excel文件的工具类
 */
public class HSSFUtils {
    public static String getCellValueForStr(HSSFCell cell){
        String str = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
            str = cell.getStringCellValue();
        }else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            str = cell.getNumericCellValue()+"";
        }else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
            str = cell.getBooleanCellValue()+"";
        }else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
            str = cell.getCellFormula();
        }else {
            str = "";
        }
        return str;
    }
}
