package com.tagcash.waalah.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.tagcash.waalah.database.DBManager;
import com.tagcash.waalah.util.DeviceInfo;
import com.tagcash.waalah.util.WAImageLoader;
import com.tagcash.waalah.util.WAPreferenceManager;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.NetworkConnectionUtil;
import com.tagcash.waalah.util.Validation;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class WAApplication extends Application {
	private SharedPreferences sharedPreferences;
	private static Context applicationContext;
	
	public static boolean INIT_FLAG = true;
	public static int mScreenWidth = 0;
	public static int mScreenHeight = 0;
	public static boolean mBPortrait = true;

	public void onCreate() {
		super.onCreate();

		applicationContext = this.getApplicationContext();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		WAPreferenceManager.initializePreferenceManager(sharedPreferences);

		MessageUtil.initializeMesageUtil(applicationContext);
		ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
		NetworkConnectionUtil.initializeNetworkConnectionUtil(connectivityManager);
		DeviceInfo.initializeDeviceInfo(applicationContext);
		Validation.initialize(this.getApplicationContext());

		// initialize Image Loader
		initImageLoader(applicationContext.getApplicationContext());
		new WAImageLoader();
		WAImageLoader.init();
		WAImageLoader.clearCache();
		
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mScreenWidth = size.x;
		mScreenHeight = size.y;

		// initialize SQL DB
		new DBManager(this.getApplicationContext());

		checkSignatures();
}

	public void onTerminate() {
		super.onTerminate();
		
		DBManager.getInstance().close();
		System.out.println("######## healthchatApplication OnTerminate ########### ");
		WAApplication.INIT_FLAG = true;
	}

	public static Context getContext() {
		return applicationContext;
	}

	public static void clearCredentialsCache() {
		WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, "");
		WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_EMAIL, "");
		WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, "");
	}

	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public void checkSignatures() { 
		// Add code to print out the key hash 
		try { 
			PackageInfo info = getPackageManager().getPackageInfo( 
					"com.tagcash.waalah", PackageManager.GET_SIGNATURES); 
			for (Signature signature : info.signatures) { 
				MessageDigest md = MessageDigest.getInstance("SHA"); 
				md.update(signature.toByteArray());

				String hashkey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				Log.d("KeyHash:", hashkey); 
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
	} 

	public static WAApplication getApp(Context ctx) {
		return (WAApplication)ctx.getApplicationContext();
	}
}