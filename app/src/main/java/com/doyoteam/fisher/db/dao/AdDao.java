package com.doyoteam.fisher.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doyoteam.fisher.db.Database;
import com.doyoteam.fisher.db.bean.Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告数据库操作类
 * Created by F on 2015/12/21.
 */
public class AdDao {

    private final static String TABLE_NAME = "tb_ad";
    private static AdDao instance;
    private Database db_helper;
    private SQLiteDatabase db;

    /**
     * 构造方法
     */
    private AdDao() {
        this.db_helper = Database.getInstance();
    }

    /**
     * 获取单例实例
     *
     * @return 广告管理类
     */
    public static AdDao getInstance() {
        if (instance == null) instance = new AdDao();
        return instance;
    }

    /**
     * 更新地区数据版本
     *
     * @param dataList 广告列表
     */
    public void insertAdData(List<Ad> dataList, boolean isReset) {

        db = db_helper.getWritableDatabase();
        if(isReset)
            db.delete(TABLE_NAME, null, null);

        for (Ad item : dataList)
            insertValue(item);
        db.close();
    }

    /**
     * 插入单个广告值
     *
     * @param item 广告对象
     */
    private void insertValue(Ad item) {
        ContentValues cv = new ContentValues();
        cv.put("id", item.id);
        cv.put("name", item.name);
        cv.put("describe", item.description);
        cv.put("url", item.url);
        cv.put("area", item.areaNum);
        cv.put("keyword", item.keyWord);
        cv.put("action", item.action);
        db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 根据id获取子地区集
     *
     * @param area 原地区Code
     * @return 子地区集
     */
    public List<Ad> getAds(String area) {
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "area = ? OR area = ''", new String[]{area},
                null, null, "id ASC");
        List<Ad> dataList = new ArrayList<>();
        while (cursor.moveToNext()) dataList.add(buildData(cursor));
        cursor.close();
        db.close();
        return dataList;
    }

    // 从查询集中生成对象
    private Ad buildData(Cursor cursor) {
        Ad bean = new Ad();
        bean.id = cursor.getInt(cursor.getColumnIndex("id"));
        bean.name = cursor.getString(cursor.getColumnIndex("name"));
        bean.description = cursor.getString(cursor.getColumnIndex("describe"));
        bean.url = cursor.getString(cursor.getColumnIndex("url"));
        bean.areaNum = cursor.getString(cursor.getColumnIndex("area"));
        bean.keyWord = cursor.getString(cursor.getColumnIndex("keyword"));
        bean.action = cursor.getInt(cursor.getColumnIndex("action"));
        return bean;
    }

}
