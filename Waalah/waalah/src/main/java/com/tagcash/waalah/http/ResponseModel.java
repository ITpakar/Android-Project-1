package com.tagcash.waalah.http;

import java.util.ArrayList;
import java.util.Date;

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
		public String user_group;
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

	public class EventModel {
		public String id; // primary key
		public int event_id;
		public String event_name;
		public String event_description;
		public String event_start_date;
		public String event_owner_id;
		public String duration;
		public String price;
		public String status;
		public String joined_count;
		public String image_url;
		public String thumb_url;
		public String video_url;
		public String audio_url;
		public String chat_url;
		public String coins_count;
		public String diamonds_count;
		public String event_created;

		// public String me;
	}

	public class EventPager {
		public int offset;
		public int limit;
		public int total;
	}

	public class EventsListModel {
		public int status;
		public int events_count;
		public ArrayList<EventModel> events;
		public EventPager pager;
	}
}
