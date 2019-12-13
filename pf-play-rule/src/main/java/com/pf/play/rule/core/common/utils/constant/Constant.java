package com.pf.play.rule.core.common.utils.constant;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/13 15:07
 * @Version 1.0
 */
public class Constant {
    public static  int   TOKEN = 1;
    public static  int   INVITE_CODE = 2;
    public static  int   TRADING_ADDRESS = 3;
    public static  int   MEMBERID = 4;

    /*****************任务类型********************/
    public static  int   TASK_TYPE1 = 1;//完成任务
    public static  int   TASK_TYPE2 = 2;//活力值奖励
    public static  int   TASK_TYPE3 = 3;//赠送给Ta
    public static  int   TASK_TYPE4 = 4;//Ta人赠送
    public static  int   TASK_TYPE5 = 5;//转出手续费
    public static  int   TASK_TYPE6 = 6;//转入手续费
    public static  int   TASK_TYPE7 = 7;//交易所转入
    public static  int   TASK_TYPE8 = 8;//转出砖石
    public static  int   TASK_TYPE9 = 9;//购买任务消耗
    public static  int   TASK_TYPE10 = 10;//购买任务消耗

    /*****************符号类型********************/
    public static  int   TASK_SYMBOL_TYPE1 = 1;//加
    public static  int   TASK_SYMBOL_TYPE2 = 2;//减

    /*****************符号类型********************/
    public static  int   REWARD_TYPE1 = 1;//直推购买任务
    public static  int   REWARD_TYPE2 = 2;//自己任务活力值
    public static  int   REWARD_TYPE3 = 3;//团队活力值
    public static  int   REWARD_TYPE4 = 4;//联盟活力值



    /***********注册验证码有效时间(单位分钟)*********/
    public  static  int  EFFECTIVE_IDENT_CODE_TIME =80000;

    /***********token 过期时间(单位 天 )*********/
    public  static  int  TOKEN_EXPIRE    =  15 ;



    public  static  int  TASK_TYPE_HISTORY    =  1 ;
    public  static  int  TASK_TYPE_EXISTING   =  2 ;

    /***********同步用户详细信息**********/
    public  static  String    USER_SYNCHRONOUS_URL="http://192.168.1.25:9093/qhr/api/syncUserInfo";
    /***********同步上下级信息详细信息**********/
    public  static  String    PRARENT_SYNCHRONOUS_URL="http://192.168.1.25:9093/qhr/api/syncPrarentId";

    public  static  Double    ACTIVE_VALUE_MASONRY =  0.4 ;


}
