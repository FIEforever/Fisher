package com.doyoteam.fisher;

import android.content.Context;
import android.content.SharedPreferences;
import com.doyoteam.fisher.db.bean.User;

/**
 * 保存程序运行过程中需要使用的数据
 */
public class DataModel {
	private static DataModel instance = null;
	private User user = null;

	private SharedPreferences preferences = null;
	private int whichTab = 0;//0:首页；1：钓场；2：学堂；3：我
	private boolean isLogin = false;//用户是否有登录的操作，并且登录成功
	private boolean haveClickFlag = false;//是否点击登录按钮（防止多次点击多次跳转）
	private boolean needUpateFlag = false;//是否需要强制刷新数据 用户注销账户后使用

	public boolean isNeedUpateFlag() {
		return needUpateFlag;
	}

	public void setNeedUpateFlag(boolean needUpateFlag) {
		this.needUpateFlag = needUpateFlag;
	}

	public boolean isHaveClickFlag() {
		return haveClickFlag;
	}

	public void setHaveClickFlag(boolean haveClickFlag) {
		this.haveClickFlag = haveClickFlag;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setIsLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public int getWhichTab() {
		return whichTab;
	}

	public void setWhichTab(int whichTab) {
		this.whichTab = whichTab;
	}

	public SharedPreferences getPreferences() {
		if(preferences == null)
		{
			preferences = MainApp.getContext().getSharedPreferences(Constants.PRE_FILE_NAME,Context.MODE_PRIVATE);
		}
		return preferences;
	}

	private DataModel(){
	}

	public static DataModel getInstance() {
		if(instance == null)
			init();
		return instance;
	}

	private static void init() {
		instance = new DataModel();
	}

	public User getUser(){
		//用户为空时，首先去preferences文件中是拿用户信息，如果preferences中没有拿到数据，
		//则说明用户没有登录过
		if(user != null)
			return user;
		else {
			//登录
			return getLoginedUser();

		}
	}

	public void setUser(User user){
		this.user = user;
	}

	/**
	 * @describe TODO 获得当前登录的用户
	 * @return
	 */
	private User getLoginedUser(){
		String userID = getPreferences().getString(Constants.PRE_KEY_USER_ID,
				Constants.PRE_DEFAULT_STR);
		if(!userID.equals(Constants.PRE_DEFAULT_STR)){
			user = new User();

			user.userId = (userID);
			user.userName = (preferences.getString(Constants.PRE_KEY_USER_NAME,
					Constants.PRE_DEFAULT_STR));
			user.headPhotoUrl = (preferences.getString(Constants.PRE_KEY_USER_HEADIMG,
					Constants.PRE_DEFAULT_STR));
			user.mobilePhone = (preferences.getString(Constants.PRE_KEY_MOBILEPHONE,
					Constants.PRE_DEFAULT_STR));
			user.email = (preferences.getString(Constants.PRE_KEY_USER_EMAIL,
					Constants.PRE_DEFAULT_STR));
			user.sex = (preferences.getString(Constants.PRE_KEY_USER_SEX,
					Constants.PRE_DEFAULT_STR));
			return user;
		}
		return null;
	}

	/**
	 * @describe TODO
	 * @return 用户是否是登录状态
	 */
	public boolean isLogined(){
		String userID = getPreferences().getString(Constants.PRE_KEY_USER_ID,
				Constants.PRE_DEFAULT_STR);
		if(!userID.equals(Constants.PRE_DEFAULT_STR))
			return true;
		return false;
	}
}