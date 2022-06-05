package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserServices;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Resource
    private DicValueService dicValueService;
    @Resource
    private UserServices userServices;
    @Resource
    private ClueService clueService;
    @Resource
    private ClueRemarkService clueRemarkService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //调用service层查询动态数据(数据字典中的)
        List<User> userList = userServices.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        //把数据保存到作用域request中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        //封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DataUtils.formatDataTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        //调用service
        try {
            int i = clueService.saveCreatClue(clue);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存错误，请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存错误，请稍后再试!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/queryClueForPage.do")
    @ResponseBody
    public Object queryClueForPage(String fullName,String company,String phone,String source,String owner,String mphone,String state,int pageNo,int pageSize){
        //收集参数
        int beginNo = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("fullName",fullName);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);

        Map<String,Object> retMap = new HashMap<>();

        try {
            List<Clue> clueList = clueService.queryClueForPage(map);
            int totalRows = clueService.queryClueForPageTotalRows(map);
            retMap.put("clueList",clueList);
            retMap.put("totalRows",totalRows);
        }catch (Exception e){
            e.printStackTrace();
        }
        return retMap;
    }

    @RequestMapping("/workbench/clue/editClueById.do")
    @ResponseBody
    public Object editClueById(Clue clue,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //收集数据
        clue.setEditBy(user.getId());
        clue.setEditTime(DataUtils.formatDataTime(new Date()));
        System.out.println(clue.toString());
        //调用service层
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueService.editClueById(clue);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("修改错误，请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("修改错误!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id){
        return clueService.queryClueById(id);
    }

    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueService.deleteClueByIds(id);
            if (i==id.length){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(i);
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

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        try {
            //调用service方法查询数据
            Clue clue = clueService.queryClueForDetailById(id);
            List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
            List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
            //把数据保存到作用域中
            request.setAttribute("clue",clue);
            request.setAttribute("remarkList",remarkList);
            request.setAttribute("activityList",activityList);
        }catch (Exception e){
            e.printStackTrace();
        }
        //请求转发
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        //根据查询结果返回响应信息
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId){
        List<ClueActivityRelation> list = new ArrayList<>();
        ClueActivityRelation clueActivityRelation = null;
        for (String aid:activityId){
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelation.setActivityId(aid);
            clueActivityRelation.setClueId(clueId);
            list.add(clueActivityRelation);
        }
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法批量保存
            int i = clueActivityRelationService.saveCreateClueActivityRelationByList(list);
            if (list.size()==i){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityService.queryActivityForDetailByIds(activityId));
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

    @RequestMapping("/workbench/clue/saveUnBund.do")
    @ResponseBody
    public Object saveUnBund(ClueActivityRelation clueActivityRelation){
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueActivityRelationService.deleteClueActivityByCIdAId(clueActivityRelation);
            if (1==i){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败，请稍后!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryBundActivityForClueById.do")
    @ResponseBody
    public Object queryBundActivityForClueById(String clueId,String activityName){
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("activityName",activityName);
        List<Activity> activityList = activityService.queryActivityForDetailByNameAndClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String ActivityId,String isCreateTran,HttpSession session){

        Map<String,Object> map = new HashMap<>();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("ActivityId",ActivityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.SESSION_USER,user);

        ReturnObject returnObject = new ReturnObject();
        //调用service
        try {
            clueService.saveConvert(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("转换失败！请稍后再试!");
        }
        return returnObject;
    }

}
