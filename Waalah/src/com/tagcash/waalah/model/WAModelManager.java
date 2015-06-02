package com.tagcash.waalah.model;

import com.tagcash.waalah.database.DBConstant;
import com.tagcash.waalah.database.DBManager;
import com.tagcash.waalah.util.WAPreferenceManager;

public class WAModelManager {

	private static WAModelManager instance;
	
	private WAUser sing_in_user;
	private WATopic topic;
	private WAPost post;

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
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, user.login);
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_EMAIL, user.email);
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, user.password);
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_USER_TOKEN, user.token);
			
			// save to database
			user.type = DBConstant.TYPE_ME;
			user.online = 1;
			DBManager.getInstance().addOrUpdateOneUser(user);
		}
		else {
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, "");
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_EMAIL, "");
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, "");
			WAPreferenceManager.setString(WAPreferenceManager.PreferenceKeys.STRING_USER_TOKEN, "");
		}
	}

	public WATopic getCurrentTopic() {
		return topic;
	}
	
	public void setCurrentTopic(WATopic topic) {
		this.topic = topic;
	}
	
	public WAPost getCurrentPost() {
		return post;
	}
	
	public void setCurrentPost(WAPost post) {
		this.post = post;
	}
}
