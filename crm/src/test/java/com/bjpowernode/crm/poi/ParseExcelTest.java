package com.bjpowernode.crm.poi;

import com.bjpowernode.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ParseExcelTest {
    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream("/Users/zhangning/IdeaProjects/crm-project/crm/src/test/serviceDri/activity.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell =null;
        String str = "";
        int name =-1;
        int startDate =-1;
        int endDate =-1;
        int cost =-1;
        int description =-1;
        row = sheet.getRow(0);
        for (int k=0;k<row.getLastCellNum();k++){
            cell = row.getCell(k);
            str = HSSFUtils.getCellValueForStr(cell);
            System.out.print(str+"  ");
            if ("活动名称".equals(str)){
                name = k;
            }else if ("开始日期".equals(str)){
                startDate = k;
            }else if ("结束日期".equals(str)){
                endDate = k;
            }else if ("市场预算".equals(str)){
                cost = k;
            }else if ("备注信息".equals(str)){
                description = k;
            }
        }

        System.out.println();
        System.out.println("name:"+name+"startDate:"+startDate+"endDate:"+endDate+"cost:"+cost+"description:"+description);
        System.out.println("---------------------");
        for (int i=1;i<=sheet.getLastRowNum();i++){
            row = sheet.getRow(i);
            for (int j=0;j<row.getLastCellNum();j++){
                cell = row.getCell(j);
                str = HSSFUtils.getCellValueForStr(cell);
                System.out.print(str+"  ");
            }
            System.out.println();
        }
    }
}
