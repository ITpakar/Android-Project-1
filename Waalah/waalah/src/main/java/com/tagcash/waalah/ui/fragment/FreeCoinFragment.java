package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.view.HexagonImageView;

public class FreeCoinFragment extends Fragment implements OnClickListener {

	private MainActivity mainActivity;
	
	public ListView lst_freecoins = null;

	private ArrayList<WAUser> _resultAL = new ArrayList<WAUser>();
	
	public FreeCoinFragment(MainActivity activity) {
		super();
		
		mainActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_freecoin, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		img_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.showMenu();
			}
		});
				
		lst_freecoins = (ListView) v.findViewById(R.id.lst_freecoins);

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		super.onStart();

		showListTest();
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
		
		LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
		lst_freecoins.setAdapter(adapter);
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
			TextView txt_free_title, txt_free_type, txt_coins;
			HexagonImageView img_picture;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_freecoin_list, null);
				holder = new ViewHolder();

				holder.txt_free_title = (TextView) convertView.findViewById(R.id.txt_free_title);
				holder.txt_free_type = (TextView) convertView.findViewById(R.id.txt_free_type);
				holder.txt_coins = (TextView) convertView.findViewById(R.id.txt_coins);
				holder.img_picture = (HexagonImageView) convertView.findViewById(R.id.img_picture);
				
				// set font
				holder.txt_free_title.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, FreeCoinFragment.this.getActivity()));
				holder.txt_free_type.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, FreeCoinFragment.this.getActivity()));
				holder.txt_coins.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, FreeCoinFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// TODO by joseph
			holder.txt_free_title.setText("Dove");
			holder.txt_free_type.setText("Task");
			holder.txt_coins.setText("+123");
			holder.img_picture.setImageResource(R.drawable.ic_sample_picture);

			return convertView;
		}
	}
}
