package com.doyoteam.fisher.db.bean;

import java.util.List;

/**
 * 团购产品信息实体
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version Car 210
 *          Datetime 2015-11-27 10:08
 *          Copyright 2015 东莞市邮政公司电子商务分局 All rights reserved.
 * @since Car 1.0
 */
public class Group {
    public int id;                      // 编号
    public String name;                 // 名称
    public String description;          // 简介
    public float price;                 // 售价
    public float orgPrice;              // 原价
    public int stock;                   // 库存量
    public int sales;                   // 销售数
    public float score;                 // 产品评分
    public String coverUrl;             // 封面Url
    public String buyDeadline;          // 购买截止日期
    public String consumeDeadline;      // 消费截止日期
    public int status;                  // 状态(-1：下架，0：未发布，1：正常)
    public List<Picture> photoList;     // 产品图片
    public boolean isFavorite;          // 是否已收藏

    public int shopId;                  // 商家ID
    public String shopName;             // 商家名
    public String shopCoverUrl;         // 商家封面
    public String address;              // 商家地址
    public String landMark;             // 商家地标
    public String parentIndustryName;   // 父行业名
    public String industryName;         // 行业名
    public int industryId;              // 行业ID
    public String province;             // 省级
    public String city;                 // 市级
    public String count;                // 区县
    public String areaNum;              // 地区编码
    public double lng;                  // 经度
    public double lat;                  // 纬度
    public String tel;                  // 电话
    public int distance;                // 距离
}
