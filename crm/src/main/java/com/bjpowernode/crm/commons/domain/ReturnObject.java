package com.bjpowernode.crm.commons.domain;

public class ReturnObject {

    //select a.id as id,u1.name as owner,a.name as name,a.start_date as start_date,a.end_date as end_date,a.cost as cost,,a.description as description,a.creat_time as creat_time,u2.name as creat_by,a.edit_time as edit_time,u3.name as edit_by


    private String code;        //处理成功或失败的标记，1成功，0失败
    private String message;     //提示信息
    private Object retData;     //返回其他数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
