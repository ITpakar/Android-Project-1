package com.tagcash.waalah.model;

import com.tagcash.waalah.database.DBManager;
import com.tagcash.waalah.util.WAPreferenceManager;

public class WAModelManager {

	private static WAModelManager instance;
	
	private WAUser sing_in_user;

	public static synchronized WAModelManager getInstance() {
		if (instance == null) {
			instance = new WAModelManager();
		}
		return instance;
	}

	public WAUser getSignInUser() {
		return sing_in_user;
	}

	public void setSignInUser(WAUser user) {
		this.sing_in_user = user;
		
		if (user != null) {
			// save last logged user info to preference.
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, user.username);
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_EMAIL, user.email);
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, user.password);
			
			// save to database
			DBManager.getInstance().addOrUpdateOneUser(user);
		}
		else {
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, "");
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_EMAIL, "");
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, "");
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_USER_TOKEN, "");
		}
	}
}
