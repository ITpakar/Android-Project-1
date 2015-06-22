package com.tagcash.waalah.model;

import com.tagcash.waalah.util.MD5Util;

public class WAUser {
	public static final String DEFAULT_PASSWORD = MD5Util.getMD5("socialpassword");  // for facebook, twitter, google

	public int id;
	public String username;
	public String password;
	public String email;
	public String firstname;
	public String lastname;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String api_token;
	public String quickblox_id;
	public String allow_notifications;
	public String picture_url;
	public int coins;
	
	private void init() {
		this.id = 0;
		this.email = "";
		this.username = "";
		this.password = "";
		this.picture_url = "";
		this.coins = 0;
	}

	public WAUser() {
		init();
	}

	public WAUser(WAUser user) {
		init();
		if (user == null)
			return;

		id = user.id;
		username = user.username;
		password = user.password;
		email = user.email;
		firstname = user.firstname;
		lastname = user.lastname;
		address = user.address;
		city = user.city;
		state = user.state;
		zip = user.zip;
		country = user.country;
		api_token = user.api_token;
		quickblox_id = user.quickblox_id;
		allow_notifications = user.allow_notifications;
		picture_url = user.picture_url;
		coins = user.coins;

	}
}
