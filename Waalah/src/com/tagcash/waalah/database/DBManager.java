package com.tagcash.waalah.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
			values.put(DBConstant.USER_EMAIL, user.email);
			values.put(DBConstant.USER_LOGIN, user.login);
			values.put(DBConstant.USER_PASSWORD, user.password);
			values.put(DBConstant.USER_PICTURE_URL, user.picture_url);

			if (mdb != null) {
				String sql_duplication_check = SELECT_DB_STR + DBConstant.TABLE_USER
						+ " where " + DBConstant.USER_ID + "=" + user.user_id;
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
					values.put(DBConstant.USER_EMAIL, user.email);
					values.put(DBConstant.USER_LOGIN, user.login);
					values.put(DBConstant.USER_PASSWORD, user.password);
					values.put(DBConstant.USER_PICTURE_URL, user.picture_url);

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

				Cursor cursor = mdb.rawQuery(sql_str, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						do {
							WAUser user = new WAUser();
							user.id = cursor.getInt(cursor.getColumnIndex(DBConstant.TBL_COLUMN_ID));
							user.user_id = cursor.getInt(cursor.getColumnIndex(DBConstant.USER_ID));
							user.email = cursor.getString(cursor.getColumnIndex(DBConstant.USER_EMAIL));
							user.login = cursor.getString(cursor.getColumnIndex(DBConstant.USER_LOGIN));
							user.password = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PASSWORD));
							user.picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PICTURE_URL));

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
						user.email = cursor.getString(cursor.getColumnIndex(DBConstant.USER_EMAIL));
						user.login = cursor.getString(cursor.getColumnIndex(DBConstant.USER_LOGIN));
						user.password = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PASSWORD));
						user.picture_url = cursor.getString(cursor.getColumnIndex(DBConstant.USER_PICTURE_URL));
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
}
