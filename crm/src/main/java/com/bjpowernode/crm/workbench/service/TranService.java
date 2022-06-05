package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.FinalVo;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {

    List<Tran> queryTranForPage(Map<String,Object> map);
    int queryCountTranForPage(Map<String,Object> map);

    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    int editTranById(Map<String,Object> map);

    List<FinalVo> queryCountOfTranGroupByStage();
}
