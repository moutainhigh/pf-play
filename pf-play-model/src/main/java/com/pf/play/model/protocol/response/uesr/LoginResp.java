package com.pf.play.model.protocol.response.uesr;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/30 20:51
 * @Version 1.0
 */
public class LoginResp {
    private   String   token ;
    private   Integer  memberId ;
    private   String   nickname;
    private   Integer  sex;
    private   String   birthday;
    private   String   province;
    private   String   city;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
