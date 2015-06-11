package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.model.WAEvent;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.DateTimeUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.WAImageLoader;

@SuppressLint("InflateParams")
public class MyEventsFragment extends Fragment implements BaseFragment.BaseFragmentInterface {

	public ListView lst_events = null;

	// prevent list to scroll to top
	private ArrayList<WAEvent> _resultAL = new ArrayList<WAEvent>();
	private WAUser mUser = null;
	private MainActivity mainActivity;

	public static MyEventsFragment newInstance() {
		MyEventsFragment fragment = new MyEventsFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_myevents, null);

		mUser = WAModelManager.getInstance().getSignInUser();

        lst_events = (ListView) view.findViewById(R.id.lst_events);
        
        return view;
    }

	@Override
	public void onStart() {
		super.onStart();

		showListTest();
	}

	private void showListTest() {
		_resultAL.clear();
		
		WAEvent event1 = new WAEvent();
		event1.event_id = 0;
		event1.event_owner = "Brandon Maslow";
		event1.event_date = new Date(2015-1900, 8, 23, 12, 34);
		event1.event_coin = 21;
		
		WAEvent event2 = new WAEvent();
		event2.event_id = 1;
		event2.event_owner = "Stanley Pauls";
		event2.event_date = new Date(2015-1900, 7, 13, 12, 34);
		event2.event_coin = 10;
		
		WAEvent event3 = new WAEvent();
		event3.event_id = 2;
		event3.event_owner = "Lana Del Ray";
		event3.event_date = new Date(2015-1900, 6, 23, 12, 34);
		event3.event_coin = 38;
		
		WAEvent event4 = new WAEvent();
		event4.event_id = 3;
		event4.event_owner = "Hayley Williams";
		event4.event_date = new Date(2015-1900, 7, 1, 12, 34);
		event4.event_coin = 121;
		
		WAEvent event5 = new WAEvent();
		event5.event_id = 4;
		event5.event_owner = "Tom O'dell";
		event5.event_date = new Date(2015-1900, 6, 30, 12, 34);
		event5.event_coin = 59;
		
		_resultAL.add(event1);
		_resultAL.add(event2);
		_resultAL.add(event3);
		_resultAL.add(event4);
		_resultAL.add(event5);
		
		LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
		lst_events.setAdapter(adapter);
	}

	private  class LasyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ArrayList<WAEvent> list_data = new ArrayList<WAEvent>();

		public LasyAdapter(Context context, ArrayList<WAEvent> list_data) {
			if (context == null)
				context = WAApplication.getContext();
			mInflater = LayoutInflater.from(context);
			this.list_data = list_data;
		}

		public int getCount() {
			int nSize = list_data.size();
			return nSize;
		}

		public Object getItem(int position) {
			return list_data.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			TextView txt_name, txt_coin, txt_time;
			ImageView img_background;

		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_myevent_list, null);
				holder = new ViewHolder();

				holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
				holder.txt_coin = (TextView) convertView.findViewById(R.id.txt_coin);
				holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
				holder.img_background = (ImageView) convertView.findViewById(R.id.img_background);
				
				// set font
				holder.txt_name.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, MyEventsFragment.this.getActivity()));
				holder.txt_coin.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, MyEventsFragment.this.getActivity()));
				holder.txt_time.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, MyEventsFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			

			final WAEvent event = (WAEvent) getItem(position);
			
			holder.txt_name.setText(event.event_owner);
			holder.txt_coin.setText(String.valueOf(event.event_coin));
			String format = "hh:mma • MMM dd, yyyy";
			holder.txt_time.setText(DateTimeUtil.dateToString(event.event_date, format));
			
			if (TextUtils.isEmpty(event.picture_url)) {
				holder.img_background.setImageResource(R.drawable.row_myevent_back);
			}else {
				WAImageLoader.showImage(holder.img_background, event.picture_url);
			}
				
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					int event_id = event.event_id; 
					boolean is_joined = true;
					
					mainActivity.showDetailEventFragment(event, is_joined);
				}
			});
			return convertView;
		}
	}

	@Override
	public void manualRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	public void setMainActivity(MainActivity activity) {
		mainActivity = activity;
	}
}
