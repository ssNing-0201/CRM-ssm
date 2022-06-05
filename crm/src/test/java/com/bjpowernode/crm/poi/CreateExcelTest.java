package com.bjpowernode.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateExcelTest {
    public static void main(String[] args) throws IOException {
        //创建HSSFWorkBook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //使用wb创建HSSFSheet，对应wb文件中的一页
        HSSFSheet sheet = wb.createSheet("给页起名字");
        //使用sheet创建HSSFRow对象，对应sheet中的一行
        HSSFRow row = sheet.createRow(0);//行号，从0开始
        //使用row对象创建HSSFCell对象，对应row中的列
        HSSFCell cell = row.createCell(0);//列号，从0开始
        //使用cell对象，写入内容
        cell.setCellValue("学号");

        cell=row.createCell(1);
        cell.setCellValue("姓名");
        cell= row.createCell(2);
        cell.setCellValue("年龄");

        //生成样式对象HSSFCellStyle
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //创建10个学生信息，使用循环
        for (int i=1;i<=10;i++){
            row = sheet.createRow(i);

            cell=row.createCell(0);
            cell.setCellValue(100+i);
            cell=row.createCell(1);
            cell.setCellValue("name"+i);
            cell=row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(20+i);
        }
        //调用工具函数生成excel
        FileOutputStream os = new FileOutputStream("/Users/zhangning/IdeaProjects/crm-project/crm/src/test/serviceDri/student.xls");
        wb.write(os);
        //使用完关闭资源
        os.close();
        wb.close();

        System.out.println("=============Create Ok=============");
    }
}
