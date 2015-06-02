package com.tagcash.waalah.ui.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.DateTimeUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.MD5Util;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.Validation;

public class RegistrationWithEmailActivity extends BaseActivity {

	private LinearLayout layout0;
	private LinearLayout layout1;

	private EditText edt_email;
	private EditText edt_username;
	private EditText edt_password;
	private EditText edt_confirm_password;
	private TextView txt_gender;
	private TextView txt_birthday;
	private EditText edt_hometown;

	private Button btn_continue;

	private boolean isShowFirstLayout = true;
	
	private WAUser mUser;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_with_email);

		layout0 = (LinearLayout) findViewById(R.id.layout0);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout0.setVisibility(View.VISIBLE);
		layout1.setVisibility(View.GONE);

		// 1st page
		edt_email = (EditText) findViewById(R.id.edt_email);
		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);

		// 2nd page
		txt_gender = (TextView) findViewById(R.id.txt_gender);
		txt_birthday = (TextView) findViewById(R.id.txt_birthday);
		edt_hometown = (EditText) findViewById(R.id.edt_hometown);
		btn_continue = (Button) findViewById(R.id.btn_continue);

		// set listener
		txt_gender.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String [] sex_arr = {"Male", "Female"};
				new AlertDialog.Builder(RegistrationWithEmailActivity.this)
				.setTitle("Select Gender")
				.setItems(sex_arr, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							txt_gender.setText(Constants.GENDER.sMale);
						} else {
							txt_gender.setText(Constants.GENDER.sFemale);
						}
						txt_gender.setTextColor(0xff000000);
					}
				})
				.show();
			}
		});
		txt_birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog();
			}
		});
		btn_continue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isVald()) {
					if (isShowFirstLayout) {
						layout0.setVisibility(View.GONE);
						layout1.setVisibility(View.VISIBLE);
						btn_continue.setBackgroundResource(R.drawable.bg_dark_purple);
						isShowFirstLayout = false;
					} else {
						BaseTask task = new BaseTask(Constants.TASK_USER_REGISTER);
						task.setListener(mTaskListener);
						task.execute();
					}
				}
			}
		});

		// set font
		((TextView) findViewById(R.id.txt_title)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		((TextView) findViewById(R.id.txt_register)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_email.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_username.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_password.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_confirm_password.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		txt_gender.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		txt_birthday.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		edt_hometown.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		((TextView) findViewById(R.id.txt_line0)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, this));
		((TextView) findViewById(R.id.txt_line1)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, this));
		btn_continue.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
	}

	private boolean isVald() {
		if (isShowFirstLayout) {
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
			if (!Validation.MatchValidation(edt_password, edt_confirm_password, "Please confrim password, your 2 passwords not match."))
				return false;
		} else {
			if (!Validation.textNoMatchValidation(txt_gender, "Your Gender:", "Please select your gender"))
				return false;
			if (!Validation.textNoMatchValidation(txt_birthday, "Your Birthday:", "Please enter your birthday"))
				return false;
			if (!Validation.EmptyValidation(edt_hometown, "Please enter your home town"))
				return false;
		}

		return true;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			txt_birthday.setText(new StringBuilder()
			.append(year).append("-")
			.append(monthOfYear + 1).append("-") // Month is 0 based so add 1
			.append(dayOfMonth));
			txt_birthday.setTextColor(0xff000000);
		}
	};

	public void showDatePickerDialog() {
		Date today = DateTimeUtil.stringToDate(txt_birthday.getText().toString().trim(), "yyyy-MM-dd");
		final Calendar c = Calendar.getInstance();
		if (today == null) {
			today = c.getTime();
		}
		c.setTime(today);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		new DatePickerDialog(this, mDateSetListener, year, month, day).show();
	}

	TaskListener mTaskListener = new TaskListener() {
		
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_REGISTER) {
				result = Server.Register(edt_email.getText().toString().trim(), edt_username.getText().toString().trim(), edt_username.getText().toString().trim(), 
						MD5Util.getMD5(edt_password.getText().toString().trim()), txt_gender.getText().toString().trim(), 
						txt_birthday.getText().toString().trim(), edt_hometown.getText().toString().trim(), "", edt_username.getText().toString().trim());
			}
			else if (taskId == Constants.TASK_USER_LOGIN) {
				result = Server.Login(edt_email.getText().toString().trim(), MD5Util.getMD5(edt_password.getText().toString().trim()));
			}

			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_USER_REGISTER) {
				boolean bShow = false;;
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
							Intent intent = new Intent(RegistrationWithEmailActivity.this, HomeActivity.class);
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
		if (isShowFirstLayout) {
			finish();
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.none, R.anim.out_left);

		} else {
			layout0.setVisibility(View.VISIBLE);
			layout1.setVisibility(View.GONE);
			btn_continue.setBackgroundResource(R.drawable.bg_mid_grey);
			isShowFirstLayout = true;
		}
	}
}