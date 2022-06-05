package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<Customer> queryCustomerForPage(Map<String,Object> map);

    int queryCountCustomerForPage(Map<String,Object> map);

    int saveNewCustomer(Customer customer);

    int deleteCustomerByIds(String[] id);

    List<Customer> queryCustomerByName(String name);

    List<String> queryAllCustomerName(String customerName);

}
