package com.doyoteam.fisher.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doyoteam.fisher.db.BaseDatabase;
import com.doyoteam.fisher.db.bean.City;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市地区
 * Created by F on 2015/12/21.
 */
public class CityDao {

    private final static String TABLE_NAME = "tb_city";
    private static CityDao instance;
    private BaseDatabase db_helper;
    private SQLiteDatabase db;

    /**
     * 构造方法
     */
    private CityDao() {
        this.db_helper = BaseDatabase.getInstance();
    }

    /**
     * 获取单例实例
     *
     * @return 城市管理类
     */
    public static CityDao getInstance() {
        if (instance == null) instance = new CityDao();
        return instance;
    }

    /**
     * 更新地区数据版本
     *
     * @param cityList 地区列表
     */
    public void updateCityVersion(List<City> cityList) {
        db = db_helper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
//        db.beginTransaction();
        for (City city : cityList)
            insertOrUpdateValue(city);
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    /**
     * 插入单个地区值
     *
     * @param city 地区对象
     */
    private void insertOrUpdateValue(City city) {
        ContentValues cv = new ContentValues();
        cv.put("id", city.id);
        cv.put("name", city.name);
        cv.put("code", city.code);
        cv.put("area", city.area);
        cv.put("lat", city.lat);
        cv.put("lng", city.lng);
        cv.put("pid", city.pid);
        cv.put("count", 0);
        db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 获取父地区对象
     *
     * @return 父地区对象
     */
    public City getCity(int id) {
        db = db_helper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        City city = buildData(cursor);
        cursor.close();
        db.close();
        return city;
    }

    public City getCity(String area) {
        db = db_helper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, "area = ?", new String[]{area},
                null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        City city = buildData(cursor);
        cursor.close();
        db.close();
        return city;
    }


    /**
     * 根据id获取子地区集
     *
     * @param id 原地区ID
     * @return 子地区集
     */
    public List<City> getChildCity(int id) {
        db = db_helper.getReadableDatabase();

        // 点击次数加1
        db.execSQL("UPDATE " + TABLE_NAME + " SET count = count + 1 WHERE id = " + id);

        Cursor cursor = db.query(TABLE_NAME, null, "pid = ?", new String[]{String.valueOf(id)},
                null, null, "count DESC, id ASC");

        List<City> cityList = new ArrayList<>();
        while (cursor.moveToNext()) cityList.add(buildData(cursor));
        cursor.close();
        db.close();
        return cityList;
    }

    // 从查询集中生成对象
    private City buildData(Cursor cursor) {
        City bean = new City();
        bean.id = cursor.getInt(cursor.getColumnIndex("id"));
        bean.name = cursor.getString(cursor.getColumnIndex("name"));
        bean.code = cursor.getString(cursor.getColumnIndex("code"));
        bean.area = cursor.getString(cursor.getColumnIndex("area"));
        bean.lat = cursor.getDouble(cursor.getColumnIndex("lat"));
        bean.lng = cursor.getDouble(cursor.getColumnIndex("lng"));
        bean.pid = cursor.getInt(cursor.getColumnIndex("pid"));
        bean.count = cursor.getInt(cursor.getColumnIndex("count"));
        bean.license = cursor.getString(cursor.getColumnIndex("license"));
        return bean;
    }

    // 设置定位城市
    public City setLocateCity(String name) {
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "name like ? AND length(area)=10", new String[]{"%" + name + "%"},
                null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        City city = buildData(cursor);
        cursor.close();

        // 置空原定位城市
        db.execSQL("UPDATE " + TABLE_NAME + " SET count = (count/10000)*10000 + count%1000 WHERE " +
                "(count%10000)/1000 > 0");

        // 更新定位城市
        db.execSQL("UPDATE " + TABLE_NAME + " SET count = (count/10000)*10000 + count%1000 + 1000" +
                " WHERE " +
                "id = " + city.id);

        db.execSQL("UPDATE " + TABLE_NAME + " SET count = (count/10000)*10000 + count%1000 + 1000" +
                " WHERE " +
                "id = " + city.pid);
        db.close();

        return city;
    }

    public void updateCurrentArea(City city) {
        db = db_helper.getReadableDatabase();

        // 置空原选定城市
        db.execSQL("UPDATE " + TABLE_NAME + " SET count = count%10000 WHERE count/10000 > 0");

        // 更新点击次数
        db.execSQL("UPDATE " + TABLE_NAME + " SET count = count%10000 + 10000 WHERE " +
                "id = " + city.id);

        db.execSQL("UPDATE " + TABLE_NAME + " SET count = count%10000 + 10000 WHERE " +
                "id = " + city.pid);
        db.close();
    }
}
