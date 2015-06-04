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

public class AddCoinFragment extends Fragment implements OnClickListener {

	private MenuAdapter adapter;
	
	public AddCoinFragment(MenuAdapter mfa) {
		super();
		
		adapter = mfa;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_addcoin, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		final LinearLayout layout_freecoin = (LinearLayout) v.findViewById(R.id.layout_freecoin);
		final LinearLayout layout_purchase = (LinearLayout) v.findViewById(R.id.layout_purchase);
		final LinearLayout layout_social = (LinearLayout) v.findViewById(R.id.layout_social);
		final LinearLayout layout_follow = (LinearLayout) v.findViewById(R.id.layout_follow);
		
		img_menu.setOnClickListener(this);
		layout_freecoin.setOnClickListener(this);
		layout_purchase.setOnClickListener(this);
		layout_social.setOnClickListener(this);
		layout_follow.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_menu:
			adapter.onMenuClicked();
			break;
		case R.id.layout_freecoin:
			break;
		case R.id.layout_purchase:
			break;
		case R.id.layout_social:
			break;
		case R.id.layout_follow:
			break;
		}
	}

}
