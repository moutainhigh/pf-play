package com.pf.play.rule.core.service.impl;

import com.pf.play.common.utils.*;
import com.pf.play.model.protocol.request.uesr.RegisterReq;
import com.pf.play.model.protocol.response.uesr.RegisterResp;
import com.pf.play.model.protocol.response.uesr.UserInfoResp;
import com.pf.play.rule.RegisterMethod;
import com.pf.play.rule.core.common.dao.BaseDao;
import com.pf.play.rule.core.common.exception.ServiceException;
import com.pf.play.rule.core.common.service.impl.BaseServiceImpl;
import com.pf.play.rule.core.common.utils.constant.CacheKey;
import com.pf.play.rule.core.common.utils.constant.CachedKeyUtils;
import com.pf.play.rule.core.common.utils.constant.Constant;
import com.pf.play.rule.core.common.utils.constant.ErrorCode;
import com.pf.play.rule.core.mapper.*;
import com.pf.play.rule.core.model.*;
import com.pf.play.rule.core.service.RegisterService;
import com.pf.play.rule.core.singleton.RegisterSingleton;
import com.pf.play.rule.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/8 14:03
 * @Version 1.0
 */
@Service
public class RegisterServicelmpl<T> extends BaseServiceImpl<T> implements RegisterService<T> {

    private static Logger log = LoggerFactory.getLogger(RegisterServicelmpl.class);

    private  Integer  SMS_REDIS_TIME =30;
    /**
     * 默认经验值
     */
    private Float  activeValue = Float.parseFloat("1");

    @Autowired
    private VcAccountRelationMapper vcAccountRelationMapper;

    @Autowired
    private VcThirdPartyMapper vcThirdPartyMapper;

    @Autowired
    private VcMemberMapper vcMemberMapper;

    @Autowired
    private VcMemberResourceMapper vcMemberResourceMapper;

    @Autowired
    private VcRewardReceiveMapper  vcRewardReceiveMapper ;

    @Autowired
    private VcMemberGiveMapper  vcMemberGiveMapper ;

    @Autowired
    private UserInfoMapper  userInfoMapper ;

    @Autowired
    private VcPhoneDeployMapper  vcPhoneDeployMapper ;


    @Override
    public BaseDao<T> getDao() {
        return vcAccountRelationMapper;
    }
    /**
     * @Description: 注册方法
     * @param registerReq
     * @return RegisterResp
     * @author long
     * @date 2019/11/12 19:34
     */
    @Override
    public UserInfoResp savaRegisterInfo(RegisterReq registerReq)throws Exception {
        UserInfoResp  userInfoResp = new UserInfoResp();
        boolean  flag = true;
        /**************用户信息是否注册*****************/
        flag = ComponentUtil.registerService.checkRegister(registerReq);
        if(!flag){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.R000001.geteCode(),ErrorCode.ENUM_ERROR.R000001.geteDesc());
        }

        VcMember  vcMember = null;
        /**************邀请码是否正确*****************/

        String  inviteCode ="";
        VcPhoneDeploy  vcPhoneDeploy =RegisterMethod.queryPhoneDeploy(registerReq.getPhone());
        List<VcPhoneDeploy> list =vcPhoneDeployMapper.selectByPrimaryKey(vcPhoneDeploy);
        for(VcPhoneDeploy  deploy :list){
            inviteCode = deploy.getInviteCode();
        }

        if(!inviteCode.equals("")){
            vcMember = ComponentUtil.registerService.checkInviteCode(inviteCode);
        }

        if(vcMember==null){
            vcMember = ComponentUtil.registerService.checkInviteCode(registerReq.getInviteCode());
        }

//        }else{

//        }
        if(null==vcMember){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.R000002.geteCode(),ErrorCode.ENUM_ERROR.R000002.geteDesc());
        }

        /**************申请新的邀请码*****************/
        String   strName  = ComponentUtil.registerService.createInviteCode(registerReq.getPhone());
        String[]   InviteAdd  = strName.split(",");
        if(InviteAdd.length!=3){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.R000003.geteCode(),ErrorCode.ENUM_ERROR.R000003.geteDesc());
        }

        /**************添加用户信息*****************/
        flag = ComponentUtil.registerService.addUserInfo(registerReq,InviteAdd,vcMember.getMemberId(),vcMember.getExtensionMemberId(),InviteAdd[2]);
        if(!flag){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.R000003.geteCode(),ErrorCode.ENUM_ERROR.R000003.geteDesc());
        }else{
            /********** 添加redis *********/
            addRedis(registerReq,InviteAdd);
            UserInfoModel   userInfoModel= new UserInfoModel();
            userInfoModel.setToken(InviteAdd[2]);
            UserInfoModel  userInfo = userInfoMapper.selectByUserInfo(userInfoModel);
            BeanUtils.copy(userInfo,userInfoResp);
        }
        return userInfoResp;
    }


    /**
     * @Description: TODO
     * @param registerReq
     * @return boolean
     * @author long
     * @date 2019/11/12 23:17
     */
    @Override
    public  boolean  checkRegister(RegisterReq registerReq)throws  Exception{
        boolean   flag = true;
        String phoneKeyCache = CachedKeyUtils.getCacheKey(CacheKey.PHONE_INFO, registerReq.getPhone());
        String wxOpenidKeyCache = CachedKeyUtils.getCacheKey(CacheKey.WX_INFO, registerReq.getWxOpenid());
        String deviceidKeyCache = CachedKeyUtils.getCacheKey(CacheKey.DEVICE_INFO, registerReq.getDeviceId());
        String strWx = (String)ComponentUtil.redisService.get(wxOpenidKeyCache);
        String strPhone = (String)ComponentUtil.redisService.get(phoneKeyCache);
        String strDid = (String)ComponentUtil.redisService.get(deviceidKeyCache);
        if (strWx != null){
            return false;
        }
        if (strPhone != null){
            return false;
        }
        if (strDid != null){
            return false;
        }
        RegisterResp  registerResp = new  RegisterResp();
        //验证码的组合方式   电话号 + 时间戳；
        String  verKey  = registerReq.getPhone() + registerReq.getTimeStamp();
        System.out.println("==================="+verKey);


        /*********   手机号码 是否被注册   ********/
        VcAccountRelation vcAccountRelation = new VcAccountRelation();
        vcAccountRelation.setAccountNum(registerReq.getPhone());
        vcAccountRelation.setAccountType(registerReq.getRegisterType());
        VcAccountRelation rsVc = vcAccountRelationMapper.selectByPrimaryKey(vcAccountRelation);
        if(null!=rsVc){
            return false;
        }
        /*********   WxOpenid  是否被注册  ********/
        VcThirdParty vcThirdParty = new VcThirdParty();
        vcThirdParty.setWxOpenid(registerReq.getWxOpenid());
        VcThirdParty rsThird = vcThirdPartyMapper.selectByPrimaryKey(vcThirdParty);
        if(null!=rsThird){
            return false;
        }
        /*********   DeviceId  是否被注册 ********/
        VcMember vcMember = new VcMember();
        vcMember.setDeviceId(registerReq.getDeviceId());
        VcMember  rsMember = vcMemberMapper.selectByPrimaryKey(vcMember);
        if(null!=rsMember){
            return false;
        }
        return  flag;
    }

    /**
     * @Description: 添加用户信息
     * @param registerReq
    * @param SecureUUID
    * @param superiorId
    * @param extensionMemberId
    * @param token
     * @return boolean
     * @author long
     * @date 2019/11/18 14:01
     */
    @Override
    public boolean  addUserInfo(RegisterReq  registerReq,String [] SecureUUID,
                                    Integer superiorId,String extensionMemberId,String token )throws  Exception{

        Integer  createTime  =  Integer.parseInt(DateUtil.timeStamp());
        Integer  loginTime  =  createTime;

//        VcMember  memberModel = new VcMember();  //会员信息
        Integer  memberId  = ComponentUtil.generateService.getMemberId();
        VcMember  vcMember =RegisterMethod.insertVcMember( memberId,  registerReq, SecureUUID, superiorId, extensionMemberId);

        VcAccountRelation  accountRelationModel =RegisterMethod.insertVcAccountRelation(memberId,registerReq.getPhone(),registerReq.getRegisterType());

        VcThirdParty   vcThirdPartyModel  = RegisterMethod.insertVcThirdParty(memberId,registerReq.getWxRefresh(),registerReq.getWxOpenid(),registerReq.getWxUnionid(),token); //三方资源信息

        VcRewardReceive  rewardReceiveModel  = RegisterMethod.insertVcRewardReceive(memberId);//会员奖励


        VcMemberResource   vcMemberResourceModel  =  RegisterMethod.insertVcMemberResource(memberId);

        VcMemberResource  vcMemberResource  =  RegisterMethod.updatePeople(extensionMemberId);

        UTaskHave  uTaskHave =RegisterMethod.insertUTaskHave(memberId);//会员资源
        UMasonrySummary  uMasonrySummary = RegisterMethod.insertUMasonrySummary(memberId);
        try{
            ComponentUtil.transactionalService.registerAdd(vcMember,accountRelationModel,vcThirdPartyModel,rewardReceiveModel,vcMemberResourceModel,uMasonrySummary,uTaskHave,vcMemberResource);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw  new ServiceException(ErrorCode.ENUM_ERROR.R000003.geteCode(),ErrorCode.ENUM_ERROR.R000003.geteDesc());
        }
        return  true;
    }

    /**
     * @Description: 检查邀请码是否正确
     * @return boolean
     * @author long
     * @date 2019/11/12 23:32
     */
    @Override
    public VcMember   checkInviteCode(String  inviteCode)throws  Exception {
        VcMember vcMember  = new VcMember();
        vcMember.setInviteCode(inviteCode);
        VcMember  rsVcMember = vcMemberMapper.selectByPrimaryKey(vcMember);
        return rsVcMember;
    }

    /**
     * @Description: 根据手机生成验证码
     * @param phone
     * @return RegisterResp
     * @author long
     * @date 2019/11/12 23:05
     */
    @Override
    public RegisterResp  getSmsVerification(String phone) throws  Exception{
        String  time = DateUtil.timeStamp()  ;
        RegisterResp    registerResp =new RegisterResp();
        registerResp.setTimeStamp(time);
        VcPhoneDeploy    vcPhoneDeploy =RegisterMethod.queryPhoneDeploy(phone);
        List<VcPhoneDeploy>  list = vcPhoneDeployMapper.selectByPrimaryKey(vcPhoneDeploy);
        if(list.size()!=0){
            registerResp.setIsLogin(1);
        }else{
            registerResp.setIsLogin(2);
        }

        String  amsVerification = RandomUtil.getRandom(6);
        if (amsVerification.startsWith("0")){
            amsVerification = amsVerification.replaceFirst("0" , "1");
        }
        ComponentUtil.redisService.set((phone+time),amsVerification,Constant.EFFECTIVE_IDENT_CODE_TIME, TimeUnit.MINUTES);
        boolean  flag = SendSms.aliSendSms(phone,amsVerification);
        if(!flag){
            registerResp=null;
        }
        return registerResp;
    }

    /**
     * @Description: TODO
     * @param
     * @return java.lang.String
     * @author long
     * @date 2019/11/13 10:40
     */
    @Override
    public String createInviteCode(String phone)throws  Exception {
        //生成邀请码是否有重复的
        String inviteCode ="";
        String tradingAddress ="";
        String token = "" ;
        VcMember vcMember = new VcMember();
        VcMember rsVcMember = new VcMember();
        int i=0;
        while(true){
            token=ComponentUtil.generateService.getNonexistentInformation(Constant.TOKEN);
            inviteCode= phone ;
            tradingAddress=ComponentUtil.generateService.getNonexistentInformation(Constant.TRADING_ADDRESS);
            vcMember.setInviteCode(inviteCode);
            vcMember.setTradingAddress(tradingAddress);
            vcMember.setToken(token);
            rsVcMember = vcMemberMapper.selectByCodeOrAddress(vcMember);
            if(null==rsVcMember||i>3){
                break;
            }
            i++;
        }
        if(i>3){
            return "";
        }
        return inviteCode+","+tradingAddress+","+token;
    }

    /**
     * @Description: 给信息新注册的用添加到缓存里面
     * @param registerReq
     * @param InviteAdd []
     * @return void
     * @author long
     * @date 2019/11/13 10:39
     */
    @Override
    public void addRedis(RegisterReq registerReq,String [] InviteAdd)throws  Exception{
        String phoneKeyCache = CachedKeyUtils.getCacheKey(CacheKey.PHONE_INFO, registerReq.getPhone());
        String wxOpenidKeyCache = CachedKeyUtils.getCacheKey(CacheKey.WX_INFO, registerReq.getWxOpenid());
        String deviceidKeyCache = CachedKeyUtils.getCacheKey(CacheKey.DEVICE_INFO, registerReq.getDeviceId());
        String inviteCode= CachedKeyUtils.getCacheKey(CacheKey.TRADING_ADDRESS_INFO, InviteAdd[0]);
        String tradingAddress = CachedKeyUtils.getCacheKey(CacheKey.TRADING_ADDRESS_INFO, InviteAdd[1]);

        ComponentUtil.redisService.onlyData(phoneKeyCache, "1");
        ComponentUtil.redisService.onlyData(wxOpenidKeyCache, "1");
        ComponentUtil.redisService.onlyData(deviceidKeyCache, "1");
        ComponentUtil.redisService.onlyData(inviteCode, "1");
        ComponentUtil.redisService.onlyData(tradingAddress, "1");
    }


    @Override
    public boolean userRegisterReward(Integer memberId) throws Exception {
        VcRewardReceive  vcRewardReceive =vcRewardReceiveMapper.selectByPrimaryKey(memberId);
        if(vcRewardReceive.getIsLevel0()!=1){
            return false;
        }
        ComponentUtil.taskService.userRegisterToTask(memberId);
        return true;
    }

    /**
     * @Description: 新用户注册初始化给的奖励
     * @param
     * @return void
     * @author long
     * @date 2019/11/13 10:39
     */
    @Override
    public void initDate()throws  Exception {
        VcMemberGive    vcMemberGive   =   vcMemberGiveMapper.selectByPrimaryKey();
        if(null!=vcMemberGive){
            RegisterSingleton.getInstance().setVcMemberGive(vcMemberGive);
        }
    }

    @Override
    public boolean isPhoneExist(String phone) {
        boolean  flag =  false;
        if(StringUtils.isBlank(phone)){
            return flag ;
        }
        VcPhoneDeploy  vcPhoneDeploy=RegisterMethod.queryVcPhoneDeploy(phone);
        List<VcPhoneDeploy> list =vcPhoneDeployMapper.selectByPrimaryKey(vcPhoneDeploy);
        if(list.size()!=0){
            return true ;
        }

        VcMember   vcMember =RegisterMethod.queryPhoneVcMember(phone);
        VcMember   vcMember1 = vcMemberMapper.selectByPrimaryKey(vcMember);
        if(vcMember1!=null){
            return flag ;
        }
        return true;
    }

    @Override
    public void addRegisterPhoneDisplay(String phone, String invite_code) {
        VcPhoneDeploy  vcPhoneDeploy = RegisterMethod.insertPhoneVcMember(phone,invite_code);
        vcPhoneDeployMapper.insertSelective(vcPhoneDeploy);
    }

    @Override
    public VcPhoneDeploy queryRegister(String phone) {
        return null;
    }
}
