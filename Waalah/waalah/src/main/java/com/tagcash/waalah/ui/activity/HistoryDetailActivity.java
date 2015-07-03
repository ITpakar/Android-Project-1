package com.tagcash.waalah.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.view.HexagonImageView;

public class HistoryDetailActivity extends Activity {

	private ImageView img_back;
	private ListView lst_history;
	private TextView txt_count;
	
	// prevent list to scroll to top
	private ArrayList<WAUser> _resultAL = new ArrayList<WAUser>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail_history);
	
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish ();
			}
		});
		
		
		lst_history = (ListView) findViewById(R.id.lst_detail_history);
		txt_count = (TextView) findViewById(R.id.txt_count);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// TODO by joseph
		showListTest();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		finish();
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

		LasyAdapter adapter = new LasyAdapter(this, _resultAL);
		lst_history.setAdapter(adapter);
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
			TextView txt_number, txt_name, txt_location, txt_time;
			ImageView img_call, img_picture;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_detail_history, null);
				holder = new ViewHolder();

				holder.txt_number = (TextView) convertView.findViewById(R.id.txt_number);
				holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
				holder.txt_location = (TextView) convertView.findViewById(R.id.txt_location);
				holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
				holder.img_picture = (HexagonImageView) convertView.findViewById(R.id.img_picture);
				holder.img_call = (ImageView) convertView.findViewById(R.id.img_call);
				
				// set font
				holder.txt_number.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, HistoryDetailActivity.this));
				holder.txt_name.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, HistoryDetailActivity.this));
				holder.txt_location.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, HistoryDetailActivity.this));
				holder.txt_time.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, HistoryDetailActivity.this));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// TODO by joseph
			holder.txt_number.setText(String.valueOf(position + 1));
			holder.txt_name.setText("Emily Green");
			holder.txt_location.setText("LA, USA");
			holder.txt_time.setText("11:34 AM");
			
			holder.img_picture.setImageResource(R.drawable.ic_sample_picture);
			
			if (position > 1)
				holder.img_call.setImageResource(R.drawable.ic_call);
			else
				holder.img_call.setImageResource(R.drawable.ic_call_store);
			
			return convertView;
		}
	}
}
