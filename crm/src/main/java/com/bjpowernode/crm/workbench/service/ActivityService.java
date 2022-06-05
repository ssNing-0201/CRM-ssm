package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;


public interface ActivityService {
    int saveCreatActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int selectCountOfActivityByCondition(Map<String, Object> map);

    int deleteActivityByIds(String[] id);

    Activity queryActivityByID(String id);

    int updateActivityById(Activity activity);

    List<Activity> selectActivityAll();

    List<Activity> selectActivityById(String[] id);

    int saveCreatActivityByList(List<Activity> activityList);

    Activity queryActivityForDetailById (String id);

    List<Activity> queryActivityForDetailByClueId(String id);

    List<Activity> queryActivityForDetailByNameClueId(Map<String,Object> map);

    List<Activity> queryActivityForDetailByIds(String[] ids);

    List<Activity> queryActivityForDetailByNameAndClueId(Map<String,Object> map);

    List<Activity> queryActivityForDetailByName(String activityName);


}
