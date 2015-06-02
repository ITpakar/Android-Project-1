package com.tagcash.waalah.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.tagcash.waalah.R;

public class WAProgressDialog extends Dialog {

	private Context mContext;

	public WAProgressDialog(Context context) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		setContentView(R.layout.dialog_progress);
		this.setCancelable(false);
	}

	public WAProgressDialog(Context context, boolean cancelable) {
		super(context);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_progress);
		this.setCancelable(cancelable);
	}

	public void show() {
		super.show();
	}
}
