package com.tagcash.waalah.ui.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.tagcash.waalah.R;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.model.WAEvent;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.fragment.DetailEventFragment;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.view.HexagonImageView;

public class MyEventsDetailActivity extends Activity {

	private ImageView img_back;
	private ListView lst_history;

	// prevent list to scroll to top
	private ArrayList<WAUser> list_data = new ArrayList<WAUser>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_detail_myevents);

		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		lst_history = (ListView) findViewById(R.id.lst_detail_history);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// TODO by joseph
		showListTest();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

	private void showListTest() {
		list_data.clear();

		WAUser user1 = new WAUser();
		WAUser user2 = new WAUser();
		WAUser user3 = new WAUser();
		WAUser user4 = new WAUser();
		list_data.add(user1);
		list_data.add(user2);
		list_data.add(user3);
		list_data.add(user4);

		LasyAdapter adapter = new LasyAdapter(this);
		lst_history.setAdapter(adapter);
	}

	private class LasyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public LasyAdapter(Context context) {
			if (context == null)
				context = WAApplication.getContext();
			mInflater = LayoutInflater.from(context);
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
			TextView txt_number, txt_name, txt_location, txt_time;
			ImageView img_call, img_picture;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_detail_myevents,
						null);
				holder = new ViewHolder();

				holder.txt_number = (TextView) convertView
						.findViewById(R.id.txt_number);
				holder.txt_name = (TextView) convertView
						.findViewById(R.id.txt_name);
				holder.txt_location = (TextView) convertView
						.findViewById(R.id.txt_location);
				holder.txt_time = (TextView) convertView
						.findViewById(R.id.txt_time);
				holder.img_picture = (HexagonImageView) convertView
						.findViewById(R.id.img_picture);
				holder.img_call = (ImageView) convertView
						.findViewById(R.id.img_call);

				// set font
				holder.txt_number.setTypeface(WAFontProvider
						.getFont(WAFontProvider.GOTHAM_BOLD,
								MyEventsDetailActivity.this));
				holder.txt_name.setTypeface(WAFontProvider.getFont(
						WAFontProvider.HELVETICA_NEUE,
						MyEventsDetailActivity.this));
				holder.txt_location.setTypeface(WAFontProvider
						.getFont(WAFontProvider.GOTHAM_BOLD,
								MyEventsDetailActivity.this));
				holder.txt_time.setTypeface(WAFontProvider
						.getFont(WAFontProvider.GOTHAM_BOLD,
								MyEventsDetailActivity.this));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final WAUser user = (WAUser)list_data.get(position);
			
			// TODO by joseph
			holder.txt_number.setText(String.valueOf(position + 1));
			holder.txt_name.setText("Emily Green");
			holder.txt_location.setText("LA, USA");
			holder.txt_time.setText("11:34 AM");

			holder.img_picture.setImageResource(R.drawable.ic_sample_picture);
			holder.img_call.setImageResource(R.drawable.ic_call);
			holder.img_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					QBRTCTypes.QBConferenceType qbConferenceType = null;

					qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

					Map<String, String> userInfo = new HashMap<String, String>();
					userInfo.put("any_custom_data", "some data");
					userInfo.put("my_avatar_url", "avatar_reference");

					Intent returnIntent = new Intent();
					returnIntent.putExtra(DetailEventFragment.REQUEST_VALUE_USER_ID, "1");
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			});
			
			

			return convertView;
		}
	}
}
