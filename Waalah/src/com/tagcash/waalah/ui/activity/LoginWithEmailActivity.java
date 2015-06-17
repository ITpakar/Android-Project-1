package com.tagcash.waalah.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.database.DBManager;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.GMailSender;
import com.tagcash.waalah.util.MD5Util;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.Validation;
import com.tagcash.waalah.util.WAFontProvider;


@SuppressLint("InflateParams")
public class LoginWithEmailActivity extends BaseActivity {

	public static LoginWithEmailActivity instance = null;
	private EditText edt_email;
	private EditText edt_password;
	private Button btn_retrieve_password;
	private Button btn_login;
	private Button btn_facebook_login;
	private Button btn_signup;


	private WAUser mUser;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_email);

		instance = this;

		edt_email = (EditText) findViewById(R.id.edt_email);
		edt_password = (EditText) findViewById(R.id.edt_password);
		btn_retrieve_password = (Button) findViewById(R.id.btn_retrieve_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_facebook_login = (Button) findViewById(R.id.btn_facebook_login);
		btn_signup = (Button) findViewById(R.id.btn_signup);


		btn_retrieve_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onResetPassword();
			}
		});
		
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				WAUser user = new WAUser();
				
				user.id = 3;
				user.fullname = "Emily Green";
				user.login = "emily";
				user.password = "emily123";
				user.email = "emily@gmail.com";
				user.hometown = "Manchester, UK";
				user.coins = 632;
				user.user_id = 3;
				user.picture_url = "";

				WAModelManager.getInstance().setSignInUser(user);

				finish();
				
				Intent intent_main = new Intent(instance, MainActivity.class);
				intent_main.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
				startActivity(intent_main);
				
				// TODO joseph

//				if (isValid()) {
//					ArrayList<String> strs = new ArrayList<String>();
//					strs.add(edt_email.getText().toString().trim());
//					strs.add(MD5Util.getMD5(edt_password.getText().toString().trim()));
//
//					BaseTask loginTask = new BaseTask(Constants.TASK_USER_LOGIN);
//					loginTask.setListener(mTaskListener);
//					loginTask.setData(strs);
//					loginTask.execute();
//				}
			}
		});
		
		btn_facebook_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickFacebookLogin();
			}
		});
		
		btn_signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickSignup();
			}
		});

		
		// using home activity, it means user could use another account, so clear database
		DBManager.getInstance().deleteAllUser();
		WAModelManager.getInstance().setSignInUser(null);

		// set font
		edt_email.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_password.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		btn_login.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
		btn_facebook_login.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
		btn_signup.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
	}

	private boolean isValid() {
		if (!Validation.EmptyValidation(edt_email, "Please input your email address"))
			return false;
		if (!Validation.EmptyValidation(edt_password, "Please input password"))
			return false;

		return true;
	}

	private void onResetPassword() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_retrieve_password, null);
		final EditText edt_user = (EditText)view.findViewById(R.id.edt_user);
		final EditText new_pwd = (EditText)view.findViewById(R.id.new_pwd);
		final EditText confirm_pwd = (EditText)view.findViewById(R.id.confirm_pwd);

		new AlertDialog.Builder(this)
		.setView(view)
		.setTitle("Forgot Password")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final String user_name = edt_user.getText().toString().trim();
				if (TextUtils.isEmpty(user_name)) {
					MessageUtil.showMessage("Please enter your email", true);
					return;
				}
				if (!Validation.EmptyValidation(new_pwd, "Please enter your password"))
					return;
				if (!Validation.LengthMoreValidation(new_pwd, Constants.PASSWORD_LENGTH, "Password length must be grater than 6"))
					return;
				if (!Validation.MatchValidation(new_pwd, confirm_pwd, "Please confrim password, your 2 passwords not match."))
					return;

				final String new_password = new_pwd.getText().toString().trim();

				new AlertDialog.Builder(LoginWithEmailActivity.this)
				.setIcon(android.R.drawable.ic_dialog_email)
				.setTitle("Please Confirm your email")
				.setMessage(user_name)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						ArrayList<String> strs = new ArrayList<String>();
						strs.add(user_name);
						strs.add(new_password);

						BaseTask task = new BaseTask(Constants.TASK_USER_RESET_PASSWORD);
						task.setListener(mTaskListener);
						task.setData(strs);
						task.execute();
					}
				})
				.setNegativeButton("Cancel", null)
				.show();
			}
		})
		.setNegativeButton("Cancel", null)
		.show();
	}

	private void onClickFacebookLogin() {
		Intent intent = new Intent(this, LoginWithFacebookActivity.class);
		intent.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
		startActivity(intent);
	}
	
	private void onClickSignup() {
		finish();
		Intent intent = new Intent(this, SignUpActivity.class);
		intent.putExtra(Constants.KEY_FLAG, Constants.MODE_REGISTER);
		startActivity(intent);
	}

	boolean isBackAllowed = false;
	@Override
	public void onBackPressed() {
		if(!isBackAllowed) {
			MessageUtil.showMessage(getResources().getString(R.string.press_back_alert), false);
			isBackAllowed = true;

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					isBackAllowed = false;
				}
			}, 5000);

		} else {
			if (MainActivity.instance != null)
				MainActivity.instance.finish();
			super.onBackPressed();
		}
	}

	class SendPasswordByEmailTask extends AsyncTask<WAUser, Void, String> {

		@Override
		protected String doInBackground(WAUser... users) {
			try {   
				GMailSender sender = new GMailSender(Constants.HEALTHCHAT_EMAIL_ADDRESS, Constants.HEALTHCHAT_EMAIL_PASSWORD);
				String body = "\nDear, " + users[0].login + "!\n\n\n"
						+ "Welcome to return to Healthchat.\n"
						+ "We retrieved your password by your request and sent you, Please try this password.\n\n\n"
						+ "****************************************************************\n"
						+ "*\n"
						+ "*       " + users[0].password + "\n"
						+ "*\n"
						+ "****************************************************************\n\n\n"
						+ "Please keep your security of your password.\n"
						+ "Hope your kind feedback to our service.\n\n\n"
						+ "Best Regards.\n\nWaalah Support Team.";

				sender.sendMail("Waalah: Retreive Password", body, Constants.HEALTHCHAT_EMAIL_ADDRESS, users[0].email);

				return "Sent email successfully";

			} catch (Exception e) {   
				return "Sent email Failed";   
			}
		}

		@Override
		protected void onPostExecute(String message) {
			LoginWithEmailActivity.this.dlg_progress.hide();
			MessageUtil.showMessage(message, true);
		}
	}

	TaskListener mTaskListener = new TaskListener() {
		
		@SuppressWarnings("unchecked")
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_ISREGISTER) {
				String str = (String) data;
				result = Server.IsRegister(str);
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				ArrayList<String> strs = (ArrayList<String>) data;
				result = Server.Login(strs.get(0), strs.get(1));
			}
			else if (taskId == Constants.TASK_USER_RESET_PASSWORD) {
				ArrayList<String> strs = (ArrayList<String>) data;
				result = Server.Reset_Password(strs.get(0), MD5Util.getMD5(strs.get(1)));
			}
			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_USER_ISREGISTER) {
				if (result != null) {
					if (result instanceof ResponseModel.CheckRegisterModel) {
						ResponseModel.CheckRegisterModel res_model = (ResponseModel.CheckRegisterModel) result;
						WAUser user = new WAUser();
						user.email = res_model.email;
						user.login = res_model.login;
						user.password = res_model.password;
						new SendPasswordByEmailTask().execute(user);
					}
					else
						MessageUtil.showMessage(result.toString(), false);
				}
				else {
					
				}
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				if (result != null) {
					if (result instanceof ResponseModel.LoginResultModel) {
						ResponseModel.LoginResultModel res_model = (ResponseModel.LoginResultModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							//MessageUtil.showMessage("Login With Email Account Success.", false);
							
							// TODO by joseph
							mUser = new WAUser();
							mUser.user_id = res_model.user.uid;
							mUser.email = res_model.user.email;
							mUser.login = res_model.user.name;
							mUser.password = res_model.user.password;
							mUser.picture_url = res_model.user.picture_url;

							WAModelManager.getInstance().setSignInUser(mUser);

							// finish and go to MainActivity
							finish();
							final Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
					        sendBroadcast(intent);
						}
						else {
							// error
							MessageUtil.showMessage(res_model.msg, false);
						}
					}
					else
						MessageUtil.showMessage(result.toString(), false);
				}
				else {
					
				}
			}
			else if (taskId == Constants.TASK_USER_RESET_PASSWORD) {
				if (result != null) {
					if (result instanceof ResponseModel.ResetPasswordModel) {
						ResponseModel.ResetPasswordModel res_model = (ResponseModel.ResetPasswordModel) result;
						MessageUtil.showMessage(res_model.msg, false);
					}
				}
				else {
					
				}
			}
			
			LoginWithEmailActivity.this.dlg_progress.hide();
		}
		
		@Override
		public void onTaskProgress(int taskId, Object... values) {
			
		}
		
		@Override
		public void onTaskPrepare(int taskId, Object data) {
			LoginWithEmailActivity.this.dlg_progress.show();
		}
		
		@Override
		public void onTaskCancelled(int taskId) {
			LoginWithEmailActivity.this.dlg_progress.hide();
		}
	};
}
