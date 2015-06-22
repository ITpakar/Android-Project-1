package com.tagcash.waalah.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.http.SocialModel.FacebookModel;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.FacebookUtils;
import com.tagcash.waalah.util.WAPreferenceManager;
import com.tagcash.waalah.util.FacebookUtils.OnFacebookListener;
import com.tagcash.waalah.util.MessageUtil;

public class LoginWithFacebookActivity extends Activity implements OnFacebookListener {

	private int mFlag;

	private WAUser mUser;
	private ProgressBar progressbar;
	
	private String mStrUser = "";
	private String mStrPwd = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_sns);

		mFlag = getIntent().getExtras().getInt(Constants.KEY_FLAG);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		progressbar.setVisibility(View.INVISIBLE);

		boolean bDirectLogin = false;
		if ((mFlag == Constants.MODE_LOGIN) && FacebookUtils.IsSessionOpend()) {
			mStrUser = WAPreferenceManager.getString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, "");
			mStrPwd = WAPreferenceManager.getString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, "");
			if (!TextUtils.isEmpty(mStrUser) && !TextUtils.isEmpty(mStrPwd))
				bDirectLogin = true;
		}
		
		if (bDirectLogin) {
			progressbar.setVisibility(View.VISIBLE);
			// login
			BaseTask loginTask = new BaseTask(Constants.TASK_USER_LOGIN);
			loginTask.setListener(mTaskListener);
			loginTask.execute();
		}
		else {
			FacebookUtils.clearFBCache();
			FacebookUtils.init(this);
			FacebookUtils.setOnFacebookListener(this);
			FacebookUtils.Login(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		FacebookUtils.onResume(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		FacebookUtils.onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void getUserInformation(FacebookModel inModel){
		mUser = new WAUser();

		mUser.username = Constants.ID_PREVFIX.FACEBOOK + inModel.id;
		mUser.password = WAUser.DEFAULT_PASSWORD;
		mUser.picture_url = "https://graph.facebook.com/"+inModel.id+"/picture?type=large";
		if (inModel.location != null) {
			mUser.address = inModel.location.name;
		}
	}

	TaskListener mTaskListener = new TaskListener() {
		
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_REGISTER) {
				result = Server.Register(mUser.email, mUser.username, mUser.password, mUser.picture_url);
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
//				result = Server.Login(mUser.login, mUser.password);
				result = Server.Login(mStrUser, mStrPwd);
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
							mStrUser = mUser.username;
							mStrPwd = mUser.password;
							
							progressbar.setVisibility(View.VISIBLE);
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
							//MessageUtil.showMessage("Login With Facebook Account Success.", false);
							
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

	@Override
	public void OnFacebookLoginSuccessListener() {
		FacebookUtils.getFBUserProfile();
	}

	@Override
	public void OnFacebookLoginFailListener(String response) {
		Toast.makeText(this, response, Toast.LENGTH_LONG).show();
		finish();
		
		WAApplication.clearCredentialsCache();
		Intent intent = new Intent(this, LoginWithEmailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_right, R.anim.none);
	}

	@Override
	public void OnFacebookGetProfileListener(FacebookModel inModel) {
		getUserInformation(inModel);
		progressbar.setVisibility(View.VISIBLE);
		
		mStrUser = mUser.username;
		mStrPwd = mUser.password;

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
	}

	@Override
	public void OnFacebookRequestPublishPermissionListener() {
	}

	@Override
	public void OnFacebookPostFeedSuccessListener(String post_id) {
	}
}