package com.tagcash.waalah.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.AppPreferences;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.MessageUtil;

public class HomeActivity extends BaseActivity {

	public static HomeActivity instance = null;
	private AppPreferences prefs;

	private Button btn_register;
	private Button btn_login;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		instance = this;
		prefs = new AppPreferences(this);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_login = (Button) findViewById(R.id.btn_login);

		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doRegister();
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLogin();
			}
		});
		
		MainActivity.isLoginSuccesed = false;

		//set font
		btn_register.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA, this));
		btn_login.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA, this));

		// initialize social information
//		TwitterUtils.initInformations(prefs);
	}
	
	private void doRegister() {
		Intent intent = new Intent(this, SignUpActivity.class); 
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.in_right, R.anim.none);
	}
	
	private void doLogin() {
		Intent intent = new Intent(this, LoginWithEmailActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.in_right, R.anim.none);
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
}
