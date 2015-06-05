package com.tagcash.waalah.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tagcash.waalah.R;
import com.tagcash.waalah.ui.activity.MainActivity;

public class DetailEventFragment extends Fragment implements OnClickListener {

	private MainActivity mainActivity;
	private int mEventId;
	private boolean mEventJoined;
	
	public DetailEventFragment(MainActivity activity, int event_id, boolean isJoined) {
		super();
		
		mainActivity = activity;
		mEventId = event_id;
		mEventJoined = isJoined;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_detail_event, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		final LinearLayout layout_addcoin = (LinearLayout) v.findViewById(R.id.layout_addcoin);
		final LinearLayout layout_myevent = (LinearLayout) v.findViewById(R.id.layout_myevent);

		
		img_menu.setOnClickListener(this);
		layout_addcoin.setOnClickListener(this);
		layout_myevent.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_menu:
			mainActivity.onMenuClicked();
			break;
		case R.id.layout_addcoin:
			break;
		case R.id.layout_myevent:
			break;
		}
	}

}
