package com.pf.play.rule.core.common.utils.constant;

/**
 * @author df
 * @Description:服务端常量
 * @create 2018-07-24 16:05
 **/
public class ServerConstant {

    /**
     * 公共，公用的常量
     */
    public final class PUBLIC_CONSTANT{
        /**
         *值是否等于0的判断条件
         */
        public static final int SIZE_VALUE_ZERO = 0;

        /**
         * 值是否等于1的判断条件
         */
        public static final int SIZE_VALUE_ONE = 1;

        /**
         * 值是否等于2的判断条件
         */
        public static final int SIZE_VALUE_TWO = 2;

        /**
         * 值是否等于3的判断条件
         */
        public static final int SIZE_VALUE_THREE = 3;

        /**
         * 字符串值等于1
         */
        public static final String STR_VALUE_ONE = "1";

        /**
         * token计算标识
         */
        public static final String TAG_HZ = "HZ";

        /**
         * 是否需要邮件提醒：1不需要邮件提醒，2需要邮件提醒
         * 这里2表示需要邮件提醒
         */
        public static final int MAIL_REMIND_YES = 2;

        /**
         * 钱包地址：前缀标签
         */
        public static final String CURRENCY_TAG = "gd";

        /**
         * 锁redis的key的前缀标签
         */
        public static final String REDIS_LOCK_TAG = "lock";


        /**
         * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功，4扣量
         * 这里3表示成功
         */
        public static final int RUN_STATUS_THREE = 3;

        /**
         * 跑数据被执行了5次为上限
         */
        public static final int RUN_NUM_FIVE = 5;


    }





    /**
     * 策略的枚举
     * 策略类型：1表示成交量虚假数据开关，2表示交易所时间控制，3买家付款超时时间，4卖家确认收款超时时间
     * 策略整形值:当策略类型等于1时（此字段值1表示虚假数据处于关闭，等于2表示开启虚假数据），等于2时（此字段值1表示双休日不交易，2表示交易）
     * 策略值：字段stg_type等于1，字段stg_num_value等于2时，则根据本字段的数据乘以倍数，等于2，表示交易时间的时间段
     */
    public enum StrategyEnum{
        STG_DATA_CLOSE(1, 1, ""),
        STG_DATA_OPEN(1, 2, ""),
        STG_TRADE_TIME_WEEKEND_CLOSE(2, 1, ""),
        STG_TRADE_TIME_WEEKEND_OPEN(2, 2, ""),
        STG_BUY_OVERTIME(3, 0, ""),
        STG_SELL_OVERTIME(4, 0, ""),
        ;
        private int stgType;
        private int stgNumValue;
        private String stgValue;

        private StrategyEnum(int stgType, int stgNumValue, String stgValue) {
            this.stgType = stgType;
            this.stgNumValue = stgNumValue;
            this.stgValue = stgValue;
        }

        public int getStgType() {
            return stgType;
        }

        public void setStgType(int stgType) {
            this.stgType = stgType;
        }

        public int getStgNumValue() {
            return stgNumValue;
        }

        public void setStgNumValue(int stgNumValue) {
            this.stgNumValue = stgNumValue;
        }

        public String getStgValue() {
            return stgValue;
        }

        public void setStgValue(String stgValue) {
            this.stgValue = stgValue;
        }
    }

    /**
     * @Description: 订单交易流水的交易状态
     * <p>
     *     交易状态：1超时，2正常进行中，3问题申诉，4确认已付款（买家等待），5确认已收款（卖家确认）
     * </p>
     * @author yoko
     * @date 2019/12/3 16:16
    */
    public enum TradeStatusEnum{
        OVERTTIME(1, "超时"),
        ACTION(2, "正常进行中"),
        QUESTION(3, "问题申诉"),
        PAY(4, "确认已付款"),
        MAKE_COLLECTIONS(5, "确认已收款"),
        ;
        private int type;
        private String desc;

        private TradeStatusEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }






}
