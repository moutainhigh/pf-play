package com.pf.play.rule.core.common.utils.constant;

/**
 * @Description 异常状态码
 * @Author yoko
 * @Date 2019/11/21 14:48
 * @Version 1.0
 */
public class PfErrorCode {

    /**
     * 常量异常
     */
    public final class ERROR_CONSTANT {
        /**
         * 没有被捕捉到的异常
         * 默认系统异常状态码=255
         */
        public static final String DEFAULT_EXCEPTION_ERROR_CODE = "255";

        /**
         * 没有被捕捉到的异常
         * 默认系统异常错误信息=SYS_ERROR
         */
        public static final String DEFAULT_EXCEPTION_ERROR_MESSAGE = "ERROR";

        /**
         * 被捕捉到的异常，则默认异常状态码
         * 默认捕捉的异常状态码=256
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_CODE = "256";

    }



    /**
     * 异常-枚举类
     */
    public enum ENUM_ERROR {
        /***********************************************
         * C打头表示用户的错误
         **********************************************/
        C00000("C00000", "请登录!", "服务端token失效!"),
        C00001("C00001", "数据为空,请填写数据!", "修改支付密码时,所有数据都为空!"),
        C00002("C00002", "错误,请重试!", "客户端没有传token值!"),
        C00003("C00003", "请填写支付密码!", "请填写支付密码!"),
        C00004("C00004", "请填写验证码!", "用户修改支付密码时,验证码为空!"),
        C00005("C00005", "验证码输入有误,请重试!", "验证码输入有误!"),
        C00006("C00006", "验证码超时,请重试!", "验证码超时!"),
        C00007("C00007", "数据为空,请填写数据!", "用户添加固定账号数据时,所有数据都为空!"),
        C00008("C00008", "姓名不能为空,请填写真实姓名!", "添加固定账号数据时,姓名数据为空!"),
        C00009("C00009", "身份证号不能为空,请填写身份证号!", "添加固定账号数据时,身份证号数据为空!"),
        C00010("C00010", "支付宝账号不能为空,请填写支付宝账号!", "添加固定账号数据时,支付宝账号数据为空!"),
        C00011("C00011", "错误,请重试!", "获取用户固定账号信息时,所有数据都为空!"),
        C00012("C00012", "数据已录入,无需在录入!", "用户添加固定账号数据时:数据库已经存在有数据!"),
        C00013("C00013", "数据为空,请填写数据!", "更新用户固定账号数据时,所有数据都为空!"),
        C00014("C00014", "支付宝账号不能为空,请填写支付宝账号!", "更新用户固定账号数据时,支付宝账号数据为空!"),
//        C00015("C00015", "错误,请重试!", "用户登录时客户端上传的账号,密码值为空!"),
//        C00016("C00016", "错误,请重试!", "用户注册时客户端上传的账号,密码值为空!"),
//        A00002("A00002", "客户端传的appKey为空!"),


        /***********************************************
         * P打头表示虚拟货币每日兑换的价格相关的错误信息
         **********************************************/
        P00001("P00001", "请登录!", "服务端token失效!"),
        P00002("P00002", "错误,请重试!", "获取虚拟币每天兑换的价格信息时,所有数据都为空!"),
        P00003("P00003", "错误,请重试!", "获取虚拟币每天兑换价格的数据为空!"),


        ;


        /**
         * 错误码
         */
        private String eCode;
        /**
         * 给客户端看的错误信息
         */
        private String eDesc;
        /**
         * 插入数据库的错误信息
         */
        private String dbDesc;

        private ENUM_ERROR(String eCode, String eDesc, String dbDesc) {
            this.eCode = eCode;
            this.eDesc = eDesc;
            this.dbDesc =dbDesc;
        }

        public String geteCode() {
            return eCode;
        }

        public void seteCode(String eCode) {
            this.eCode = eCode;
        }

        public String geteDesc() {
            return eDesc;
        }

        public void seteDesc(String eDesc) {
            this.eDesc = eDesc;
        }

        public String getDbDesc() {
            return dbDesc;
        }

        public void setDbDesc(String dbDesc) {
            this.dbDesc = dbDesc;
        }
    }
}
