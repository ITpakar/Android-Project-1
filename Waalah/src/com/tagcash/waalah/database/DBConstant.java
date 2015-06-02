package com.tagcash.waalah.database;

public class DBConstant {
	/*
	 * Common
	 */
	protected static final String TBL_COLUMN_ID = "id";
	public static String SPLIT_STRING = "#";

	/* Related DB */
	public static final String DATABASE_NAME = "waalah.db";
	public static final int DATABASE_VERSION = 1;
	
	/*
	 * Type
	 */
	public static final int TYPE_ALL = 0;
	public static final int TYPE_SUGGESTED = 1;
	public static final int TYPE_POPULAR = 2;
	public static final int TYPE_FAVORITE = 3;
	public static final int TYPE_ME = 4;

	/*
	 *  DB Tables Name
	 */
	public static final String TABLE_USER = "user";
	public static final String TABLE_TOPIC = "topic";
	public static final String TABLE_POST = "post";
	public static final String TABLE_COMMENT = "comment";
	public static final String TABLE_CHATMESSAGE = "chatmessage";
	public static final String TABLE_POSTLIKE = "postlike";

	/*
	 *  TABLE_USER
	 */
	public static final String USER_ID = "user_id";
	public static final String USER_ONLINE = "online";
	public static final String USER_TOKEN = "token";
	public static final String USER_TYPE = "type";
	public static final String USER_EMAIL = "email";
	public static final String USER_LOGIN = "login";
	public static final String USER_FULLNAME = "fullname";
	public static final String USER_PASSWORD = "password";
	public static final String USER_GENDER = "gender";
	public static final String USER_BIRTHDAY = "birthday";
	public static final String USER_PICTURE_URL = "picture_url";
	public static final String USER_HOMETOWN = "hometown";
	public static final String USER_HEALTH_TOPICS_ARRAY = "health_topics_array";
	public static final String USER_DIAGNOSED_WITH_ARRAY = "diagnosed_with_array";
	public static final String USER_DIAGNOSED_WITH_PRIVACY = "diagnosed_with_privacy";
	public static final String USER_MEDICATED_ARRAY = "medicated_array";
	public static final String USER_MEDICATED_PRIVACY = "medicated_privacy";
	public static final String USER_LIKE_COUNT = "like_count";
	public static final String USER_LIKE_ID = "lid";
	public static final String USER_ABOUT = "about";
	public static final String USER_REQEUST_TIME = "last_reqeust_time";

	/*
	 *  TABLE_TOPIC
	 */
	public static final String TOPIC_ID = "topic_id";
	public static final String TOPIC_USER_ID = "user_id";
	public static final String TOPIC_TITLE = "title";
	public static final String TOPIC_DESC = "desc";
	public static final String TOPIC_POST_COUNT = "post_count";
	public static final String TOPIC_TYPE = "type";
	public static final String TOPIC_LIKE_ID = "like_id";
	public static final String TOPIC_FLAG_ID = "flag_id";

	/*
	 * TABLE_POST
	 */
	public static final String POST_ID = "post_id";
	public static final String POST_USER_ID = "user_id";
	public static final String POST_USER_FULLNAME = "user_full_name";
	public static final String POST_USER_PICTURE_URL = "user_picture_url";
	public static final String POST_USER_GENDER = "user_gender";
	public static final String POST_TOPIC_ID = "topic_id";
	public static final String POST_TOPIC_TITLE = "topic_title";
	public static final String POST_MESSAGE = "message";
	public static final String POST_IMAGE_URL = "image_url";
	public static final String POST_COMMEMNT_COUNT = "comment_count";
	public static final String POST_LIKE_COUNT = "like_count";
	public static final String POST_FLAG_COUNT = "flag_count";
	public static final String POST_LIKE_ID = "like_id";
	public static final String POST_COMMENT_ID = "comment_id";
	public static final String POST_FLAG_ID = "flag_id";
	public static final String POST_SHARED = "shared";
	public static final String POST_SHARED_TYPE = "sharedtype";
	public static final String POST_CREATED_TIME = "created_time";

	/*
	 * TABLE_COMMENT
	 */
	public static final String COMMENT_ID = "comment_id";
	public static final String COMMENT_USER_ID = "user_id";
	public static final String COMMENT_USER_FULLNAME = "user_full_name";
	public static final String COMMENT_USER_PICTURE_URL = "user_picture_url";
	public static final String COMMENT_USER_GENDER = "user_gender";
	public static final String COMMENT_POST_ID = "post_id";
	public static final String COMMENT_MESSAGE = "message";
	public static final String COMMENT_LIKE_COUNT = "like_count";
	public static final String COMMENT_FLAG_COUNT = "flag_count";
	public static final String COMMENT_LIKE_ID = "like_id";
	public static final String COMMENT_FLAG_ID = "flag_id";
	public static final String COMMENT_CREATED_TIME = "created_time";

	/*
	 * TABLE_CHATMESSAGE
	 */
	public static final String MESSAGE_ID = "mid";
	public static final String MESSAGE_MY_ID = "my_id";
	public static final String MESSAGE_FRIEND_ID = "friend_id";
	public static final String MESSAGE_CONTENT = "message";
	public static final String MESSAGE_CREATED_TIME = "created_time";
	public static final String MESSAGE_NEW = "nNew";
	public static final String MESSAGE_INCOMING = "nIncoming";

	/*
	 * TABLE_POSTLIKE
	 */
	public static final String POSTLIKE_LIKE_ID = "lid";
	public static final String POSTLIKE_USER_ID = "user_id";
	public static final String POSTLIKE_POST_ID = "post_id";
	public static final String POSTLIKE_USER_FULLNAME = "user_full_name";
	public static final String POSTLIKE_USER_PICTURE_URL = "user_picture_url";
	public static final String POSTLIKE_USER_BIRTHDAY = "user_birthday";
	public static final String POSTLIKE_USER_GENDER = "user_gender";
}
