package com.tagcash.waalah.ui.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.SmackException;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnection.IceServer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientCallback;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import com.tagcash.waalah.R;
import com.tagcash.waalah.app.AppPreferences;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.base.BaseTask;
import com.tagcash.waalah.base.BaseTask.TaskListener;
import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.http.Server;
import com.tagcash.waalah.model.WAEvent;
import com.tagcash.waalah.model.WAModelManager;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.fragment.AddCoinFragment;
import com.tagcash.waalah.ui.fragment.ConversationFragment;
import com.tagcash.waalah.ui.fragment.DetailEventFragment;
import com.tagcash.waalah.ui.fragment.FreeCoinFragment;
import com.tagcash.waalah.ui.fragment.IncomeCallFragment;
import com.tagcash.waalah.ui.fragment.MainFragment;
import com.tagcash.waalah.ui.fragment.PurchaseFragment;
import com.tagcash.waalah.util.FacebookUtils;
import com.tagcash.waalah.util.MessageUtil;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.util.WAImageLoader;
import com.tagcash.waalah.util.WAPreferenceManager;

public class MainActivity extends FragmentActivity implements Callback, QBRTCClientCallback {

	private static String TAG = "MainActivity";
	public static MainActivity instance = null;
	public static int REQUEST_MESSAGEACTIVITY_CODE = 1000;
	
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "1077342770386";
    public static String regid = "";
	/*
	 * UI
	 */
	private DrawerLayout parent_drawer_layout;
	private LinearLayout child_drawer_layout;
	public ImageView img_user_avatar;
	public TextView txt_username, txt_location;
	private ListView lst_menu_item;
	private ActionBarDrawerToggle mDrawerToggle;

	public static boolean isBackPressed = false;
//	private TimerTask mSetOnlineTask;
	
	private Handler mHandler = null;

	/*
	 * Data
	 */
	public static boolean isLoginSuccesed = false;
	private int mCurrentFragmentIndex = -1;
	private Fragment mCurrentFragment = null;
	private WAUser mUser;
	private LasyAdapter mLeftMenuAdapter;
	
	private CharSequence mTitle;
	private class LeftMenuItem {
		public int icon_resource_id;
		public String title;
		public LeftMenuItem(int id, String t) {
			icon_resource_id = id;
			title = t;
		}
	}
	private ArrayList<LeftMenuItem> left_menu_list_data = new ArrayList<MainActivity.LeftMenuItem>();
	public AppPreferences prefs;


    public static final String CONFERENCE_TYPE = "conference_type";
    public static final String OPPONENTS_CALL_FRAGMENT = "opponents_call_fragment";
    public static final String INCOME_CALL_FRAGMENT = "income_call_fragment";
    public static final String CONVERSATION_CALL_FRAGMENT = "conversation_call_fragment";
    public static final String CALLER_NAME = "caller_name";
    private static final java.lang.String ADD_OPPONENTS_FRAGMENT_HANDLER = "opponentHandlerTask";
    private static final long TIME_BEGORE_CLOSE_CONVERSATION_FRAGMENT = 3;
    private static final java.lang.String INCOME_WINDOW_SHOW_TASK = "INCOME_WINDOW_SHOW";
    private static VideoRenderer.Callbacks REMOTE_RENDERER;
    private static VideoRenderer.Callbacks LOCAL_RENDERER;

    private int selectedOpponentId;

    private static QBChatService chatService;
    public static final String START_CONVERSATION_REASON = "start_conversation_reason";
    public static final String SESSION_ID = "sessionID";
    private QBRTCVideoTrack localVideoTrack;
    private String currentSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prefs = new AppPreferences(this);

//		Date curdt = new Date();
//		Date checkdt = new Date(115, 2, 31); // 2015-3-31
//		if (curdt.compareTo(checkdt) > 0)
//			finish();

		instance = this;
		isLoginSuccesed = false;

		left_menu_list_data.add(new LeftMenuItem(R.drawable.ic_waalah, "Waalah Events"));
		left_menu_list_data.add(new LeftMenuItem(R.drawable.ic_addcoin, "Earn Coins"));
		left_menu_list_data.add(new LeftMenuItem(R.drawable.ic_invite, "Invite Friends"));
		left_menu_list_data.add(new LeftMenuItem(R.drawable.ic_setting, "Settings"));
		left_menu_list_data.add(new LeftMenuItem(R.drawable.ic_help, "Help & Info"));

		mTitle = getTitle();

		parent_drawer_layout = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
		parent_drawer_layout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		child_drawer_layout = (LinearLayout) findViewById(R.id.child_drawer_layout);

		img_user_avatar = (ImageView)findViewById(R.id.img_user_avatar);
		txt_username = (TextView)findViewById(R.id.txt_username);
		txt_location = (TextView)findViewById(R.id.txt_location);
		lst_menu_item = (ListView)findViewById(R.id.lst_menu_item);

		// set a custom shadow that overlays the main content when the drawer opens
		// set up the drawer's list view with items and click listener
		mLeftMenuAdapter = new LasyAdapter(this);
		lst_menu_item.setAdapter(mLeftMenuAdapter);
		lst_menu_item.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  		/* host Activity */
				parent_drawer_layout,		/* DrawerLayout object */
				R.drawable.ic_drawer,		/* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,		/* "open drawer" description for accessibility */
				R.string.drawer_close		/* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
//				titlebar_txt_title.setText(mTitle);
//				if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_PROFILE) {
//					titlebar_btn_edit.setVisibility(View.VISIBLE);
//				}
//				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
//				titlebar_txt_title.setText(getString(R.string.app_name));
//				titlebar_btn_edit.setVisibility(View.GONE);
//				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		parent_drawer_layout.setDrawerListener(mDrawerToggle);

		// set font
		txt_username.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, this));
		txt_location.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, this));

		if (mHandler == null)
			mHandler = new Handler(this);

		FacebookUtils.init(this);
		
		// register broadcastreceiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_LOGIN_SUCCESS);
		filter.addAction(Constants.ACTION_SWITCHTO_PROFILE);
		filter.addAction(Constants.ACTION_NEW_CHATMESSAGE);
		registerReceiver(mReceiver, filter);

	    initData();
	    
	    //WAUser logUser = WAModelManager.getInstance().getSignInUser();
		SwitchContent(Constants.SW_FRAGMENT_WAALAH);
	    
	    // tmp make db file
		//SqliteUtil.exportSQLite(MainActivity.this, "healthchat.db", "healthchat.db");


        // Quickblox
        QBSettings.getInstance().fastConfigInit(Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);

        if (!QBChatService.isInitialized()) {
            QBChatService.init(this);
            chatService = QBChatService.getInstance();
        }
	}
	
   
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();

		int rotation = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();

		if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) { 
			WAApplication.mBPortrait = true;
		} else { // landscape
			WAApplication.mBPortrait = false;
		}
		
//		if (mBFromNotification && !mBFirstCreate) {
//			mBFromNotification = false;
//			Intent intent = new Intent(this, MessageActivity.class);
//			intent.putExtra(ProfileActivity.PROFILE_ACTIVITY_USER_ID, mFriendId);
//			intent.putExtra(ProfileActivity.PROFILE_ACTIVITY_USER_TYPE, DBConstant.TYPE_ALL);
//			startActivity(intent);
//			overridePendingTransition(R.anim.fliping_in, R.anim.none);
//		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_MESSAGEACTIVITY_CODE) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					showMainUI();
				}
			});
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
			FacebookUtils.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
//		if (titlebar_txt_title != null) {
//			titlebar_txt_title.setText(mTitle);
//		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
		try {
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				WAApplication.mBPortrait = false;
			} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				WAApplication.mBPortrait = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
//		stopService(new Intent(MainActivity.this, KeepLoginService.class));
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		// TODO by joseph
		mUser = WAModelManager.getInstance().getSignInUser();
		if (mUser == null)
			showLoginPage();
		
		createSession(mUser.username, mUser.password);
	}

	@Override
	public void onStop() {
		super.onStop();
		isBackAllowed = false;
	}

	@Override
	public void onDestroy() {
    	unregisterReceiver(mReceiver);
		super.onDestroy();

		if (QBChatService.isInitialized()) {
			QBRTCClient.getInstance().close();
		}
		
		System.out.println(" ######### MainActivity onDestroy ######### ");
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
	}

	boolean isBackAllowed = false;
	@Override
	public void onBackPressed() {
		if (this instanceof MainActivity) {

		} else {
			isBackPressed = true;
		}

		if (child_drawer_layout.isShown()) {
			parent_drawer_layout.closeDrawer(child_drawer_layout);
			return;
		}

		if(!isBackAllowed) {
			MessageUtil.showMessage(getResources().getString(R.string.press_back_alert), false);
			isBackAllowed = true;

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isBackAllowed = false;
				}
			}, 5000);

		} else {
			finish();
			
            if (QBChatService.isInitialized()) {
                try {
                    QBRTCClient.getInstance().close();
                    QBChatService.getInstance().logout();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
		}
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equalsIgnoreCase(Constants.ACTION_LOGIN_SUCCESS)) {
    			new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
		    			initData();
					}
				}, 2000);
    		}
    		else if (action.equalsIgnoreCase(Constants.ACTION_NEW_CHATMESSAGE)) {
    			new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if (mUser != null) {
//							ArrayList<ChatMessage> newMsgs = DBManager.getInstance().getAllRecentChatPerFriendOne(mUser.user_id, true);
//							int nCount = newMsgs.size();
//							left_menu_list_data.get(2).count = nCount;	// Chat message menu
//							if (mLeftMenuAdapter != null)
//								mLeftMenuAdapter.notifyDataSetChanged();
//							
//							if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_INBOX) {
//								((InboxFragment)mCurrentFragment).refreshList();
//							}
						}
					}
				}, 500);
    		}
//    		else if (action.equalsIgnoreCase(Constants.ACTION_SWITCHTO_PROFILE)) {
//    			new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//		    			SwitchContent(Constants.SW_FRAGMENT_PROFILE);
//					}
//				}, 500);
//    		}
		}
	};

	public void initData() {
		// set flag
		isLoginSuccesed = true;
		
		mUser = WAModelManager.getInstance().getSignInUser();
		if (mUser != null) {
			showMainUI();

//			if (mCheckServerTimer == null) {
//				mCheckServerTimer = new Timer();
//				mCheckServerTimer.schedule(mSetOnlineTask, 0, Constants.SERVER_CHECK_TIME);
//			}
		}
	}

	public void showMainUI() {
		// show user's info on the left drawer
		mUser = WAModelManager.getInstance().getSignInUser();
		if (mUser != null) {
			if (TextUtils.isEmpty(mUser.picture_url)) 
				img_user_avatar.setImageDrawable(getResources().getDrawable(R.drawable.female));
			else
				WAImageLoader.showImage(img_user_avatar, mUser.picture_url, "Male");

			txt_username.setText(mUser.firstname + " " + mUser.lastname);
			txt_location.setText(mUser.city + ", " + mUser.country);
			
		}

		// first show health feed UI
		SwitchContent(Constants.SW_FRAGMENT_WAALAH);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (Constants.SW_FRAGMENT_INVITE == position)
			{
				// update selected item and title, then close the drawer
				if (child_drawer_layout.isShown())
					parent_drawer_layout.closeDrawer(child_drawer_layout);
				
				
			    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
			    intent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.app_name));
			    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
			    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.strShareUrl));
			    intent.setType("*/*");
			    startActivity(Intent.createChooser(intent, ""));	
			}
			else
			{
				SwitchContent(position);
			}
		}
	}

	public void SwitchContent(int fragment_index) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		if (mCurrentFragmentIndex != fragment_index) {
			mCurrentFragmentIndex = fragment_index;

			if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_WAALAH) {
				fragment = new MainFragment(this);
				lst_menu_item.setItemChecked(0, true);
			} else if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_ADDCOIN) {
				fragment = new AddCoinFragment(this);
				lst_menu_item.setItemChecked(1, true);
	
			} else if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_INVITE) {
				fragment = new MainFragment(this);
				lst_menu_item.setItemChecked(2, true);
	
			} else if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_SETTING) {
				fragment = new MainFragment(this);
				lst_menu_item.setItemChecked(3, true);
	
			} else if (mCurrentFragmentIndex == Constants.SW_FRAGMENT_HELP) {
				fragment = new MainFragment(this);
				lst_menu_item.setItemChecked(4, true);
			}

			mCurrentFragment = fragment;
			if (fragment != null) {
				try {
					FragmentManager fragmentManager = this.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		// update selected item and title, then close the drawer
		if (child_drawer_layout.isShown())
			parent_drawer_layout.closeDrawer(child_drawer_layout);
	}

	private void LoginWithLastAccount() {
		String user_login = WAPreferenceManager.getString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_LOGIN, "");
		String user_email = WAPreferenceManager.getString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_EMAIL, "");
		String user_password = WAPreferenceManager.getString(WAPreferenceManager.PreferenceKeys.STRING_LAST_SINGED_USER_PASSWORD, "");

		// initialize social information
//		TwitterUtils.initInformations(prefs);
		
		if (!TextUtils.isEmpty(user_login) && !TextUtils.isEmpty(user_password)) {
			if (user_login.contains(Constants.ID_PREVFIX.FACEBOOK)) {
				Intent intent = new Intent(this, LoginWithFacebookActivity.class);
				intent.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
				startActivity(intent);
//			} else if (user_login.contains(Constants.ID_PREVFIX.GOOGLE)) {
//				Intent intent = new Intent(this, LoginWithGoolgeActivity.class);
//				intent.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
//				startActivity(intent);
//			} else if (user_login.contains(Constants.ID_PREVFIX.TWITTER)) {
//				Intent intent = new Intent(this, LoginWithTwitterActivity.class);
//				intent.putExtra(Constants.KEY_FLAG, Constants.MODE_LOGIN);
//				startActivity(intent);
			} else {
				String strId = !user_email.isEmpty() ? user_email : user_login;
				LoginToServer(strId, user_password);
			}
		} else {
			showLoginPage();
		}
	}

	private void showLoginPage() {
		Intent intent = new Intent(this, LoginWithEmailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_right, R.anim.none);
	}

	private void signOut() {
		// close drawer
		parent_drawer_layout.closeDrawer(child_drawer_layout);
		// clear cache
		WAApplication.clearCredentialsCache();
		mCurrentFragmentIndex = -1;
		mCurrentFragment = null;

		if (isLoginSuccesed) {
			// User sign out successful
			showLoginPage();
		} else {
			MessageUtil.showMessage("You didnot logined", true);
			showLoginPage();
		}
	}

	public void showSessionTimeoutWarningDlg() {
		new AlertDialog.Builder(this)
		.setTitle("Session Time out")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage("Sorry, Connection closed due to time out, Login again failed.")
		.setPositiveButton("Login Again", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.instance.finish();
			}
		}).show();
	}
	
	public void finishAllActivity() {
		if (LoginWithEmailActivity.instance != null)
			LoginWithEmailActivity.instance.finish();
		if (SignUpActivity.instance != null)
			SignUpActivity.instance.finish();
	}

	private  class LasyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public LasyAdapter(Context context) {
			if (context == null)
				context = WAApplication.getContext();
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return left_menu_list_data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			ImageView img_icon;
			TextView txt_title;
			ImageView img_toward;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_left_drawer, null);
				holder = new ViewHolder();
				holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
				holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
				holder.img_toward = (ImageView) convertView.findViewById(R.id.img_toward);

				// set font
				holder.txt_title.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, MainActivity.this));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img_icon.setImageResource(left_menu_list_data.get(position).icon_resource_id);
			holder.txt_title.setText("" + left_menu_list_data.get(position).title);
			holder.img_toward.setImageResource(R.drawable.ic_toward);

			return convertView;
		}
	}

	private void LoginToServer(String strId, String strPwd) {
		ArrayList<String> strs = new ArrayList<String>();
		strs.add(strId);
		strs.add(strPwd);
		
		BaseTask loginTask = new BaseTask(Constants.TASK_USER_LOGIN);
		loginTask.setListener(mTaskListener);
		loginTask.setData(strs);
		loginTask.execute();
	}

	TaskListener mTaskListener = new TaskListener() {
		@Override
		public Object onTaskRunning(int taskId, Object data) {
			Object result = null;
			if (taskId == Constants.TASK_USER_LOGIN) {
				@SuppressWarnings("unchecked")
				ArrayList<String> strs = (ArrayList<String>) data;
				result = Server.Login(strs.get(0), strs.get(1));
			}
			else if (taskId == Constants.TASK_USER_GETME) {
				
			}

			return result;
		}
		
		@Override
		public void onTaskResult(int taskId, Object result) {
			if (taskId == Constants.TASK_USER_GETME) {
				if (result != null) {
					if (result instanceof ResponseModel.LoginResultModel) {
						ResponseModel.LoginResultModel res_model = (ResponseModel.LoginResultModel) result;
						if (res_model.status == Constants.HTTP_ACTION_STATUS_SUCCESS) {
							
							// getUserInformation
//							mUser = new WAUser();
//							mUser.user_id = res_model.user.uid;
//							mUser.email = res_model.user.email;
//							mUser.login = res_model.user.name;
//							mUser.password = res_model.user.password;
//							mUser.hometown = res_model.user.address;
//							mUser.picture_url = res_model.user.picture_url;

							WAModelManager.getInstance().setSignInUser(mUser);
							
							initData();
						}
						else {
							// error
							MessageUtil.showMessage(res_model.reason, false);
							showLoginPage();
						}
					}
				}
				else {
					
				}
			}
			
			if (taskId == Constants.TASK_USER_LOGIN) {
				Message msg = new Message();
				msg.arg1 = Constants.EVENT_DIALOG_HIDE;
				mHandler.sendMessage(msg);
			}
		}
		
		@Override
		public void onTaskProgress(int taskId, Object... values) {
			
		}
		
		@Override
		public void onTaskPrepare(int taskId, Object data) {
			if (taskId == Constants.TASK_USER_LOGIN) {
				Message msg = new Message();
				msg.arg1 = Constants.EVENT_DIALOG_SHOW;
				mHandler.sendMessage(msg);
			}
		}
		
		@Override
		public void onTaskCancelled(int taskId) {
			if (taskId == Constants.TASK_USER_LOGIN) {
				Message msg = new Message();
				msg.arg1 = Constants.EVENT_DIALOG_HIDE;
				mHandler.sendMessage(msg);
			}
		}
	};
	
	@Override
	public boolean handleMessage(Message msg) {
		try {
			switch (msg.arg1) {
			case Constants.EVENT_DIALOG_SHOW:
				//dlg_progress.show();
				break;
			case Constants.EVENT_DIALOG_HIDE:
				//dlg_progress.hide();
				break;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//			} else {
//				MessageUtil.showMessage("This device is not supported.", true);
//				finish();
//			}
//			return false;
//		}
		return true;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		BaseTask task = new BaseTask(Constants.TASK_GET_GCMREGID);
		task.setListener(mTaskListener);
		task.execute();
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		//MessageUtil.showMessage("Saving regId on app version " + appVersion, true);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	public void showMenu() {
		if (child_drawer_layout.isShown()) {
			parent_drawer_layout.closeDrawer(child_drawer_layout);
		} else {
			parent_drawer_layout.openDrawer(child_drawer_layout);
		}
	}
	
	public void showDetailEventFragment(WAEvent event, boolean isJoined)
	{
		DetailEventFragment fragment = new DetailEventFragment(this, event, isJoined);
		mCurrentFragment = fragment;
		mCurrentFragmentIndex = Constants.SW_FRAGMENT_DETAIL_EVENT;
		try {
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		// update selected item and title, then close the drawer
		if (child_drawer_layout.isShown())
			parent_drawer_layout.closeDrawer(child_drawer_layout);
	}
	
	public void showFreeCoinFragment()
	{
		FreeCoinFragment fragment = new FreeCoinFragment(this);
		mCurrentFragment = fragment;
		mCurrentFragmentIndex = Constants.SW_FRAGMENT_FREE_COINS;
		try {
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		// update selected item and title, then close the drawer
		if (child_drawer_layout.isShown())
			parent_drawer_layout.closeDrawer(child_drawer_layout);
	}
	
	public void showPurchaseFragment()
	{
		PurchaseFragment fragment = new PurchaseFragment(this);
		mCurrentFragment = fragment;
		mCurrentFragmentIndex = Constants.SW_FRAGMENT_PURCHASE;
		try {
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		// update selected item and title, then close the drawer
		if (child_drawer_layout.isShown())
			parent_drawer_layout.closeDrawer(child_drawer_layout);
	}
	
	public void showEainCoinsFragment()
	{
		SwitchContent(Constants.SW_FRAGMENT_ADDCOIN);
	}
	
	// QuickBlox
	
    private void createSession(final String login, final String password) {

//        loginPB.setVisibility(View.VISIBLE);

        final QBUser user = new QBUser(login, password);
        QBAuth.createSession(login, password, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle bundle) {
                Log.d(TAG, "onSuccess create session with params");
                user.setId(session.getUserId());

//                loginPB.setVisibility(View.INVISIBLE);

                chatService.login(user, new QBEntityCallbackImpl<QBUser>() {

                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.d(TAG, "onSuccess login to chat with params");
//                        Intent intent = new Intent(ListUsersActivity.this, CallActivity.class);
//                        intent.putExtra("login", login);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess login to chat");
//                        Intent intent = new Intent(ListUsersActivity.this, CallActivity.class);
//                        intent.putExtra("login", login);
//                        startActivity(intent);
                        initQBRTCClient();
                    }

                    @Override
                    public void onError(List errors) {
//                        loginPB.setVisibility(View.INVISIBLE);
                        MessageUtil.showMessage("Error when login", false);
                        for (Object error : errors) {
                            Log.d(TAG, error.toString());
                        }
                    }
                });

            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                Log.d(TAG, "onSuccess create session");
            }

            @Override
            public void onError(List<String> errors) {
//                loginPB.setVisibility(View.INVISIBLE);
//                Toast.makeText(ListUsersActivity.this, "Error when login, check test users login and password", Toast.LENGTH_SHORT).show();
            	MessageUtil.showMessage("Error when login, check test users login and password", false);
            }
        });
    }
    
    private void initQBRTCClient() {
        // From hear we start listening income call
        QBRTCClient.init(this);

        // Add signalling manager
        QBRTCClient.getInstance().setSignalingManager(chatService.getVideoChatWebRTCSignalingManager());

        // Set custom ice servers up. Use it in case you want set your own servers instead of defaults
        List<PeerConnection.IceServer> iceServerList = new LinkedList<IceServer>();
        iceServerList.add(new PeerConnection.IceServer("turn:numb.viagenie.ca", "petrbubnov@grr.la", "petrbubnov@grr.la"));
        iceServerList.add(new PeerConnection.IceServer("turn:numb.viagenie.ca:3478?transport=udp", "petrbubnov@grr.la", "petrbubnov@grr.la"));
        iceServerList.add(new PeerConnection.IceServer("turn:numb.viagenie.ca:3478?transport=tcp", "petrbubnov@grr.la", "petrbubnov@grr.la"));
        QBRTCConfig.setIceServerList(iceServerList);

        // Add activity as callback to RTCClient
        if (QBRTCClient.isInitiated()) {
            QBRTCClient.getInstance().addCallback(this);
        }
    }
    
    public QBRTCSession getCurrentSession() {
        return QBRTCClient.getInstance().getSessions().get(currentSession);
    }

    public void setCurrentSession(QBRTCSession session) {
        if (!QBRTCClient.getInstance().getSessions().containsKey(session.getSessionID())) {
            addSession(session);
        }
        currentSession = session.getSessionID();
    }

    public QBRTCSession getSession(String sessionID) {
        return QBRTCClient.getInstance().getSessions().get(sessionID);
    }

    public void addSession(QBRTCSession session) {
        QBRTCClient.getInstance().getSessions().put(session.getSessionID(), session);
    }

    public void setCurrentSessionId(String sesionId) {
        this.currentSession = sesionId;
    }


    // ---------------Chat callback methods implementation  ----------------------//

    @Override
    public void onReceiveNewSession(QBRTCSession session) {
//        Toast.makeText(this, "IncomeCall", Toast.LENGTH_SHORT).show();

        if (currentSession == null) {
            Log.d(TAG, "Start new session");
            Log.d(TAG, "Income call");
            QBRTCClient.getInstance().getSessions().put(session.getSessionID(), session);
            setCurrentSession(session);
            addIncomeCallFragment(session);
        } else {
            Log.d(TAG, "Stop new session. Device now is busy");
            session.rejectCall(null);
        }
    }

    @Override
    public void onUserNotAnswer(QBRTCSession session, Integer userID) {
//        setStateTitle(userID, R.string.noAnswer, View.VISIBLE);
//        addOpponentsFragmentWithDelay();
        ConversationFragment fragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_CALL_FRAGMENT);
        if (fragment!=null) {
            fragment.stopOutBeep();
        }
    }

    @Override
    public void onStartConnectToUser(QBRTCSession session, Integer userID) {
//        setStateTitle(userID, R.string.checking, View.VISIBLE);

        ConversationFragment fragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_CALL_FRAGMENT);
        if (fragment != null) {
            fragment.actionButtonsEnabled(true);
            fragment.stopOutBeep();
        }
    }

    @Override
    public void onCallRejectByUser(QBRTCSession session, Integer userID, Map<String, String> userInfo) {
//        setStateTitle(userID, R.string.rejected, View.INVISIBLE);

        ConversationFragment fragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_CALL_FRAGMENT);
        if (fragment!=null) {
            fragment.stopOutBeep();
        }
    }

    @Override
    public void onLocalVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack) {
        this.localVideoTrack = videoTrack;
        videoTrack.addRenderer(new VideoRenderer(LOCAL_RENDERER));
//        videoTrack.addRenderer(new VideoRenderer(new VideoCallBacks(videoView, QBGLVideoView.Endpoint.LOCAL)));
//        videoView.setVideoTrack(videoTrack, QBGLVideoView.Endpoint.LOCAL);
        Log.d("Track", "onLocalVideoTrackReceive() is raned");
    }

    @Override
    public void onRemoteVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack, Integer userID) {
//        VideoCallBacks videoCallBacks = new VideoCallBacks(videoView, QBGLVideoView.Endpoint.REMOTE);
//        videoCallBacks.setSize(200, 300);
//        VideoRenderer remouteRenderer = new VideoRenderer(videoCallBacks);
//        videoTrack.addRenderer(remouteRenderer);
        videoTrack.addRenderer(new VideoRenderer(REMOTE_RENDERER));
//        videoTrackList.put(userID, videoTrack);
        Log.d("Track", "onRemoteVideoTrackReceive() is raned");
    }

    @Override
    public void onConnectionClosedForUser(QBRTCSession session, Integer userID) {
//        setStateTitle(userID, R.string.closed, View.INVISIBLE);
    }

    @Override
    public void onConnectedToUser(QBRTCSession session, Integer userID) {
//        startTimer();

//        setStateTitle(userID, R.string.connected, View.INVISIBLE);

        Log.d("Track", "onConnectedToUser() is started");

        ConversationFragment fragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_CALL_FRAGMENT);
        if (fragment!=null) {
            fragment.stopOutBeep();
        }
    }

    @Override
    public void onDisconnectedTimeoutFromUser(QBRTCSession session, Integer userID) {
//        setStateTitle(userID, R.string.time_out, View.INVISIBLE);
    }

    @Override
    public void onConnectionFailedWithUser(QBRTCSession session, Integer userID) {
//        setStateTitle(userID, R.string.failed, View.INVISIBLE);
    }

    @Override
    public void onSessionClosed(QBRTCSession session) {
        if (session.getSessionID().equals(currentSession)) {
            Log.d(TAG, "Stop session");
//            if (session.getState().ordinal() > QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_REJECTED.ordinal()) {
            
            
//            addOpponentsFragmentWithDelay();

//            } else {
//                Log.d(TAG, "Can't hangup session with status -->" + session.getState().name());
//            }
            // Remove current session
            Log.d(TAG, "Remove current session");
            currentSession = null;
        }
    }

    @Override
    public void onSessionStartClose(QBRTCSession session) {

        ConversationFragment fragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_CALL_FRAGMENT);
        if (fragment != null && session.equals(getCurrentSession())) {
            fragment.actionButtonsEnabled(false);
        }

        Log.d(TAG, "Start stopping session");
    }

    @Override
    public void onDisconnectedFromUser(QBRTCSession session, Integer userID) {
//        setStateTitle(userID, R.string.disconnected, View.INVISIBLE);
    }

//    private void setStateTitle(Integer userID, int stringID, int progressBarVisibility) {
//        Log.d(TAG, "Start Change opponent state");
//        View opponentItemView = findViewById(userID);
//        if (opponentItemView != null) {
//            TextView connectionStatus = (TextView) opponentItemView.findViewById(R.id.connectionStatus);
//            connectionStatus.setText(getString(stringID));
//
//            ProgressBar connectionStatusPB = (ProgressBar) opponentItemView.findViewById(R.id.connectionStatusPB);
//            connectionStatusPB.setVisibility(progressBarVisibility);
//            Log.d(TAG, "Opponent state changed to " + getString(stringID));
//        }
//    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession session, Integer userID) {

        // TODO update view of this user

//        setStateTitle(userID, R.string.hungUp, View.INVISIBLE);

        Log.d(TAG, "CHECK SESSION STATE");
        for (String key : QBRTCClient.getInstance().getSessions().keySet()) {
            Log.d(TAG, QBRTCClient.getInstance().getSessions().get(key).toString());
        }

////        Toast.makeText(this, "User with ID:" + userID + "disconnected", Toast.LENGTH_SHORT).show();
//        if (session.getState().ordinal() < QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_REJECTED.ordinal()){
//            addOpponentsFragmentWithDelay();
//        } else {
//            Log.d(TAG, "Can't hangup session with status -->" + session.getState().name());
//        }
//
//        for (String key : QBRTCClient.getInstance().getSessions().keySet()) {
//            Log.d(TAG, QBRTCClient.getInstance().getSessions().get(key).toString());
//        }

    }

//    private void removeUserWithID(Integer userID) {
//        QBRTCSession session = getCurrentSession();
//        if (session != null) {
//            session.removeUser(userID, new HashMap<String, String>());
//        }
//    }
    
//    public void addOpponentsFragmentWithDelay() {
//        HandlerThread handlerThread = new HandlerThread(ADD_OPPONENTS_FRAGMENT_HANDLER);
//        handlerThread.start();
//        new Handler(handlerThread.getLooper()).postAtTime(new Runnable() {
//            @Override
//            public void run() {
//                if (!MainActivity.this.isDestroyed()) {
//                    getFragmentManager().beginTransaction().replace(R.id.main_content_frame, new OpponentsFragment(), OPPONENTS_CALL_FRAGMENT).commit();
//                    currentSession = null;
//                }
//            }
//        }, SystemClock.uptimeMillis() + TimeUnit.SECONDS.toMillis(TIME_BEGORE_CLOSE_CONVERSATION_FRAGMENT));
//    }

//    public void addOpponentsFragment() {
//        if (!isDestroyed()) {
//            getFragmentManager().beginTransaction().replace(R.id.main_content_frame, new OpponentsFragment(), OPPONENTS_CALL_FRAGMENT).commit();
//        }
//    }


    public void removeIncomeCallFragment() {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(INCOME_CALL_FRAGMENT);

        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }


    private void addIncomeCallFragment(QBRTCSession session) {
        IncomeCallFragment fragment = new IncomeCallFragment(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sessionDescription", session.getSessionDescription());
        bundle.putIntegerArrayList("opponents", new ArrayList<Integer>(session.getOpponents()));
        bundle.putInt(CONFERENCE_TYPE, session.getConferenceType().getValue());
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_content_frame, fragment, INCOME_CALL_FRAGMENT).commit();

    }


//    public void addConversationFragmentStartCall(List<Integer> opponents,
//                                                 QBRTCTypes.QBConferenceType qbConferenceType,
//                                                 Map<String, String> userInfo) {
//
//        // init session for new call
//        try {
//            QBRTCSession newSessionWithOpponents = QBRTCClient.getInstance().createNewSessionWithOpponents(opponents, qbConferenceType);
//            setCurrentSession(newSessionWithOpponents);
//
//            ConversationFragment fragment = new ConversationFragment(this);
//            Bundle bundle = new Bundle();
//
//            bundle.putInt(CONFERENCE_TYPE, qbConferenceType.getValue());
//            bundle.putInt(START_CONVERSATION_REASON, StartConversetionReason.OUTCOME_CALL_MADE.ordinal());
////            bundle.putString(CALLER_NAME, DataHolder.getUserNameByID(opponents.get(0)));
//
//            for (String key : userInfo.keySet()) {
//                bundle.putString("UserInfo:" + key, userInfo.get(key));
//            }
//            fragment.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.main_content_frame, fragment, CONVERSATION_CALL_FRAGMENT).commit();
//        } catch (IllegalStateException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
    
    public void addConversationFragmentStartCall(String sessionID) {

		QBRTCTypes.QBConferenceType qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

    	// init session for new call
    	try {
            ArrayList<Integer> opponents = new ArrayList<Integer>();
            opponents.add(Integer.valueOf("3569055"));
            
    		QBRTCSession newSessionWithOpponents = QBRTCClient.getInstance().createNewSessionWithOpponents(opponents, qbConferenceType);
    		setCurrentSession(newSessionWithOpponents);

    		ConversationFragment fragment = new ConversationFragment(this);
    		Bundle bundle = new Bundle();

    		bundle.putInt(CONFERENCE_TYPE, qbConferenceType.getValue());
    		bundle.putInt(START_CONVERSATION_REASON, StartConversetionReason.OUTCOME_CALL_MADE.ordinal());
    		//bundle.putString(CALLER_NAME, DataHolder.getUserNameByID(opponents.get(0)));

//    		for (String key : userInfo.keySet()) {
//    			bundle.putString("UserInfo:" + key, userInfo.get(key));
//    		}
    		fragment.setArguments(bundle);
    		getFragmentManager().beginTransaction().replace(R.id.main_content_frame, fragment, CONVERSATION_CALL_FRAGMENT).commit();
    	} catch (IllegalStateException e) {
    		Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    	}

}

    public void addConversationFragmentReceiveCall(String sessionID) {

        // set current session
        setCurrentSessionId(sessionID);
        QBRTCSession session = getCurrentSession();
        Integer myId = QBChatService.getInstance().getUser().getId();
        ArrayList<Integer> opponentsWithoutMe = new ArrayList<Integer>(session.getOpponents());
        opponentsWithoutMe.remove(new Integer(myId));
        opponentsWithoutMe.add(session.getCallerID());

        // init conversation fragment
        ConversationFragment fragment = new ConversationFragment(this);
        Bundle bundle = new Bundle();
        bundle.putInt(CONFERENCE_TYPE, session.getConferenceType().getValue());
        bundle.putInt(START_CONVERSATION_REASON, StartConversetionReason.INCOME_CALL_FOR_ACCEPTION.ordinal());
//        bundle.putString(SESSION_ID, sessionID);
//        bundle.putString(CALLER_NAME, DataHolder.getUserNameByID(session.getCallerID()));

        if (session.getUserInfo() != null) {
            for (String key : session.getUserInfo().keySet()) {
                bundle.putString("UserInfo:" + key, session.getUserInfo().get(key));
            }
        }
        fragment.setArguments(bundle);

        // Start conversation fragment
        getFragmentManager().beginTransaction().replace(R.id.main_content_frame, fragment, CONVERSATION_CALL_FRAGMENT).commit();
    }


    public void setCurrentVideoView(GLSurfaceView videoView) {
        VideoRendererGui.ScalingType scaleType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
//
        REMOTE_RENDERER = VideoRendererGui.create(0, 0, 100, 100, scaleType, true);
//
//        //next value in percentage of the available space
//        int marginLeft = 0;
//        int marginTop = 0;
//        int height = 0;
//        int width = 0;
//
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//            marginLeft = 70;
//            marginTop = 0;
//            height = 30;
//            width = 22;
//        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//            marginLeft = 69;
//            marginTop = 0;
//            height = 15;
//            width = 34;
//        }

        LOCAL_RENDERER = VideoRendererGui.create(70, 0, 30, 30, scaleType, true);
    }

    public static enum StartConversetionReason {
        INCOME_CALL_FOR_ACCEPTION,
        OUTCOME_CALL_MADE;
    }

}