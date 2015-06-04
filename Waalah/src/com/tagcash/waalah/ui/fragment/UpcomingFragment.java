package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.database.DBConstant;
import com.tagcash.waalah.database.DBManager;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.util.DateTimeUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.WAImageLoader;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.view.DragListView;
import com.tagcash.waalah.view.DragListView.OnRefreshLoadingMoreListener;
import com.tagcash.waalah.view.SquareImageView;

@SuppressLint("InflateParams")
public class UpcomingFragment extends BaseFragment implements OnRefreshLoadingMoreListener, BaseFragment.BaseFragmentInterface {

	public DragListView lst_people = null;
	private View btn_refresh;

	// prevent list to scroll to top
	private int page_index;
	private int old_index = 0;
	private int old_top = 0;
	private ArrayList<WAUser> _resultAL = new ArrayList<WAUser>();
	private ArrayList<WAUser> _showAL = new ArrayList<WAUser>();
	private WAUser mUser = null;

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
		layout_content = (LinearLayout) view.findViewById(R.id.layout_content);
		layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
		showEmptyLayout(true);

//        final EditText edt_search = (EditText) view.findViewById(R.id.edt_search);
//		edt_search.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) { }
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//			@Override
//			public void afterTextChanged(Editable s) {
//				try {
//					_showAL.clear();
//					if ((edt_search.getText().toString()).length() == 0) {
//						_showAL.addAll(_resultAL);
//
//					} else {
//						for (int i = 0; i < _resultAL.size(); i++) {
//							if (_resultAL.get(i).fullname.toString().toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
//								WAUser item = new WAUser(_resultAL.get(i));
//								_showAL.add(item);
//							}
//						}
//					}
//					LasyAdapter adapter = new LasyAdapter(UpcomingFragment.this.getActivity(), _showAL);
//					lst_people.setAdapter(adapter);
//					lst_people.invalidate();
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
        lst_people = (DragListView) view.findViewById(R.id.lst_people);
        lst_people.setOnRefreshListener(this);
        
		// empty layout
		btn_refresh = (ImageView) view.findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GetDataFromDB();
			}
		});

		// set font
//		edt_search.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this.getActivity()));

		return view;
    }

	@Override
	public void onStart() {
		super.onStart();

		GetDataFromDB();
	}

	private void GetDataFromDB() {
		_resultAL = DBManager.getInstance().getAllUserByType(DBConstant.TYPE_FAVORITE);

		if (_resultAL != null && _resultAL.size() > 0) {
			showList();

			showEmptyLayout(false);

			// first refresh
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lst_people.refresh();
				}
			}, Constants.FRIST_REFRESH_TASK_DELAY_TIME);

		} else {
			GetDataFromServer(Constants.REFRESH_HEADER_TASK);
		}
	}

	private void GetDataFromServer(int task_type) {
		if (task_type == Constants.REFRESH_HEADER_TASK) {
			page_index = 0;

		} else {
			page_index++;
		}

		BaseTask task = new BaseTask(Constants.TASK_USER_GETLIST);
		task.setListener(mTaskListener);
		task.execute();
	}

	private void showList() {
		if (_resultAL.size() > 0) {
			showEmptyLayout(false);
			_showAL.clear();
			_showAL.addAll(_resultAL);
			LasyAdapter adapter = new LasyAdapter(this.getActivity(), _showAL);
			lst_people.setAdapter(adapter);

		} else {
			showEmptyLayout(true);
		}
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
			int retSize = nSize / 2;
			if (nSize % 2 == 1)
				retSize ++;
			return retSize;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			LinearLayout layer_left;
			SquareImageView img_user_avatar_left;
			ImageView img_status_left;
			TextView txt_user_name_left;
			TextView txt_age_left;

			LinearLayout layer_right;
			SquareImageView img_user_avatar_right;
			ImageView img_status_right;
			TextView txt_user_name_right;
			TextView txt_age_right;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_people_list, null);
				holder = new ViewHolder();
				holder.layer_left = (LinearLayout) convertView.findViewById(R.id.layer_left);
				holder.img_user_avatar_left = (SquareImageView) convertView.findViewById(R.id.img_user_avatar_left);
				holder.img_status_left = (ImageView) convertView.findViewById(R.id.img_status_left);
				holder.txt_user_name_left = (TextView) convertView.findViewById(R.id.txt_user_name_left);
				holder.txt_age_left = (TextView) convertView.findViewById(R.id.txt_age_left);
				holder.layer_right = (LinearLayout) convertView.findViewById(R.id.layer_right);
				holder.img_user_avatar_right = (SquareImageView) convertView.findViewById(R.id.img_user_avatar_right);
				holder.img_status_right = (ImageView) convertView.findViewById(R.id.img_status_right);
				holder.txt_user_name_right = (TextView) convertView.findViewById(R.id.txt_user_name_right);
				holder.txt_age_right = (TextView) convertView.findViewById(R.id.txt_age_right);
				
				// set font
				holder.txt_user_name_left.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, UpcomingFragment.this.getActivity()));
				holder.txt_age_left.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, UpcomingFragment.this.getActivity()));
				((TextView)convertView.findViewById(R.id.txt_line0_left)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, UpcomingFragment.this.getActivity()));
				holder.txt_user_name_right.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, UpcomingFragment.this.getActivity()));
				holder.txt_age_right.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, UpcomingFragment.this.getActivity()));
				((TextView)convertView.findViewById(R.id.txt_line0_right)).setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, UpcomingFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// left layout
			final WAUser tmpUser_left = list_data.get(position * 2);
			holder.layer_left.setClickable(true);
			holder.layer_left.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					Intent intent = new Intent(UpcomingFragment.this.getActivity(), ProfileActivity.class);
//					intent.putExtra(ProfileActivity.PROFILE_ACTIVITY_USER_ID, tmpUser_left.user_id);
//					intent.putExtra(ProfileActivity.PROFILE_ACTIVITY_USER_TYPE, tmpUser_left.type);
//					startActivity(intent);
//					UpcomingFragment.this.getActivity().overridePendingTransition(R.anim.in_up, R.anim.none);
				}
			});
			
			if (TextUtils.isEmpty(tmpUser_left.picture_url)) {
				if (tmpUser_left.gender.equalsIgnoreCase(Constants.GENDER.sFemale))
					holder.img_user_avatar_left.setImageDrawable(getResources().getDrawable(R.drawable.female));
				else
					holder.img_user_avatar_left.setImageDrawable(getResources().getDrawable(R.drawable.male));
			}
			else
				WAImageLoader.showImage(holder.img_user_avatar_left, tmpUser_left.picture_url, tmpUser_left.gender);
			if (tmpUser_left.online > 0) {
				holder.img_status_left.setImageResource(R.drawable.ic_online);
			} else {
				holder.img_status_left.setImageResource(R.drawable.ic_offline);
			}
			if (!TextUtils.isEmpty(tmpUser_left.fullname))
				holder.txt_user_name_left.setText(tmpUser_left.fullname);
			else
				holder.txt_user_name_left.setText(tmpUser_left.login);
			holder.txt_age_left.setText(DateTimeUtil.getAgeFromDateString(tmpUser_left.birthday, "yyyy-MM-dd") + "");
			
			// right layout
			if (list_data.size() > (position * 2 + 1)) {
				holder.layer_right.setVisibility(View.VISIBLE);
				final WAUser tmpUser_right = list_data.get(position * 2 + 1);
				holder.layer_right.setClickable(true);
				holder.layer_right.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
//						Intent intent = new Intent(UpcomingFragment.this.getActivity(), ProfileActivity.class);
//						intent.putExtra(ProfileActivity.PROFILE_ACTIVITY_USER_ID, tmpUser_right.user_id);
//						intent.putExtra(ProfileActivity.PROFILE_ACTIVITY_USER_TYPE, tmpUser_right.type);
//						startActivity(intent);
//						UpcomingFragment.this.getActivity().overridePendingTransition(R.anim.in_up, R.anim.none);
					}
				});
				
				if (TextUtils.isEmpty(tmpUser_right.picture_url)) {
					if (tmpUser_right.gender.equalsIgnoreCase(Constants.GENDER.sFemale))
						holder.img_user_avatar_right.setImageDrawable(getResources().getDrawable(R.drawable.female));
					else
						holder.img_user_avatar_right.setImageDrawable(getResources().getDrawable(R.drawable.male));
				}
				else
					WAImageLoader.showImage(holder.img_user_avatar_right, tmpUser_right.picture_url, tmpUser_right.gender);
				if (tmpUser_right.online > 0) {
					holder.img_status_right.setImageResource(R.drawable.ic_online);
				} else {
					holder.img_status_right.setImageResource(R.drawable.ic_offline);
				}
				if (!TextUtils.isEmpty(tmpUser_right.fullname))
					holder.txt_user_name_right.setText(tmpUser_right.fullname);
				else
					holder.txt_user_name_right.setText(tmpUser_right.login);
				holder.txt_age_right.setText(DateTimeUtil.getAgeFromDateString(tmpUser_right.birthday, "yyyy-MM-dd") + "");
			}
			else {
				holder.layer_right.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}
	}

	@Override
	public void onRefresh() {
		GetDataFromServer(Constants.REFRESH_HEADER_TASK);
	}

	@Override
	public void onLoadMore() {
		old_index = lst_people.getFirstVisiblePosition();
		View v = lst_people.getChildAt(0);
		old_top = (v == null) ? 0 : v.getTop();

		GetDataFromServer(Constants.REFRESH_FOOTER_TASK);
	}
	
	TaskListener mTaskListener = new TaskListener() {
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_GETLIST) {
				// TODO by joseph
//				result = Server.GetUserList(mUser.token, DBConstant.TYPE_FAVORITE, page_index * Constants.USER_COUNT_PER_ONE_TIME, Constants.USER_COUNT_PER_ONE_TIME);
			}
			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_USER_GETLIST) {
				lst_people.onRefreshComplete();
				if (result != null) {
					if (result instanceof ResponseModel.UserModelList) {
						ResponseModel.UserModelList res_model = (ResponseModel.UserModelList) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							if (page_index == 0) {
								DBManager.getInstance().deleteAllUserByType(DBConstant.TYPE_FAVORITE);
								lst_people.onLoadMoreComplete(false);
							}
							_resultAL.clear();
							
							int nCount = res_model.member_list.size();
							if (nCount > 0) {
								for (int i = 0; i < res_model.member_list.size(); i ++) {
									ResponseModel.UserModel user = res_model.member_list.get(i);
									WAUser huser = new WAUser();
									huser.user_id = user.uid;
									huser.online = user.online;
									huser.token = user.token;
									huser.email = user.email;
									huser.login = user.name;
									huser.password = user.password;
									huser.birthday = user.birthday;
									huser.fullname = user.full_name;
									huser.gender = WAUser.iGengerToSGender(Integer.parseInt(user.gender));
									huser.hometown = user.address;
									if (!TextUtils.isEmpty(user.picture_url))
										huser.picture_url = user.picture_url;
									else {
										if (!TextUtils.isEmpty(user.social_picture_url))
											huser.picture_url = user.social_picture_url;
									}
									huser.health_topics_array = user.health_topic_array;
									huser.diagnosed_with_array = user.diagnosed_with_array;
									huser.diagnosed_with_privacy = user.diagnosed_with_privacy;
									huser.medicated_array = user.medicated_array;
									huser.medicated_privacy = user.medicated_privacy;
									huser.like_count = user.like_count;
									huser.lid = user.lid;
									huser.about = user.about;
									huser.type = DBConstant.TYPE_FAVORITE;
									
									_resultAL.add(huser);
								}
								// write to database
								DBManager.getInstance().addAllUser(_resultAL);
								
								if (nCount < Constants.USER_COUNT_PER_ONE_TIME)
									lst_people.onLoadMoreComplete(true);
							}
							else {
								lst_people.onLoadMoreComplete(true);
							}
							_resultAL = DBManager.getInstance().getAllUserByType(DBConstant.TYPE_FAVORITE);

							// show
							showList();
							
							if (page_index != 0)
								lst_people.setSelectionFromTop(old_index, old_top);
						}
						else
							MessageUtil.showMessage(res_model.msg, false);
					}
					else
						MessageUtil.showMessage(result.toString(), false);
				}
				else {
					
				}
			}
		}
		
		@Override
		public void onTaskProgress(int taskId, Object... values) {
			
		}
		
		@Override
		public void onTaskPrepare(int taskId, Object data) {
			
		}
		
		@Override
		public void onTaskCancelled(int taskId) {
			
		}
	};

	@Override
	public void manualRefresh() {
		GetDataFromDB();
	}
}
