package com.pf.play.common.alipay.model;

/**
 * @Description 阿里支付的实体bean
 * @Author yoko
 * @Date 2019/12/19 17:53
 * @Version 1.0
 */
public class AlipayModel {
    /**
     * 订单内容；
     * 我是测试数据
     */
    public String body;

    /**
     *主题
     */
    public String subject;

    /**
     * 交易订单号
     */
    public String outTradeNo;

    /**
     * 超时时间
     * 30m（30分钟）
     */
    public String timeoutExpress;

    /**
     * 总金额
     */
    public String totalAmount;

    /**
     * 商品编码
     */
    public String productCode;

    /**
     * 订单状态：1正常，2超时，3完成交易
     */
    public Integer outTradeStatus;

    /**
     * 创建日期：存的日期格式20160530
     */
    public Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    public Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    public Integer curminute;

    /**
     * 阿里返回的订单串
     */
    public String aliOrder;



    public AlipayModel(){

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    public Integer getOutTradeStatus() {
        return outTradeStatus;
    }

    public void setOutTradeStatus(Integer outTradeStatus) {
        this.outTradeStatus = outTradeStatus;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public String getAliOrder() {
        return aliOrder;
    }

    public void setAliOrder(String aliOrder) {
        this.aliOrder = aliOrder;
    }
}
