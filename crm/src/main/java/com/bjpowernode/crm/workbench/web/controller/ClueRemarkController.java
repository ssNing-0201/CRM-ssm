package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ClueRemarkController {

    @Resource
    private ClueRemarkService clueRemarkService;

    @RequestMapping("/workbench/clue/saveClueRemark.do")
    @ResponseBody
    public Object saveClueRemark(ClueRemark clueRemark, HttpSession session){
        //封装数据
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DataUtils.formatDataTime(new Date()));
        clueRemark.setEditFlag(Contants.CLUE_REMARK_FLG_NOT_CHANGE);
        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service
            int i = clueRemarkService.saveClueRemark(clueRemark);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(clueRemark);
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

    @RequestMapping("/workbench/clue/deleteClueRemarkById.do")
    @ResponseBody
    public Object deleteClueRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueRemarkService.deleteClueRemarkById(id);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败，请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败!");
        }
        return returnObject;
    }
}
