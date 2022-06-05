package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("TranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {

    @Resource
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public List<TranRemark> queryTranRemarkForDetailById(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailById(tranId);
    }

    @Override
    public int saveTranRemark(TranRemark tranRemark) {
        return tranRemarkMapper.insertTranRemark(tranRemark);
    }
}
