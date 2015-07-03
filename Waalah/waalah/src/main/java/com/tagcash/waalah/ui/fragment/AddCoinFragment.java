package com.tagcash.waalah.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.ui.activity.LoginWithTwitterActivity;
import com.tagcash.waalah.ui.activity.MainActivity;

public class AddCoinFragment extends Fragment implements OnClickListener {

	private MainActivity mainActivity;

	public AddCoinFragment(MainActivity activity) {
		super();
		
		mainActivity = activity;
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
			mainActivity.showMenu();
			break;
			
		case R.id.layout_freecoin:
			mainActivity.showFreeCoinFragment();
			break;
			
		case R.id.layout_purchase:
			mainActivity.showPurchaseFragment();
			break;
			
		case R.id.layout_social:
			Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		    intent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.app_name));
		    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
		    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.strShareUrl));
		    intent.setType("*/*");
		    startActivity(Intent.createChooser(intent, ""));
			break;
			
		case R.id.layout_follow:
			Intent intent_follow = new Intent(this.getActivity(), LoginWithTwitterActivity.class);
			intent_follow.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
			startActivity(intent_follow);

			break;
		}
	}

}
