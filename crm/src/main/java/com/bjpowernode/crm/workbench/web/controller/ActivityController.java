package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserServices;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {

    @Resource
    private UserServices userServices;
    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service方法查询所有用户
        List<User> userList = userServices.queryAllUsers();
        request.setAttribute("userList",userList);
        //请求转发到市场活动的主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        //封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DataUtils.formatDataTime(new Date()));
        activity.setCreateBy(user.getId());
        System.out.println(activity);
        //调用service保存市场信息
        ReturnObject returnObject = new ReturnObject();
        try {
            int flg = activityService.saveCreatActivity(activity);

            if (flg>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("添加失败，请查看填写数据!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败，请查看填写数据!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        //封装参数
        int beginNo = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("StartDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);

        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.selectCountOfActivityByCondition(map);
        //根据查询结果生成响应信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }
    @RequestMapping("/workbench.activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try{
            int rows = activityService.deleteActivityByIds(id);
            if (id.length==rows){
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
    @RequestMapping("/workbench.activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        return activityService.queryActivityByID(id);
    }
    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity ,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DataUtils.formatDataTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try{
            int flg = activityService.updateActivityById(activity);
            if (flg==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("修改失败，请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("修改失败，请稍后再试!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench.activity/exportAllActivity.do")
    public void exportAllActivity(String[] id,HttpServletResponse response ) throws IOException {
        System.out.println("测试提取id信息"+id);
        List<Activity> activityList = new ArrayList<>();
        if (id==null || id.length==0){
            activityList = activityService.selectActivityAll();
        }else {
            activityList = activityService.selectActivityById(id);
        }

        //根据查询结果生成响应文件excel表
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id");
        //数据库查询所有字段名
        /*String[] activity = activityService.queryActivityColumns();
        System.out.println(activity);*/
        String[] activity = {"id","owner","name","startDate","endDate","cost","description","createTime","createBy","editTime","editBy"};
        /*for (int j=1;j<=activity.length;j++){
            cell= row.createCell(j);
            cell.setCellValue(activity[j]);
        }*/

        cell= row.createCell(1);
        cell.setCellValue("所有者");
        cell= row.createCell(2);
        cell.setCellValue("活动名称");
        cell= row.createCell(3);
        cell.setCellValue("开始日期");
        cell= row.createCell(4);
        cell.setCellValue("结束日期");
        cell= row.createCell(5);
        cell.setCellValue("市场预算");
        cell= row.createCell(6);
        cell.setCellValue("备注信息");
        cell= row.createCell(7);
        cell.setCellValue("创建时间");
        cell= row.createCell(8);
        cell.setCellValue("创建人");
        cell= row.createCell(9);
        cell.setCellValue("修改时间");
        cell= row.createCell(10);
        cell.setCellValue("修改人");


        //判断List是否为空
        if (activityList!=null && activityList.size()>0){
            Activity activity1 = null;
            for (int i=0;i<activityList.size();i++){
                activity1 = activityList.get(i);
                //遍历出一行创建一行
                row=sheet.createRow(i+1);
                //填写列内容
                cell= row.createCell(0);
                cell.setCellValue(activity1.getId());
                cell= row.createCell(1);
                cell.setCellValue(activity1.getOwner());
                cell= row.createCell(2);
                cell.setCellValue(activity1.getName());
                cell= row.createCell(3);
                cell.setCellValue(activity1.getStartDate());
                cell= row.createCell(4);
                cell.setCellValue(activity1.getEndDate());
                cell= row.createCell(5);
                cell.setCellValue(activity1.getCost());
                cell= row.createCell(6);
                cell.setCellValue(activity1.getDescription());
                cell= row.createCell(7);
                cell.setCellValue(activity1.getCreateTime());
                cell= row.createCell(8);
                cell.setCellValue(activity1.getCreateBy());
                cell= row.createCell(9);
                cell.setCellValue(activity1.getEditTime());
                cell= row.createCell(10);
                cell.setCellValue(activity1.getEditBy());
            }
        }
        //根据wb对象生成excel文件
        /*FileOutputStream os = new FileOutputStream("/Users/zhangning/IdeaProjects/crm-project/crm/src/test/serviceDri/activity.xls");
        wb.write(os);
        //关闭资源
        os.close();
        wb.close();*/

        //文件下载
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        //设置响应头
        response.addHeader("Content-Disposition","attachment;filename=activity.xls");

        /*FileInputStream is = new FileInputStream("/Users/zhangning/IdeaProjects/crm-project/crm/src/test/serviceDri/activity.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len=is.read(buff))!=-1){
            out.write(buff,0,len);
        }
        is.close();*/

        //将文件流直接传到文件输出流提供下载，提高效率
        wb.write(out);
        wb.close();

        out.flush();
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
         try {
             //以下注释部分，被优化掉，优化前网页传数据-->保存服务器——>创建读取文件流读取——>操作文件数据
             //优化后为，网页数据——>创建流读取文件数据
             //-----------------------------------------------------------------------------------------
             /*//把excel文件写到磁盘目录中
             String originalFilename = activityFile.getOriginalFilename();
             File file = new File("/Users/zhangning/IdeaProjects/crm-project/crm/src/test/serviceDri",originalFilename);
             activityFile.transferTo(file);
             //解析excel文件，获取数据，并封装成activityList
             //根据文件生成workbook对象
             FileInputStream is = new FileInputStream("/Users/zhangning/IdeaProjects/crm-project/crm/src/test/serviceDri/" + originalFilename);*/
             //-----------------------------------------------------------------------------------------
             //优化后只有一行，直接从内存文件读取文件省去了保存服务器，提高效率。x`
             InputStream is = activityFile.getInputStream();

             HSSFWorkbook wb = new HSSFWorkbook(is);
             HSSFSheet sheet = wb.getSheetAt(0);
             HSSFRow row = null;
             HSSFCell cell = null;
             Activity activity = null;
             String id = ((User) session.getAttribute(Contants.SESSION_USER)).getId();
             List<Activity> activityList = new ArrayList<>();
             int name =-1;
             int startDate =-1;
             int endDate =-1;
             int cost =-1;
             int description =-1;
             row = sheet.getRow(0);
             String str = "";
             //个人想法，由表第一行数据遍历后建立映射，以方便后边取数据（待测试）
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

             for (int i=1;i<=sheet.getLastRowNum();i++){
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(id);
                activity.setCreateTime(DataUtils.formatDataTime(new Date()));
                activity.setCreateBy(id);
                for (int j=0;j<row.getLastCellNum();j++){
                    cell=row.getCell(j);
                    String val = HSSFUtils.getCellValueForStr(cell);
                    if (j==name){
                        activity.setName(val);
                    }else if (j==startDate){
                        activity.setStartDate(val);
                    }else if (j==endDate){
                        activity.setEndDate(val);
                    }else if (j==cost){
                        activity.setCost(val);
                    }else if(j==description){
                        activity.setDescription(val);
                    }
                }
                //分装好后保存到Lise中
                 activityList.add(activity);
             }
             //调用service方法将数据保存到数据库中
             int flg = activityService.saveCreatActivityByList(activityList);
             System.out.println("flg="+flg);
             if (flg == activityList.size()){
                 returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                 returnObject.setRetData(flg);
             }else {
                 returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                 returnObject.setMessage("保存失败，请稍后再试!");
             }
         } catch (IOException e) {
            e.printStackTrace();
             returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
             returnObject.setMessage("保存失败，请稍后再试!");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarksList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //数据保存作用域request中中
        request.setAttribute("activity",activity);
        request.setAttribute("remarksList",remarksList);
        //  请求转发
        return "workbench/activity/detail";
    }
}
