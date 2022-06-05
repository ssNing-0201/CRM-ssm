package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String id);

    int saveClueRemark(ClueRemark clueRemark);

    int deleteClueRemarkById(String id);
}
