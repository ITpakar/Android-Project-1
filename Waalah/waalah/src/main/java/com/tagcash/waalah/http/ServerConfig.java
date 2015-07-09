package com.tagcash.waalah.http;

public class ServerConfig {
	
	// my computer
	//public final static String HOST = "http://10.70.3.10";
	// beta server
//	public static String HOST_URL = "10.70.3.10";
//	public static String HOST_URL = "10.70.3.61";
//	public static String HOST_PORT = "8080";
	// publish server
	public static String HOST_URL = "54.164.161.49";
//	public static String HOST_PORT = "8080";

	public static final int CONNECTION_TIMEOUT = 60;
	public static final String BUSY = "-1";
	public static final String NO_INTERNET = "-2";
	
	public static String getServerUrl() {
		return "http://" + HOST_URL + "/";
	}
	public static String getWebserviceUrl() {
		return getServerUrl() + "api/";
	}

	// for User moddule
	public static String getUrl_UserLogin() {
		return getWebserviceUrl() + "authenticate";
	}
	public static String getUrl_UserLoginWithFacebook() {
		return getWebserviceUrl() + "authenticate/facebook";
	}
	public static String getUrl_UserRegister() {
		return getWebserviceUrl() + "register";
	}
	public static String getUrl_CurrentUser() {
		return getWebserviceUrl() + "me";
	}
	public static String getUrl_UserIsRegister() {
		return getWebserviceUrl() + "member/check_name_or_email";
	}
	public static String getUrl_UserGetList() {
		return getWebserviceUrl() + "member/search";
	}
	public static String getUrl_UserResetPassword() {
		return getWebserviceUrl() + "member/recover_password";
	}
	public static String getUrl_UserUpdateProfile() {
		return getWebserviceUrl() + "member/update_profile";
	}
	public static String getUrl_UserUpdateAvatar() {
		return getWebserviceUrl() + "member/update_avatar";
	}
	public static String getUrl_UserSetOnline() {
		return getWebserviceUrl() + "member/online";
	}
	public static String getUrl_UserLike() {
		return getWebserviceUrl() + "member/like";
	}
	public static String getUrl_UserUnlike() {
		return getWebserviceUrl() + "member/unlike";
	}
	public static String getUrl_UserGetOneUser() {
		return getWebserviceUrl() + "member/get";
	}

	public static String getUrl_MyEvents() { return getWebserviceUrl() + "me/my-events";}

	// for event module
	public static String getUrl_EventList() { return getWebserviceUrl() + "events";}

	// for Topic moddule
	public static String getUrl_TopicAdd() {
		return getWebserviceUrl() + "topic/register";
	}
	public static String getUrl_TopicGetList() {
		return getWebserviceUrl() + "topic/search";
	}
	public static String getUrl_TopicLike() {
		return getWebserviceUrl() + "topic/like";
	}
	public static String getUrl_TopicUnlike() {
		return getWebserviceUrl() + "topic/unlike";
	}
	public static String getUrl_TopicFlag() {
		return getWebserviceUrl() + "topic/flag";
	}
	public static String getUrl_TopicUnflag() {
		return getWebserviceUrl() + "topic/unflag";
	}
	
	// for Post moddule
	public static String getUrl_PostAdd() {
		return getWebserviceUrl() + "post/register";
	}
	public static String getUrl_PostGetList() {
		return getWebserviceUrl() + "post/search";
	}
	public static String getUrl_PostLike() {
		return getWebserviceUrl() + "post/like";
	}
	public static String getUrl_PostUnlike() {
		return getWebserviceUrl() + "post/unlike";
	}
	public static String getUrl_PostFlag() {
		return getWebserviceUrl() + "post/flag";
	}
	public static String getUrl_PostUnflag() {
		return getWebserviceUrl() + "post/unflag";
	}
	public static String getUrl_PostShare() {
		return getWebserviceUrl() + "post/share";
	}
	public static String getUrl_PostEdit() {
		return getWebserviceUrl() + "post/update";
	}
	public static String getUrl_PostDelete() {
		return getWebserviceUrl() + "post/delete";
	}
	public static String getUrl_PostGetOne() {
		return getWebserviceUrl() + "post/get";
	}
	public static String getUrl_PostLikelist() {
		return getWebserviceUrl() + "post/likelist";
	}
	
	// for Comment moddule
	public static String getUrl_CommentAdd() {
		return getWebserviceUrl() + "comment/register";
	}
	public static String getUrl_CommentGetList() {
		return getWebserviceUrl() + "comment/search";
	}
	public static String getUrl_CommentLike() {
		return getWebserviceUrl() + "comment/like";
	}
	public static String getUrl_CommentUnlike() {
		return getWebserviceUrl() + "comment/unlike";
	}
	public static String getUrl_CommentFlag() {
		return getWebserviceUrl() + "comment/flag";
	}
	public static String getUrl_CommentUnflag() {
		return getWebserviceUrl() + "comment/unflag";
	}
	public static String getUrl_CommentEdit() {
		return getWebserviceUrl() + "comment/update";
	}
	public static String getUrl_CommentDelete() {
		return getWebserviceUrl() + "comment/delete";
	}
	
	// for Message module
	public static String getUrl_MessageConnected() {
		return getWebserviceUrl() + "message/connected";
	}
	public static String getUrl_MessageSend() {
		return getWebserviceUrl() + "message/send";
	}
	public static String getUrl_MessageSearch() {
		return getWebserviceUrl() + "message/search";
	}
}
