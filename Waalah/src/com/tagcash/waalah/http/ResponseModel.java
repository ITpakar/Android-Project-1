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

	/*
	 * for User model
	 */
	public class RegisterModel {
		public int status;
		public String msg;
		public int error_code;
		public String token;
	}

	public class CheckRegisterModel {
		public int error_code;
		public String email;
		public String login;
		public String password;
	}

	public class UserModel {
		public int uid;
		public int online; // online status : 1 - online, 0 - offline
		public String name;
		public String email;
		public String password;
		public String full_name;
		public String gender;
		public String birthday;
		public String picture_url;
		public String social_picture_url;
		public String token;
		public String address;
		public String health_topic_array;
		public String diagnosed_with_array;
		public int diagnosed_with_privacy;
		public String medicated_array;
		public int medicated_privacy;
		public int like_count;
		public int lid; // like id
		public String last_reqeust_time;
		public String about;
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
	
	public class LoginResultModel {
		public int status;
		public int error_code;
		public UserModel user;
		public String msg;
		public String token;
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
	
	/*
	 * for Topic model
	 */
	public class AddTopicModel {
		public int status;
		public String msg;
		public int error_code;
		public int tid; // topic_id
	}
	
	public class TopicModel {
		public int tid;
		public String title;
		public String desc;
		public int user_id;
		public int post_count;
		public int like_count;
		public int flag_count;
		public int lid; // like id
		public int fid; // flag_id
	}
	
	public class TopicListModel {
		public int status;
		public String msg;
		public int error_code;
		public ArrayList<TopicModel> topic_list;
	}
	
	public class TopicLikedModel {
		public int status;
		public String msg;
		public int error_code;
		public int lid; // like id
	}
	
	public class TopicFlagedModel {
		public int status;
		public String msg;
		public int error_code;
		public int fid; // flag id
	}
	
	/*
	 * for Post model
	 */
	public class PostModel {
		public int pid;
		public int user_id;
		public String user_full_name;
		public String user_picture_url;
		public String user_social_picture_url;
		public int user_gender;
		public int topic_id;
		public String topic_title;
		public String title;
		public String picture_url;
		public int comment_count;
		public int like_count;
		public int flag_count;
		public int lid; // like id
		public int cid; // comment id
		public int shared;
		public String shared_type;
		public int fid; // flag id
		public String created_time;
	}
	
	public class PostListModel {
		public int status;
		public String msg;
		public int error_code;
		public ArrayList<PostModel> post_list;
	}
	
	public class PostOneModel {
		public int status;
		public String msg;
		public int error_code;
		public PostModel post;
	}
	
	public class PostLikedModel {
		public int status;
		public String msg;
		public int error_code;
		public int lid; // like id
		public int post_id;
		public int like_count;
	}
	
	public class PostFlagedModel {
		public int status;
		public String msg;
		public int error_code;
		public int fid; // flag id
		public int post_id;
	}
	
	public class PostSharedModel {
		public int status;
		public String msg;
		public int error_code;
		public int sid; // shared id
		public int post_id;
		public int shared; // shared count?
	}
	
	public class PostLikeModel {
		public int lid;
		public int user_id;
		public int post_id;
		public String user_full_name;
		public String user_social_picture_url;
		public String user_picture_url;
		public String user_birthday;
		public int user_gender;
	}

	public class PostLikelistModel {
		public int status;
		public String msg;
		public int error_code;
		public ArrayList<PostLikeModel> like_list;
	}

	public class PostEditModel {
		public int status;
		public String msg;
		public int error_code;
		public int pid;
		public String picture_url;
	}
	
	/*
	 * for Comment model
	 */
	public class AddCommentModel {
		public int status;
		public String msg;
		public int error_code;
		public int cid; // comment id
	}

	public class CommentModel {
		public int cid;
		public int user_id;
		public String user_full_name;
		public String user_picture_url;
		public String user_social_picture_url;
		public int user_gender;
		public int post_id;
		public String title;
		public int like_count;
		public int flag_count;
		public int lid;
		public int fid;
		public String created_time;
	}
	
	public class CommentListModel {
		public int status;
		public String msg;
		public int error_code;
		public ArrayList<CommentModel> comment_list;
	}
	
	public class CommentLikedModel {
		public int status;
		public String msg;
		public int error_code;
		public int lid; // like id
		public int comment_id;
		public int like_count;
	}
	
	public class CommentFlagedModel {
		public int status;
		public String msg;
		public int error_code;
		public int fid; // flag id
		public int comment_id;
	}
	
	/*
	 * for Message model
	 */
	
	public class MessageConnectedModel {
		public int status;
		public String msg;
		public int error_code;
		public int mid; // message id
	}
	
	public class MessageModel {
		public int mid; // message id
		public int user_id; // sender id
		public String message; // chat message
		public int receiver_id; // receiver id
		public String created_time;
	}
	
	public class MessageSendModel {
		public int status;
		public String msg;
		public int error_code;
		public MessageModel message;
	}
	
	public class MessageListModel {
		public int status;
		public String msg;
		public int error_code;
		public ArrayList<MessageModel> message_list;
	}
}
