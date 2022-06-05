package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreatActivityRemark.do")
    @ResponseBody
    public Object saveCreatActivityRemark(ActivityRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DataUtils.formatDataTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contants.ACTIVITY_REMARK_FLG_NOT_CHANGE);
        //调用service层方法。保存市场活动备注
        //创建返回值
        ReturnObject returnObject = new ReturnObject();
        try{
            int i = activityRemarkService.saveCreatActivityRemark(remark);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存备注信息失败!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存备注信息失败!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityRemarkService.deleteActivityRemarkById(id);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark remark,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setEditFlag(Contants.ACTIVITY_REMARK_FLG_CHANGE);
        remark.setEditBy(user.getId());
        remark.setEditTime(DataUtils.formatDataTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityRemarkService.updateActivityRemarkById(remark);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("修改失败!请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("修改失败!");
        }
        return returnObject;
    }
}
