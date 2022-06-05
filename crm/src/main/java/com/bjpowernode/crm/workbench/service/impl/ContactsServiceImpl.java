package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ContactsServiceImpl implements ContactsService {

    @Resource
    private ContactsMapper contactsMapper;

    @Override
    public List<Contacts> queryContactsForPage(Map<String, Object> map) {
        return contactsMapper.selectContactsForPage(map);
    }

    @Override
    public int queryCountContactsForPage(Map<String, Object> map) {
        return contactsMapper.selectCountContactsForPage(map);
    }

    @Override
    public List<Contacts> queryContactsForCreateTran(String contectsName) {
        return contactsMapper.selectContactsForCreateTran(contectsName);
    }
}
