package com.tagcash.waalah.model;

import java.util.Date;

import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.util.MD5Util;

public class WAUser {

	public static final String DEFAULT_PASSWORD = MD5Util.getMD5("socialpassword");  // for facebook, twitter, google
	public static final String DEFAULT_BIRTHDAY = "1982-01-01";  // for facebook, twitter, google
	public static final int AVATAR_SIZE = 384;

	public int id; // primary key
	public int user_id;
	public int online;
	public String token;
	public int type;
	public String email;
	public String login;
	public String fullname;
	public String password;
	public String gender;
	public String birthday;
	public String picture_url;
	public String hometown;
	public String health_topics_array;
	public String diagnosed_with_array;
	public int diagnosed_with_privacy;
	public String medicated_array;
	public int medicated_privacy;
	public int like_count;
	public int lid; // like id
	public String about;
	public Date last_reqeust_time;
	
	private void init() {
		this.user_id = 0;
		this.online = 0;
		this.token = "";
		this.type = 0;
		this.email = "";
		this.login = "";
		this.fullname = "";
		this.password = "";
		this.gender = Constants.GENDER.sMale;
		this.birthday = "";
		this.picture_url = "";
		this.hometown = "";
		this.health_topics_array = "";
		this.diagnosed_with_array = "";
		this.diagnosed_with_privacy = 0;
		this.medicated_array = "";
		this.medicated_privacy = 0;
		this.like_count = 0;
		this.lid = 0;
		this.about = "";
		this.last_reqeust_time = new Date();
	}

	public WAUser() {
		init();
	}

	public WAUser(WAUser user) {
		init();
		if (user == null)
			return;
		
		user_id = user.user_id;
		online = user.online;
		token = user.token;
		type = user.type;
		email = user.email;
		login = user.login;
		fullname = user.fullname;
		gender = user.gender;
		birthday = user.birthday;
		picture_url = user.picture_url;
		hometown = user.hometown;
		health_topics_array = user.health_topics_array;
		diagnosed_with_array = user.diagnosed_with_array;
		diagnosed_with_privacy = user.diagnosed_with_privacy;
		medicated_array = user.medicated_array;
		medicated_privacy = user.medicated_privacy;
		like_count = user.like_count;
		lid = user.lid;
		about = user.about;
		last_reqeust_time = user.last_reqeust_time;
	}

//	public boolean isOnline() {
//		long currentTime = System.currentTimeMillis();
//		long userLastRequestAtTime = this.last_reqeust_time.getTime();
//		/*
//		 * if user didn't do anything last 5 minutes (5*60*1000 milliseconds)    
//		 */
//		if((currentTime - userLastRequestAtTime) < Constants.SESSION_TIME_OUT){ 
//			return true;
//		}
//
//		return false;
//	}
//
	public static String iGengerToSGender(final int i) {
		String s = Constants.GENDER.sMale;
		switch (i) {
		case Constants.GENDER.iMale:
			s = Constants.GENDER.sMale;
			break;
		case Constants.GENDER.iFemale:
			s = Constants.GENDER.sFemale;
			break;
		case Constants.GENDER.iOther:
			s = Constants.GENDER.sOther;
			break;
		}

		return s;
	}

	public static int sGengerToiGender(String s) {
		int i = Constants.GENDER.iMale;
		if (s.equalsIgnoreCase(Constants.GENDER.sMale)) {
			i = Constants.GENDER.iMale;
		} else if (s.equalsIgnoreCase(Constants.GENDER.sFemale)) {
			i = Constants.GENDER.iFemale;
		} else if (s.equalsIgnoreCase(Constants.GENDER.sOther)) {
			i = Constants.GENDER.iOther;
		}
		return i;
	}
}
