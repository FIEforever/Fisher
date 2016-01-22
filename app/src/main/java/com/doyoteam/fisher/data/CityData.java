package com.doyoteam.fisher.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.doyoteam.fisher.MainApp;
import com.doyoteam.fisher.db.bean.City;
import com.doyoteam.fisher.db.dao.CityDao;

/**
 * 城市信息
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version NearShop 4.0
 *          Datetime 2015-04-23 20:20
 *          Copyright 2015 东莞邮政电子商务分局 All rights reserved.
 * @since NearShop 4.0
 */
public class CityData {

    public static final int DEFAULT_PID = 1;    // 默认父城市ID
    private static final String PRE_NAME = "preferences_city";
    private static CityData instance = null; // 实例对象
    public City currentCity;       // 当前城市信息
    public int currentCounty;      // 当前县区信息
    public float cityDataVersion;  // 城市当前版本
    public double lat;  // 当前设备纬度
    public double lng;  // 当前设备经度

    /**
     *
     * 获城市信息实例
     *
     * @return 用户信息实例
     */
    public static CityData getInstance() {
        if(instance == null)
            instance = new CityData();
        return instance;
    }

    // 私有构造函数
    private CityData() {
        SharedPreferences preferences = MainApp.getContext().getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE);
        cityDataVersion = preferences.getFloat("city_version", 0f);
        currentCity = new City();
        currentCity.id = preferences.getInt("city_id", 249);
        currentCity.name = preferences.getString("city_name", "东莞市");
        currentCity.code = preferences.getString("city_code", null);
        currentCity.area = preferences.getString("city_area", "0001900017");
        currentCity.lat = preferences.getFloat("city_lat", 23.020536f);
        currentCity.lng = preferences.getFloat("city_lng", 113.75176499999998f);
        currentCity.pid = preferences.getInt("city_parent_id", DEFAULT_PID);
        currentCounty = preferences.getInt("county_id", 0);

        lat = preferences.getFloat("dev_lat", 23.020536f);
        lng = preferences.getFloat("dev_lng", 113.75176499999998f);
//                currentCity = new City();
//        currentCity.id = preferences.getInt("city_id", 217);
//        currentCity.name = preferences.getString("city_name", "恩施土家族苗族自治州");
//        currentCity.code = preferences.getString("city_code", "445000");
//        currentCity.area = preferences.getString("city_area", "0001700013");
//        currentCity.lat = preferences.getFloat("city_lat", 0);
//        currentCity.lng = preferences.getFloat("city_lng", 0);
//        currentCity.pid = preferences.getInt("city_parent_id", DEFAULT_PID);
//        currentCounty = preferences.getInt("county_id", 0);
//        CityDao.getInstance().updateCurrentArea(currentCity);
    }

    // 获取当前地区
    public City getCurrentArea() {
        if(currentCounty != 0)
            return CityDao.getInstance().getCity(currentCounty);
        else
            return currentCity;
    }

    // 同步当前城市ID
    public void syncCurrentCity(City currentCity) {
        if(currentCity != null) {
            this.currentCounty = 0;
            this.currentCity = currentCity;
            SharedPreferences preferences = MainApp.getContext().getSharedPreferences(PRE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("city_id", currentCity.id);
            editor.putString("city_name", currentCity.name);
            editor.putString("city_code", currentCity.code);
            editor.putString("city_area", currentCity.area);
            editor.putFloat("city_lat", (float) currentCity.lat);
            editor.putFloat("city_lng", (float) currentCity.lng);
            editor.putInt("city_parent_id", currentCity.pid);
            editor.putInt("county_id", 0);
            editor.apply();
        }
    }

    // 同步当前县区ID
    public void syncCurrentCounty(int countyId) {
        if(this.currentCounty != countyId) {
            currentCounty = countyId;
            SharedPreferences preferences = MainApp.getContext().getSharedPreferences(PRE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("county_id", currentCounty);
            editor.apply();
        }
    }

    // 同步城市版本
    public void syncCityVersion(float cityDataVersion) {
        this.cityDataVersion = cityDataVersion;
        SharedPreferences preferences = MainApp.getContext().getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("city_version", cityDataVersion);
        editor.apply();
    }

    // 同步设备经纬度
    public void syncLatLng(double lat, double lng) {
        if(lat * lng != 0) {
            this.lat = lat;
            this.lng = lng;
            SharedPreferences preferences = MainApp.getContext().getSharedPreferences(PRE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("dev_lat", (float) lat);
            editor.putFloat("dev_lng", (float) lng);
            editor.apply();
        }
    }
}
