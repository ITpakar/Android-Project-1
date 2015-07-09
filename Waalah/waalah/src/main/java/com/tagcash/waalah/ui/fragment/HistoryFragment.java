package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.tagcash.waalah.ui.activity.HistoryDetailActivity;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.DateTimeUtil;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.WAImageLoader;


@SuppressLint("InflateParams")
public class HistoryFragment extends Fragment implements BaseFragment.BaseFragmentInterface {

	public static HistoryFragment instance = null;
	public ListView lst_history = null;

	// prevent list to scroll to top
	private ArrayList<WAEvent> _resultAL = new ArrayList<WAEvent>();
	private WAUser mUser = null;
	private MainActivity mainActivity;

	public static HistoryFragment newInstance() {
		HistoryFragment fragment = new HistoryFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		instance = this;
		View view = inflater.inflate(R.layout.fragment_history, null);

		mUser = WAModelManager.getInstance().getSignInUser();
//		layout_content = (LinearLayout) view.findViewById(R.id.layout_content);
//		layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
//		showEmptyLayout(true);

        lst_history = (ListView) view.findViewById(R.id.lst_history);
        
        return view;
    }

	@Override
	public void onStart() {
		super.onStart();

//		GetDataFromDB();
		_resultAL.clear();

		ArrayList<String> strs = new ArrayList<String>();
		strs.add(mUser.api_token);

		BaseTask task = new BaseTask(Constants.TASK_LIST_EVENTS);
		task.setListener(mTaskListener);
		task.setData(strs);
		task.execute();
	}

	private void showList() {
		if (_resultAL.size() > 0) {
//			showEmptyLayout(false);
			LasyAdapter adapter = new LasyAdapter(this.getActivity(), _resultAL);
			lst_history.setAdapter(adapter);

		} else {
//			showEmptyLayout(true);
		}
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
			TextView txt_name, txt_won, txt_time;
			ImageView img_won;
			ImageView img_background;
			LinearLayout layout_name, layout_result;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_history_list, null);
				holder = new ViewHolder();

				holder.img_won = (ImageView) convertView.findViewById(R.id.img_won);
				holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
				holder.txt_won = (TextView) convertView.findViewById(R.id.txt_won);
				holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
				holder.img_background = (ImageView) convertView.findViewById(R.id.img_background);
				
				holder.layout_name = (LinearLayout) convertView.findViewById(R.id.layout_name);
				holder.layout_result = (LinearLayout) convertView.findViewById(R.id.layout_result);
				
				// set font
				holder.txt_name.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, HistoryFragment.this.getActivity()));
				holder.txt_won.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, HistoryFragment.this.getActivity()));
				holder.txt_time.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, HistoryFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// TODO by joseph
			if ((position % 2) == 0)
			{
				holder.img_won.setVisibility(View.VISIBLE);
				holder.txt_won.setText("WON");
			}
			else
			{
				holder.img_won.setVisibility(View.GONE);
				holder.txt_won.setText("5th");
			}

			final WAEvent event = (WAEvent) getItem(position);

			holder.txt_name.setText(event.event_name);
			String format = "hh:mma MMM dd, yyyy";
			holder.txt_time.setText(DateTimeUtil.dateToString(event.event_date, format));

			holder.img_background.setImageResource(R.drawable.row_history_back);

			if (TextUtils.isEmpty(event.thumb_url) && ((position % 5) % 2) == 0) {
				holder.img_background.setImageResource(R.drawable.row_history_back);
			} else if (TextUtils.isEmpty(event.thumb_url) && ((position % 5) % 2) == 1) {
				holder.img_background.setImageResource(R.drawable.row_history_back);
			} else {
				WAImageLoader.showImage(holder.img_background, event.thumb_url);
			}
        
			holder.layout_name.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					AlertDialog alertDialog = new AlertDialog.Builder(HistoryFragment.this.getActivity()).create();
					alertDialog.setTitle("Alert");
					alertDialog.setMessage("Video call is Coming soon ");
					alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            dialog.dismiss();
					        }
					    });
					alertDialog.show();					
				}
			});
			
			holder.layout_result.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(HistoryFragment.this.getActivity(), HistoryDetailActivity.class);
					startActivity(intent);
					
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
			if (taskId == Constants.TASK_LIST_EVENTS) {
				ArrayList<String> strs = (ArrayList<String>) data;
				result = Server.GetEventHistory(strs.get(0));
				System.out.println(result);
			}
			return result;
		}

		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_LIST_EVENTS) {
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
							instance.lst_history.setAdapter(adapter);
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
