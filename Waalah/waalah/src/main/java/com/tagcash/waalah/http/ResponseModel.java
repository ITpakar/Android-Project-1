package com.tagcash.waalah.http;

import java.util.ArrayList;

public class ResponseModel {

	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAIL = 0;

	public class GeneralModel {
		public int status;
		public int error_code;
		public String msg;
	}

	public class LoginResultModel {
		public int status;
		public int id;
		public String api_token;
		public String reason;
	}
	
	public class RegisterModel {
		public int status;
		public int id;
		public String api_token;
		public String reason;

	}

	public class CurrentUserResultModel {
		public int status;
		public UserModel me;
		public BalanceModel balance;
		public ImgModel img;
	}

	/*
	 * for User model
	 */
	public class UserModel {
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
	}
	
	public class BalanceModel {
		public float coins;
		public float diamonds;
	}
	
	public class ImgModel {
		public String original;
		public String large;
		public String medium;
		public String small;
	}
	
	public class OneUserModel {
		public int status;
		public int error_code;
		public String msg;
		public UserModel user;
	}
	
	public class UserModelList {
		public int status;
		public int error_code;
		public String msg;
		public ArrayList<UserModel> member_list;
	}
	
	public class ResetPasswordModel {
		public int status;
		public String msg;
		public int error_code;
	}
	
	public class UpdateAvatarModel {
		public int status;
		public String msg;
		public int error_code;
		public String picture_url;
	}
	
	public class UserLikedModel {
		public int status;
		public String msg;
		public int error_code;
		public int lid; // like id
		public int member_id;
		public int like_count;
	}
	
}
