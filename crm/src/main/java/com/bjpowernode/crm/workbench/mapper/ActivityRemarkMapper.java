package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Sat May 21 10:23:15 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Sat May 21 10:23:15 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Sat May 21 10:23:15 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Sat May 21 10:23:15 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Sat May 21 10:23:15 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Sat May 21 10:23:15 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 根据activityId查询该市场活动下所有备注信息
     * @param id
     * @return
     */
    List<ActivityRemark> selectActivityRemarkForDetailById(String id);

    /**
     * 添加备注信息
     * @param activityRemark
     * @return
     */
    int insertActivityRemark(ActivityRemark activityRemark);
}