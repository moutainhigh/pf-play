package com.pf.play.rule.core.model;

import java.util.Date;

public class DisPlayReward {
    /**
     * 自增id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 玩法id
     *
     * @mbggenerated
     */
    private String playId;

    /**
     * 玩法名称
     *
     * @mbggenerated
     */
    private String playName;

    /**
     * 表名备注
     *
     * @mbggenerated
     */
    private String tableRemarks;

    /**
     * 表名值
     *
     * @mbggenerated
     */
    private String tableValue;

    /**
     * 条件值
     *
     * @mbggenerated
     */
    private String whereValue;

    /**
     * 字段名字
     *
     * @mbggenerated
     */
    private String attributeName;

    /**
     * 奖励周期是指 1、分钟 2、小时 3、天 
     *
     * @mbggenerated
     */
    private Integer rewardType;

    /**
     * 表字段
     *
     * @mbggenerated
     */
    private String attributeValue;

    /**
     * 添加时间数据格式
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * 修改时间数据格式
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     * 是否有效; 1有效 2无效
     *
     * @mbggenerated
     */
    private Integer isValid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayId() {
        return playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getTableRemarks() {
        return tableRemarks;
    }

    public void setTableRemarks(String tableRemarks) {
        this.tableRemarks = tableRemarks;
    }

    public String getTableValue() {
        return tableValue;
    }

    public void setTableValue(String tableValue) {
        this.tableValue = tableValue;
    }

    public String getWhereValue() {
        return whereValue;
    }

    public void setWhereValue(String whereValue) {
        this.whereValue = whereValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}