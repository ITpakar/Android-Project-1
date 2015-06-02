package com.tagcash.waalah.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.MD5Util;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.Validation;
import com.tagcash.waalah.util.WAFontProvider;

public class SignUpActivity extends BaseActivity {

	public static SignUpActivity instance = null;
	
	private EditText edt_email;
	private EditText edt_password;
	private EditText edt_username;
	private Button btn_signup;
	private ImageView img_back;


	private boolean isShowFirstLayout = true;
	
	private WAUser mUser;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		// 1st page
		edt_email = (EditText) findViewById(R.id.edt_email);
		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_password = (EditText) findViewById(R.id.edt_password);
		
		btn_signup = (Button) findViewById(R.id.btn_signup);
		btn_signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isVald()) {
					BaseTask task = new BaseTask(Constants.TASK_USER_REGISTER);
					task.setListener(mTaskListener);
					task.execute();
				}
			}
		});
		
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}

		});


		// set font
		edt_email.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_username.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_password.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		btn_signup.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
	}

	private boolean isVald() {
		if (!Validation.EmptyValidation(edt_email, "Please enter your email address"))
			return false;
		if (!Validation.EmailValid(edt_email, "Please enter correct email address"))
			return false;
		if (!Validation.EmptyValidation(edt_username, "Please enter your user name"))
			return false;
		if (!Validation.EmptyValidation(edt_password, "Please enter your password"))
			return false;
		if (!Validation.LengthMoreValidation(edt_password, Constants.PASSWORD_LENGTH, "Password length must be grater than 6"))
			return false;

		return true;
	}


	TaskListener mTaskListener = new TaskListener() {
		
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_REGISTER) {
				
				result = Server.Register(
						edt_email.getText().toString().trim(), 
						edt_username.getText().toString().trim(), 
						MD5Util.getMD5(edt_password.getText().toString().trim()), 
						"");
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				result = Server.Login(edt_email.getText().toString().trim(), MD5Util.getMD5(edt_password.getText().toString().trim()));
			}

			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_USER_REGISTER) {
				boolean bShow = false;
				if (result != null) {
					if (result instanceof ResponseModel.RegisterModel) {
						ResponseModel.RegisterModel res_model = (ResponseModel.RegisterModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							//MessageUtil.showMessage(res_model.msg, false);
							// if register success, using that information, start login
							BaseTask loginTask = new BaseTask(Constants.TASK_USER_LOGIN);
							loginTask.setListener(mTaskListener);
							loginTask.execute();
							bShow = true;
						}
						else {
							// error
							if (res_model.error_code == Constants.HTTP_ACTION_ERRORCODE_NAMEOREMAIL_ALREADY_REGISTERED ||
								res_model.error_code == Constants.HTTP_ACTION_ERRORCODE_NAME_ALREADY_REGISTERED ||
								res_model.error_code == Constants.HTTP_ACTION_ERRORCODE_EMAIL_ALREADY_REGISTERED) {
								MessageUtil.showMessage(getString(R.string.user_already_registered), false);
								dlg_progress.hide();
								onBackPressed();
							}
							else
								MessageUtil.showMessage(res_model.msg, false);
						}
					}
					else
						MessageUtil.showMessage(result.toString(), false);
				}
				else {
					
				}
				
				if (!bShow)
					dlg_progress.hide();
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				if (result != null) {
					if (result instanceof ResponseModel.LoginResultModel) {
						ResponseModel.LoginResultModel res_model = (ResponseModel.LoginResultModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							//MessageUtil.showMessage("Login success.", false);
							
							// TODO by joseph
							//getUserInformation();
							mUser = new WAUser();
							mUser.user_id = res_model.user.uid;
							mUser.online = Constants.HTTP_ACTION_ONLINE;
							mUser.token = res_model.token;
							mUser.email = res_model.user.email;
							mUser.login = res_model.user.name;
							mUser.password = res_model.user.password;
							mUser.birthday = res_model.user.birthday;
							mUser.fullname = res_model.user.full_name;
							mUser.gender = WAUser.iGengerToSGender(Integer.parseInt(res_model.user.gender));
							mUser.hometown = res_model.user.address;
							mUser.picture_url = res_model.user.picture_url;
							mUser.health_topics_array = res_model.user.health_topic_array;
							mUser.diagnosed_with_array = res_model.user.diagnosed_with_array;
							mUser.diagnosed_with_privacy = res_model.user.diagnosed_with_privacy;
							mUser.medicated_array = res_model.user.medicated_array;
							mUser.medicated_privacy = res_model.user.medicated_privacy;
							mUser.about = res_model.user.about;
							
							WAModelManager.getInstance().setSignInUser(mUser);
							// finish and go to MainActivity
							finish();
							final Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
					        sendBroadcast(intent);
						}
						else {
							// error
							MessageUtil.showMessage(res_model.msg, false);
							finish();
							Intent intent = new Intent(SignUpActivity.this, LoginWithEmailActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.none, R.anim.out_left);
						}
					}
				}
				
				dlg_progress.hide();
			}
		}
		
		@Override
		public void onTaskProgress(int taskId, Object... values) {
			
		}
		
		@Override
		public void onTaskPrepare(int taskId, Object data) {
			dlg_progress.show();
		}
		
		@Override
		public void onTaskCancelled(int taskId) {
			dlg_progress.hide();
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(this, LoginWithEmailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.none, R.anim.out_left);
	}
}
