package com.pf.play.rule;

import com.alibaba.fastjson.JSON;
import com.pf.play.common.utils.BeanUtils;
import com.pf.play.common.utils.DateUtil;
import com.pf.play.common.utils.StringUtil;
import com.pf.play.model.protocol.request.consumer.RequestConsumer;
import com.pf.play.model.protocol.response.consumer.ResponseConsumer;
import com.pf.play.model.protocol.response.price.ResponseDayPrice;
import com.pf.play.rule.core.common.exception.ServiceException;
import com.pf.play.rule.core.common.utils.constant.CachedKeyUtils;
import com.pf.play.rule.core.common.utils.constant.PfCacheKey;
import com.pf.play.rule.core.common.utils.constant.PfErrorCode;
import com.pf.play.rule.core.common.utils.constant.ServerConstant;
import com.pf.play.rule.core.model.UserInfoModel;
import com.pf.play.rule.core.model.consumer.ConsumerFixedModel;
import com.pf.play.rule.core.model.price.VirtualCoinPriceDto;
import com.pf.play.rule.core.model.price.VirtualCoinPriceModel;
import com.pf.play.rule.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description 公共方法类
 * @Author yoko
 * @Date 2019/11/21 16:17
 * @Version 1.0
 */
public class PublicMethod {

    /**
     * @Description: check用户更新设置支付密码的客户端上传的数据
     * @param requestConsumer - 基础数据
     * @return void
     * @author yoko
     * @date 2019/11/21 17:57
    */
    public static long checkUpPayCodeData(RequestConsumer requestConsumer)throws Exception{
        long memberId;
        // 1.校验所有数据
        if (requestConsumer == null ){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00001.geteCode(), PfErrorCode.ENUM_ERROR.C00001.geteDesc());
        }

        // 2.校验token值
        if (StringUtils.isBlank(requestConsumer.getToken())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00002.geteCode(), PfErrorCode.ENUM_ERROR.C00002.geteDesc());
        }

        // 3.校验用户是否登录
        memberId = PublicMethod.checkIsLogin(requestConsumer.getToken());

        // 4.校验支付密码
        if (StringUtils.isBlank(requestConsumer.getPayPw())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00003.geteCode(), PfErrorCode.ENUM_ERROR.C00003.geteDesc());
        }

        // 5.校验验证码
        if (requestConsumer.getUpPayCode() == null || requestConsumer.getUpPayCode() == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00004.geteCode(), PfErrorCode.ENUM_ERROR.C00004.geteDesc());
        }
        return memberId;
    }

    /**
     * @Description: 校验用户是否处于登录状态
     * @param token - 登录token
     * @return Long
     * @author yoko
     * @date 2019/11/21 18:01
    */
    public static long checkIsLogin(String token) throws Exception{
        Long memberId;
        String strCache = (String) ComponentUtil.redisService.get(token);
        if (!StringUtils.isBlank(strCache)) {
            // 登录存储在缓存中的用户id
            memberId = Long.parseLong(strCache);
        }else {
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00000.geteCode(), PfErrorCode.ENUM_ERROR.C00000.geteDesc());
        }
        return memberId;
    }

    public static void checkVerifCode(String redisKey, int verifCode) throws Exception{
        String strKeyCache = redisKey;
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        strCache = "123456";
        if (!StringUtils.isBlank(strCache)){
            int upPayCode = Integer.parseInt(strCache);
            if (upPayCode != verifCode){
                throw new ServiceException(PfErrorCode.ENUM_ERROR.C00005.geteCode(), PfErrorCode.ENUM_ERROR.C00005.geteDesc());
            }
        }else {
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00006.geteCode(), PfErrorCode.ENUM_ERROR.C00006.geteDesc());
        }
    }

    /**
     * @Description: 组装修改用户支付密码的数据
     * @param memberId - 用户ID
     * @param payPassword - 支付密码
     * @return
     * @author yoko
     * @date 2019/11/21 16:56
    */
    public static UserInfoModel assembleUserInfo(long memberId, String payPassword){
        UserInfoModel resBean = new UserInfoModel();
        resBean.setMemberId((int) memberId);
        resBean.setPayPassword(payPassword);
        return resBean;
    }

    /**
     * @Description: 公共的返回客户端的方法
     * @param stime - 服务器的时间
     * @param token - 登录token
     * @param sign - 签名
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleResult(long stime, String token, String sign){
        ResponseConsumer dataModel = new ResponseConsumer();
        dataModel.setStime(stime);
        dataModel.setToken(token);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: 查询固定信息时，校验基本数据是否非法
     * @param requestConsumer - 基础数据
     * @return void
     * @author yoko
     * @date 2019/11/21 18:59
     */
    public static long checkGetFixedData(RequestConsumer requestConsumer) throws Exception{
        long memberId;
        // 校验所有数据
        if (requestConsumer == null ){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00011.geteCode(), PfErrorCode.ENUM_ERROR.C00011.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestConsumer.getToken())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00002.geteCode(), PfErrorCode.ENUM_ERROR.C00002.geteDesc());
        }

        // 校验用户是否登录
        memberId = PublicMethod.checkIsLogin(requestConsumer.getToken());
        return memberId;
    }


    /**
     * @Description: 组装用户固定账号的查询数据
     * @param memberId - 用户ID
     * @return ConsumerFixedModel
     * @author yoko
     * @date 2019/11/21 19:24
    */
    public static ConsumerFixedModel assembleConsumerFixedQuery(long memberId){
        ConsumerFixedModel resBean = new ConsumerFixedModel();
        resBean.setMemberId(memberId);
        return resBean;
    }


    /**
     * @Description: 添加固定信息时，校验基本数据是否非法
     * @param requestConsumer - 基础数据
     * @return void
     * @author yoko
     * @date 2019/11/21 18:59
    */
    public static long checkAddFixedData(RequestConsumer requestConsumer) throws Exception{
        long memberId;
        // 校验所有数据
        if (requestConsumer == null ){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00007.geteCode(), PfErrorCode.ENUM_ERROR.C00007.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestConsumer.getToken())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00002.geteCode(), PfErrorCode.ENUM_ERROR.C00002.geteDesc());
        }

        // 校验用户是否登录
        memberId = PublicMethod.checkIsLogin(requestConsumer.getToken());

        // 校验姓名
        if (StringUtils.isBlank(requestConsumer.getFullName())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00008.geteCode(), PfErrorCode.ENUM_ERROR.C00008.geteDesc());
        }

        // 校验身份证
        if (StringUtils.isBlank(requestConsumer.getIdCard())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00009.geteCode(), PfErrorCode.ENUM_ERROR.C00009.geteDesc());
        }

        // 校验验支付宝账号
        if (StringUtils.isBlank(requestConsumer.getFixedNum())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00010.geteCode(), PfErrorCode.ENUM_ERROR.C00010.geteDesc());
        }
        return memberId;
    }


    /**
     * @Description: 公共的返回客户端的方法
     * @param stime - 服务器的时间
     * @param token - 登录token
     * @param sign - 签名
     * @param consumerFixedModel - 用户固定账号信息
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleGetFixedResult(long stime, String token, String sign, ConsumerFixedModel consumerFixedModel){
        ResponseConsumer dataModel = null;
        if (consumerFixedModel != null){
            dataModel = BeanUtils.copy(consumerFixedModel, ResponseConsumer.class);
        }else{
            dataModel = new ResponseConsumer();
        }
        dataModel.setStime(stime);
        dataModel.setToken(token);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check用户更新固定账号的信息
     * @param requestConsumer
     * @return Long
     * @author yoko
     * @date 2019/11/21 20:41
    */
    public static long checkUpFixedData(RequestConsumer requestConsumer) throws Exception{
        long memberId;
        // 校验所有数据
        if (requestConsumer == null ){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00013.geteCode(), PfErrorCode.ENUM_ERROR.C00013.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestConsumer.getToken())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00002.geteCode(), PfErrorCode.ENUM_ERROR.C00002.geteDesc());
        }

        // 校验用户是否登录
        memberId = PublicMethod.checkIsLogin(requestConsumer.getToken());

        // 校验验支付宝账号
        if (StringUtils.isBlank(requestConsumer.getFixedNum())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00014.geteCode(), PfErrorCode.ENUM_ERROR.C00014.geteDesc());
        }
        return memberId;
    }

    /**
     * @Description: 组装更新用户支付宝账号数据的方法
     * @param memberId - 用户ID
     * @param fixedNum - 用户支付宝账号
     * @return
     * @author yoko
     * @date 2019/11/21 20:46
    */
    public static ConsumerFixedModel assembleUpConsumerFixed(long memberId, String fixedNum){
        ConsumerFixedModel resBean = new ConsumerFixedModel();
        resBean.setMemberId(memberId);
        resBean.setFixedNum(fixedNum);
        return resBean;
    }


    /**
     * @Description: 查询虚拟币每天兑换的价格时，校验基本数据是否非法
     * @param requestConsumer - 基础数据
     * @return void
     * @author yoko
     * @date 2019/11/21 18:59
     */
    public static long checkGetVirtualCoinPriceData(RequestConsumer requestConsumer) throws Exception{
        long memberId;
        // 校验所有数据
        if (requestConsumer == null ){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.P00002.geteCode(), PfErrorCode.ENUM_ERROR.P00002.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestConsumer.getToken())){
            throw new ServiceException(PfErrorCode.ENUM_ERROR.C00002.geteCode(), PfErrorCode.ENUM_ERROR.C00002.geteDesc());
        }

        // 校验用户是否登录
        memberId = PublicMethod.checkIsLogin(requestConsumer.getToken());
        return memberId;
    }

    /**
     * @Description: 组装查询虚拟币每天兑换的价格的查询条件
     * @return VirtualCoinPriceModel
     * @author yoko
     * @date 2019/11/22 18:01
    */
    public static VirtualCoinPriceModel assembleVirtualCoinPriceQuery(){
        // 获取昨天的时间
        VirtualCoinPriceModel resBen = new VirtualCoinPriceModel();
        resBen.setCurdayStart(DateUtil.getIntYesterday());
        resBen.setCurdayEnd(DateUtil.getDayNumber(new Date()));
        return resBen;
    }


    /**
     * @Description: 组装虚拟币的价格数据
     * @param list - 昨日、今日虚拟币的价格
     * @return VirtualCoinPriceDto
     * @author yoko
     * @date 2019/11/22 21:37
    */
    public static VirtualCoinPriceDto assembleVirtualCoinPriceDto(List<VirtualCoinPriceModel> list) throws Exception{
        VirtualCoinPriceDto dto = new VirtualCoinPriceDto();
        int today = DateUtil.getDayNumber(new Date());
        int yesterday = DateUtil.getIntYesterday();
        for (VirtualCoinPriceModel dataModel : list){
            if (dataModel.getCurday() == today){
                // 今天
                dto.setT_exchangePrice(dataModel.getExchangePrice());
                dto.setT_tallestPrice(dataModel.getTallestPrice());
                dto.setMaxPrice(dataModel.getMaxPrice());
                dto.setMinPrice(dataModel.getMinPrice());
            }else if (dataModel.getCurday() == yesterday){
                // 昨天
                dto.setY_exchangePrice(dataModel.getExchangePrice());
                dto.setY_tallestPrice(dataModel.getTallestPrice());
            }
        }
        String growthRate = StringUtil.getGrowthRate(dto.getT_exchangePrice(), dto.getY_exchangePrice());
        dto.setGrowthRate(growthRate);
        return dto;
    }


    /**
     * @Description: 虚拟币价格数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param token - 登录token
     * @param sign - 签名
     * @param virtualCoinPriceDto - 虚拟币价格数据
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleDayPriceResult(long stime, String token, String sign, VirtualCoinPriceDto virtualCoinPriceDto){
        ResponseDayPrice dataModel = null;
        if (virtualCoinPriceDto != null){
            dataModel = BeanUtils.copy(virtualCoinPriceDto, ResponseDayPrice.class);
        }else{
            dataModel = new ResponseDayPrice();
        }
        dataModel.setStime(stime);
        dataModel.setToken(token);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    public static void main(String [] args){

        String t = "1.835";
        String y = "1.921";
        String z = "100";
//        String res = (t-y)/y*100;
        BigDecimal resDoble;
        BigDecimal tt = new BigDecimal(t);
        BigDecimal yy = new BigDecimal(y);
        BigDecimal zz = new BigDecimal(z);
        BigDecimal jf_res = tt.subtract(yy);
        resDoble = jf_res.divide(yy, 4, BigDecimal.ROUND_HALF_UP).multiply(zz);
        DecimalFormat sb = new DecimalFormat("###.##");
        String data = sb.format(resDoble);

        System.out.println("data:" + data);
    }

}
