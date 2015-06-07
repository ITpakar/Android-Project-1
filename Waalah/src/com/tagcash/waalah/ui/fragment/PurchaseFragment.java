package com.tagcash.waalah.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tagcash.waalah.R;
import com.tagcash.waalah.ui.activity.MainActivity;

public class PurchaseFragment extends Fragment implements OnClickListener {

	private MainActivity mainActivity;
	
	public PurchaseFragment(MainActivity activity) {
		super();
		
		mainActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_purchase, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		final LinearLayout layout_first = (LinearLayout) v.findViewById(R.id.layout_first);
		final LinearLayout layout_second = (LinearLayout) v.findViewById(R.id.layout_second);
		final LinearLayout layout_third = (LinearLayout) v.findViewById(R.id.layout_third);
		final LinearLayout layout_fouth = (LinearLayout) v.findViewById(R.id.layout_fouth);
		final LinearLayout layout_fifth = (LinearLayout) v.findViewById(R.id.layout_fifth);
		
		img_menu.setOnClickListener(this);
		layout_first.setOnClickListener(this);
		layout_second.setOnClickListener(this);
		layout_third.setOnClickListener(this);
		layout_fouth.setOnClickListener(this);
		layout_fifth.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_menu:
			mainActivity.showMenu();
			break;
		case R.id.layout_first:
			break;
		case R.id.layout_second:
			break;
		case R.id.layout_third:
			break;
		case R.id.layout_fouth:
			break;
		case R.id.layout_fifth:
			break;
		}
	}

}

