package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.FinalVo;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ChartController {

    @Resource
    private TranService tranService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ClueService clueService;
    @Resource
    private CustomerService customerService;

    @RequestMapping("/workbench/chart/activity/indexActivityChart.do")
    public String indexActivityChart(){
        return "workbench/chart/activity/index";
    }

    @RequestMapping("/workbench/chart/clue/indexClueChart.do")
    public String indexClueChart(){
        return "workbench/chart/clue/index";
    }

    @RequestMapping("/workbench/chart/customerAndContacts/indexCustomerAndContactsChart.do")
    public String indexCustomerAndContactsChart(){
        return "workbench/chart/customerAndContacts/index";
    }

    @RequestMapping("/workbench/chart/transaction/indexTransactionChart.do")
    public String indexTransactionChart(){
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStage(){
        List<FinalVo> finalVoList = tranService.queryCountOfTranGroupByStage();
        return finalVoList;
    }



}
