package com.tagcash.waalah.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

public class SignUpActivity extends BaseActivity {

	public static SignUpActivity instance = null;
	
	private EditText edt_email;
	private EditText edt_password;
	private EditText edt_username;
	private Button btn_signup;
	private ImageView img_back;

	private WAUser mUser;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		instance = this;

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
			else if (taskId == Constants.TASK_USER_GETME) {
				ArrayList<String> strs = (ArrayList<String>) data;
				result = Server.GetCurrentUser(strs.get(0)); // set api_token
			}

			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_USER_REGISTER) {
				boolean bShow = false;
				Log.v(Constants.LOG_TAG, result.toString());

				if (result != null) {
					if (result instanceof ResponseModel.RegisterModel) {
						
						ResponseModel.RegisterModel res_model = (ResponseModel.RegisterModel) result;
						
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							
							// if register success, using that information, start main home
							ArrayList<String> strs = new ArrayList<String>();
							strs.add(res_model.api_token);

							BaseTask meTask = new BaseTask(Constants.TASK_USER_GETME);
							meTask.setListener(mTaskListener);
							meTask.setData(strs);
							meTask.execute();

							bShow = true;
						}
						else {
							// error
							if (res_model.status == Constants.HTTP_ACTION_ERRORCODE_NAMEOREMAIL_ALREADY_REGISTERED ||
								res_model.status == Constants.HTTP_ACTION_ERRORCODE_NAME_ALREADY_REGISTERED ||
								res_model.status == Constants.HTTP_ACTION_ERRORCODE_EMAIL_ALREADY_REGISTERED) {
								MessageUtil.showMessage(getString(R.string.user_already_registered), false);
								dlg_progress.hide();
								onBackPressed();
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
				
				if (!bShow)
					dlg_progress.hide();
			}
			else if (taskId == Constants.TASK_USER_GETME) {
				if (result != null) {
					if (result instanceof ResponseModel.CurrentUserResultModel) {
						Log.v(Constants.LOG_TAG, result.toString());
						ResponseModel.CurrentUserResultModel res_model = (ResponseModel.CurrentUserResultModel) result;

						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {

							mUser = new WAUser();
							if (res_model.me != null)
							{
								mUser.id = res_model.me.id;
								mUser.username = res_model.me.username;
//								mUser.password = res_model.me.password;
								mUser.password = MD5Util.getMD5(edt_password.getText().toString().trim());
								mUser.email = res_model.me.email;
								mUser.firstname = res_model.me.firstname;
								mUser.lastname = res_model.me.lastname;
								mUser.address = res_model.me.address;
								mUser.city = res_model.me.city;
								mUser.state = res_model.me.state;
								mUser.zip = res_model.me.zip;
								mUser.country = res_model.me.country;
								mUser.api_token = res_model.me.api_token;
								mUser.quickblox_id = res_model.me.quickblox_id;
								mUser.allow_notifications = res_model.me.allow_notifications;
							}

							if (res_model.img != null)
								mUser.picture_url = res_model.img.original;

							if (res_model.balance != null)
								mUser.coins = (int)res_model.balance.coins;

							WAModelManager.getInstance().setSignInUser(mUser);
							// finish and go to MainActivity
							Intent intent = new Intent(instance, MainActivity.class);
							intent.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
							startActivity(intent);
							finish();
						}
						else {
							// error
							finish();
							Intent intent = new Intent(instance, LoginWithEmailActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.none, R.anim.out_left);
						}

						return;
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
