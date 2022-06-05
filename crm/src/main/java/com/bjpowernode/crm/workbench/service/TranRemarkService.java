package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkService {

    List<TranRemark> queryTranRemarkForDetailById(String tranId);

    int saveTranRemark(TranRemark tranRemark);
}
