package com.pf.play.model.protocol.response.uesr;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/12/13 9:45
 * @Version 1.0
 */
public class MasonryList {
    /**
     * 类型: 1、完成任务 2、活力值奖励 3、赠送给Ta  4、Ta人赠送 5、转出手续费 6、转入手续费 7、交易所转入 8、转出砖石 9、购买任务消耗
     *
     * @mbggenerated
     */
    private Integer type;
    /**
     * 类型值: 1、完成任务 2、活力值奖励 3、赠送给Ta  4、Ta人赠送 5、转出手续费 6、转入手续费 7、交易所转入 8、转出砖石 9、购买任务消耗
     *
     * @mbggenerated
     */
    private String  typeValue;


    /**
     * 符号类型:1、加  2 减
     *
     * @mbggenerated
     */
    private Integer symbolType;

    /**
     * 砖石数
     *
     * @mbggenerated
     */
    private Double masonryNum;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private String createTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public Integer getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(Integer symbolType) {
        this.symbolType = symbolType;
    }

    public Double getMasonryNum() {
        return masonryNum;
    }

    public void setMasonryNum(Double masonryNum) {
        this.masonryNum = masonryNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
