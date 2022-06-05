package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityMapper activityMapper;

    @Override
    public int saveCreatActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }
    @Override
    public int selectCountOfActivityByCondition(Map<String,Object> map){
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] id) {
        return activityMapper.deleteActivityByIds(id);
    }

    @Override
    public Activity queryActivityByID(String id) {
        return activityMapper.selectActivityByID(id);
    }

    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateByPrimaryKeySelective(activity);
    }

    @Override
    public List<Activity> selectActivityAll() {
        return activityMapper.selectActivity();
    }

    @Override
    public List<Activity> selectActivityById(String[] id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveCreatActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }

    @Override
    public List<Activity> queryActivityForDetailByClueId(String id) {
        return activityMapper.selectActivityForDetailByClueId(id);
    }

    @Override
    public List<Activity> queryActivityForDetailByNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }

    @Override
    public List<Activity> queryActivityForDetailByIds(String[] ids) {
        return activityMapper.selectActivityForDetailByIds(ids);
    }

    @Override
    public List<Activity> queryActivityForDetailByNameAndClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameAndClueId(map);
    }

    @Override
    public List<Activity> queryActivityForDetailByName(String activityName) {
        return activityMapper.selectActivityForDetailByName(activityName);
    }


}
