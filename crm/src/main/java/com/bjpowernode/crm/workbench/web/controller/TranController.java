package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserServices;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TranController {

    @Resource
    private DicValueService dicValueService;
    @Resource
    private TranService tranService;
    @Resource
    private UserServices userServices;
    @Resource
    private ActivityService activityService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private CustomerService customerService;
    @Resource
    private TranRemarkService tranRemarkService;
    @Resource
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //调用service方法
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存在作用域
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/queryTranForPage.do")
    @ResponseBody
    public Object queryTranForPage(String owner,String name,String customerName,String stage,String transactionType,String source,String contactsName,int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        int beginNo = (pageNo-1)*pageSize;
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerName",customerName);
        map.put("stage",stage);
        map.put("transactionType",transactionType);
        map.put("source",source);
        map.put("contactsName",contactsName);
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);
        Map<String,Object> retMap = new HashMap<>();
       try {
           List<Tran> tranList = tranService.queryTranForPage(map);
           int totalRows = tranService.queryCountTranForPage(map);
           retMap.put("tranList",tranList);
           retMap.put("totalRows",totalRows);
       }catch (Exception e){
           e.printStackTrace();
       }
        return retMap;
    }
    @RequestMapping("/workbench/transaction/toSave.do")
    public Object toSave(HttpServletRequest request){
        List<User> userList = userServices.queryAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存在作用域中
        request.setAttribute("userList",userList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/queryActivityForCreateTran.do")
    @ResponseBody
    public Object queryActivityForCreateTranByName(String activityName){
        return activityService.queryActivityForDetailByName(activityName);

    }

    @RequestMapping("/workbench/transaction/queryContactsForCreateTran.do")
    @ResponseBody
    public Object queryContactsForCreateTran(String contactsName){
        return contactsService.queryContactsForCreateTran(contactsName);
    }

    @RequestMapping("/workbench/transaction/getpossibilityByStage.do")
    @ResponseBody
    public Object getpossibilityByStage(String stage){
        //解析properties文件
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stage);
        return possibility;
    }

    @RequestMapping("/workbench/transaction/queryAllCustomerName.do")
    @ResponseBody
    public Object queryAllCustomerName(String customerName){
        //查询所有客户以List形式返回
        List<String> customerList = customerService.queryAllCustomerName(customerName);
        return customerList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法保存数据
            tranService.saveCreateTran(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String tranId,HttpServletRequest request){
        //调用service方法
        Tran tran = tranService.queryTranForDetailById(tranId);
        //查询可能性
        String possibility = ResourceBundle.getBundle("possibility").getString(tran.getStage());
        tran.setPossibility(possibility);
        List<TranRemark> tranRemarkList = tranRemarkService.queryTranRemarkForDetailById(tranId);
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryForDetailByTranId(tranId);

        request.setAttribute("tran",tran);
        request.setAttribute("tranRemarkList",tranRemarkList);
        request.setAttribute("tranHistoryList",tranHistoryList);
        //调用service方法查询交易阶段
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);

        return "workbench/transaction/detail";
    }

    @RequestMapping("/workbench/transaction/toEdit.do")
    public Object toEdit(HttpServletRequest request,String id){
        Tran tran = tranService.queryTranForDetailById(id);
        List<User> userList = userServices.queryAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存在作用域中
        request.setAttribute("userList",userList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("tran",tran);
        //请求转发
        return "workbench/transaction/edit";
    }

    @RequestMapping("/workbench/transaction/saveEditTranById.do")
    @ResponseBody
    public Object saveEditTranById(@RequestParam Map<String,Object> map,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        map.put("editBy",user.getId());
        map.put("editTime", DataUtils.formatDataTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = tranService.editTranById(map);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("修改失败，请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("修改失败!");
        }
        return returnObject;
    }

}
