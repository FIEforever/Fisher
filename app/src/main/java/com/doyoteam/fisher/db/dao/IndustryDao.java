package com.doyoteam.fisher.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doyoteam.fisher.db.BaseDatabase;
import com.doyoteam.fisher.db.bean.Industry;

import java.util.ArrayList;
import java.util.List;

/**
 * 行业分类数据库管理类
 * Created by F on 2015/12/21.
 */
public class IndustryDao {

    private final static String TABLE_NAME = "tb_industry";
    private static IndustryDao instance;
    private BaseDatabase db_helper;
    private SQLiteDatabase db;

    /**
     * 构造方法
     */
    private IndustryDao() {
        this.db_helper = BaseDatabase.getInstance();
    }

    /**
     * 获取单例实例
     *
     * @return 行业分类管理类
     */
    public static IndustryDao getInstance() {
        if (instance == null) instance = new IndustryDao();
        return instance;
    }

    /**
     * 更新数据版本
     *
     * @param dataList 数据列表
     */
    public void updateDataVersion(final List<Industry> dataList) {
        db = db_helper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        for (Industry data : dataList)
            insertValue(data);
        db.close();
    }

    /**
     * 插入单个行业分类值
     *
     * @param data 行业对象
     */
    private void insertValue(Industry data) {
        ContentValues cv = new ContentValues();
        cv.put("id", data.id);
        cv.put("name", data.name);
        cv.put("pid", data.pid);
        cv.put("weight", data.weight);
        if (db.isOpen())
            db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 获取父地区对象
     *
     * @return 父地区对象
     */
    public Industry getData(int id) {
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        Industry data = buildData(cursor);
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 根据id获取子行业集
     *
     * @param id 父行业ID
     * @return 子行业集
     */
    public List<Industry> getChildren(int id) {
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "pid = ?", new String[]{String.valueOf(id)},
                null, null, "weight ASC");
        List<Industry> dataList = new ArrayList<>();
        while (cursor.moveToNext()) dataList.add(buildData(cursor));
        cursor.close();
        db.close();
        return dataList;
    }

    // 从查询集中生成对象
    private Industry buildData(Cursor cursor) {
        Industry bean = new Industry();
        bean.id = cursor.getInt(cursor.getColumnIndex("id"));
        bean.name = cursor.getString(cursor.getColumnIndex("name"));
        bean.pid = cursor.getInt(cursor.getColumnIndex("pid"));
        return bean;
    }
}
