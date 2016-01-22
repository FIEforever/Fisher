package com.doyoteam.fisher.db.bean;

import java.util.List;

/**
 * 商家实体
 * Created by F on 2015/12/21.
 */
public class Shop {
    public int id;                      // 商家ID
    public String name;                 // 商家名
    public String description;          // 商家描述
    public String landMark;             // 商家地标
    public String parentIndustryName;   // 商家父行业
    public String industryName;         // 商家行业
    public int industryId;              // 行业ID
    public String province;             // 省份
    public String city;                 // 城市
    public String count;                // 县区
    public String areaNum;              // 地区Num
    public double lng;                   // 经度
    public double lat;                   // 维度
    public String tel;                  // 电话
    public String coverUrl;             // 封面图
    public String address;              // 地址
    public int distance;                // 距离
    public float score;                 // 评分
    public int groupId;               // 产品ID
    public String groupName;          // 产品名
    public float price;                 // 价格
    public float orgPrice;              // 原价
    public int stock;                   // 库存
    public int sales;                   // 销售量

    public List<Picture> photoList;     // 照片列表
}
