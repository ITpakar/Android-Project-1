package com.tagcash.waalah.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.tagcash.waalah.app.AppPreferences;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.view.WAProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TwitterUtils {

	public static Twitter twitter = null;
	public static RequestToken requestToken;
	public static String twitter_user_name = "";
	public static OnTwitterListener mListener = null;
	private static WAProgressDialog mProgDlg;

	public static void setListener(OnTwitterListener inLinstener) {
		mListener = inLinstener;
	}
	
	public static void sendAuthSuccessMessage() {
		if (mListener != null)
			mListener.OnTwitterAuthSuccessListener();
	}
	
	public static void saveInformations(AppPreferences pref, AccessToken accessToken) {
		twitter_user_name = accessToken.getScreenName();
		String twitterToken = accessToken.getToken();
		String twitterSecret = accessToken.getTokenSecret();
		
		pref.setTwitterToken(twitterToken);
		pref.setTwitterSecret(twitterSecret);
		pref.setTwitterUserName(twitter_user_name);
	}
	
	public static void initInformations(AppPreferences pref) {
		pref.setTwitterToken("");
		pref.setTwitterSecret("");
		pref.setTwitterUserName("");
	}
	
	public static void CheckAuthenticated(final Context inContext, final AppPreferences pref) {
		mProgDlg = new WAProgressDialog(inContext);
		mProgDlg.setCancelable(true);
		mProgDlg.show();

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... param) {
				String twitterToken = pref.getTwitterToken();
				String twitterSecret = pref.getTwitterSecret();
				twitter_user_name = pref.getTwitterUserName();
				boolean bAlreadyAuth = false;
				
				if (TextUtils.isEmpty(twitterToken) || TextUtils.isEmpty(twitterSecret)) {
					return bAlreadyAuth;
				}
				
				AccessToken a = new AccessToken(twitterToken, twitterSecret);
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(Constants.TWITTER.CONSUMER_KEY, Constants.TWITTER.CONSUMER_SECRET);
				twitter.setOAuthAccessToken(a);
		
				try {
					twitter.getAccountSettings();
					bAlreadyAuth = true;
				} catch (TwitterException e) {
					pref.setTwitterToken("");
					pref.setTwitterSecret("");
				}
				
				return bAlreadyAuth;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (mProgDlg != null)
					mProgDlg.hide();
				
				if (mListener != null)
					mListener.OnTwitterAlreadyAuthenticatedListener(result);
			}
		}.execute();
	}

	public static void twitterOAuth(final Context inContext, AppPreferences pref) {
		if (mProgDlg != null)
			mProgDlg.show();
		
		pref.setTwitterToken("");
		pref.setTwitterSecret("");
		pref.setTwitterUserName("");

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... param) {
				try {
					ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(Constants.TWITTER.CONSUMER_KEY);
					builder.setOAuthConsumerSecret(Constants.TWITTER.CONSUMER_SECRET);
					Configuration configuration = builder.build();

					TwitterFactory factory = new TwitterFactory(configuration);
					twitter = factory.getInstance();

					requestToken = twitter.getOAuthRequestToken(Constants.TWITTER.CALLBACK_URL);
					return requestToken.getAuthenticationURL();

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String url) {
				if (mProgDlg != null)
					mProgDlg.hide();
				
				if (url == null) {
					if (mListener != null)
						mListener.OnTwitterAuthFailListener("Twitter RequestToken is Null");
					return;
				}
				else {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
					inContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				}
			}
		}.execute();
	}

	public static void sendTweet(AppPreferences pref, final String msg, final String pict_url) {
		if (mProgDlg != null)
			mProgDlg.show();
		
		final String twitterToken = pref.getTwitterToken();
		final String twitterSecret = pref.getTwitterSecret();
		final Twitter twitter = new TwitterFactory().getInstance();

		Log.d("twitter token : ", twitterToken);
		Log.d("twitter secret : ", twitterSecret);
		Log.d("twitter msg : ", msg);
		Log.d("twitter pict url : ", pict_url);
		
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... param) {
				String result = "OK";
				try {
					AccessToken a = new AccessToken(twitterToken, twitterSecret);
					Log.d("acc_token :", "" + a);
					twitter.setOAuthConsumer(Constants.TWITTER.CONSUMER_KEY, Constants.TWITTER.CONSUMER_SECRET);
					twitter.setOAuthAccessToken(a);
					if (!TextUtils.isEmpty(pict_url)) {
						String image_dir = Environment.getExternalStorageDirectory() + Constants.SHARE_UPLOAD_FOLDERNAME;
						String image_file = Constants.SHARE_UPLOAD_FILENAME;
						if (makeUploadFile(pict_url, image_dir, image_file)) {
							File uploadFile = new File(image_dir + image_file);
							twitter.uploadMedia(uploadFile);
				            StatusUpdate update = new StatusUpdate(msg);
				            update.setMedia(uploadFile);
				            twitter4j.Status status = twitter.updateStatus(update);
				            System.out.println("Successfully updated the status to [" + status.getText() + "][" + status.getId() + "].");
						}
						else {
							result = "Error : " + "Cannot make upload picture.";
							return result;
						}
					}
					else {
						twitter.updateStatus(""+msg);
					}
				} catch (Exception e) {
					Log.d("error dj-->", e.getMessage().toString());
					result = "Error : " + e.getMessage().toString();
				}
				
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				if (mProgDlg != null)
					mProgDlg.hide();
				
				if (mListener != null) {
					if (result.contains("OK")) {
						mListener.OnTwitterPostSuccessListener();
					}
					else {
						mListener.OnTwitterPostFailListener(result);
					}
				}
			}
		}.execute();
	}
	
	public static boolean makeUploadFile(String srcFileName, String dstDirPath, String dstFileName) {
		boolean bMake = false;
		if (!TextUtils.isEmpty(srcFileName)) {
			Bitmap bmp = ImageLoader.getInstance().loadImageSync(srcFileName);
			if (bmp != null) {
				File tempDir = new File(dstDirPath);
				tempDir.mkdirs();
				File file = new File(dstDirPath + dstFileName);
				
				if (file.exists()) {
					try {
						file.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				try {
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					bmp.compress(CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();
					bMake = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bMake;
	}
	
	public interface OnTwitterListener {
		public void OnTwitterAlreadyAuthenticatedListener(boolean bAlreadyAuth);
		public void OnTwitterAuthSuccessListener();
		public void OnTwitterAuthFailListener(String reason);
		public void OnTwitterPostSuccessListener();
		public void OnTwitterPostFailListener(String response);
	}
}
