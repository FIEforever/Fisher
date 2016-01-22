package com.doyoteam.fisher.db;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.MainApp;

/**
 * 数据库Helper
 */
public class BaseDatabase extends SQLiteAssetHelper {

    private static BaseDatabase instance;

    private BaseDatabase() {
        super(MainApp.getContext(), Constants.DB_BASE_NAME, null, Constants.DB_BASE_VERSION);
    }

    public static BaseDatabase getInstance() {
        if (instance == null) instance = new BaseDatabase();
        return instance;
    }
}