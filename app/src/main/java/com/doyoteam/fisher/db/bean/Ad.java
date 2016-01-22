package com.doyoteam.fisher.db.bean;

/**
 * 广告实体
 * Created by F on 2015/12/21.
 */
public class Ad {

    public int id;              // 广告id
    public String name;         // 广告名
    public String description;  // 广告描述
    public String url;          // 广告图片Url
    public String keyWord;      // 广告内含关键字
    public String areaNum;      // 广告地区
    public int action;          // 广告操作

    public Ad() {}

    public Ad(int id) {
        this.id = id;
    }
}
