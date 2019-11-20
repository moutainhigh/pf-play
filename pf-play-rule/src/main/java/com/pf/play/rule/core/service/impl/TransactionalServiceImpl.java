package com.pf.play.rule.core.service.impl;

import com.pf.play.rule.core.common.dao.BaseDao;
import com.pf.play.rule.core.common.service.impl.BaseServiceImpl;
import com.pf.play.rule.core.mapper.*;
import com.pf.play.rule.core.model.*;
import com.pf.play.rule.core.service.AipRequestInfoService;
import com.pf.play.rule.core.service.TransactionalService;
import com.pf.play.rule.util.ComponentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


/**
 * @Description 所有需要事务回滚的方法都放这里面
 * @Author long
 * @Date 2019/11/19 11:02
 * @Version 1.0
 */
@Service
@Transactional
public class TransactionalServiceImpl<T> extends BaseServiceImpl<T> implements TransactionalService<T> {
    @Autowired
    private UTaskHaveMapper uTaskHaveMapper;

    @Autowired
    private UDayTaskRewardMapper uDayTaskRewardMapper;
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
    private UMasonryListLogMapper  uMasonryListLogMapper ;

    @Override
    public BaseDao<T> getDao() {
        return vcRewardReceiveMapper;
    }
    /**
     * @Description: 用户注册信息同一个事物进行添加
     * @param memberModel
    * @param accountRelationModel
    * @param vcThirdPartyModel
    * @param rewardReceiveModel
    * @param vcMemberResourceModel
     * @return void
     * @author long
     * @date 2019/11/19 11:48
     */
    @Override
    public void registerAdd(VcMember memberModel, VcAccountRelation accountRelationModel, VcThirdParty vcThirdPartyModel,
                            VcRewardReceive rewardReceiveModel, VcMemberResource vcMemberResourceModel)throws  Exception {

        vcMemberMapper.insertSelective(memberModel);
        vcAccountRelationMapper.insertSelective(accountRelationModel);
        vcThirdPartyMapper.insertSelective(vcThirdPartyModel);
        vcRewardReceiveMapper.insertSelective(rewardReceiveModel);
        vcMemberResourceMapper.insertSelective(vcMemberResourceModel);
        ComponentUtil.registerService.userRegisterReward(memberModel.getMemberId());
    }

    /**
     * @Description: 用户领取当天奖励，需要操做的表信息
     * @param record
    * @param uTaskHave
     * @return void
     * @author long
     * @date 2019/11/19 11:48
     */
    @Override
    public void updateTask(UDayTaskReward record, UTaskHave uTaskHave) {
        uDayTaskRewardMapper.insertSelective(record);
        uTaskHaveMapper.updateByPrimaryKey(uTaskHave);
    }


    @Override
    public void buyTaskUpdateInfo(UTaskHave uTaskHave, VcMemberResource resource, UMasonryListLog uMasonryLog) {
        uTaskHaveMapper.insertSelective(uTaskHave);
        vcMemberResourceMapper.updateByPrimaryKeySelective(resource);
        uMasonryListLogMapper.insertSelective(uMasonryLog);
    }
}