package com.tagcash.waalah.ui.activity;

import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.AppPreferences;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.TwitterUtils;
import com.tagcash.waalah.util.TwitterUtils.OnTwitterListener;

public class LoginWithTwitterActivity extends Activity {

	private int mFlag;
	private WAUser mUser;
	private AppPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_sns);
		
		prefs = new AppPreferences(this);

		Intent intent = getIntent();
		Uri uri = intent.getData();
		boolean bCallback = false;
		if (uri != null && uri.toString().startsWith(Constants.TWITTER.CALLBACK_URL)) {
			processTwitterAuthCallback(intent);
			bCallback = true;
		}

		if (!bCallback) {
			mFlag = intent.getExtras().getInt(Constants.KEY_FLAG);
	
			if (android.os.Build.VERSION.SDK_INT > 8) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
	
			TwitterUtils.setListener(new OnTwitterListener() {
				
				@Override
				public void OnTwitterAuthSuccessListener() {
					getUserInformationAndStart();
				}
				
				@Override
				public void OnTwitterAuthFailListener(String reason) {
					MessageUtil.showMessage(reason, true);
					LoginWithTwitterActivity.this.finish();
					
					WAApplication.clearCredentialsCache();
//					Intent intent = new Intent(LoginWithTwitterActivity.this, HomeActivity.class);
//					startActivity(intent);
//					overridePendingTransition(R.anim.in_right, R.anim.none);
				}
	
				@Override
				public void OnTwitterPostSuccessListener() {
					
				}
	
				@Override
				public void OnTwitterPostFailListener(String response) {
					
				}

				@Override
				public void OnTwitterAlreadyAuthenticatedListener(
						boolean bAlreadyAuth) {
					if (!bAlreadyAuth)
						TwitterUtils.twitterOAuth(LoginWithTwitterActivity.this, prefs);
					else
						getUserInformationAndStart();
				}
			});
			
			TwitterUtils.CheckAuthenticated(this, prefs);
		}
		else {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					LoginWithTwitterActivity.this.finish();
				}
			}, 100);
		}
	}

	@Override
	public void onNewIntent(Intent intent) {	// only for twitterOAuth
		super.onNewIntent(intent);
		
		processTwitterAuthCallback(intent);
	}
	
	private void processTwitterAuthCallback(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(Constants.TWITTER.CALLBACK_URL)) {
			final String verifier = uri.getQueryParameter(Constants.TWITTER.IEXTRA_OAUTH_VERIFIER);
			new AsyncTask<String,Void,AccessToken >() {
				@Override
				protected AccessToken doInBackground(String... params) {
					AccessToken accessToken = null;
					try {
						accessToken = TwitterUtils.twitter.getOAuthAccessToken(TwitterUtils.requestToken, verifier);
						return accessToken;

					} catch ( Exception e) {
						e.printStackTrace();
					}

					return null;
				}

				@Override
				protected void onPostExecute(AccessToken accessToken) {
					if (accessToken == null) {
						MessageUtil.showMessage("Twitter Access Token is null", true);
						LoginWithTwitterActivity.this.finish();
						return;
					}
					
					TwitterUtils.saveInformations(prefs, accessToken);
					TwitterUtils.sendAuthSuccessMessage();
				}
			}.execute(verifier);

		} else {
			MessageUtil.showMessage( "Twitter Authentication Error: Intent data null", true);
			LoginWithTwitterActivity.this.finish();
		}
	}

	private void getUserInformationAndStart() {
		try {
			User twitter_user = TwitterUtils.twitter.showUser(TwitterUtils.twitter_user_name);

			mUser = new WAUser();
			mUser.username = Constants.ID_PREVFIX.TWITTER + twitter_user.getId();
			//user.setEmail("twitter@not.alw");
			mUser.password = WAUser.DEFAULT_PASSWORD;
			mUser.picture_url = twitter_user.getOriginalProfileImageURL();
			mUser.address = twitter_user.getLocation();

			if (mFlag == Constants.MODE_REGISTER) {
				// register
				BaseTask task = new BaseTask(Constants.TASK_USER_REGISTER);
				task.setListener(mTaskListener);
				task.execute();
			} else {
				// login
				BaseTask loginTask = new BaseTask(Constants.TASK_USER_LOGIN);
				loginTask.setListener(mTaskListener);
				loginTask.execute();
			}
		} catch (TwitterException e) {
			e.printStackTrace();
			MessageUtil.showMessage("Twiiter account has problem", true);
		}
	}

	TaskListener mTaskListener = new TaskListener() {
		
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_REGISTER) {
//				result = Server.Register(mUser.email, mUser.login, mUser.fullname, mUser.password, 
//						mUser.gender, mUser.birthday, mUser.hometown, mUser.picture_url, mUser.about);
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				result = Server.Login(mUser.username, mUser.password);
			}
			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			boolean bFinish = true;
			if (taskId == Constants.TASK_USER_REGISTER) {
				if (result != null) {
					if (result instanceof ResponseModel.RegisterModel) {
						ResponseModel.RegisterModel res_model = (ResponseModel.RegisterModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							//MessageUtil.showMessage(res_model.msg, false);
							// if register success, using that information, start login
							BaseTask loginTask = new BaseTask(Constants.TASK_USER_LOGIN);
							loginTask.setListener(mTaskListener);
							loginTask.execute();
							bFinish = false;
						}
						else {
							// error
							if (res_model.status == Constants.HTTP_ACTION_ERRORCODE_NAMEOREMAIL_ALREADY_REGISTERED ||
								res_model.status == Constants.HTTP_ACTION_ERRORCODE_NAME_ALREADY_REGISTERED ||
								res_model.status == Constants.HTTP_ACTION_ERRORCODE_EMAIL_ALREADY_REGISTERED) {
								MessageUtil.showMessage(getString(R.string.user_already_registered), false);
							}
							else
								MessageUtil.showMessage(res_model.reason, false);
						}
					}
					else
						MessageUtil.showMessage(result.toString(), false);
				}
				else {
					
				}
				
				if (bFinish)
					finish();
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				if (result != null) {
					if (result instanceof ResponseModel.LoginResultModel) {
						ResponseModel.LoginResultModel res_model = (ResponseModel.LoginResultModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							//MessageUtil.showMessage("Login With Twitter Account Success.", false);
							
							// getUserInformation
//							mUser = new WAUser();
//							mUser.user_id = res_model.user.uid;
//							mUser.email = res_model.user.email;
//							mUser.login = res_model.user.name;
//							mUser.password = res_model.user.password;
//							mUser.hometown = res_model.user.address;
//							if (!TextUtils.isEmpty(res_model.user.picture_url))
//								mUser.picture_url = res_model.user.picture_url;
//							else {
//								if (!TextUtils.isEmpty(res_model.user.social_picture_url))
//									mUser.picture_url = res_model.user.social_picture_url;
//							}

							WAModelManager.getInstance().setSignInUser(mUser);

							if (MainActivity.instance != null)
								MainActivity.instance.finishAllActivity();

							// finish and go to MainActivity
							final Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
					        sendBroadcast(intent);
						}
						else {
							// error
							if (res_model.status == Constants.HTTP_ACTION_ERRORCODE_INVALID_EMAIL_OR_PASSWORD) {
								MessageUtil.showMessage("This account was not registered, Please register with this account in Register Page", true);
							}
							else
								MessageUtil.showMessage(res_model.reason, false);
						}
					}
					else
						MessageUtil.showMessage(result.toString(), false);
				}
				else {
					
				}
				finish();
			}
		}
		
		@Override
		public void onTaskProgress(int taskId, Object... values) {
		}
		
		@Override
		public void onTaskPrepare(int taskId, Object data) {
		}
		
		@Override
		public void onTaskCancelled(int taskId) {
		}
	};
}