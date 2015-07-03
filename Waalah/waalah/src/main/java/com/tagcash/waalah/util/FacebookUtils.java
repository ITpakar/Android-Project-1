package com.tagcash.waalah.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.gson.Gson;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.http.SocialModel.FacebookModel;
import com.tagcash.waalah.view.WAProgressDialog;

public class FacebookUtils {
	
	private static String TAG = "FacebookUtils";
	
	public static final List<String> READ_PERMISSIONS = Arrays.asList(
			"public_profile",
			"user_about_me",
			"user_birthday",
			"user_photos",
			"user_hometown",
			"user_work_history",
			"user_likes",
			"user_location",
			"user_education_history");
	private static final List<String> PUBLISH_PERMISSIONS = Arrays.asList("publish_actions");
	
	
	// Value
	public static OnFacebookListener mOnFacebookListener = null;
	private static WAProgressDialog mProgDlg;
	
	private static Session session = null;
	private static Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	//////////////
	public static void init(final Activity activity) {
		if (Session.getActiveSession() == null) {
			session = new Session(WAApplication.getContext());
			Session.setActiveSession(session);
		}
		else
			session = Session.getActiveSession();
		
		mProgDlg = new WAProgressDialog(activity);
		mProgDlg.setCancelable(true);
		mProgDlg.hide();
	}
	
	public static void onResume(Activity activity) {
	}
	
	public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(activity, requestCode, resultCode, data);
		}
	}

	public static void setOnFacebookListener(OnFacebookListener listener) {
		mOnFacebookListener = listener;
	}

	public static boolean IsSessionOpend() {
		if (Session.getActiveSession() == null)
			return false;
		
		session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed())
			return false;
		
		return true;
	}
	
	public static void Login(Activity activity) {

		if (Session.getActiveSession() == null) {
			session = new Session(WAApplication.getContext());
			Session.setActiveSession(session);
		}
		else
			session = Session.getActiveSession();
		
		Session.getActiveSession().addCallback(callback);
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(activity).setPermissions(READ_PERMISSIONS).setCallback(callback).setRequestCode(100));
		} else {
			Session.openActiveSession(activity, true, callback);
		}
	}
	
	public static boolean requestPublishPermission(Activity activity) {
		Session session = Session.getActiveSession();
		if (session != null) {
			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PUBLISH_PERMISSIONS, permissions)) {
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(activity, PUBLISH_PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return true;
			}
		}
		return false;
	}
	
	public static void PostFeed(final Activity activity, String str_message, String str_name, String str_caption, String str_desc,
			String str_picture_url, String str_link, ArrayList<String> str_property_array) {

		Bundle postParams = new Bundle();
		postParams.putString("message",str_message);              
		postParams.putString("name",str_name);
		postParams.putString("link",str_link);
		postParams.putString("picture",str_picture_url);
		postParams.putString("description",str_desc);
		postParams.putString("access_token", session.getAccessToken());

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity, Session.getActiveSession(), postParams)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error) {
						if (error == null) {
							final String postId = values.getString("post_id");
							if (postId != null) {
								if (mOnFacebookListener != null)
									mOnFacebookListener.OnFacebookPostFeedSuccessListener(postId);
							} else {
								// User clicked the Cancel button
								MessageUtil.showMessage("Post cancelled", true);
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							MessageUtil.showMessage("Post Failed: " + error.toString(), true);
						} else {
							// Generic, ex: network error
							MessageUtil.showMessage("Post Failed", true);
						}
					}

				}).build();
		feedDialog.show();
	}
	
	private static boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private static void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if ((exception instanceof FacebookAuthorizationException)) {
			Log.d(TAG, "SessionStatusCallback Exception");
			exception.printStackTrace();
			if (mOnFacebookListener != null)
				mOnFacebookListener.OnFacebookLoginFailListener(exception.toString());
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			Log.d(TAG, "SessionStatusCallback OPENED_TOKEN_UPDATED");
			if (mOnFacebookListener != null)
				mOnFacebookListener.OnFacebookRequestPublishPermissionListener();
		} else if (state == SessionState.OPENED) {
			Log.d(TAG, "SessionStatusCallback OPENED");
			if (mOnFacebookListener != null)
				mOnFacebookListener.OnFacebookLoginSuccessListener();
		} else if (state == SessionState.CLOSED_LOGIN_FAILED) {
			Log.d(TAG, "SessionStatusCallback failed.");
			if (mOnFacebookListener != null)
				mOnFacebookListener.OnFacebookLoginFailListener("Login failed.");
		}
	}
	
	public static void getFBUserProfile() {
		new RequestAsyncTask(new Request(session, "me", null, null, new Callback() {
			
			@Override
			public void onCompleted(Response response) {
				boolean bSuccess = false;
				String josnStr = response.getRawResponse();
				if (!TextUtils.isEmpty(josnStr)) {
					Gson gson = new Gson();
					FacebookModel fbModel = gson.fromJson(josnStr, FacebookModel.class);
					if (fbModel != null) {
						if (mOnFacebookListener != null)
							mOnFacebookListener.OnFacebookGetProfileListener(fbModel);
						bSuccess = true;
					}
				}
				if (!bSuccess) {
					if (mOnFacebookListener != null)
						mOnFacebookListener.OnFacebookLoginFailListener("Cannot get profile information.");
				}
			}
		})).execute();
	}
	
	public static void clearFBCache() {
		if (Session.getActiveSession() != null) {
			Session session = Session.getActiveSession();
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
			}
			Session.getActiveSession().close();
			Session.setActiveSession(null);
		}
	}

	public static void createFBSession() {
		Session session = Session.getActiveSession();
		if (session == null) {
			session = new Session(WAApplication.getContext());
			Session.setActiveSession(session);
		}
	}
	
	public interface OnFacebookListener {
		public void OnFacebookLoginSuccessListener();
		public void OnFacebookLoginFailListener(String response);
		public void OnFacebookGetProfileListener(FacebookModel inModel);
		public void OnFacebookRequestPublishPermissionListener();
		public void OnFacebookPostFeedSuccessListener(String post_id);
	}
}
