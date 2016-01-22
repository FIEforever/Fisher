package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 用户基类
 * Created by F on 2015/12/21.
 */
public class User implements Serializable, Cloneable {

    public String userId;//会员ID
    public String userName;//昵称
    public String mobilePhone;//手机号
    public String email;//邮箱
    public String headPhotoUrl;//头像
    public String headPhotoUrlShortPath;//头像相对路径
    public String registerTime;//注册时间(yyyy-mm-dd HH:mm:ss)
    public String platform;//平台，1为Android，2为IOS
    public String sex;//性别
    public String code;//验证码
    public String password;//密码
    public String weChatId;//微信用户唯一码
    public double lat;//经度
    public double lng;//纬度

    @Override
    public String toString() {
        return userName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

