package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {

    int saveCreatClue(Clue clue);

    List<Clue> queryClueForPage(Map<String,Object> map);
    int queryClueForPageTotalRows(Map<String,Object> map);

    int editClueById(Clue clue);
    Clue queryClueById(String id);

    int deleteClueByIds(String[] id);

    Clue queryClueForDetailById(String id);

    Clue queryClueForConvertById(String id);

    void saveConvert(Map<String,Object> map);
}
