package com.pf.play.rule.core.service.impl;

import com.pf.play.common.utils.BeanUtils;
import com.pf.play.common.utils.DateUtil;
import com.pf.play.rule.TaskMeThod;
import com.pf.play.rule.core.common.dao.BaseDao;
import com.pf.play.rule.core.common.exception.ServiceException;
import com.pf.play.rule.core.common.service.impl.BaseServiceImpl;
import com.pf.play.rule.core.common.utils.constant.ErrorCode;
import com.pf.play.rule.core.mapper.*;
import com.pf.play.rule.core.model.*;
import com.pf.play.rule.core.model.Enum.TypeTaskEnum;
import com.pf.play.rule.core.runner.RegisTerRunner;
import com.pf.play.rule.core.service.TaskService;
import com.pf.play.rule.core.singleton.RegisterSingleton;
import com.pf.play.rule.core.singleton.TaskSingleton;
import com.pf.play.rule.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/7 15:14
 * @Version 1.0
 */
@Service
public class TaskServiceImpl<T> extends BaseServiceImpl<T> implements TaskService<T> {
    private final static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private UserInfoMapper   userInfoMapper;

    @Autowired
    private UTaskHaveMapper uTaskHaveMapper;

    @Autowired
    private DisTaskAttributeMapper disTaskAttributeMapper;

    @Autowired
    private DisTaskTypeMapper disTaskTypeMapper;

    @Autowired
    private UdailyTaskStatMapper udailyTaskStatMapper;

    @Autowired
    private UDayTaskRewardMapper uDayTaskRewardMapper;

    @Autowired
    private VcRewardReceiveMapper  vcRewardReceiveMapper;


    @Autowired
    private VcMemberResourceMapper  vcMemberResourceMapper;

    @Autowired
    private VcMemberMapper vcMemberMapper;

    @Autowired
    private UvitalityValueListMapper uvitalityValueListMapper;





    @Override
    public BaseDao<T> getDao() {
        return userInfoMapper;
    }

    /**
     * @Description: 用户的任务表
     * @param memberId
     * @return java.util.List<com.pf.play.rule.core.model.UTaskHave>
     * @author long
     * @date 2019/11/19 20:54
     */
    @Override
    public List<UTaskHave> getMyTask(Integer  memberId) {
        UTaskHave record   =  new  UTaskHave();
        record.setMemberId(memberId);
        record.setCurrentState(1);
        List<UTaskHave>  list = uTaskHaveMapper.selectByPrimaryKey(record);
        return list;
    }

    @Override
    public int typeToReceiveTask(String  token ,Integer member_id, Integer type) {
        List<SysTypeDictionary>    taskTypeList = RegisterSingleton.getInstance().getTaskType();
        //信息准确性功能

        //验证是否自有条件进行购买


        //
        for(SysTypeDictionary sysTypeDictionary:taskTypeList){
            if(sysTypeDictionary.getValue().equals("1")){
                UserInfoModel  userInfoModel=  new UserInfoModel();
                UserInfoModel  userList = userInfoMapper.selectByUserInfo(userInfoModel);
                break;
            }else if(sysTypeDictionary.getValue().equals("2")){
                break;
            }else if(sysTypeDictionary.getValue().equals("3")){
                break;
            }else if(sysTypeDictionary.getValue().equals("4")){
                break;
            }else if(sysTypeDictionary.getValue().equals("5")){
                break;
            }else if(sysTypeDictionary.getValue().equals("6")){
                break;
            }else if(sysTypeDictionary.getValue().equals("7")){
                break;
            }
        }
        return 0;
    }

    /**
     * @Description: 用户过期任务详细信息
     * @param member_id
     * @return java.util.List<com.pf.play.rule.core.model.UTaskHave>
     * @author long
     * @date 2019/11/19 21:07
     */
    @Override
    public List<UTaskHave> queryInvalidHaveTask(Integer member_id) throws Exception {
        UTaskHave record  = new UTaskHave();
        record.setMemberId(member_id);
        record.setCurrentState(1);
        List<UTaskHave> list = uTaskHaveMapper.selectNoValidTask(record);
        return list;
    }
    /**
     * @Description: 检查最大的是否有效
     * @param list
     * @return boolean
     * @author long
     * @date 2019/11/18 22:36
     */
    @Override
    public boolean checkSatisfyMaxTask(List<UTaskHave> list) {
        if(list.size()==0){
            return  false;
        }
        UTaskHave  model = list.get(0);
        DisTaskType   disTaskType   =    new DisTaskType();
        disTaskType.setTaskId(model.getTaskId());
        List<DisTaskType>  disTaskType1 =disTaskTypeMapper.selectByPrimaryKey(disTaskType);
        DisTaskAttribute   disTaskAttribute = new  DisTaskAttribute();
        disTaskAttribute.setTaskId(model.getTaskId());

        String  startTime = model.getStartTime()+"000";
        Long  day =DateUtil.timeDifference(Long.parseLong(startTime));
        if(disTaskType1.size()==0){
            return  false;
        }

        List<DisTaskAttribute>    disAttributeList = disTaskAttributeMapper.selectByPrimaryKey(disTaskAttribute);
        if(disAttributeList.size()==0){
            return  false;
        }

        if(model.getSurplusNum()==0){//任务次数到了
            return  false;
        }else if(disTaskType1.get(0).getTaskValidityDay()<=day){//任务周期到了
            return  false;
        }

        UdailyTaskStat record  =  new UdailyTaskStat();
        record.setCurday(DateUtil.getDayNumber(new Date()));
        record.setMemberId(model.getMemberId());
        UdailyTaskStat  statModel = udailyTaskStatMapper.selectByPrimaryKey(record);  //每天用户名明细信息
        if(statModel==null){
            return  false;
        }

        if(Integer.getInteger(disAttributeList.get(0).getKey1())>statModel.getAcceptNumber()){//点赞次数
            return  false;
        }
        if(Integer.getInteger(disAttributeList.get(0).getKey2())>statModel.getLookCommodityNum()){//查看商品次数
            return  false;
        }
        return true;
    }

    @Override
    public void taskReward(List<UTaskHave> list, Integer memberId) throws Exception {
        Date date  = new Date();
        for(UTaskHave uTaskHave:list){
            UDayTaskReward record =  new UDayTaskReward();
            record.setMemberId(memberId);
            record.setTaskId(uTaskHave.getTaskId());
            record.setCurday(DateUtil.getDayNumber(date));
            record.setCurhour(DateUtil.getHour(date));
            record.setCurminute(DateUtil.getCurminute(date));
            ComponentUtil.transactionalService.updateTask(record,uTaskHave);
        }

    }

    /**
     * @Description: 用户注册获取初级任务奖励
     * @param memberId
     * @return void
     * @author long
     * @date 2019/11/19 16:34
     */
    @Override
    public void userRegisterToTask(Integer memberId) throws Exception {
        DisTaskType    disTaskType = new DisTaskType();
        disTaskType.setTaskId(1);
        List<DisTaskType>  taskList  = TaskSingleton.getInstance().getDisTaskTypeList();

        if(taskList.size()==0){
            return;
        }

        Date   date  = new Date();
        Date endTime = DateUtil.getDateBetween(date,taskList.get(0).getTaskValidityDay());
        UTaskHave   uTaskHave =new  UTaskHave();
        uTaskHave.setMemberId(memberId);
        uTaskHave.setTaskId(1);
        uTaskHave.setSurplusDay(taskList.get(0).getTaskValidityDay());
        uTaskHave.setStartTime(date);
        uTaskHave.setEndTime(endTime);
        uTaskHave.setSurplusNum(taskList.get(0).getTotalNum());
        uTaskHave.setCurday(DateUtil.getDayNumber(date));
        uTaskHave.setCurhour(DateUtil.getHour(date));
        uTaskHave.setCurminute(DateUtil.getCurminute(date));
        uTaskHave.setGiveType(1);
        uTaskHave.setTaskLevel(1);
        uTaskHave.setCreateTime(date);
        uTaskHave.setUpdateTime(date);
        uTaskHaveMapper.insertSelective(uTaskHave);
        VcRewardReceive  vcRewardReceive = new VcRewardReceive();
        vcRewardReceive.setMemberId(memberId);
        vcRewardReceive.setIsLevel0(2);
        vcRewardReceiveMapper.updateByPrimaryKeySelective(vcRewardReceive);

//        uTaskHaveMapper.insertSelective();
    }

    /**
     * @Description: 查询还有这用户的领取任务情况，包括自己拥有多少任务
     * @param memberid
     * @return java.util.List<com.pf.play.rule.core.model.DisTaskType>
     * @author long
     * @date 2019/11/19 20:35
     */
    @Override
    public List<DisTaskType> queryReceiveTask(Integer memberid) {
        UTaskHave uTaskHave  =new UTaskHave();
        uTaskHave.setMemberId(memberid);
        uTaskHave.setCurrentState(1);
        List<UTaskHave>  haveList =uTaskHaveMapper.selectValidTask(memberid);

        List<DisTaskType>  list  = TaskSingleton.getInstance().getDisTaskTypeList();
        for(DisTaskType disTaskType1:list){
            List<DisTaskAttribute>  attributeList = TaskSingleton.getInstance().getAttributeTypeList1();
            for(DisTaskAttribute disTaskAttribute1:attributeList){
                if(disTaskAttribute1.getTaskId()==disTaskType1.getTaskId()){
                    disTaskType1.setWhere1(disTaskAttribute1.getKey1());
                    disTaskType1.setWhere2(disTaskAttribute1.getKey2());
                }
            }

            if(haveList.size()==0){
                disTaskType1.setHavaCount(0);
            }else{
                for(UTaskHave uTaskHave1:haveList){
                    if(uTaskHave1.getTaskId()==disTaskType1.getTaskId()){
                        disTaskType1.setHavaCount(uTaskHave1.getTaskCount());
                    }else{
                        disTaskType1.setHavaCount(0);
                    }
                }
            }

        }
        return list;
    }


    @Override
    public boolean checkUserCondition(Integer memberId, Integer taskId)throws Exception {
        //第一步check 用户金额以及taskId 是否有效
        boolean  flag = false ;
        if(taskId==1){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR10.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR10.geteDesc());
        }

        List<DisTaskType>  taskList = TaskSingleton.getInstance().getDisTaskTypeList();

        DisTaskType  disTaskType1 =new DisTaskType();
        for(DisTaskType disTaskType:taskList){
            if(disTaskType.getTaskId()==taskId){
                BeanUtils.copy(disTaskType,disTaskType1);
                flag =  true;
                break;
            }
        }

        if(!flag){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR1.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR1.geteDesc());
        }

        //查询任务数是否到了


        //用户是否有足够的金额
        VcMemberResource record  =  new VcMemberResource();

        VcMemberResource   vcMemberResource  =  vcMemberResourceMapper.selectByPrimaryKey(record);

        if(vcMemberResource==null){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR2.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR2.geteDesc());
        }
        //用户金额是否足够购买任务
        if(vcMemberResource.getDayMasonry()<disTaskType1.getNeedResource()){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR3.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR3.geteDesc());
        }

        UTaskHave      uTaskHave = new UTaskHave();
        uTaskHave.setMemberId(memberId);
        int   count  =0 ;
        List<UTaskHave>  list = uTaskHaveMapper.selectByPrimaryKey(uTaskHave);
        for(UTaskHave uTaskHave1 :list){
            if(taskId==uTaskHave1.getTaskId()){
                count++;
            }
        }

        //持有数是否满足
        if(count>=disTaskType1.getHoldNumber()){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR4.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR4.geteDesc());
        }

        return true;
    }

    @Override
    public boolean addUserTask(Integer memberId, Integer taskId)throws Exception {
        List<DisTaskType>  taskList  = TaskSingleton.getInstance().getDisTaskTypeList();
        if(taskList.size()==0){
            return false;
        }
        Date   date  = new Date();
        Date endTime = DateUtil.getDateBetween(date,taskList.get(0).getTaskValidityDay());
        //添加一条用户拥有表数据
        UTaskHave   uTaskHave =new  UTaskHave();
        uTaskHave.setMemberId(memberId);
        uTaskHave.setTaskId(taskId);
        uTaskHave.setSurplusDay(taskList.get(0).getTaskValidityDay());
        uTaskHave.setStartTime(date);
        uTaskHave.setEndTime(endTime);
        uTaskHave.setSurplusNum(taskList.get(0).getTotalNum());
        uTaskHave.setCurday(DateUtil.getDayNumber(date));
        uTaskHave.setCurhour(DateUtil.getHour(date));
        uTaskHave.setCurminute(DateUtil.getCurminute(date));
        uTaskHave.setGiveType(0);
        uTaskHave.setTaskLevel(taskList.get(0).getTaskLevel());
        uTaskHave.setCreateTime(date);
        uTaskHave.setUpdateTime(date);
        //修改用户砖石金额

        DisTaskAttribute disTaskAttribute = new DisTaskAttribute();

        List<DisTaskAttribute>  buteList = TaskSingleton.getInstance().getAttributeTypeList2();

        for(DisTaskAttribute disTaskAttribute1:buteList){
            if(disTaskAttribute1.getTaskId()==taskId){
                BeanUtils.copy(disTaskAttribute1,disTaskAttribute);
                break;
            }
        }

        VcMemberResource  vcMemberResource  =new VcMemberResource();
        vcMemberResource.setMemberId(memberId);
        VcMember record =new VcMember();
        record.setMemberId(memberId);
        VcMemberResource  resourceRs = vcMemberResourceMapper.selectByPrimaryKey(vcMemberResource);
        VcMember record1 = vcMemberMapper.selectByPrimaryKey(record);
        Float  masonry=resourceRs.getDayMasonry()-taskList.get(0).getNeedResource();
        Float  myActiveValue=Float.parseFloat(disTaskAttribute.getKey1());
        Float  upActiveValue=Float.parseFloat(disTaskAttribute.getKey2());
        if(masonry<0){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR5.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR5.geteDesc());
        }
        vcMemberResource.setDayMasonry(masonry);
//        vcMemberResource.setActiveValue(activeValue);
        vcMemberResource.setUpdateTime(date);

        //用户砖石明细表添加一条记录
        UMasonryListLog   uMasonryListLog  =  new UMasonryListLog();
        uMasonryListLog.setMemberId(memberId);
        uMasonryListLog.setTaskId(taskId);
        uMasonryListLog.setType(9);
        uMasonryListLog.setSymbolType(2);
        uMasonryListLog.setMasonryNum(taskList.get(0).getNeedResource());
        uMasonryListLog.setCurday(DateUtil.getDayNumber(date));
        uMasonryListLog.setCurhour(DateUtil.getHour(date));
        uMasonryListLog.setCurminute(DateUtil.getCurminute(date));
        uMasonryListLog.setUpdateTime(date);
        uMasonryListLog.setCreateTime(date);

        UvitalityValueList myUvitalityValueList =TaskMeThod.pottingVitalityValue(memberId,1,1,myActiveValue);
        UvitalityValueList uqUvitalityValueList =TaskMeThod.pottingVitalityValue(record1.getSuperiorId(),1,1,upActiveValue);

        try{
            ComponentUtil.transactionalService.buyTaskUpdateInfo(uTaskHave,vcMemberResource,uMasonryListLog,myUvitalityValueList,uqUvitalityValueList);
        }catch (Exception e){
            throw  new ServiceException(ErrorCode.ENUM_ERROR.TASK_ERRPR6.geteCode(),ErrorCode.ENUM_ERROR.TASK_ERRPR6.geteDesc());
        }
        return true;
    }

    @Override
    public boolean addUserGrantReward(Integer memberId, Integer taskId)throws Exception {
        return false;
    }

    /**
     * @Description: 用户未处理的活力值明细，跟新用户明细表。
     * @param
     * @return void
     * @author long
     * @date 2019/11/21 13:49
     */
    @Override
    public void updateUserVitalityValue(List<UvitalityValueList>  uvitalityValueList ) {
        List<UvitalityValueList>  successObject=new ArrayList<>();
        List<UvitalityValueList>  failObject=new ArrayList<>();
        updateUserVitalityValueType(uvitalityValueList,2);//更新处理状态
        for(UvitalityValueList uvitalityValue:uvitalityValueList){
            //更新用户经验值
            VcMemberResource vcMemberResource = new VcMemberResource();
            vcMemberResource.setMemberId(uvitalityValue.getMemberId());
            VcMemberResource vcMemberResource1 = vcMemberResourceMapper.selectByPrimaryKey(vcMemberResource);
            if(vcMemberResource1==null){
                failObject.add(uvitalityValue);
                continue;
            }else{
                int stat = ComponentUtil.taskService.updateUserInfoToVitalityValue(vcMemberResource1,uvitalityValue);
                if(stat==0){
                    failObject.add(uvitalityValue);
                    continue;
                }else{
                    successObject.add(uvitalityValue);
                }
            }
        }

        if(successObject.size()==uvitalityValueList.size()){
            for(UvitalityValueList uvitalityValue:successObject){
                updateUserVitalityValueType(uvitalityValueList,4);
            }
        }else{
            for(UvitalityValueList uvitalityValue:uvitalityValueList){
                updateUserVitalityValueType(uvitalityValueList,3);
            }
            for(UvitalityValueList uvitalityValue:failObject){
                updateUserVitalityValueType(uvitalityValueList,2);
            }
        }
    }

    @Override
    public void updateUserVitalityValueType(List<UvitalityValueList> list, Integer type) {
        if(list.size()==0){
            return;
        }
        for(UvitalityValueList uvitalityValueList:list){
            uvitalityValueList.setIsCount(type);
            UvitalityValueList  bean  =  TaskMeThod.toUqdateInfo(uvitalityValueList);
            uvitalityValueListMapper.updateByPrimaryKey(bean);
        }
    }

    @Override
    public int updateUserInfoToVitalityValue(VcMemberResource vcMemberResource,UvitalityValueList uvitalityValueList) {
        if(vcMemberResource==null||uvitalityValueList==null){
            return  0;
        }
        Float  updateValue=null;
        if(uvitalityValueList.getSymbolType()==1){
            updateValue=vcMemberResource.getActiveValue()+uvitalityValueList.getActiveValue();
        }else{
            updateValue=vcMemberResource.getActiveValue()-uvitalityValueList.getActiveValue();
        }
        if(updateValue>=0){
            VcMemberResource vcMemberResource1  =  new VcMemberResource();
            vcMemberResource1.setMemberId(vcMemberResource.getMemberId());
            vcMemberResource1.setActiveValue(updateValue);
            vcMemberResourceMapper.updateByPrimaryKey(vcMemberResource1);
            return 1;
        }
        return 0;
    }

    @Override
    public void openUpdateTask() {
        while (true){
            log.debug("=======================");
            List<UvitalityValueList>  list = uvitalityValueListMapper.selectNeedHandle();
            if(list.size()!=0){
                ComponentUtil.taskService.updateUserVitalityValue(list);
            }else{
                try{
                    Thread.sleep(300000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
