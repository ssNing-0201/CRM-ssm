package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Thu May 26 09:20:29 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Thu May 26 09:20:29 CST 2022
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Thu May 26 09:20:29 CST 2022
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Thu May 26 09:20:29 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Thu May 26 09:20:29 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Thu May 26 09:20:29 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    /**
     * 批量保存关联的市场活动和线索的关系
     * @param list
     * @return
     */
    int insertClueActivityRelationByList(List<ClueActivityRelation> list);

    /**
     * 删除关联市场活动
     * @param clueActivityRelation
     * @return
     */
    int deleteClueActivityByCIdAId(ClueActivityRelation clueActivityRelation);

    /**
     * 根据clueId查询线索和市场活动的关联关系
     * @param id
     * @return
     */
    List<ClueActivityRelation> selectClueActivityRelationByClueId(String id);

    /**
     * 根据clueId删除线索和市场关连信息
     * @param id
     * @return
     */
    int deleteClueActivityRelationByClueId(String id);
}