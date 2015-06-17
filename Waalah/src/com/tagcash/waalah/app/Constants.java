package com.tagcash.waalah.app;

public class Constants {

    public static final long SESSION_TIME_OUT = 5*60*1000;
    public static final long SERVER_CHECK_TIME = 2*60*1000;
    public static final int PASSWORD_LENGTH = 6;
    
    /*
     * for chat message
     */
    public static final int MAXIMUM_TEXT_LENGTH = 5000;

    /*
	 * Server ID prefix for SNS account
	 */
	public static class ID_PREVFIX {
		public static String FACEBOOK = "facebook_";
		public static String GOOGLE = "google_";
		public static String TWITTER = "twitter_";
		public static String EMAIL = "email_";
	}
    /*
     * Social Network
     */
    public static final String FaceBook_Access_Token = "dae7dabd94dd8cb61f147867276cea03";
    
    public static class TWITTER {
		public static String CONSUMER_KEY = "RjVoVedX85EEWqYtKO6eM5Ht1";
		public static String CONSUMER_SECRET = "o5u4keayWdzAdq8BfuAQegdwfg0mBymh5V7BHpeDcD3sbD5pz5";

		public static final String CALLBACK_URL = "oauth://twitter-healthchat";
		public static final String IEXTRA_AUTH_URL = "auth_url";
		public static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
		public static final String IEXTRA_OAUTH_TOKEN = "oauth_token";
		
		// preference key
		public static final String PREF_KEY_SECRET = "oauth_token_secret";
		public static final String PREF_KEY_TOKEN = "oauth_token";
	}
    
    public static final int MODE_REGISTER = 10;
    public static final int MODE_LOGIN = 11;

    public static final int MODE_ADD = 20;
    public static final int MODE_EDIT = 21;

    public static final String SHARE_UPLOAD_FOLDERNAME = "/waalah/temp/";
    public static final String SHARE_UPLOAD_FILENAME = "upload.jpg";
    /*
     * Login Type
     */
	public static class LOGIN_TYPE {
		public static int FACEBOOK = 0;
		public static int GOOGLE = 1;
		public static int TWITTER = 2;
		public static int EMAIL = 3;
		public static int USER_NAME = 4;
	}
	
	public static class SHARE_TYPE {
		public static int FACEBOOK = (1 << 1);
		public static int TWITTER = (1 << 2);
		public static int WHATSAPP = (1 << 3);
	}
	
	/*
	 * Gender String
	 */
	public static class GENDER {
		public static final int iMale = 0;
		public static final int iFemale = 1;
		public static final int iOther = 2;
		public static final String sMale = "Male";
		public static final String sFemale = "Female";
		public static final String sOther = "Other";
	}
	
	public static class PRIVACY { // match to strings.xml : privacy_array
		public static final int PRIVACY_EVERYONE = 0;
		public static final int PRIVACY_ONLYLIKE = 1;
		public static final int PRIVACY_NOBODY = 2;
	}
	/*
	 * the activity with Emoji
	 */
	public static class EMOJI {
		public static final int WRITE_POST_ACTIVITY = 1;
	}
	
	/*
	 * Image size for posting
	 */
	public static int POST_IMAGE_SIZE = 512;
	
    /*
	 * Main Activity Switch content
	 */
	public static final int SW_FRAGMENT_WAALAH = 0;
	public static final int SW_FRAGMENT_ADDCOIN = 1;
	public static final int SW_FRAGMENT_INVITE = 2;
	public static final int SW_FRAGMENT_SETTING = 3;
	public static final int SW_FRAGMENT_HELP = 4;

	public static final int SW_FRAGMENT_DETAIL_EVENT = 10;
	public static final int SW_FRAGMENT_FREE_COINS = 11;
	public static final int SW_FRAGMENT_PURCHASE = 12;

	
	/*
	 * Scrolling factor of Customize view pager
	 */
	public static final float VP_SCROLL_FACTOR = 5.0f;
	
	/*
	 * DragListview
	 */
	public static final int FRIST_REFRESH_TASK_DELAY_TIME = 1000;
	public static final int REFRESH_HEADER_TASK = 101;
	public static final int REFRESH_FOOTER_TASK = 102;
	
	/*
	 *  Count per every refresh
	 */
	public static final int USER_COUNT_PER_ONE_TIME = 10;
	public static final int TOPIC_COUNT_PER_ONE_TIME = 10;
	public static final int POST_COUNT_PER_ONE_TIME = 5;
	public static final int COMMENT_COUNT_PER_ONE_TIME = 5;
	public static final int POSTLIKE_COUNT_PER_ONE_TIME = 10;
	public static final int CHAT_COUNT_PER_ONE_TIME = 100;
	
	/*
	 * Service Interval time for checking login status
	 */
	public static final int SERVICE_INTERVAL_TIME = 1000 * 20;
	
	/*
	 * Time Format for saving
	 */
	public static String DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/*
	 * Administrator's email
	 */
	public static final String HEALTHCHAT_EMAIL_ADDRESS = "healthchatapp@gmail.com";
	public static final String HEALTHCHAT_EMAIL_PASSWORD = "jeff1234567890";
	
	/*
	 * Key
	 */
	public static final String KEY_FLAG = "key_flag";
	public static final String KEY_SPLIT = ";";
	
	/*
	 * Notification
	 */
	public static final String ACTION_LOGIN_SUCCESS = "com.tagcash.waalah.ACTION_LOGIN_SUCCESS";
	public static final String ACTION_SWITCHTO_PROFILE = "com.tagcash.waalah.ACTION_SWITCHTO_PROFILE";
    public static final String ACTION_NEW_CHATMESSAGE = "com.tagcash.waalah.ACTION_NEW_CHATMESSAGE";
    public static final String ACTION_FROM_NOTIFICATION = "com.tagcash.waalah.ACTION_FROM_NOTIFICATION";
    public static final String ACTION_FROM_FRIENDID = "com.tagcash.waalah.ACTION_FROM_FRIENDID";

	/*
	 * Http Action Status
	 */
	public static final int HTTP_ACTION_STATUS_SUCCESS = 1;
	public static final int HTTP_ACTION_STATUS_ERROR = 0;

	/*
	 * Online Status
	 */
	public static final int HTTP_ACTION_ONLINE = 1;
	public static final int HTTP_ACTION_OFFLINE = 0;

	/*
	 * Http Action ErrorCode
	 */
	public static final int HTTP_ACTION_ERRORCODE_UNKNOWN = 100;
	public static final int HTTP_ACTION_ERRORCODE_NAMEOREMAIL_ALREADY_REGISTERED = 101;
	public static final int HTTP_ACTION_ERRORCODE_NAME_ALREADY_REGISTERED = 102;
	public static final int HTTP_ACTION_ERRORCODE_EMAIL_ALREADY_REGISTERED = 103;
	public static final int HTTP_ACTION_ERRORCODE_INVALID_EMAIL_OR_PASSWORD = 104;
	public static final int HTTP_ACTION_ERRORCODE_AUTHENTICATION_REQUIRED = 105;
	
	/*
	 * Task ids
	 */
	public static final int TASK_USER_REGISTER = 1;
	public static final int TASK_USER_ISREGISTER = 2;
	public static final int TASK_USER_GETLIST = 3;
	public static final int TASK_USER_LOGIN = 4;
	public static final int TASK_USER_RESET_PASSWORD = 5;
	public static final int TASK_USER_UPDATE_PROFILE = 6;
	public static final int TASK_USER_UPDATE_AVATAR = 7;
	public static final int TASK_USER_SETONLINE = 8;
	public static final int TASK_USER_FAVORITE = 9;
	public static final int TASK_USER_UNFAVORITE = 10;
	public static final int TASK_USER_GETONE = 11;
	
	
	public static final int TASK_GET_GCMREGID = 100;
	
	/*
	 * for UI
	 */
	public static final int EVENT_DIALOG_SHOW = 1;
	public static final int EVENT_DIALOG_HIDE = 2;
	
	/*
	 * for check camera/gallery pick
	 */
	public static final int PIICTURE_TAKE_NONE = 0;
	public static final int PIICTURE_TAKE_CAMERA = 1;
	public static final int PIICTURE_TAKE_GALLERY = 2;
	
	/*
	 * Google Play billing
	 */
	
	public static final String BILLING_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkirwC/1jalSx0MLRpr04Hd5BT4LS5VukEzGrdSLq3q22Dq47MZhzP9eDXmyfimbg66lJsXm5HEHwng5IiQi+MzjOzp/+YP+DsslUGio1oGqcrt95uY1yPhYrX2OOgJwbuAV4IrgylOFR89h1+Q7IZIBDZACFGDDwelzLfwDhlhIh/CUa9MXeea1pEcw4U6BCrYCP0Viryn24LtLE4B9+MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAglugUccAwKGMvz9vfZslUhPK/JLYx5Pzh5j4mcMfvEaPDHtariEBKRMq85iBplyh1iVwPaVei/UF+ztSK0WeTSohet+rtW/9skqplwXoUI6yL7BCwlTXhn/ESRj1miVMfpaWfvC6XpLl+YI6crrL/+z11/c4GhPvY+4XmVMdc168Pd08JRi0+dQiPR4gTCyPNHD3RCG19UccT3VmgPoJpXQWRF8UEEaXQhUdGVGSJGjBzVNWQVNW4mTYjtdxRBozgd5IISw1woJFk4/bHZbWf8pcRzSCkPPADF5rAgt2KlqcSAKKKxjAceUNbjZld/LluK1k/yflkAaEeP4iebPOnQIDAQAB";
	
	/*
	 * QuickBlox Constants
	 */
	public static final String APP_ID = "24217";
	public static final String AUTH_KEY	= "42vOuhnUpevybEk";
	public static final String AUTH_SECRET = "7ex-Lbd9ZRhAwVR";
	
}
