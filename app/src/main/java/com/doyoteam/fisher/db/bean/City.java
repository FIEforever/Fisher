package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 城市地区类
 * Created by F on 2015/12/21.
 */
public class City implements Serializable, Cloneable {

    public int id;          // 地区ID
    public String name;     // 地区名
    public String code;     // 地区邮编
    public String area;     // 地区编码
    public double lat;      // 维度
    public double lng;      // 经度
    public int pid;         // 父城市ID
    public int count;       // 点击次数
    public String license;     // 车牌前缀

    public void setCity(City city) {
        this.id = city.id;
        this.name = city.code;
        this.code = city.code;
        this.area = city.area;
        this.lat = city.lat;
        this.lng = city.lng;
        this.pid = city.pid;
        this.count = city.count;
        this.license = city.license;
    }

    public City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
