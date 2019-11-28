package com.pf.play.rule.core.mapper;

import com.pf.play.rule.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 交易的Dao层
 * @Author yoko
 * @Date 2019/11/26 22:40
 * @Version 1.0
 */
@Mapper
public interface TradeMapper<T> extends BaseDao<T> {
}
