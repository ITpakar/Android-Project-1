package com.tagcash.waalah.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceInfo {
	private final static String DEBUG_TAG = "DeviceInfo";
	private static Context context;
	private static PackageManager packageManager;
	private static TelephonyManager telephonyManager;
	public static final String PACKAGE_NAMESPACE = "com.jmd.healthchat";


	//  #############################################################################
	//  #                                 DeviceInfo                                #
	//  #############################################################################
	// Mandatory: We need to initialize this class when the application starts.
	public static void initializeDeviceInfo(Context applicationContext) {
		context = applicationContext;
		packageManager = applicationContext.getPackageManager();
		telephonyManager = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
	}


	public static boolean isDevice() {
		return !android.os.Build.MODEL.equals("sdk")
				&& !android.os.Build.PRODUCT.equals("sdk")
				&& !android.os.Build.DEVICE.equals("generic");
	}


	@SuppressWarnings("deprecation")
	public static void printMemoryInfo() {
		String orientation = "";

		int o = context.getResources().getConfiguration().orientation;
		orientation = ((o == Configuration.ORIENTATION_LANDSCAPE) ? "LANDSCAPE" : "PORTRAIT");
		orientation = "[" + orientation + "]";

		double totalMemoryUsed = (Runtime.getRuntime().totalMemory() + Debug.getNativeHeapAllocatedSize());
		int percentUsed = (int) (totalMemoryUsed / Runtime.getRuntime().maxMemory() * 100);

		Log.i(DEBUG_TAG, "-->" + orientation + "getNativeHeapFreeSize() = " + (Debug.getNativeHeapFreeSize() / 1000f) + "kb");
		Log.i(DEBUG_TAG, "-->" + orientation + "getNativeHeapAllocatedSize() = " + (Debug.getNativeHeapAllocatedSize() / 1000f) + "kb");
		Log.i(DEBUG_TAG, "-->" + orientation + "getNativeHeapSize() = " + (Debug.getNativeHeapSize() / 1000f) + "kb");
		Log.i(DEBUG_TAG, "-->" + orientation + "totalMemoryUsed = " + (totalMemoryUsed / 1000f) + "kb");
		Log.i(DEBUG_TAG, "-->" + orientation + "percentUsed = " + (percentUsed) + "%");
		Log.i(DEBUG_TAG, "-->" + orientation + "getLoadedClassCount() = " + (Debug.getLoadedClassCount() / 1000f) + "kb");
		Log.i(DEBUG_TAG, "-->" + orientation + "getThreadAllocSize() = " + (Debug.getThreadAllocSize() / 1000f) + "kb");
		Log.i(DEBUG_TAG, "-->" + orientation + "getThreadExternalAllocSize() = " + (Debug.getThreadExternalAllocSize() / 1000f) + "kb");
	}


	@SuppressWarnings("deprecation")
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();

		return (availableBlocks * blockSize);
	}


	@SuppressWarnings("deprecation")
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();

		return (totalBlocks * blockSize);
	}


	public static String getDeviceInformation() {
		StringBuilder info = new StringBuilder();
		info.append("\n____________________________________________________________________________");
		info.append("\nDeviceInformation");
		info.append("\n____________________________________________________________________________");

		int versionCode = getVersionCode();
		if (versionCode > 0) {
			info.append("\nVersion Code: " + getVersionCode());
		} else {
			info.append("\nVersion Code: " + "Unknown");
		}
		info.append("\nVersion Name: " + getVersionName());
		info.append("\nPackage: " + PACKAGE_NAMESPACE);

		info.append("\nAndroid Version: " + getAndroidVersion());
		int apiLevel = getAPILevel();
		if (apiLevel > 0) {
			info.append("\nAPI Level: " + apiLevel);
		} else {
			info.append("\nAPI Level: " + "Unknown");
		}
		info.append("\nBrand: " + getBrand());
		info.append("\nDevice: " + getDeviceName());
		info.append("\nPhone Model: " + getModelName());
		info.append("\nProduct: " + getProductName());
		info.append("\nBoard: " + getBoard());
		info.append("\nDisplay: " + getDisplay());
		info.append("\nFinger Print: " + getFingerPrint());
		info.append("\nHost: " + getHost());
		info.append("\nID: " + getId());
		info.append("\nTags: " + getTags());
		info.append("\nTime: " + getTime());
		info.append("\nType: " + getType());
		info.append("\nUser: " + getUser());

		info.append("\nTotal Internal memory: " + getTotalInternalMemorySize());
		info.append("\nAvailable Internal memory: " + getAvailableInternalMemorySize());

		info.append("\n____________________________________________________________________________");

		return info.toString();
	}
	//  #############################################################################


	//  #############################################################################
	//  #                             Device Information                            #
	//  #############################################################################
	public static String getAndroidVersion() {
		String androidVersion;
		try {
			androidVersion = android.os.Build.VERSION.RELEASE;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			androidVersion = "Unknown";
		}

		return androidVersion;
	}


	@SuppressWarnings("deprecation")
	public static int getAPILevel() {
		int apiLevel = -1;
		try {
			apiLevel = Integer.parseInt(android.os.Build.VERSION.SDK);
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
		}

		return apiLevel;
	}


	public static String getBrand() {
		String brand;
		try {
			brand = android.os.Build.BRAND;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			brand = "Unknown";
		}

		return brand;
	}


	public static String getDeviceName() {
		String deviceName;
		try {
			deviceName = android.os.Build.DEVICE;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			deviceName = "Unknown";
		}

		return deviceName;
	}


	public static String getModelName() {
		String modelName;
		try {
			modelName = android.os.Build.MODEL;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			modelName = "Unknown";
		}

		return modelName;
	}


	public static String getProductName() {
		String productName;
		try {
			productName = android.os.Build.PRODUCT;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			productName = "Unknown";
		}

		return productName;
	}


	public static String getBoard() {
		String board;
		try {
			board = android.os.Build.BOARD;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			board = "Unknown";
		}

		return board;
	}


	public static String getDisplay() {
		String display;
		try {
			display = android.os.Build.DISPLAY;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			display = "Unknown";
		}

		return display;
	}


	public static String getFingerPrint() {
		String fingerPrint;
		try {
			fingerPrint = android.os.Build.FINGERPRINT;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			fingerPrint = "Unknown";
		}

		return fingerPrint;
	}


	public static String getHost() {
		String host;
		try {
			host = android.os.Build.HOST;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			host = "Unknown";
		}

		return host;
	}


	public static String getId() {
		String id;
		try {
			id = android.os.Build.ID;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			id = "Unknown";
		}

		return id;
	}


	public static String getTags() {
		String tags;
		try {
			tags = android.os.Build.TAGS;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			tags = "Unknown";
		}

		return tags;
	}


	public static String getTime() {
		String time;
		try {
			Date date = new Date(android.os.Build.TIME);
			time = date.toString();
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			time = "Unknown";
		}

		return time;
	}


	public static String getType() {
		String type;
		try {
			type = android.os.Build.TYPE;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			type = "Unknown";
		}

		return type;
	}


	public static String getUser() {
		String user;
		try {
			user = android.os.Build.USER;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			user = "Unknown";
		}

		return user;
	}

	public static String getCountry() {
		String country;
		try {
			country = telephonyManager.getNetworkCountryIso();
			country = (country == null || "".equals(country)) ? "Unknown" : country;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			country = "Unknown";
		}
		return country;
	}

	public static String getCarrier() {
		String carrier;
		try {
			carrier = telephonyManager.getSimOperatorName();
			carrier = (carrier == null || "".equals(carrier)) ? "Unknown" : carrier;
		} catch (Exception exception) {
			//Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			carrier = "Unknown";
		}
		return carrier;
	}

	public static String getDeviceId() {
		String deviceId;
		try {
			deviceId = telephonyManager.getDeviceId();
			deviceId = (deviceId == null || "".equals(deviceId)) ? "Unknown" : deviceId;
		} catch (Exception exception) {
			////Log.e(DEBUG_TAG, exception);
			exception.printStackTrace();
			deviceId = "Unknown";
		}
		return deviceId;
	}
	//  #############################################################################


	//  #############################################################################
	//  #                          Application Information                          #
	//  #############################################################################
	public static int getVersionCode() {
		int version = -1;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(PACKAGE_NAMESPACE, PackageManager.GET_META_DATA);
			version = packageInfo.versionCode;
		} catch (Exception exception) {
			//Diva//Log.error(DEBUG_TAG, exception);
			exception.printStackTrace();
		}

		return version;
	}


	public static String getVersionName() {
		String versionName;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(PACKAGE_NAMESPACE, PackageManager.GET_META_DATA);
			versionName = packageInfo.versionName;
		} catch (Exception exception) {
			//Diva//Log.error(DEBUG_TAG, exception);
			exception.printStackTrace();
			versionName = "Unknown";
		}

		return versionName;
	}


	public static boolean isAppForeground() {
		return isAppForeground(1);
	}


	public static boolean isAppForeground(int leastRunningTaskCount) {
		ComponentName componentName = getTopActivityComponentName(leastRunningTaskCount);
		System.out.println("######## isAppForeground ###### "+componentName+" : "+PACKAGE_NAMESPACE);
		return (componentName != null) && componentName.getPackageName().startsWith(PACKAGE_NAMESPACE);
	}


	public static ComponentName getTopActivityComponentName(int leastRunningTaskCount) {
		ComponentName componentName = null;
		try {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTask = activityManager.getRunningTasks(1);

			if (runningTask != null && runningTask.size() > 0) {
				RunningTaskInfo info = runningTask.get(0);
				if (info != null && info.numRunning >= leastRunningTaskCount) {
					componentName = info.topActivity;
					System.out.println("######## gettop activity name ###### "+componentName.getPackageName()+" : "+componentName.getClassName());
				}
			}
		} catch (Exception exception) {
			//Diva//Log.error(DEBUG_TAG, exception);
			exception.printStackTrace();
		}

		return componentName;
	}


	public static boolean isServiceRunning(String serviceClassName) {
		boolean isServiceRunning = false;
		try {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningServiceInfo> runningServices = activityManager.getRunningServices(50);

			for (RunningServiceInfo runningService : runningServices) {
				if (runningService != null && runningService.service != null &&
						runningService.service.getClassName() != null &&
						runningService.service.getClassName().equals(serviceClassName))
					isServiceRunning = true;
			}
		} catch (Exception exception) {
			//Diva//Log.error(DEBUG_TAG, exception);
			exception.printStackTrace();
		}

		return isServiceRunning;
	}


	public static boolean isAppRunning(String appProcessName) {
		boolean result = false;

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> listOfApps = am.getRunningAppProcesses();

		if (listOfApps != null && listOfApps.size() > 0) {
			for (int i = 0; i < listOfApps.size(); i++) {
				String appName = listOfApps.get(i).processName;
				if (StringUtil.same(appName, appProcessName))
					result = true;
			}
		}

		return result;
	}
	//  #############################################################################


	//  #############################################################################
	//  #                              Third-Party Apps                             #
	//  #############################################################################
	public static List<String> getInstalledApplicationNames() {
		List<String> applicationLabels = new ArrayList<String>();
		try {
			List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
			for (ApplicationInfo packageInfo : installedApps) {
				CharSequence label = packageInfo.loadLabel(packageManager);

				if (label != null)
					applicationLabels.add(label.toString());

			}
		} catch (Exception exception) {
			//Diva//Log.error(DEBUG_TAG, exception);
			exception.printStackTrace();
		}
		return applicationLabels;
	}


	public static boolean launchApp(String appPackageName) {
		return launchApp(appPackageName, null);
	}


	public static boolean launchApp(String appPackageName, String appActivityName) {
		boolean result = false;
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);

		for (ResolveInfo resolveInfo : apps) {
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			if (activityInfo == null)
				continue;

			String packageName = activityInfo.packageName;
			if (StringUtil.isNullOrEmpty(packageName))
				continue;

			String name = appActivityName;
			if (StringUtil.isNullOrEmpty(name))
				name = activityInfo.name;

			if (StringUtil.same(packageName, appPackageName)) {
				Intent appLauncherIntent = new Intent(Intent.ACTION_MAIN, null);
				appLauncherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

				Log.i(DEBUG_TAG, "Launching: packageName=" + packageName + " name=" + name);
				appLauncherIntent.setComponent(new ComponentName(packageName, name));
				appLauncherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				context.startActivity(appLauncherIntent);
				result = true;
			}
		}

		return result;
	}


	public static boolean isPackageInstalled(String packageName, int minVersionCode) {
		boolean isPackageInstalled = false;
		List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo appInfo : installedApps) {
			if (StringUtil.sameIgnoreCase(packageName, appInfo.packageName)) {
				try {
					PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
					if (minVersionCode > 0 && packageInfo != null) {
						isPackageInstalled = packageInfo.versionCode >= minVersionCode;
					} else {
						isPackageInstalled = true;
					}
				} catch (Exception exception) {
					////Diva//Log.error(DEBUG_TAG, exception);
					exception.printStackTrace();
				}
				break;
			}
		}
		return isPackageInstalled;
	}


	public static boolean isActivityAvailable(Intent intent) {
		boolean result = true;
		try {
			List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
			if (activities == null || activities.size() == 0) {
				result = false;
			}
		} catch (Exception exception) {
			////Diva//Log.error(DEBUG_TAG, exception);
			exception.printStackTrace();
		}
		return result;
	}


	public static boolean isAndroidMarketAvailable() {
		Uri marketUri = Uri.parse("market://search?q=checking");
		Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);

		return isActivityAvailable(intent);
	}

	public static boolean isSDCardAvailable() {
		boolean externalStorageAvailable = false;
		boolean externalStorageWriteable = false;
		try {
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				externalStorageAvailable = externalStorageWriteable = true;
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				externalStorageAvailable = true;
				externalStorageWriteable = false;
			} else {
				externalStorageAvailable = externalStorageWriteable = false;
			}
			if (externalStorageAvailable && externalStorageWriteable) return true;
			return false;
		} catch (Exception ex) {
			////Diva//Log.error(DEBUG_TAG, ex);
			ex.printStackTrace();
			return false;
		}
	}

	public static String getUniqueDeviceId() {
		final String tmDevice, tmSerial, androidId;
		String deviceId = "";
		try {
			tmDevice = "" + telephonyManager.getDeviceId();
			tmSerial = "" + telephonyManager.getSimSerialNumber();
			androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			deviceId = deviceUuid.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return deviceId;
	}
	//  #############################################################################
}
