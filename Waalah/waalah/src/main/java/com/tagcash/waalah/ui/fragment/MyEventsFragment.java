package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAEvent;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.DateTimeUtil;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.WAImageLoader;

@SuppressLint("InflateParams")
public class MyEventsFragment extends Fragment implements BaseFragment.BaseFragmentInterface {

	public static MyEventsFragment instance = null;
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

		instance = this;

		View view = inflater.inflate(R.layout.fragment_myevents, null);

		mUser = WAModelManager.getInstance().getSignInUser();

        lst_events = (ListView) view.findViewById(R.id.lst_events);
        
        return view;
    }

	@Override
	public void onStart() {
		super.onStart();

		_resultAL.clear();

		ArrayList<String> strs = new ArrayList<String>();
		strs.add(mUser.api_token);

		BaseTask task = new BaseTask(Constants.TASK_MY_EVENTS);
		task.setListener(mTaskListener);
		task.setData(strs);
		task.execute();
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
			
			holder.txt_name.setText(event.event_name);
			holder.txt_coin.setText(String.valueOf(event.coins_count));
			String format = "hh:mma MMM dd, yyyy";
			holder.txt_time.setText(DateTimeUtil.dateToString(event.event_date, format));
			
			if (TextUtils.isEmpty(event.image_url)) {
				holder.img_background.setImageResource(R.drawable.row_myevent_back);
			}else {
				WAImageLoader.showImage(holder.img_background, event.image_url);
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

	BaseTask.TaskListener mTaskListener = new BaseTask.TaskListener() {

		@SuppressWarnings("unchecked")
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_MY_EVENTS) {
				ArrayList<String> strs = (ArrayList<String>) data;
				result = Server.GetMyEvent(strs.get(0));
				System.out.println(result);
			}
			return result;
		}

		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_MY_EVENTS) {
				if (result != null) {
					if (result instanceof ResponseModel.EventsListModel) {

						//		2015-05-25 05:17:20
						ResponseModel.EventsListModel res_model = (ResponseModel.EventsListModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							ArrayList<ResponseModel.EventModel> events = res_model.events;
							for (int i = 0; i < events.size(); i++) {
								WAEvent event = new WAEvent(events.get(i));
								_resultAL.add(event);
							}

						LasyAdapter adapter = new LasyAdapter(instance.getActivity(), _resultAL);
						instance.lst_events.setAdapter(adapter);
					}
					else {
						// error
						MessageUtil.showMessage("connection failed", false);
					}
					}
					else {
						Log.v(Constants.LOG_TAG, result.toString());
						MessageUtil.showMessage(result.toString(), false);
					}
				}
				else {

				}
			}

//			dlg_progress.hide();
		}

		@Override
		public void onTaskProgress(int taskId, Object... values) {

		}

		@Override
		public void onTaskPrepare(int taskId, Object data) {
//			dlg_progress.show();
		}

		@Override
		public void onTaskCancelled(int taskId) {
//			dlg_progress.hide();
		}
	};

	@Override
	public void manualRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	public void setMainActivity(MainActivity activity) {
		mainActivity = activity;
	}
}
