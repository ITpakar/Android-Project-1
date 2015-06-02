package com.tagcash.waalah.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	public static Object DB_LOCK = new Object();
	private static DBOpenHelper mdbopenhelper = null;
	private static SQLiteDatabase mwritabledb = null;
	
	private final String DATABASE_FILENAME = "Waalah.sql";
	private final String DATABASE_UPGRADE_FILENAME = "Upgrade.sql";
	private AssetManager assetManager;

	public DBOpenHelper(Context context) {
		super(context, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
		assetManager = context.getAssets();
		mdbopenhelper = this;
	}

	public SQLiteDatabase openDatabase() {
		if ((mwritabledb == null) && (mdbopenhelper != null))
			mwritabledb = mdbopenhelper.getWritableDatabase();
		return mwritabledb;
	}

	public void closeDatabase() {
		if ((mwritabledb == null) && (mdbopenhelper != null))
			mwritabledb.close();
	}

	@Override
	public final void onCreate(SQLiteDatabase db) {
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = assetManager.open(DATABASE_FILENAME);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				db.execSQL(line);
			}
		} catch (IOException e) {
			Log.e("DBOpenHelper", e.getMessage());
		} finally {
			try {
				if (reader != null)
					reader.close();
				
				if (in != null)
					in.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public final void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w("---[DEBUG]----", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = assetManager.open(DATABASE_UPGRADE_FILENAME);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				db.execSQL(line);
			}
		} catch (IOException e) {
			Log.e("DatabaseHelper", e.getMessage());
		} finally {
			try {
				reader.close();
				in.close();
			} catch (IOException e) {
				// Log.e("DatabaseHelper", e.getMessage());
			}
		}
	}
	
	public synchronized int deleteAllData(String tablename) {
		SQLiteDatabase db = getWritableDatabase();
		int answer = db.delete(tablename, null, null);
		db.close();
		return answer;
	}
}