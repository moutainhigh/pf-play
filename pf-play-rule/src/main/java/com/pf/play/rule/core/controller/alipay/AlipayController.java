package com.pf.play.rule.core.controller.alipay;

import com.alibaba.fastjson.JSON;
import com.pf.play.common.alipay.Alipay;
import com.pf.play.common.alipay.model.AlipayModel;
import com.pf.play.common.utils.JsonResult;
import com.pf.play.common.utils.SignUtil;
import com.pf.play.common.utils.StringUtil;
import com.pf.play.model.protocol.request.alipay.RequestAlipay;
import com.pf.play.model.protocol.request.trade.RequestTrade;
import com.pf.play.model.protocol.response.ResponseEncryptionJson;
import com.pf.play.rule.PublicMethod;
import com.pf.play.rule.core.common.exception.ExceptionMethod;
import com.pf.play.rule.core.common.utils.constant.ServerConstant;
import com.pf.play.rule.core.model.region.RegionModel;
import com.pf.play.rule.core.model.strategy.StrategyModel;
import com.pf.play.rule.core.model.stream.StreamConsumerModel;
import com.pf.play.rule.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 阿里支付的Controller层
 * @Author yoko
 * @Date 2019/12/19 19:40
 * @Version 1.0
 */
@RestController
@RequestMapping("/play/ali")
public class AlipayController {
    private static Logger log = LoggerFactory.getLogger(AlipayController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;

    @Value("${alipay.notify.url}")
    private String alipayNotifyUrl;


    /**
     * @Description: 阿里支付：生成订单码
     * <p>
     *     把调用阿里支付宝的类生成的订单码返回给客户端
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/play/ali/sendAli
     * 请求的属性类:RequestAlipay
     * 必填字段:{"agtVer":1,"clientVer":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:ctime+cctime+token+秘钥=sign
     * 服务端加密字段:aliOrder+stime+token+秘钥=sign
     */
    @RequestMapping(value = "/sendAli", method = {RequestMethod.POST})
//    public JsonResult<Object> sendAli(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
    public JsonResult<Object> sendAli(HttpServletRequest request, HttpServletResponse response, @RequestParam String jsonData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getSgid();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long memberId = 0;
        RegionModel regionModel = PublicMethod.assembleRegionModel(ip);
        RequestAlipay requestAlipay = new RequestAlipay();
        try{
//            String tempToken = "111111";
//            ComponentUtil.redisService.set(tempToken, "3");
            // 解密
            data = StringUtil.decoderBase64(jsonData);
            requestAlipay  = JSON.parseObject(data, RequestAlipay.class);
            // check校验数据、校验用户是否登录、获得用户ID
            memberId = PublicMethod.checkAlipayData(requestAlipay);
            token = requestAlipay.getToken();
            // 校验ctime
            // 校验sign
            String totalAmount = "";
            if (StringUtils.isBlank(requestAlipay.totalAmount)){
                // 默认实名认证的支付金额
                StrategyModel strategyQuery = PublicMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.STG_ALIPAY_MONEY.getStgType());
                StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                totalAmount = PublicMethod.checkAlipayMoney(strategyModel);
            }

            // 调用阿里云支付宝生成订单
            AlipayModel alipayModel = PublicMethod.assembleAlipayData(requestAlipay, sgid, totalAmount);
            String aliOrder = Alipay.createAlipaySend(alipayModel, alipayNotifyUrl);
            PublicMethod.checkAliOrder(aliOrder);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(aliOrder, stime, token, secretKeySign); // aliOrder+stime+token+秘钥=sign
            String strData = PublicMethod.assembleAlipayResult(stime, token, sign, aliOrder);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 用户注册完毕则直接让用户处于登录状态
            ComponentUtil.redisService.set(token, String.valueOf(memberId), FIFTEEN_MIN, TimeUnit.SECONDS);
            // 添加流水
            StreamConsumerModel streamConsumerModel = PublicMethod.assembleStream(sgid, cgid, memberId, regionModel, requestAlipay, ServerConstant.InterfaceEnum.ALIPAY_SENDALI.getType(),
                    ServerConstant.InterfaceEnum.ALIPAY_SENDALI.getDesc(), null, data, strData, null);
            ComponentUtil.streamConsumerService.addVisit(streamConsumerModel);
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            // 添加异常
            StreamConsumerModel streamConsumerModel = PublicMethod.assembleStream(sgid, cgid, memberId, regionModel, requestAlipay, ServerConstant.InterfaceEnum.ALIPAY_SENDALI.getType(),
                    ServerConstant.InterfaceEnum.ALIPAY_SENDALI.getDesc(), null, data, null, map);
            ComponentUtil.streamConsumerService.addError(streamConsumerModel);
            log.error(String.format("this AlipayController.sendAli() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }

}
