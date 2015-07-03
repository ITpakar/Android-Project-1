package com.tagcash.waalah.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


import com.tagcash.waalah.R;
import com.tagcash.waalah.ui.activity.MainActivity;

public class SettingFragment extends Fragment implements OnClickListener {

	private MainActivity mainActivity;

	public SettingFragment(MainActivity activity) {
		super();
		
		mainActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_setting, null);


		
		return v;
	}

	@Override
	public void onClick(View v) {

	}

}
