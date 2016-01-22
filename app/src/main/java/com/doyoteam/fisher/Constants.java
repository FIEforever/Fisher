package com.doyoteam.fisher;

/**
 * 常量类
 */
public class Constants {
    // switcher of log
    public final static boolean IS_LOG_ON   = true;
    // 签名生成码
    public static final String SIGN_NAME = "apiSign";
    public static final String SIGN_KEY = "25273ddcc15b4202b47b08492d73d47f";
    // App Token
    public final static String TOKEN_NAME = "apiToken";
    public final static String TOKEN_KEY = "android";
    // 数据库名称和版本
    public final static String DB_NAME = "fisher.db";
    public final static int DB_VERSION = 1;

    public final static String DB_BASE_NAME = "fisher_base"; // 无需 .db
    public final static int DB_BASE_VERSION = 1;
    public final static int DB_BASE_FORCE_VERSION = 1;         // 最低版本号

    public final static int REQUEST_CODE_CITY = 100;            // 选择城市的回调值
    public final static int REQUEST_CODE_USER = 102;            // 登录用户的回调值
    // Http服务器接口前缀
    public static final String BASE_URL= "http://www.diaoyur.com/api/"; // API接口
//    public static final String BASE_URL= "http://school.diaoyur.cn/"; // API接口
    public static final String BASE_URL_SCHOOL = "http://school.diaoyur.cn/"; // 学堂接口
    /**preference 文件名*/
    public static final String PRE_FILE_NAME = "COM.DOYOTEAM.FISHER.PREFERENCE_FILE_KEY";
    /**用户id 在preference文件中的key*/
    public static final String PRE_KEY_USER_ID = "PRE_KEY_USER_ID";
    /**手机号 在preference文件中的key*/
    public static final String PRE_KEY_MOBILEPHONE= "PRE_KEY_MOBILEPHONE";
    /**昵称 在preference文件中的key*/
    public static final String PRE_KEY_USER_NAME = "PRE_KEY_USER_NAME";
    /**用户头像地址 在preference文件中的key*/
    public static final String PRE_KEY_USER_HEADIMG = "PRE_KEY_USER_HEADIMG";
    /**邮件 在preference文件中的key*/
    public static final String PRE_KEY_USER_EMAIL = "PRE_KEY_USER_EMAIL";
    /**用户性别 在preference文件中的key*/
    public static final String PRE_KEY_USER_SEX = "PRE_KEY_USER_SEX";
    /**地区 在preference文件中的key*/
    public static final String PRE_KEY_USER_AREA= "PRE_KEY_USER_AREA";
    /**用户是否第一次启动 在preference文件中的key*/
    public static final String PRE_KEY_FIRST_BOOT = "IS_FIRST_BOOT";
    /**preference文件String串默认值*/
    public static final String PRE_DEFAULT_STR = "";
    /**preference文件中int默认值*/
    public static final int PRE_DEFAULT_INT = -1;

    public final static int IMAGE_DEFAULT_TYPE1 = 1;        // 默认图标1
    public final static int IMAGE_DEFAULT_POST = 2;        // 默认帖子图片
    public final static int IMAGE_DEFAULT_PHOTO = 14;       // 默认相册图片
    public final static int IMAGE_DEFAULT_AD = 10;          // 默认广告图片

    //网页类型（标题栏控制）：1默认、2
    public static final String WEBVIEW_NORMAL = "WEBVIEW_NORMAL";//默认
}
