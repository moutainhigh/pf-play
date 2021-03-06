package com.pf.play.rule.core.singleton;

import com.pf.play.rule.core.model.SysTypeDictionary;
import com.pf.play.rule.core.model.VcMemberGive;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/12 11:57
 * @Version 1.0
 */
public class RegisterSingleton {
    private static RegisterSingleton registerSingleton;
    private static ReentrantLock lock = new ReentrantLock();

    private RegisterSingleton() {

    }

    public static RegisterSingleton getInstance() {
        if (registerSingleton == null) {
            lock.lock();
            if (registerSingleton == null) {
                registerSingleton = new RegisterSingleton();
            }
            lock.unlock();
        }
        return registerSingleton;
    }

    public VcMemberGive vcMemberGive;

    public Integer realNameCycle ;
    public Double realNameReward ;
    public Double initEmpiricalValue ;
    public String ip ;

    public List<SysTypeDictionary> commenType ;
    public List<SysTypeDictionary> masonryType ;
    public List<SysTypeDictionary> empiricalType ;
    public List<SysTypeDictionary> vitalityType ;
    public List<SysTypeDictionary> taskType ;
    public List<SysTypeDictionary> procedType ;



    public VcMemberGive getVcMemberGive() {
        return vcMemberGive;
    }

    public void setVcMemberGive(VcMemberGive vcMemberGive) {
        this.vcMemberGive = vcMemberGive;
    }

    public List<SysTypeDictionary> getCommenType() {
        return commenType;
    }

    public void setCommenType(List<SysTypeDictionary> commenType) {
        this.commenType = commenType;
    }

    public List<SysTypeDictionary> getMasonryType() {
        return masonryType;
    }

    public void setMasonryType(List<SysTypeDictionary> masonryType) {
        this.masonryType = masonryType;
    }

    public List<SysTypeDictionary> getEmpiricalType() {
        return empiricalType;
    }

    public void setEmpiricalType(List<SysTypeDictionary> empiricalType) {
        this.empiricalType = empiricalType;
    }

    public List<SysTypeDictionary> getVitalityType() {
        return vitalityType;
    }

    public void setVitalityType(List<SysTypeDictionary> vitalityType) {
        this.vitalityType = vitalityType;
    }

    public List<SysTypeDictionary> getTaskType() {
        return taskType;
    }

    public void setTaskType(List<SysTypeDictionary> taskType) {
        this.taskType = taskType;
    }

    public List<SysTypeDictionary> getProcedType() {
        return procedType;
    }

    public void setProcedType(List<SysTypeDictionary> procedType) {
        this.procedType = procedType;
    }

    public Integer getRealNameCycle() {
        return realNameCycle;
    }

    public void setRealNameCycle(Integer realNameCycle) {
        this.realNameCycle = realNameCycle;
    }

    public Double getRealNameReward() {
        return realNameReward;
    }

    public void setRealNameReward(Double realNameReward) {
        this.realNameReward = realNameReward;
    }

    public Double getInitEmpiricalValue() {
        return initEmpiricalValue;
    }

    public void setInitEmpiricalValue(Double initEmpiricalValue) {
        this.initEmpiricalValue = initEmpiricalValue;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
