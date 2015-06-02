package com.tagcash.waalah.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.util.WAFontProvider;

public class SignUpActivity extends BaseActivity {

	public static SignUpActivity instance = null;
	
	private Button btn_facebook_login;
	private Button btn_google_login;
	private Button btn_twitter_login;
	private Button btn_register_with_email;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		instance = this;

		btn_facebook_login = (Button) findViewById(R.id.btn_facebook_login);
		btn_google_login = (Button) findViewById(R.id.btn_google_login);
		btn_twitter_login = (Button) findViewById(R.id.btn_twitter_login);
		btn_register_with_email = (Button) findViewById(R.id.btn_register_with_email);

		btn_facebook_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickFacebookLogin();
			}
		});
//		btn_google_login.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onClickGoogleLogin();
//			}
//		});
//		btn_twitter_login.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onClickTwitterLogin();
//			}
//		});
		btn_register_with_email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SignUpActivity.this, RegistrationWithEmailActivity.class));
				SignUpActivity.this.finish();
				overridePendingTransition(R.anim.in_right, R.anim.none);
			}
		});

		// set font
		((TextView)findViewById(R.id.txt_welcome)).setTypeface(WAFontProvider.getFont(WAFontProvider.MUSEO_700, this));
		((TextView)findViewById(R.id.txt_welcome1)).setTypeface(WAFontProvider.getFont(WAFontProvider.MUSEO_500, this));
		((TextView)findViewById(R.id.txt_welcome2)).setTypeface(WAFontProvider.getFont(WAFontProvider.MUSEO_300, this));
		((TextView)findViewById(R.id.txt_welcome3)).setTypeface(WAFontProvider.getFont(WAFontProvider.MUSEO_300, this));
		btn_facebook_login.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
		btn_google_login.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
		btn_twitter_login.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
		((TextView)findViewById(R.id.txt_line0)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA, this));
		((TextView)findViewById(R.id.txt_line1)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA, this));
		((TextView)findViewById(R.id.txt_or)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
		btn_register_with_email.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_CE, this));
	}

	private void onClickFacebookLogin() {
		Intent intent = new Intent(this, LoginWithFacebookActivity.class);
		intent.putExtra(Constants.KEY_FLAG, Constants.MODE_REGISTER);
		startActivity(intent);
	}

//	private void onClickGoogleLogin() {
//		Intent intent = new Intent(this, LoginWithGoolgeActivity.class);
//		intent.putExtra(Constants.KEY_FLAG, Constants.MODE_REGISTER);
//		startActivity(intent);
//	}
	
//	private void onClickTwitterLogin() {
//		Intent intent = new Intent(this, LoginWithTwitterActivity.class);
//		intent.putExtra(Constants.KEY_FLAG, Constants.MODE_REGISTER);
//		startActivity(intent);
//	}
	
	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.none, R.anim.out_left);
	}
}
