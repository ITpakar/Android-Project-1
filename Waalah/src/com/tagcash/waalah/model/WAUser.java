package com.tagcash.waalah.model;

import com.tagcash.waalah.util.MD5Util;

import android.location.Location;

public class WAUser {
	public static final String DEFAULT_PASSWORD = MD5Util.getMD5("socialpassword");  // for facebook, twitter, google


	public int id; // primary key
	public int user_id;
	public String email;
	public String login;
	public String password;
	public String fullname;
	public String picture_url;
	public String hometown; 
	public int coins;
	
	private void init() {
		this.user_id = 0;
		this.email = "";
		this.login = "";
		this.password = "";
		this.picture_url = "";
		this.hometown = "";
		this.coins = 0;
		this.fullname = "";
	}

	public WAUser() {
		init();
	}

	public WAUser(WAUser user) {
		init();
		if (user == null)
			return;
		
		user_id = user.user_id;
		email = user.email;
		login = user.login;
		picture_url = user.picture_url;
		hometown = user.hometown;
		coins = user.coins;
		fullname = user.fullname;
	}
}
