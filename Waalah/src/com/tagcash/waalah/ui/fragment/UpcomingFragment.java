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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.model.WAEvent;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.DateTimeUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.WAImageLoader;
import com.tagcash.waalah.view.StaggeredGridView;

@SuppressLint("InflateParams")
public class UpcomingFragment extends Fragment implements BaseFragment.BaseFragmentInterface {

	private StaggeredGridView grid_events;

	// prevent list to scroll to top
	private ArrayList<WAEvent> _resultAL = new ArrayList<WAEvent>();
	private MainActivity mainActivity;

	public static UpcomingFragment newInstance() {
		UpcomingFragment fragment = new UpcomingFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_upcoming, null);

		WAModelManager.getInstance().getSignInUser();

		grid_events = (StaggeredGridView) view.findViewById(R.id.grid_upcoming);
        
        return view;
    }

	@Override
	public void onStart() {
		super.onStart();

//		GetDataFromDB();
		
		// TODO by joseph
		showListTest();
	}

	@SuppressWarnings("deprecation")
	private void showListTest() {
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
		
		LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
		grid_events.setAdapter(adapter);
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
			ImageView img_picture;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if (convertView == null) {
				if (((position % 5) % 2) == 0)
					convertView = mInflater.inflate(R.layout.row_upcoming_small, null);
				else
					convertView = mInflater.inflate(R.layout.row_upcoming_tall, null);
				
				holder = new ViewHolder();

				holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
				holder.txt_coin = (TextView) convertView.findViewById(R.id.txt_coin);
				holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
				holder.img_picture = (ImageView) convertView.findViewById(R.id.img_picture);
				
				// set font
				holder.txt_name.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, UpcomingFragment.this.getActivity()));
				holder.txt_coin.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, UpcomingFragment.this.getActivity()));
				holder.txt_time.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, UpcomingFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			
			final WAEvent event = (WAEvent) getItem(position);
			
			holder.txt_name.setText(event.event_owner);
			holder.txt_coin.setText(String.valueOf(event.event_coin));
			String format = "hh:mma • MMM dd, yyyy";
			holder.txt_time.setText(DateTimeUtil.dateToString(event.event_date, format));
			
			if (TextUtils.isEmpty(event.picture_url) && ((position % 5) % 2) == 0) {
				holder.img_picture.setImageResource(R.drawable.row_upcoming_small_back);
			} else if (TextUtils.isEmpty(event.picture_url) && ((position % 5) % 2) == 1) {
				holder.img_picture.setImageResource(R.drawable.row_upcoming_tall_back);
			} else {
				WAImageLoader.showImage(holder.img_picture, event.picture_url);
			}

			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					int event_id = event.event_id; 
					boolean is_joined = false;
					
					mainActivity.showDetailEventFragment(event_id, is_joined);
				}
			});
			
			if (((position % 5) % 2) == 0)
				convertView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
			else
				convertView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 304));
			
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
