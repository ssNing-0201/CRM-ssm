package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ClueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Resource
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String id) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(id);
    }

    @Override
    public int saveClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueById(id);
    }
}
