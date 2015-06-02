package com.tagcash.waalah.util;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

public class WAPreferenceManager {
	private static SharedPreferences preferences;
	
	public static class PreferenceKeys {
		public static String STRING_LAST_SINGED_USER_LOGIN = "string_last_singed_user_login";
		public static String STRING_LAST_SINGED_USER_EMAIL = "string_last_singed_user_email";
		public static String STRING_LAST_SINGED_USER_PASSWORD = "string_last_singed_user_password";
		public static String STRING_USER_TOKEN = "string_user_token";
		public static String INT_EMOJI_ACTIVITY = "int_emoji_activity";
	}

	public static void initializePreferenceManager(SharedPreferences _preferences) {
		preferences = _preferences;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}
	public static void setBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static int getInteger(String key, int defaultInt) {
		return preferences.getInt(key, defaultInt);
	}
	public static void setInt(String key, int value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static String getString(String key,String defaultValue) {
		return preferences.getString(key,defaultValue);
	}
	public static void setString(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static Set<String> getMessageIds(String key, Set<String> defaultValue) {
		return preferences.getStringSet(key, defaultValue);
	}
	@SuppressLint("NewApi")
	public static void setMessageIds(String key, Set<String> value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
		editor.putStringSet(key, value);
		editor.commit();
	}

//	public static int getUnreadMessageCount() {
//		String userId = WAPreferenceManager.getString(
//				PreferenceKeys.USER_ID, "");
//
//		Set<String> unreadMsgIds = getMessageIds(
//				PreferenceKeys.UNREAD_MESSAGE_IDS + "_" + userId, null);
//		if (unreadMsgIds == null)
//			return 0;
//		return unreadMsgIds.size();
//	}
//
//	public static void saveUnreadMessageId(String messageId) {
//		String userId = WAPreferenceManager.getString(
//				PreferenceKeys.USER_ID, "");
//
//		Set<String> unreadMsgIds = WAPreferenceManager.getMessageIds(
//				WAPreferenceManager.PreferenceKeys.UNREAD_MESSAGE_IDS + "_" + userId, null);
//		if (unreadMsgIds == null)
//			unreadMsgIds = new HashSet<String>();
//		if (!unreadMsgIds.contains(messageId))
//			unreadMsgIds.add(messageId);
//		WAPreferenceManager.setMessageIds(
//				WAPreferenceManager.PreferenceKeys.UNREAD_MESSAGE_IDS + "_" + userId, unreadMsgIds);
//	}
//
}
