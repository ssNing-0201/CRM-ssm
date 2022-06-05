package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserServices;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContactsController {

    @Resource
    private UserServices userServices;
    @Resource
    private DicValueService dicValueService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private CustomerService customerService;

    @RequestMapping("/workbench/contacts/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userServices.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/contacts/index";
    }

    @RequestMapping("/workbench/contacts/queryContactsForPage.do")
    @ResponseBody
    public Object queryContactsForPage(String source,String customerName,String fullName,String owner,String birthday,int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        int beginNo = (pageNo-1)*pageSize;
        map.put("source",source);
        map.put("customerName",customerName);
        map.put("fullName",fullName);
        map.put("owner",owner);
        map.put("birthday",birthday);
        map.put("pageSize",pageSize);
        map.put("beginNo",beginNo);
        //创建返回map
        Map<String,Object> retMap = new HashMap<>();
        try {
            //调用service
            List<Contacts> contactsList = contactsService.queryContactsForPage(map);
            int totalRows = contactsService.queryCountContactsForPage(map);
            retMap.put("contactsList",contactsList);
            retMap.put("totalRows",totalRows);
        }catch (Exception e){
            e.printStackTrace();
        }
        return retMap;
    }

    @RequestMapping("/workbench/contacts/queryCustomerByName.do")
    @ResponseBody
    public Object queryCustomerByName(String name){
        List<Customer> customerList = customerService.queryCustomerByName(name);
        return customerList;
    }
}
