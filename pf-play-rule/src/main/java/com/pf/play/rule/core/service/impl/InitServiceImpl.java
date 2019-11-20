package com.pf.play.rule.core.service.impl;

import com.pf.play.rule.core.common.dao.BaseDao;
import com.pf.play.rule.core.common.service.impl.BaseServiceImpl;
import com.pf.play.rule.core.mapper.DisTaskAttributeMapper;
import com.pf.play.rule.core.mapper.DisTaskTypeMapper;
import com.pf.play.rule.core.mapper.UTaskHaveMapper;
import com.pf.play.rule.core.model.DisTaskAttribute;
import com.pf.play.rule.core.model.DisTaskType;
import com.pf.play.rule.core.service.InitService;
import com.pf.play.rule.core.service.UserInfoSevrice;
import com.pf.play.rule.core.singleton.TaskSingleton;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/20 10:33
 * @Version 1.0
 */
public class InitServiceImpl<T> extends BaseServiceImpl<T> implements InitService<T> {

    @Autowired
    private DisTaskTypeMapper disTaskTypeMapper;

    @Autowired
    private DisTaskAttributeMapper disTaskAttributeMapper;

    @Override
    public BaseDao<T> getDao() {
        return disTaskTypeMapper;
    }

    /**
     * @Description: 任务初始化加载，不用频繁访问数据库获取
     * @param
     * @return void
     * @author long
     * @date 2019/11/20 11:02
     */
    @Override
    public void initTask() {
        DisTaskType  disTaskType = new DisTaskType();
        List<DisTaskType>  list = disTaskTypeMapper.selectByPrimaryKey(disTaskType);
        TaskSingleton.getInstance().setDisTaskTypeList(list);//任务类型明细表

        DisTaskAttribute    disTaskAttribute = new DisTaskAttribute();
        List<DisTaskAttribute>  attributeList  = disTaskAttributeMapper.selectByPrimaryKey(disTaskAttribute);

        List<DisTaskAttribute>  attributeList1 =new ArrayList<>();
        List<DisTaskAttribute>  attributeList2 =new ArrayList<>();
        List<DisTaskAttribute>  attributeList3 =new ArrayList<>();
        for(DisTaskAttribute disTaskAttribute1:attributeList){
            if(disTaskAttribute1.getAttributeType()==1){
                attributeList1.add(disTaskAttribute1);
            }else if(disTaskAttribute1.getAttributeType()==2){
                attributeList2.add(disTaskAttribute1);
            }else if(disTaskAttribute1.getAttributeType()==3){
                attributeList3.add(disTaskAttribute1);
            }
        }
        TaskSingleton.getInstance().setDisTaskAttributeList(attributeList);//所有的任务属性表

        TaskSingleton.getInstance().setAttributeTypeList1(attributeList1);//条件任务属性
        TaskSingleton.getInstance().setAttributeTypeList2(attributeList2);//奖励任务属性
        TaskSingleton.getInstance().setAttributeTypeList3(attributeList3);//消耗任务属性
    }
}