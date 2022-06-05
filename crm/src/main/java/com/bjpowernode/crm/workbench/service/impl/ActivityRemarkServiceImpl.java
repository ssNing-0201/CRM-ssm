package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Resource
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String id) {
        return activityRemarkMapper.selectActivityRemarkForDetailById(id);
    }

    @Override
    public int saveCreatActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertActivityRemark(activityRemark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateActivityRemarkById(ActivityRemark remark) {
        return activityRemarkMapper.updateByPrimaryKeySelective(remark);
    }
}
