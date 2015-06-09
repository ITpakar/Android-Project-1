package com.tagcash.waalah.ui.fragment;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.ui.activity.MyEventsDetailActivity;
import com.tagcash.waalah.ui.activity.UpcomingDetailActivity;

public class DetailEventFragment extends Fragment implements OnClickListener {

	private MainActivity mainActivity;
	private int mEventId;
	private boolean mEventJoined;
	
	private int event_count = 0;
	private final static int REQUEST_CALENDAR = 10000;
	
	private Button btn_join;
	private LinearLayout layout_member;
	
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
		final Button btn_earn_coin = (Button) v.findViewById(R.id.btn_earn_coin);
		
		layout_member = (LinearLayout) v.findViewById(R.id.layout_member);
		btn_join = (Button) v.findViewById(R.id.btn_join);

		
		img_menu.setOnClickListener(this);
		layout_addcoin.setOnClickListener(this);
		layout_myevent.setOnClickListener(this);
		layout_member.setOnClickListener(this);
		btn_earn_coin.setOnClickListener(this);
		btn_join.setOnClickListener(this);
		
		if (mEventJoined == true)
		{
			btn_join.setBackgroundResource(R.drawable.event_joined);
			btn_join.setText("");
			
			layout_member.setVisibility(View.VISIBLE);
		}
		else
		{
			btn_join.setBackgroundResource(R.drawable.event_not_join);
			btn_join.setText("JOIN");
			
			layout_member.setVisibility(View.GONE);
		}
		
		return v;
	}

		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_CALENDAR)
		{
			int new_event_count = getEventCount();
			
			if (event_count != new_event_count) // save action
			{
				mEventJoined = true;
				btn_join.setBackgroundResource(R.drawable.event_joined);
				btn_join.setText("");
				
				layout_member.setVisibility(View.VISIBLE);
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_menu:
			mainActivity.showMenu();
			break;
			
		case R.id.layout_addcoin:
			addCoins();
			break;
			
		case R.id.layout_myevent:
			if (mEventJoined == true)
			{
				Intent intent = new Intent(this.getActivity(), MyEventsDetailActivity.class);
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(this.getActivity(), UpcomingDetailActivity.class);
				startActivity(intent);
			}
			break;
			
		case R.id.layout_member:
			Intent intent = new Intent(this.getActivity(), UpcomingDetailActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btn_earn_coin:
			mainActivity.showEainCoinsFragment();
			break;
			
		case R.id.btn_join:
			if (mEventJoined == false)
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
							addTask();
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							//No button clicked
							break;
						}					
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				builder.setTitle("Calendar");
				builder.setMessage("Would you like to add the event to your calendar?");
				builder.setNegativeButton("No", dialogClickListener);
				builder.setPositiveButton("Yes", dialogClickListener);
				
				AlertDialog dialog = builder.show();
				TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				
				dialog.show();	
			}
			break;
		}
	}

	public void addTask()
	{
		event_count = getEventCount();
		
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("allDay", false);
//		intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", "A Test Event");
		startActivityForResult(intent, REQUEST_CALENDAR);
//		startActivity(intent);	
	}
	
	public int getEventCount()
	{
        Cursor cursor = this.getActivity().getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();

        return cursor.getCount();
	}
	
	public void addCoins()
	{
//		AlertDialog.Builder builder;
//		AlertDialog alertDialog;
//
		final Dialog dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_addcoins);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_add_coins, null);
		
		Button btn_addcoins = (Button) view.findViewById(R.id.btn_add_coins);
		btn_addcoins.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.hide();
			}
		});

//		builder = new AlertDialog.Builder(getActivity());
//		builder.setView(view);
//		alertDialog = builder.create();
//		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		alertDialog.show();

		dialog.setContentView(view);
		dialog.show();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = 430;
		lp.height = 450;
		dialog.getWindow().setAttributes(lp);
	}
}
