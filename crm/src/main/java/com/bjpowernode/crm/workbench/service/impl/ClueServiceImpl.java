package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("ClueService")
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueMapper clueMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private ContactsMapper contactsMapper;
    @Resource
    private ClueRemarkMapper clueRemarkMapper;
    @Resource
    private CustomerRemarkMapper customerRemarkMapper;
    @Resource
    private ContactsRemarkMapper contactsRemarkMapper;
    @Resource
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Resource
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Resource
    private TranMapper tranMapper;
    @Resource
    private TranRemarkMapper tranRemarkMapper;
    @Resource
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public int saveCreatClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueForPage(Map<String, Object> map) {
        return clueMapper.selectClueForPage(map);
    }

    @Override
    public int queryClueForPageTotalRows(Map<String, Object> map) {
        return clueMapper.selectClueForPageTotalRows(map);
    }

    @Override
    public int editClueById(Clue clue) {
        return clueMapper.updateClueById(clue);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int deleteClueByIds(String[] id) {
        return clueMapper.deleteClueByIds(id);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public Clue queryClueForConvertById(String id) {
        return clueMapper.selectClueForConvertById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Contants.SESSION_USER);
        //??????Id???????????????
        Clue clue = clueMapper.selectClueForConvertById(clueId);
        //???????????????????????????????????????????????????
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DataUtils.formatDataTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        //??????mapper????????????
        customerMapper.insertCustomer(customer);

        //??????????????????????????????????????????????????????
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DataUtils.formatDataTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullName(clue.getFullName());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        //??????mapper????????????
        contactsMapper.insertSelective(contacts);

        //????????????????????????????????????????????????
        List<ClueRemark> remarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        if (remarkList!=null && remarkList.size()>0){
            //???????????????????????????
            List<CustomerRemark> curList = new ArrayList<>();
            CustomerRemark customerRemark = null;
            //??????????????????
            List<ContactsRemark> corList = new ArrayList<>();
            ContactsRemark contactsRemark = null;
            for (ClueRemark c:remarkList){
                customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setCreateBy(c.getCreateBy());
                customerRemark.setNoteContent(c.getNoteContent());
                customerRemark.setCreateTime(c.getCreateTime());
                customerRemark.setEditFlag(c.getEditFlag());
                customerRemark.setEditBy(c.getEditBy());
                customerRemark.setEditTime(c.getEditTime());
                curList.add(customerRemark);
                //
                contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(c.getCreateBy());
                contactsRemark.setNoteContent(c.getNoteContent());
                contactsRemark.setCreateTime(c.getCreateTime());
                contactsRemark.setEditFlag(c.getEditFlag());
                contactsRemark.setEditBy(c.getEditBy());
                contactsRemark.setEditTime(c.getEditTime());
                corList.add(contactsRemark);

            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }

        //??????clueId?????????????????????????????????????????????
        List<ClueActivityRelation> carlist = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        if (carlist!=null && carlist.size()>0){
            ContactsActivityRelation contactsActivityRelation = null;
            List<ContactsActivityRelation> conarList = new ArrayList<>();
            for (ClueActivityRelation c:carlist){
                contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelation.setActivityId(c.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                conarList.add(contactsActivityRelation);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(conarList);
        }
        //????????????????????????
        if ("true".equals(map.get("isCreateTran"))){
            Tran tran = new Tran();
            tran.setActivityId((String) map.get("activityId"));
            tran.setMoney((String) map.get("money"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DataUtils.formatDataTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));

            tranMapper.insertTran(tran);
            //??????????????????
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtils.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setCreateBy(user.getId());
            tranHistory.setCreateTime(DataUtils.formatDataTime(new Date()));
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            //??????????????????
            tranHistoryMapper.insertTranHistory(tranHistory);

            TranRemark tranRemark = null;
            List<TranRemark> tList = new ArrayList<>();
            if (remarkList!=null && remarkList.size()>0){
                for (ClueRemark c:remarkList){
                    tranRemark = new TranRemark();
                    tranRemark.setId(UUIDUtils.getUUID());
                    tranRemark.setTranId(tran.getId());
                    tranRemark.setCreateBy(c.getCreateBy());
                    tranRemark.setNoteContent(c.getNoteContent());
                    tranRemark.setCreateTime(c.getCreateTime());
                    tranRemark.setEditFlag(c.getEditFlag());
                    tranRemark.setEditBy(c.getEditBy());
                    tranRemark.setEditTime(c.getEditTime());
                    tList.add(tranRemark);
                }
                tranRemarkMapper.insertTranRemarkByList(tList);
            }
        }

        //????????????????????????
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //???????????????????????????????????????
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //????????????
        clueMapper.deleteClueForConvertById(clueId);


    }
}
