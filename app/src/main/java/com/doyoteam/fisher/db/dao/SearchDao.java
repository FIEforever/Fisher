package com.doyoteam.fisher.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doyoteam.fisher.db.Database;
import com.doyoteam.fisher.db.bean.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索记录操作类
 * Created by F on 2015/12/21.
 */
public class SearchDao {

    private final static String TABLE_NAME = "tb_search";
    private static SearchDao instance;
    private Database db_helper;
    private SQLiteDatabase db;

    /**
     * 构造方法
     */
    private SearchDao() {
        this.db_helper = Database.getInstance();
    }

    /**
     * 获取单例实例
     *
     * @return 搜索记录管理类
     */
    public static SearchDao getInstance() {
        if (instance == null) instance = new SearchDao();
        return instance;
    }

    /**
     * 更新地区数据版本
     *
     * @param keyword 搜索关键字
     */
    public void insertOrUpdate(String keyword) {
        db = db_helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("keyword", keyword);
        cv.put("time", System.currentTimeMillis());
        db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * 获取历史查询记录
     * @return 历史查询记录集
     */
    public List<String> getKeywords() {
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "time DESC limit 0, 12");
        List<String> recordList = new ArrayList<>();
        while (cursor.moveToNext())
            recordList.add(cursor.getString(cursor.getColumnIndex("keyword")));
        cursor.close();
        db.close();
        return recordList;
    }

    // 从查询集中生成对象
    private Search buildData(Cursor cursor) {
        Search bean = new Search();
        bean.keyword = cursor.getString(cursor.getColumnIndex("id"));
        bean.time = cursor.getLong(cursor.getColumnIndex("name"));
        return bean;
    }

    public void delete() {
        db = db_helper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
