package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsService {

    List<Contacts> queryContactsForPage(Map<String,Object> map);

    int queryCountContactsForPage(Map<String,Object> map);

    List<Contacts> queryContactsForCreateTran(String contectsName);
}
