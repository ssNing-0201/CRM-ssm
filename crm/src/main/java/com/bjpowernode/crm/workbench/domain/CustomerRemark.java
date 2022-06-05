package com.bjpowernode.crm.workbench.domain;

public class CustomerRemark {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.id
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.noteContent
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String noteContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.createBy
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.createTime
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.editBy
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String editBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.editTime
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String editTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.editFlag
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String editFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer_remark.customerId
     *
     * @mbg.generated Wed Jun 01 19:04:33 CST 2022
     */
    private String customerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerRemark{" +
                "id='" + id + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", editBy='" + editBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editFlag='" + editFlag + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}