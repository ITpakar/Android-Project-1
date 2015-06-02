package com.tagcash.waalah.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.util.CommonUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.view.WAProgressDialog;

public class BaseActivity extends FragmentActivity {

	public LinearLayout layout_content;
	public LinearLayout layout_empty;

	public TextView titlebar_txt_title;
	public View	titlebar_btn_edit;
	public WAProgressDialog dlg_progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.img_dark_purple));
			getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_bar_home));

			getActionBar().setDisplayShowCustomEnabled(true);
			getActionBar().setDisplayShowTitleEnabled(false);

			LayoutInflater inflator = LayoutInflater.from(this);
			View v = inflator.inflate(R.layout.actionbar_title, null);
			titlebar_txt_title = (TextView)v.findViewById(R.id.titlebar_txt_title);
			titlebar_txt_title.setText(this.getTitle());
			titlebar_btn_edit = v.findViewById(R.id.titlebar_btn_edit);

			//assign the view to the actionbar
			getActionBar().setCustomView(v);

			// set font
			titlebar_txt_title.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this));
		}

		//
		dlg_progress = new WAProgressDialog(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			return false;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean result = super.dispatchTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			View view = getCurrentFocus();
			if (view != null && (!result || (view instanceof Button))) {
				CommonUtil.hideKeyboard(view);
			}
		}
		return result;
	}

	public void showEmptyLayout(boolean show) {
		if (layout_content == null || layout_empty == null)
			return;

		if (show) {
			layout_content.setVisibility(View.GONE);
			layout_empty.setVisibility(View.VISIBLE);
		} else {
			layout_content.setVisibility(View.VISIBLE);
			layout_empty.setVisibility(View.GONE);
		}
	}
}