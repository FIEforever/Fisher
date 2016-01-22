package com.doyoteam.util;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.MainApp;
import com.doyoteam.fisher.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImageLoaderConfig {
	private DisplayImageOptions options;
	private static ImageLoaderConfig config;

	public static ImageLoaderConfig init(int role){
		if(config == null) {
			config = new ImageLoaderConfig(role);
		}
		config.initOptions(role);
		return config;
	}
	
	private ImageLoaderConfig(int role){
		initConfigs();
		initOptions(role);
	}

	private void initConfigs() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainApp.getContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
//			.writeDebugLogs() // Remove for release app
			.build();
		
		ImageLoader.getInstance().init(config);
	}

	
	/**
	 * 初始化imageloader显示图片时的参数
	 * @param type, 0为图片加载失败，1为头像，2为商家封面
	 */
	private void initOptions(int type) {
		int resId = 0;
		switch(type) {
            case Constants.IMAGE_DEFAULT_TYPE1:			// 默认图片1
//                resId = R.drawable.user_head;
                break;
			case Constants.IMAGE_DEFAULT_POST:				// 默认帖子图片
				break;
			case Constants.IMAGE_DEFAULT_PHOTO:			// 相册默认图
				break;
		}

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(resId)
			.showImageForEmptyUri(resId)
			.showImageOnFail(resId)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
//			.displayer(new RoundedBitmapDisplayer((int) (100 * (new DisplayMetrics().density))).
				.build();
	}
	
	public DisplayImageOptions getDisplayImageOptions(){
		return options;
	}
}
