package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.tagcash.waalah.model.WAEvent;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.ui.activity.MyEventsDetailActivity;
import com.tagcash.waalah.ui.activity.UpcomingDetailActivity;

public class DetailEventFragment extends Fragment implements OnClickListener {

	private ArrayList<WAEvent> _resultAL = new ArrayList<WAEvent>();

	private MainActivity mainActivity;
	private int mEventId;
	private boolean mEventJoined;
	
	private int event_count = 0;
	private final static int REQUEST_CALENDAR = 10000;
	
	private Button btn_join;
	private LinearLayout layout_member;
	
	@SuppressWarnings("deprecation")
	public DetailEventFragment(MainActivity activity, int event_id, boolean isJoined) {
		super();
		
		mainActivity = activity;
		mEventId = event_id;
		mEventJoined = isJoined;
		
		_resultAL.clear();
		
		WAEvent event1 = new WAEvent();
		event1.event_id = 0;
		event1.event_owner = "Brandon Maslow";
		event1.event_coin = 320;
		event1.event_date = new Date(2015-1900, 8, 23, 12, 34);
		
		WAEvent event2 = new WAEvent();
		event2.event_id = 1;
		event2.event_owner = "Stanley Pauls";
		event2.event_coin = 120;
		event2.event_date = new Date(2015-1900, 7, 13, 12, 34);
		
		WAEvent event3 = new WAEvent();
		event3.event_id = 2;
		event3.event_owner = "Lana Del Ray";
		event3.event_coin = 420;
		event3.event_date = new Date(2015-1900, 6, 23, 12, 34);
		
		WAEvent event4 = new WAEvent();
		event4.event_id = 3;
		event4.event_owner = "Hayley Williams";
		event4.event_coin = 540;
		event4.event_date = new Date(2015-1900, 7, 1, 12, 34);
		
		WAEvent event5 = new WAEvent();
		event5.event_id = 4;
		event5.event_owner = "Tom O'dell";
		event5.event_coin = 275;
		event5.event_date = new Date(2015-1900, 6, 30, 12, 34);
		
		_resultAL.add(event1);
		_resultAL.add(event2);
		_resultAL.add(event3);
		_resultAL.add(event4);
		_resultAL.add(event5);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_detail_event, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		final LinearLayout layout_addcoin = (LinearLayout) v.findViewById(R.id.layout_addcoin);
		final LinearLayout layout_myevent = (LinearLayout) v.findViewById(R.id.layout_myevent);
		final Button btn_earn_coin = (Button) v.findViewById(R.id.btn_earn_coin);
		final TextView txt_time = (TextView) v.findViewById(R.id.txt_time);
		final TextView txt_name = (TextView) v.findViewById(R.id.txt_name);
		final TextView txt_name1 = (TextView) v.findViewById(R.id.txt_name1);
		final TextView txt_coins = (TextView) v.findViewById(R.id.txt_coins);
		final TextView txt_events = (TextView) v.findViewById(R.id.txt_events);
		
		WAEvent event = _resultAL.get(mEventId);
		
		Date currentDate = Calendar.getInstance().getTime();
		txt_time.setText(getDifference(currentDate, event.event_date));
		txt_name.setText(event.event_owner);
		txt_name1.setText(event.event_owner);
		
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

    public String getDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        if (different < 0)
        	return "";

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String time = String.format("%ddd : %dhrs : %dMin : %dSec%n", 
            elapsedDays,
            elapsedHours, elapsedMinutes, elapsedSeconds);
        
        return time;
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
			if (mEventJoined == true)
			{
				addCoins();
			}
			else
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
				            dialog.dismiss();
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				builder.setTitle("Waalah");
				builder.setMessage("Please join the event");
				builder.setPositiveButton("Ok", dialogClickListener);
				
				AlertDialog dialog = builder.show();
				TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				
				dialog.show();	
				
			}
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
		intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", "A Test Event");
		startActivityForResult(intent, REQUEST_CALENDAR);
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
		final Dialog dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

		dialog.setContentView(view);
		dialog.show();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = 430;
		lp.height = 450;
		dialog.getWindow().setAttributes(lp);
	}
}
