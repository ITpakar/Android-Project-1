package com.tagcash.waalah.database;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.tagcash.waalah.model.WAPost;
import com.tagcash.waalah.model.WAPostlike;
import com.tagcash.waalah.model.WATopic;
import com.tagcash.waalah.model.WAUser;

public class DBManager {
	private static DBManager instance;

	private DBOpenHelper mdbhelper;
	private SQLiteDatabase mdb;
	private final String SELECT_DB_STR = "select * from ";
	private final String DELETE_DB_STR = "delete from ";

	public DBManager(Context context) {
		this.mdbhelper = new DBOpenHelper(context);
		mdb = mdbhelper.openDatabase();
		instance = this;
	}

	public static DBManager getInstance() {
		return instance; 
	}

	public void close() {
		if (mdbhelper != null)
			mdbhelper.closeDatabase();
	}
	
	/*
	 * User
	 */
	synchronized public void addOrUpdateOneUser(WAUser user) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ContentValues values = new ContentValues();
			values.put(DBConstant.USER_ID, user.user_id);
			values.put(DBConstant.USER_ONLINE, user.online);
			values.put(DBConstant.USER_TOKEN, user.token);
			values.put(DBConstant.USER_TYPE, user.type);
			values.put(DBConstant.USER_EMAIL, user.email);
			values.put(DBConstant.USER_LOGIN, user.login);
			values.put(DBConstant.USER_FULLNAME, user.fullname);
			values.put(DBConstant.USER_PASSWORD, user.password);
			values.put(DBConstant.USER_GENDER, user.gender);
			values.put(DBConstant.USER_BIRTHDAY, user.birthday);
			values.put(DBConstant.USER_PICTURE_URL, user.picture_url);
			values.put(DBConstant.USER_HOMETOWN, user.hometown);
			values.put(DBConstant.USER_HEALTH_TOPICS_ARRAY, user.health_topics_array);
			values.put(DBConstant.USER_DIAGNOSED_WITH_ARRAY, user.diagnosed_with_array);
			values.put(DBConstant.USER_DIAGNOSED_WITH_PRIVACY, user.diagnosed_with_privacy);
			values.put(DBConstant.USER_MEDICATED_ARRAY, user.medicated_array);
			values.put(DBConstant.USER_MEDICATED_PRIVACY, user.medicated_privacy);
			values.put(DBConstant.USER_LIKE_COUNT, user.like_count);
			values.put(DBConstant.USER_LIKE_ID, user.lid);
			values.put(DBConstant.USER_ABOUT, user.about);
			values.put(DBConstant.USER_REQEUST_TIME, user.last_reqeust_time.getTime());

			if (mdb != null) {
				String sql_duplication_check = SELECT_DB_STR + DBConstant.TABLE_USER
						+ " where " + DBConstant.USER_ID + "=" + user.user_id
						+ " and " + DBConstant.USER_TYPE + "=" + user.type;
				Cursor cursor = mdb.rawQuery(sql_duplication_check, null);
				if (cursor != null) {
					int count = cursor.getCount();
					if (count == 0) {
						// insert new record
						mdb.insert(DBConstant.TABLE_USER, null, values);

					} else {
						// update old record
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
						mdb.update(DBConstant.TABLE_USER, values, DBConstant.TBL_COLUMN_ID+"="+id, null);
					}
					cursor.close();
				}
			}		
		}
	}

	synchronized public void addAllUser(ArrayList<WAUser> list_data) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				mdb.beginTransaction();

				for (WAUser user : list_data) {
					ContentValues values = new ContentValues();
					values.put(DBConstant.USER_ID, user.user_id);
					values.put(DBConstant.USER_ONLINE, user.online);
					values.put(DBConstant.USER_TOKEN, user.token);
					values.put(DBConstant.USER_TYPE, user.type);
					values.put(DBConstant.USER_EMAIL, user.email);
					values.put(DBConstant.USER_LOGIN, user.login);
					values.put(DBConstant.USER_FULLNAME, user.fullname);
					values.put(DBConstant.USER_PASSWORD, user.password);
					values.put(DBConstant.USER_GENDER, user.gender);
					values.put(DBConstant.USER_BIRTHDAY, user.birthday);
					values.put(DBConstant.USER_PICTURE_URL, user.picture_url);
					values.put(DBConstant.USER_HOMETOWN, user.hometown);
					values.put(DBConstant.USER_HEALTH_TOPICS_ARRAY, user.health_topics_array);
					values.put(DBConstant.USER_DIAGNOSED_WITH_ARRAY, user.diagnosed_with_array);
					values.put(DBConstant.USER_DIAGNOSED_WITH_PRIVACY, user.diagnosed_with_privacy);
					values.put(DBConstant.USER_MEDICATED_ARRAY, user.medicated_array);
					values.put(DBConstant.USER_MEDICATED_PRIVACY, user.medicated_privacy);
					values.put(DBConstant.USER_LIKE_COUNT, user.like_count);
					values.put(DBConstant.USER_LIKE_ID, user.lid);
					values.put(DBConstant.USER_ABOUT, user.about);
					values.put(DBConstant.USER_REQEUST_TIME, user.last_reqeust_time.getTime());

					mdb.insert(DBConstant.TABLE_USER, null, values);
				}

				mdb.setTransactionSuccessful();
				mdb.endTransaction();
			}
		}
	}

	synchronized public ArrayList<WAUser> getAllUserByType(int type) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ArrayList<WAUser> list = new ArrayList<WAUser>();

			if (mdb != null) {
				String sql_str = SELECT_DB_STR + DBConstant.TABLE_USER
						+ " where " + DBConstant.USER_TYPE + " =" + type
						+ " ORDER BY id ASC";
//				if (type == DBConstant.TYPE_POPULAR)
//					sql_str += " ORDER BY " + DBConstant.TOPIC_POST_COUNT + " DESC";
//				else
//					sql_str += " ORDER BY " + DBConstant.USER_ID + " DESC";
				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						do {
							WAUser user = new WAUser();
							user.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
							user.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_ID));
							user.online = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_ONLINE));
							user.token = cursor.getString(cursor.getColumnIndex(DBConstant.USER_TOKEN));
							user.type = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_TYPE));
							user.email = cursor.getString(cursor.getColumnIndex(DBConstant.USER_EMAIL));
							user.login = cursor.getString(cursor.getColumnIndex(DBConstant.USER_LOGIN));
							user.fullname = cursor.getString(cursor.getColumnIndex(DBConstant.USER_FULLNAME));
							user.password = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PASSWORD));
							user.gender = cursor.getString(cursor.getColumnIndex(DBConstant.USER_GENDER));
							user.birthday = cursor.getString(cursor.getColumnIndex(DBConstant.USER_BIRTHDAY));
							user.picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PICTURE_URL));
							user.hometown = cursor.getString(cursor.getColumnIndex(DBConstant.USER_HOMETOWN));
							user.health_topics_array = cursor.getString(cursor.getColumnIndex(DBConstant.USER_HEALTH_TOPICS_ARRAY));
							user.diagnosed_with_array = cursor.getString(cursor.getColumnIndex(DBConstant.USER_DIAGNOSED_WITH_ARRAY));
							user.diagnosed_with_privacy = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_DIAGNOSED_WITH_PRIVACY));
							user.medicated_array = cursor.getString(cursor.getColumnIndex(DBConstant.USER_MEDICATED_ARRAY));
							user.medicated_privacy = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_MEDICATED_PRIVACY));
							user.like_count = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_LIKE_COUNT));
							user.lid = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_LIKE_ID));
							user.about = cursor.getString(cursor.getColumnIndex(DBConstant.USER_ABOUT));
							user.last_reqeust_time = new Date(cursor.getLong(cursor.getColumnIndex(DBConstant.USER_REQEUST_TIME)));

							list.add(user);
						} while (cursor.moveToNext());
					}
					cursor.close();
				}
				cursor.close();
			}
			return list;
		}
	}

	synchronized public WAUser getOneUser(int user_id, int type) {
		synchronized (DBOpenHelper.DB_LOCK) {
			
			WAUser user = null;
			if (mdb != null) {
				String sql_duplication_check = SELECT_DB_STR + DBConstant.TABLE_USER
						+ " where " + DBConstant.USER_ID + "=" + user_id
						+ " and " + DBConstant.USER_TYPE + "=" + type;
				Cursor cursor = mdb.rawQuery(sql_duplication_check, null);
				if (cursor != null) {
					int count = cursor.getCount();
					if (count == 0) {
						return null;
					} else {
						cursor.moveToFirst();
						user = new WAUser();

						user.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
						user.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_ID));
						user.online = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_ONLINE));
						user.token = cursor.getString(cursor.getColumnIndex(DBConstant.USER_TOKEN));
						user.type = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_TYPE));
						user.email = cursor.getString(cursor.getColumnIndex(DBConstant.USER_EMAIL));
						user.login = cursor.getString(cursor.getColumnIndex(DBConstant.USER_LOGIN));
						user.fullname = cursor.getString(cursor.getColumnIndex(DBConstant.USER_FULLNAME));
						user.password = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PASSWORD));
						user.gender = cursor.getString(cursor.getColumnIndex(DBConstant.USER_GENDER));
						user.birthday = cursor.getString(cursor.getColumnIndex(DBConstant.USER_BIRTHDAY));
						user.picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PICTURE_URL));
						user.hometown = cursor.getString(cursor.getColumnIndex(DBConstant.USER_HOMETOWN));
						user.health_topics_array = cursor.getString(cursor.getColumnIndex(DBConstant.USER_HEALTH_TOPICS_ARRAY));
						user.diagnosed_with_array = cursor.getString(cursor.getColumnIndex(DBConstant.USER_DIAGNOSED_WITH_ARRAY));
						user.diagnosed_with_privacy = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_DIAGNOSED_WITH_PRIVACY));
						user.medicated_array = cursor.getString(cursor.getColumnIndex(DBConstant.USER_MEDICATED_ARRAY));
						user.medicated_privacy = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_MEDICATED_PRIVACY));
						user.like_count = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_LIKE_COUNT));
						user.lid = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_LIKE_ID));
						user.about = cursor.getString(cursor.getColumnIndex(DBConstant.USER_ABOUT));
						user.last_reqeust_time = new Date(cursor.getLong(cursor.getColumnIndex(DBConstant.USER_REQEUST_TIME)));
					}
					cursor.close();
				}
			}
			return user;
		}
	}

	synchronized public void deleteAllUserByType(int type) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_USER
						+ " where " + DBConstant.USER_TYPE + "=" + type;
				mdb.execSQL(sql_str);
			}
		}
	}

	synchronized public void deleteAllUser() {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_USER
				+ " where " + DBConstant.TBL_COLUMN_ID + "> 0";
				mdb.execSQL(sql_str);
			}
		}
	}

	/*
	 * Topic
	 */
	synchronized public void addOrUpdateOneTopic(WATopic topic) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ContentValues values = new ContentValues();
			values.put(DBConstant.TOPIC_ID, topic.topic_id);
			values.put(DBConstant.TOPIC_USER_ID, topic.user_id);
			values.put(DBConstant.TOPIC_TITLE, topic.title);
			values.put(DBConstant.TOPIC_DESC, topic.desc);
			values.put(DBConstant.TOPIC_POST_COUNT, topic.post_count);
			values.put(DBConstant.TOPIC_TYPE, topic.type);
			values.put(DBConstant.TOPIC_LIKE_ID, topic.like_id);
			values.put(DBConstant.TOPIC_FLAG_ID, topic.flag_id);

			if (mdb != null) {
				String sql_duplication_check = SELECT_DB_STR + DBConstant.TABLE_TOPIC
						+ " where " + DBConstant.TOPIC_ID + "=" + topic.topic_id
						+ " and " + DBConstant.TOPIC_TYPE + "=" + topic.type;
				Cursor cursor = mdb.rawQuery(sql_duplication_check, null);
				if (cursor != null) {
					int count = cursor.getCount();
					if (count == 0) {
						// insert new record
						mdb.insert(DBConstant.TABLE_TOPIC, null, values);

					} else {
						// update old record
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
						mdb.update(DBConstant.TABLE_TOPIC, values, DBConstant.TBL_COLUMN_ID+"="+id, null);
					}
					cursor.close();
				}
			}		
		}
	}

	synchronized public void addAllTopic(ArrayList<WATopic> list_data, int type) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				mdb.beginTransaction();

				for (WATopic topic : list_data) {
					ContentValues values = new ContentValues();
					values.put(DBConstant.TOPIC_ID, topic.topic_id);
					values.put(DBConstant.TOPIC_USER_ID, topic.user_id);
					values.put(DBConstant.TOPIC_TITLE, topic.title);
					values.put(DBConstant.TOPIC_DESC, topic.desc);
					values.put(DBConstant.TOPIC_POST_COUNT, topic.post_count);
					values.put(DBConstant.TOPIC_TYPE, topic.type);
					values.put(DBConstant.TOPIC_LIKE_ID, topic.like_id);
					values.put(DBConstant.TOPIC_FLAG_ID, topic.flag_id);

					mdb.insert(DBConstant.TABLE_TOPIC, null, values);
				}

				mdb.setTransactionSuccessful();
				mdb.endTransaction();
			}
		}
	}

	synchronized public ArrayList<WATopic> getAllTopicByType(int type) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ArrayList<WATopic> list = new ArrayList<WATopic>();

			if (mdb != null) {
				String sql_str = SELECT_DB_STR + DBConstant.TABLE_TOPIC
						+ " where " + DBConstant.TOPIC_TYPE + " =" + type;
				if (type == DBConstant.TYPE_POPULAR)
					sql_str += " ORDER BY " + DBConstant.TOPIC_POST_COUNT + " DESC";
				else
					sql_str += " ORDER BY " + DBConstant.TOPIC_ID + " DESC";
				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						do {
							WATopic topic = new WATopic();
							topic.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
							topic.topic_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_ID));
							topic.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_USER_ID));
							topic.title = cursor.getString(cursor.getColumnIndex(DBConstant.TOPIC_TITLE));
							topic.desc = cursor.getString(cursor.getColumnIndex(DBConstant.TOPIC_DESC));
							topic.post_count = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_POST_COUNT));
							topic.type = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_TYPE));
							topic.like_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_LIKE_ID));
							topic.flag_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_FLAG_ID));

							list.add(topic);
						} while (cursor.moveToNext());
					}
					cursor.close();
				}
				cursor.close();
			}
			return list;
		}
	}

	synchronized public WATopic getOneTopic(int in_topic_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			WATopic topic = null;

			if (mdb != null) {
				String sql_str = SELECT_DB_STR + DBConstant.TABLE_TOPIC
						+ " where " + DBConstant.TOPIC_ID + "=" + in_topic_id
						+ " ORDER BY id ASC";
				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					int count = cursor.getCount();
					if (count == 0) {
						return null;
					} else {
						cursor.moveToFirst();
						topic = new WATopic();

						topic.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
						topic.topic_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_ID));
						topic.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_USER_ID));
						topic.title = cursor.getString(cursor.getColumnIndex(DBConstant.TOPIC_TITLE));
						topic.desc = cursor.getString(cursor.getColumnIndex(DBConstant.TOPIC_DESC));
						topic.post_count = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_POST_COUNT));
						topic.type = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_TYPE));
						topic.like_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_LIKE_ID));
						topic.flag_id = cursor.getInt(cursor.getColumnIndex(DBConstant.TOPIC_FLAG_ID));
					}
					cursor.close();
				}
			}

			return topic;
		}
	}

	synchronized public void deleteAllTopicByType(int type) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_TOPIC
						+ " where " + DBConstant.TOPIC_TYPE + "=" + type;
				mdb.execSQL(sql_str);
			}
		}
	}

	synchronized public void deleteAllTopic() {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_TOPIC
						+ " where " + DBConstant.TBL_COLUMN_ID + "> 0";
				mdb.execSQL(sql_str);
			}
		}
	}

	/*
	 * Post
	 */
	synchronized public void addOrUpdateOnePost(WAPost post) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ContentValues values = new ContentValues();
			values.put(DBConstant.POST_ID, post.post_id);
			values.put(DBConstant.POST_USER_ID, post.user_id);
			values.put(DBConstant.POST_USER_FULLNAME, post.user_full_name);
			values.put(DBConstant.POST_USER_PICTURE_URL, post.user_picture_url);
			values.put(DBConstant.POST_USER_GENDER, post.user_gender);
			values.put(DBConstant.POST_TOPIC_ID, post.topic_id);
			values.put(DBConstant.POST_TOPIC_TITLE, post.topic_title);
			values.put(DBConstant.POST_MESSAGE, post.message);
			values.put(DBConstant.POST_IMAGE_URL, post.image_url);
			values.put(DBConstant.POST_COMMEMNT_COUNT, post.comment_count);
			values.put(DBConstant.POST_LIKE_COUNT, post.like_count);
			values.put(DBConstant.POST_FLAG_COUNT, post.flag_count);
			values.put(DBConstant.POST_LIKE_ID, post.like_id);
			values.put(DBConstant.POST_COMMENT_ID, post.comment_id);
			values.put(DBConstant.POST_FLAG_ID, post.flag_id);
			values.put(DBConstant.POST_SHARED, post.shared);
			values.put(DBConstant.POST_SHARED_TYPE, post.sharedtype);
			values.put(DBConstant.POST_CREATED_TIME, post.created_time.getTime());

			if (mdb != null) {
				String sql_duplication_check = SELECT_DB_STR + DBConstant.TABLE_POST
						+ " where " + DBConstant.POST_ID + "=" + post.post_id;
				Cursor cursor = mdb.rawQuery(sql_duplication_check, null);
				if (cursor != null) {
					int count = cursor.getCount();
					if (count == 0) {
						// insert new record
						mdb.insert(DBConstant.TABLE_POST, null, values);

					} else {
						// update old record
						cursor.moveToFirst();
						int id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
						mdb.update(DBConstant.TABLE_POST, values, DBConstant.TBL_COLUMN_ID+"="+id, null);
					}
					cursor.close();
				}
			}		
		}
	}
	
	synchronized public void addAllPost(ArrayList<WAPost> post_list) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				mdb.beginTransaction();

				for (WAPost post : post_list) {
					ContentValues values = new ContentValues();
					values.put(DBConstant.POST_ID, post.post_id);
					values.put(DBConstant.POST_USER_ID, post.user_id);
					values.put(DBConstant.POST_USER_FULLNAME, post.user_full_name);
					values.put(DBConstant.POST_USER_PICTURE_URL, post.user_picture_url);
					values.put(DBConstant.POST_USER_GENDER, post.user_gender);
					values.put(DBConstant.POST_TOPIC_ID, post.topic_id);
					values.put(DBConstant.POST_TOPIC_TITLE, post.topic_title);
					values.put(DBConstant.POST_MESSAGE, post.message);
					values.put(DBConstant.POST_IMAGE_URL, post.image_url);
					values.put(DBConstant.POST_COMMEMNT_COUNT, post.comment_count);
					values.put(DBConstant.POST_LIKE_COUNT, post.like_count);
					values.put(DBConstant.POST_FLAG_COUNT, post.flag_count);
					values.put(DBConstant.POST_LIKE_ID, post.like_id);
					values.put(DBConstant.POST_COMMENT_ID, post.comment_id);
					values.put(DBConstant.POST_FLAG_ID, post.flag_id);
					values.put(DBConstant.POST_SHARED, post.shared);
					values.put(DBConstant.POST_SHARED_TYPE, post.sharedtype);
					values.put(DBConstant.POST_CREATED_TIME, post.created_time.getTime());

					mdb.insert(DBConstant.TABLE_POST, null, values);
				}

				mdb.setTransactionSuccessful();
				mdb.endTransaction();
			}
		}
	}

	synchronized public ArrayList<WAPost> getAllPost() {
		synchronized (DBOpenHelper.DB_LOCK) {
			ArrayList<WAPost> list = new ArrayList<WAPost>();

			if (mdb != null) {
				String sql_str = SELECT_DB_STR + DBConstant.TABLE_POST
						+ " ORDER BY " + DBConstant.POST_ID + " DESC";
				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						do {
							WAPost post = new WAPost();
							post.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
							post.post_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_ID));
							post.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_USER_ID));
							post.user_full_name = cursor.getString(cursor.getColumnIndex(DBConstant.POST_USER_FULLNAME));
							post.user_picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.POST_USER_PICTURE_URL));
							post.user_gender = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_USER_GENDER));
							post.topic_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_TOPIC_ID));
							post.topic_title = cursor.getString(cursor.getColumnIndex(DBConstant.POST_TOPIC_TITLE));
							post.message = cursor.getString(cursor.getColumnIndex(DBConstant.POST_MESSAGE));
							post.image_url = cursor.getString(cursor.getColumnIndex(DBConstant.POST_IMAGE_URL));
							post.comment_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_COMMEMNT_COUNT));
							post.like_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_LIKE_COUNT));
							post.flag_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_FLAG_COUNT));
							post.like_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_LIKE_ID));
							post.comment_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_COMMENT_ID));
							post.flag_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_FLAG_ID));
							post.shared = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_SHARED));
							post.sharedtype = cursor.getString(cursor.getColumnIndex(DBConstant.POST_SHARED_TYPE));
							post.created_time = new Date(cursor.getLong(cursor.getColumnIndex(DBConstant.POST_CREATED_TIME)));

							list.add(post);
						} while (cursor.moveToNext());
					}
					cursor.close();
				}
			}
			return list;
		}
	}

	synchronized public ArrayList<WAPost> getAllPostByTopicId(int in_topic_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ArrayList<WAPost> list = new ArrayList<WAPost>();

			if (mdb != null) {
				String sql_str = SELECT_DB_STR + DBConstant.TABLE_POST
						+ " where " + DBConstant.POST_TOPIC_ID + "=" + in_topic_id
						+ " ORDER BY " + DBConstant.POST_ID + " DESC";
				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						do {
							WAPost post = new WAPost();
							post.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
							post.post_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_ID));
							post.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_USER_ID));
							post.user_full_name = cursor.getString(cursor.getColumnIndex(DBConstant.POST_USER_FULLNAME));
							post.user_picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.POST_USER_PICTURE_URL));
							post.user_gender = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_USER_GENDER));
							post.topic_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_TOPIC_ID));
							post.topic_title = cursor.getString(cursor.getColumnIndex(DBConstant.POST_TOPIC_TITLE));
							post.message = cursor.getString(cursor.getColumnIndex(DBConstant.POST_MESSAGE));
							post.image_url = cursor.getString(cursor.getColumnIndex(DBConstant.POST_IMAGE_URL));
							post.comment_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_COMMEMNT_COUNT));
							post.like_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_LIKE_COUNT));
							post.flag_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_FLAG_COUNT));
							post.like_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_LIKE_ID));
							post.comment_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_COMMENT_ID));
							post.flag_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_FLAG_ID));
							post.shared = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_SHARED));
							post.sharedtype = cursor.getString(cursor.getColumnIndex(DBConstant.POST_SHARED_TYPE));
							post.created_time = new Date(cursor.getLong(cursor.getColumnIndex(DBConstant.POST_CREATED_TIME)));

							list.add(post);
						} while (cursor.moveToNext());
					}
					cursor.close();
				}
			}

			return list;
		}
	}

	synchronized public WAPost getOnePost(int in_post_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			WAPost post = null;

			if (mdb != null) {
				String sql_duplication_check = SELECT_DB_STR + DBConstant.TABLE_POST
						+ " where " + DBConstant.POST_ID + "=" + in_post_id;
				Cursor cursor = mdb.rawQuery(sql_duplication_check, null);
				if (cursor != null) {
					int count = cursor.getCount();
					if (count == 0) {
						return null;
					} else {
						cursor.moveToFirst();
						post = new WAPost();
						post.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
						post.post_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_ID));
						post.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_USER_ID));
						post.user_full_name = cursor.getString(cursor.getColumnIndex(DBConstant.POST_USER_FULLNAME));
						post.user_picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.POST_USER_PICTURE_URL));
						post.user_gender = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_USER_GENDER));
						post.topic_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_TOPIC_ID));
						post.topic_title = cursor.getString(cursor.getColumnIndex(DBConstant.POST_TOPIC_TITLE));
						post.message = cursor.getString(cursor.getColumnIndex(DBConstant.POST_MESSAGE));
						post.image_url = cursor.getString(cursor.getColumnIndex(DBConstant.POST_IMAGE_URL));
						post.comment_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_COMMEMNT_COUNT));
						post.like_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_LIKE_COUNT));
						post.flag_count = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_FLAG_COUNT));
						post.like_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_LIKE_ID));
						post.comment_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_COMMENT_ID));
						post.flag_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_FLAG_ID));
						post.shared = cursor.getInt(cursor.getColumnIndex(DBConstant.POST_SHARED));
						post.sharedtype = cursor.getString(cursor.getColumnIndex(DBConstant.POST_SHARED_TYPE));
						post.created_time = new Date(cursor.getLong(cursor.getColumnIndex(DBConstant.POST_CREATED_TIME)));
					}
					cursor.close();
				}
			}
			return post;
		}
	}

	synchronized public void deleteAllPostByTopicId(int topic_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_POST
						+ " where " + DBConstant.POST_TOPIC_ID + "=" + topic_id;
				mdb.execSQL(sql_str);
			}
		}
	}

	synchronized public void deleteAllPost() {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_POST
						+ " where " + DBConstant.TBL_COLUMN_ID + "> 0";
				mdb.execSQL(sql_str);
			}
		}
	}

	synchronized public void deleteOnePost(int post_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_POST
						+ " where " + DBConstant.POST_ID + "=" + post_id;
				mdb.execSQL(sql_str);
			}
		}
	}

	/*
	 * Postlike
	 */
	
	synchronized public void addAllPostlike(ArrayList<WAPostlike> postlike_list) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				mdb.beginTransaction();

				for (WAPostlike postlike : postlike_list) {
					ContentValues values = new ContentValues();
					values.put(DBConstant.POSTLIKE_LIKE_ID, postlike.lid);
					values.put(DBConstant.POSTLIKE_POST_ID, postlike.post_id);
					values.put(DBConstant.POSTLIKE_USER_ID, postlike.user_id);
					values.put(DBConstant.POSTLIKE_USER_FULLNAME, postlike.user_full_name);
					values.put(DBConstant.POSTLIKE_USER_PICTURE_URL, postlike.user_picture_url);
					values.put(DBConstant.POSTLIKE_USER_BIRTHDAY, postlike.user_birthday);
					values.put(DBConstant.POSTLIKE_USER_GENDER, postlike.user_gender);

					mdb.insert(DBConstant.TABLE_POSTLIKE, null, values);
				}

				mdb.setTransactionSuccessful();
				mdb.endTransaction();
			}
		}
	}

	synchronized public ArrayList<WAPostlike> getAllPostlikeByPostId(int in_post_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			ArrayList<WAPostlike> list = new ArrayList<WAPostlike>();

			if (mdb != null) {
				String sql_str = SELECT_DB_STR + DBConstant.TABLE_POSTLIKE
						+ " where " + DBConstant.POSTLIKE_POST_ID + "=" + in_post_id
						+ " ORDER BY " + DBConstant.POSTLIKE_LIKE_ID + " DESC";
				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						do {
							WAPostlike postlike = new WAPostlike();
							postlike.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
							postlike.lid = cursor.getInt(cursor.getColumnIndex(DBConstant.POSTLIKE_LIKE_ID));
							postlike.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POSTLIKE_USER_ID));
							postlike.post_id = cursor.getInt(cursor.getColumnIndex(DBConstant.POSTLIKE_POST_ID));
							postlike.user_full_name = cursor.getString(cursor.getColumnIndex(DBConstant.POSTLIKE_USER_FULLNAME));
							postlike.user_picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.POSTLIKE_USER_PICTURE_URL));
							postlike.user_birthday = cursor.getString(cursor.getColumnIndex(DBConstant.POSTLIKE_USER_BIRTHDAY));
							postlike.user_gender = cursor.getInt(cursor.getColumnIndex(DBConstant.POSTLIKE_USER_GENDER));

							list.add(postlike);
						} while (cursor.moveToNext());
					}
					cursor.close();
				}
			}

			return list;
		}
	}

	synchronized public void deleteAllPostlikeByPostId(int post_id) {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_POSTLIKE
						+ " where " + DBConstant.POSTLIKE_POST_ID + "=" + post_id;
				mdb.execSQL(sql_str);
			}
		}
	}

	synchronized public void deleteAllPostlike() {
		synchronized (DBOpenHelper.DB_LOCK) {
			if (mdb != null) {
				String sql_str = DELETE_DB_STR + DBConstant.TABLE_POSTLIKE
						+ " where " + DBConstant.TBL_COLUMN_ID + "> 0";
				mdb.execSQL(sql_str);
			}
		}
	}

}
