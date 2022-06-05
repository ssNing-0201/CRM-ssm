package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class TranRemarkController {

    @Resource
    private TranRemarkService tranRemarkService;


    @RequestMapping("/workbench.transaction/saveTranRemark.do")
    @ResponseBody
    public Object saveTranRemark(TranRemark tranRemark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        tranRemark.setId(UUIDUtils.getUUID());
        tranRemark.setCreateBy(user.getId());
        tranRemark.setCreateTime(DataUtils.formatDataTime(new Date()));
        tranRemark.setEditFlag(Contants.TRAN_REMARK_FLG_NOT_CHANGE);
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = tranRemarkService.saveTranRemark(tranRemark);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(tranRemark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("添加失败，请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败!");
        }
        return returnObject;

    }
}
