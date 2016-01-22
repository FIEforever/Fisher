package com.doyoteam.fisher.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.MainApp;

/**
 * 用户数据数据库
 * 搜索记录操作类
 * Created by F on 2015/12/21.
 */

public class Database extends SQLiteOpenHelper {

    private static Database instance;
    private static final String tb_ad = "tb_ad";
    private static final String tb_search = "tb_search";

    private Database() {
        super(MainApp.getContext(), Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableAd(db);
        createTableSearch(db);
    }

    // 创建聊天信息表
    private void createTableSearch(SQLiteDatabase db) {
        String sqlString = "CREATE TABLE IF NOT EXISTS " + tb_search
                + "( keyword Text PRIMARY KEY,"    // 消息Id
                + "  time	 INTEGER NOT NULL)";   // 消息时间
        db.execSQL(sqlString);
    }

    // 创建聊天信息表
    private void createTableAd(SQLiteDatabase db) {
        String sqlString = "CREATE TABLE IF NOT EXISTS " + tb_ad
                + "( id INTEGER PRIMARY KEY,"       // 消息Id
                + "  name	 TEXT NOT NULL,"        // 消息时间
                + "  describe	 TEXT NOT NULL," // 消息时间
                + "  area	 TEXT NOT NULL,"     // 消息时间
                + "  url	 TEXT NOT NULL,"     // 消息时间
                + "  keyword TEXT NOT NULL,"        // 消息时间
                + "  action	 INTEGER NOT NULL)";    // 消息时间
        db.execSQL(sqlString);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tb_ad);
        db.execSQL("DROP TABLE IF EXISTS " + tb_search);
        onCreate(db);
    }
}