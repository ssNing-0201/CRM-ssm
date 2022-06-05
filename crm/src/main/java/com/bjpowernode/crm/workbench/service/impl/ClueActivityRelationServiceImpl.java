package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ClueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {

    @Resource
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueActivityRelationByList(list);
    }

    @Override
    public int deleteClueActivityByCIdAId(ClueActivityRelation clueActivityRelation) {
        return clueActivityRelationMapper.deleteClueActivityByCIdAId(clueActivityRelation);
    }
}
