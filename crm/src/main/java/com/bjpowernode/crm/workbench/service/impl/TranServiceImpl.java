package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.FinalVo;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("TranService")
public class TranServiceImpl implements TranService {

    @Resource
    private TranMapper tranMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<Tran> queryTranForPage(Map<String, Object> map) {
        return tranMapper.selectTranForPage(map);
    }

    @Override
    public int queryCountTranForPage(Map<String, Object> map) {
        return tranMapper.selectCountTranForPage(map);
    }

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerName");
        User user = (User) map.get(Contants.SESSION_USER);
        //精确查询name
        Customer customer = customerMapper.selectCustomerByCustomerName(customerName);
        //判断客户是否存在
        if (customer==null){
            customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateBy(user.getId());
            customer.setOwner(user.getId());
            customer.setCreateTime(DataUtils.formatDataTime(new Date()));
            customer.setName(customerName);
            customerMapper.insertNewCustomer(customer);
        }
        //保存创建交易
        Tran tran = new Tran();
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtils.getUUID());
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DataUtils.formatDataTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));

        tranMapper.insertTran(tran);
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DataUtils.formatDataTime(new Date()));
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        //保存交易历史
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public int editTranById(Map<String, Object> map) {
        return tranMapper.updateTranById(map);
    }

    @Override
    public List<FinalVo> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
