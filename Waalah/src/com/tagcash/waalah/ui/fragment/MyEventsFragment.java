package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.WAFontProvider;

@SuppressLint("InflateParams")
public class MyEventsFragment extends Fragment implements BaseFragment.BaseFragmentInterface {

	public ListView lst_events = null;

	// prevent list to scroll to top
	private ArrayList<WAUser> _resultAL = new ArrayList<WAUser>();
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
//		layout_content = (LinearLayout) view.findViewById(R.id.layout_content);
//		layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
//		showEmptyLayout(true);

        lst_events = (ListView) view.findViewById(R.id.lst_events);
        
        return view;
    }

	@Override
	public void onStart() {
		super.onStart();

//		GetDataFromDB();
		
		// TODO by joseph
		showListTest();
	}

	private void showList() {
		if (_resultAL.size() > 0) {
//			showEmptyLayout(false);
			LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
			lst_events.setAdapter(adapter);

		} else {
//			showEmptyLayout(true);
		}
	}

	private void showListTest() {
		_resultAL.clear();
		
		WAUser user1 = new WAUser();
		WAUser user2 = new WAUser();
		WAUser user3 = new WAUser();
		WAUser user4 = new WAUser();
		_resultAL.add(user1);
		_resultAL.add(user2);
		_resultAL.add(user3);
		_resultAL.add(user4);
//		showEmptyLayout(false);
		LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
		lst_events.setAdapter(adapter);
	}

	private  class LasyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ArrayList<WAUser> list_data = new ArrayList<WAUser>();

		public LasyAdapter(Context context, ArrayList<WAUser> list_data) {
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
			return position;
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
			
			// TODO by joseph
			holder.txt_name.setText("Lana Del Ray");
			holder.txt_coin.setText("450");
			holder.txt_time.setText("12:34 AM  APR 23. 2015 ");
			holder.img_background.setImageResource(R.drawable.row_myevent_back);
        
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO by joseph
					int event_id = 0; // from position
					boolean is_joined = false; // from position
					
					mainActivity.showDetailEventFragment(event_id, is_joined);
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
