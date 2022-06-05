package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbg.generated Wed Jun 01 16:24:03 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbg.generated Wed Jun 01 16:24:03 CST 2022
     */
    int insert(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbg.generated Wed Jun 01 16:24:03 CST 2022
     */
    int insertSelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbg.generated Wed Jun 01 16:24:03 CST 2022
     */
    Customer selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbg.generated Wed Jun 01 16:24:03 CST 2022
     */
    int updateByPrimaryKeySelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbg.generated Wed Jun 01 16:24:03 CST 2022
     */
    int updateByPrimaryKey(Customer record);

    /**
     * 保存创建的客户
     * @param customer
     * @return
     */
    int insertCustomer(Customer customer);

    /**
     * 给客户界面分页查询做功能
     * @return
     */
    List<Customer> selectCustomerForPage(Map<String,Object> map);
    int selectCountCustomerForPage(Map<String,Object> map);

    /**
     * 添加新的客户功能
     * @param customer
     * @return
     */
    int insertNewCustomer(Customer customer);

    int deleteCustomerByIds(String[] id);

    /**
     * 查询客户名支持自动补全
     * @param name
     * @return
     */
    List<Customer> selectCustomerByName(String name);

    /**
     * 给自动补全插件提供客户名称搜索
     * @param customerName
     * @return
     */
    List<String> selectAllCustomerName(String customerName);

    Customer selectCustomerByCustomerName(String name);
}