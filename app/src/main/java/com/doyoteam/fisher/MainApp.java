package com.doyoteam.fisher;

import android.app.Application;

import com.doyoteam.util.SystemModel;

/**
 * App单例
 */
public class MainApp extends Application {
    private static MainApp instance;
//    private IWXAPI wxapi;

    public static MainApp getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SystemModel.init();
//        regToWX();
//        //默认进入是页面 0:首页；1：钓场；2：学堂；3：我的
        DataModel.getInstance().setWhichTab(3);
        SystemModel.getInstance().init();
    }
}
