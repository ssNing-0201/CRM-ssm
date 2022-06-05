package com.bjpowernode.crm.commons.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对日期类型数据处理的工具类
 */
public class DataUtils {
    /**
     * 对指定的date对象格式化
     * @param data
     * @return
     */
    public static String formatDataTime(Date data){
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataStr = sfd.format(data);
        return dataStr;
    }

}
