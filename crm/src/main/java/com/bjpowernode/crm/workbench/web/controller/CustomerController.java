package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserServices;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Resource
    private UserServices userServices;
    @Resource
    private CustomerService customerService;

    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userServices.queryAllUsers();
        request.setAttribute("userList",userList);
        return "workbench/customer/index";
    }

    @RequestMapping("/workbench/customer/queryCustomerForPage.do")
    @ResponseBody
    public Object queryCustomerForPage(String name,String owner,String phone,String website,int pageNo,int pageSize){
        //计算起始页
        int beginNo = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        Map<String,Object> retMap = new HashMap<>();
        try {
            List<Customer> customerList = customerService.queryCustomerForPage(map);
            int totalRows = customerService.queryCountCustomerForPage(map);
            retMap.put("customerList",customerList);
            retMap.put("totalRows",totalRows);
        }catch (Exception e){
            e.printStackTrace();
        }
        return retMap;
    }

    @RequestMapping("/workbench/customer/saveNewCustomer.do")
    @ResponseBody
    public Object saveNewCustomer(Customer customer, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customer.setId(UUIDUtils.getUUID());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DataUtils.formatDataTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = customerService.saveNewCustomer(customer);
            if (i==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("创建失败,请稍后再试!");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("创建失败!");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/customer/deleteCustomerByIds.do")
    @ResponseBody
    public Object deleteCustomerByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = customerService.deleteCustomerByIds(id);
            if (i==id.length){
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
