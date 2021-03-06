package com.pf.play.rule.core.mapper;

import com.pf.play.rule.core.common.dao.BaseDao;
import com.pf.play.rule.core.model.task.TaskAlipayNotifyModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 定时任务：大杂烩的Dao层
 * @Author yoko
 * @Date 2019/12/27 22:09
 * @Version 1.0
 */
@Mapper
public interface TaskHodgepodgeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据条件查询阿里订单同步数据需要跑task的数据：runStatus属于初始化值
     * @param obj - 查询条件
     * @return List
     * @author yoko
     * @date 2019/12/27 22:24
     */
    public List<TaskAlipayNotifyModel> getTaskAlipayNotify(Object obj);

    /**
     * @Description: 更新阿里订单同步数据task的状态
     * @param obj - 更新状态
     * @return int
     * @author yoko
     * @date 2019/12/27 22:26
     */
    public int updateTaskAlipayNotifyStatus(Object obj);

    /**
     * @Description: 更新用户是否支付实名制费用
     * <p>
     *       is_pay = 1、未支付  2、是已支付
     * </p>
     * @param obj
     * @return int
     * @author yoko
     * @date 2019/12/27 22:44
     */
    public int updateConsumerIsPay(Object obj);
}
