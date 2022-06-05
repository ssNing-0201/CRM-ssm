package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> queryCustomerForPage(Map<String, Object> map) {
        return customerMapper.selectCustomerForPage(map);
    }

    @Override
    public int queryCountCustomerForPage(Map<String, Object> map) {
        return customerMapper.selectCountCustomerForPage(map);
    }

    @Override
    public int saveNewCustomer(Customer customer) {
        return customerMapper.insertNewCustomer(customer);
    }

    @Override
    public int deleteCustomerByIds(String[] id) {
        return customerMapper.deleteCustomerByIds(id);
    }

    @Override
    public List<Customer> queryCustomerByName(String name) {
        return customerMapper.selectCustomerByName(name);
    }

    @Override
    public List<String> queryAllCustomerName(String customerName) {
        return customerMapper.selectAllCustomerName(customerName);
    }
}
