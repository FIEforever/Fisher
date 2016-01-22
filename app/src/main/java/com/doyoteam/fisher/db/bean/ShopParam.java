package com.doyoteam.fisher.db.bean;

import com.doyoteam.fisher.DataModel;
import com.doyoteam.fisher.data.CityData;
import com.doyoteam.fisher.db.dao.CityDao;

/**
 * 获取商家列表请求参数封装
 * Created by F on 2015/12/21.
 */
public class ShopParam {

    public int userId;       // 会员id
    public City city;           // 当前地区
    public int industryId;      // 行业id
    public int circleId;        // 商圈id
    public int brandId;         // 品牌id
    public int shopType;        // 商家类型（1常惠生活商家 2自邮一族商家 3推荐商家）
    public int shopMode;        // 商家模式（0不限 1团购商家 2优惠商家）
    public String shopName;     // 商家名
    public String shopIds;      // 商家id串　以逗号分隔开，如 5705,5702 ）
    public double lng;          // 用户经度
    public double lat;          // 用户纬度
    public int radius;          // 搜索半径
    public int curPage;         // 当前页码
    public int state;           // 团购状态 （-1:下架，-2：过期，1：正常）
    public int pageSize;        // 页面容量
    public String orderColumn;  // 排序字段
    public int orderType;       // 排序类型 0.升序，1.降序

    public int orderIndex;      // 筛选排序
    public boolean isDate;      // 是否节假日通用
    public boolean isNoReserve; // 是否无需预约
    public boolean isSuite;     // 是否多套餐
    public int amountIndex;     // 用餐人数

    public ShopParam(boolean isShop) {

        if(DataModel.getInstance().isLogined())
            userId = Integer.parseInt(DataModel.getInstance().getUser().userId);

        if (CityData.getInstance().currentCounty != 0)
            city = CityDao.getInstance().getCity(CityData.getInstance().currentCounty);
        else
            city = CityData.getInstance().currentCity;

        lat = CityData.getInstance().lat;
        lng = CityData.getInstance().lng;

        radius = 50000;     // 默认50公里
        industryId = 0;     // 默认全部行业
        shopType = 1;       // 默认常惠生活商家
        shopMode = 0;       // 默认团购商家、优惠商家不限
        curPage = 0;        // 初始化第一页
        state = 1;          // 初始化状态, 正常
        orderIndex = 0;     // 默认智能排序
        setOrderIndex(isShop, orderIndex);
    }

//    public City getCurrentCity() {
//        City tempCity;
//        if (CityData.getInstance().currentCounty != 0)
//            tempCity = CityDao.getInstance().getCity(CityData.getInstance().currentCounty);
//        else
//            tempCity = CityData.getInstance().currentCity;
//
//        if(UserData.getInstance().lat * UserData.getInstance().lng > 0) {
//            lat = UserData.getInstance().lat;
//            lng = UserData.getInstance().lng;
//        } else {
//            lat = tempCity.lat;
//            lng = tempCity.lng;
//        }
//        return tempCity;
//    }

    public int getIndustryPrimaryId() {
        if(industryId < 1000)
            return industryId;
        else
            return industryId/1000;
    }

    public void setOrderIndex(boolean isShop, int orderIndex) {
        this.orderIndex = orderIndex;
        switch (orderIndex) {
            case 0:     // 离我最近
                if(isShop) {
                    orderType = 0;  // 升序
                    orderColumn = "radius";
                } else {            // 人气最高
                    orderType = 0;  // 降序
                    orderColumn = "c0_p1";
                }
                break;

            case 1:     // 好评优先
                if(isShop) {
                    orderType = 1;  // 降序
                    orderColumn = "avgRating";
                } else {            // 折扣最大
                    orderType = 1;
                    orderColumn = "d1_p1";
                }
                break;

            case 2:     // 人气最高
                if(isShop) {
                    orderType = 1;  // 降序
                    orderColumn = "totalComments";
                } else {            // 价格最低
                    orderType = 1;
                    orderColumn = "p1";
                }
                break;

            case 3:     // 销量优先
                if(!isShop) {    // 销量
                    orderType = 0;
                    orderColumn = "s0_p1";
                }
                break;
        }
    }
}
