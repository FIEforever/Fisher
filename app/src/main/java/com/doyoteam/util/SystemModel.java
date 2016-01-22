package com.doyoteam.util;

import android.os.Environment;

import java.io.File;

/**
 * 系统参数类，保存系统常变量，通常情况下只允许被一次赋值
 * @author imatrix
 *
 */
public class SystemModel {
	private static SystemModel instance;
	private boolean isServiceOn;
	private boolean isLogout;
	private boolean isSdcardExist;
	private String tempDir;
	private String iconDir;
    private String picDir;
	private float screanDensity = 1.0f;
	
	public boolean isServiceOn() {
		return isServiceOn;
	}
	
	public void setServiceTag(boolean bool) {
		isServiceOn = bool;
	}
	
	public boolean isSdcardExist() {
		if(isSdcardExist)
			return true;
		Tools.showToast("设备SD卡不可用");
		return false;
	}
	
	public String getTempDir() {
		if(!isSdcardExist) {
			Tools.showToast("设备SD卡不可用");
			return null;
		}
		mkdir(tempDir);
		return tempDir;
	}
	
	public String getIconDir() {
		if(!isSdcardExist) {
			Tools.showToast("设备SD卡不可用");
			return null;
		}
		mkdir(tempDir);
		return iconDir;
	}

    public String getPicDir() {
        if(!isSdcardExist) {
            Tools.showToast("设备SD卡不可用");
            return null;
        }
        mkdir(picDir);
        return picDir;
    }

	private SystemModel() {
		this.isSdcardExist = initAppRootDir();
		this.isServiceOn = false;
	}
	

	/**
	 * 检查系统SD卡的可用性，并创建程序的专属文件夹
	 */
	private boolean initAppRootDir() {
		boolean sdCardExist = Environment.getExternalStorageState() 
			.equals(Environment.MEDIA_MOUNTED); 			//判断sd卡是否存在
		if (!sdCardExist) {
			Tools.showToast("设备SD卡不可用");
			return false;
		}
		String appHomeDir = Environment.getExternalStorageDirectory() + "/MyPost";
		tempDir = appHomeDir + "/temp/";
		mkdir(tempDir);
		iconDir = appHomeDir + "/headicon/";
		mkdir(iconDir);
        picDir = appHomeDir + "/picture/";
        mkdir(picDir);
		return true;
	}
	
	private void mkdir(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
	}

	public void setSdcardExist(boolean isSdcardExist) {
		this.isSdcardExist = isSdcardExist;
	}
	
	public static SystemModel getInstance() {
		if(instance == null)
			init();
		return instance;
	}
	
	public static void init() {
		instance = new SystemModel();
	}
	
	public static void destroy() {
		instance = null;
	}

	public float getScreanDensity() {
		return screanDensity;
	}

	public void setScreanDensity(float screanDensity) {
		this.screanDensity = screanDensity;
	}

	public boolean isLogout() {
		return isLogout;
	}

	public void setLogout(boolean isLogout) {
		this.isLogout = isLogout;
	}
}