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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.view.StaggeredGridView;

@SuppressLint("InflateParams")
public class UpcomingFragment extends Fragment implements BaseFragment.BaseFragmentInterface {

	private StaggeredGridView grid_events;

	// prevent list to scroll to top
	private ArrayList<WAUser> _resultAL = new ArrayList<WAUser>();
	private WAUser mUser = null;
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

		mUser = WAModelManager.getInstance().getSignInUser();

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

	private void showList() {
		if (_resultAL.size() > 0) {
			LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
			grid_events.setAdapter(adapter);

		}
	}

	private void showListTest() {
		_resultAL.clear();
		
		WAUser user1 = new WAUser();
		WAUser user2 = new WAUser();
		WAUser user3 = new WAUser();
		WAUser user4 = new WAUser();
		WAUser user5 = new WAUser();
		
		_resultAL.add(user1);
		_resultAL.add(user2);
		_resultAL.add(user3);
		_resultAL.add(user4);
		_resultAL.add(user5);
		
		LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
		grid_events.setAdapter(adapter);
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
			LinearLayout layout_row, layout_footer;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_upcoming_list, null);
				holder = new ViewHolder();

				holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
				holder.txt_coin = (TextView) convertView.findViewById(R.id.txt_coin);
				holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
				holder.layout_row = (LinearLayout) convertView.findViewById(R.id.layout_row);
				holder.layout_footer = (LinearLayout) convertView.findViewById(R.id.layout_row_footer);
				
				// set font
				holder.txt_name.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, UpcomingFragment.this.getActivity()));
				holder.txt_coin.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, UpcomingFragment.this.getActivity()));
				holder.txt_time.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, UpcomingFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// TODO by joseph
			if ((position % 2) == 0)
			{
				holder.txt_name.setText("Stanley Pauls");
				holder.txt_coin.setText("320");
				holder.txt_time.setText("12:34 AM APR 23, 2015");
				holder.layout_row.setBackgroundResource(R.drawable.row_upcoming_small_back);
				holder.layout_footer.setBackgroundResource(R.drawable.row_upcoming_small_footer);
			}
			else
			{
				holder.txt_name.setText("Hayley Williams");
				holder.txt_coin.setText("320");
				holder.txt_time.setText("12:34 AM APR 23, 2015");
				holder.layout_row.setBackgroundResource(R.drawable.row_upcoming_tall_back);
				holder.layout_footer.setBackgroundResource(R.drawable.row_upcoming_tall_footer);
			}
			
        
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
