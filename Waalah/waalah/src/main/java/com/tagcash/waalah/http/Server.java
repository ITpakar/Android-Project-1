package com.tagcash.waalah.http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tagcash.waalah.app.Constants;


public class Server {
	/*
	 * for User module
	 */
	public static Object Register(String email, String username, String password, String picture_url) {
		HttpParams params = new HttpParams();
		params.addParam("email", email);
		params.addParam("username", username);
		params.addParam("password", password);
		if (!TextUtils.isEmpty(picture_url))
			params.addParam("social_picture_url", picture_url);

		Log.v(Constants.LOG_TAG, ServerConfig.getUrl_UserRegister() + "\nparams -> " + params.getParams());
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserRegister(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.RegisterModel result = gson.fromJson(response, ResponseModel.RegisterModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetUserList(String user_token, int type, int start_index, int count) {
		HttpParams params = new HttpParams();
		params.addParam("token", user_token);
		params.addParam("type", Integer.toString(type));
		params.addParam("offset", Integer.toString(start_index));
		params.addParam("limit", Integer.toString(count));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserGetList(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.UserModelList result = gson.fromJson(response, ResponseModel.UserModelList.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	public static Object Login(String username, String password) {
		HttpParams params = new HttpParams();
		params.addParam("email", username);
		params.addParam("password", password);

		Log.v(Constants.LOG_TAG, ServerConfig.getUrl_UserLogin() + "\nparams -> " + params.getParams());

		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserLogin(), params);

		System.out.println(response);

		if (response != null) {
			try {
				Gson gson = new Gson();
				System.out.print(gson.toJson(response));
				ResponseModel.LoginResultModel result = gson.fromJson(response, ResponseModel.LoginResultModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	// Login With Facebook id
	public static Object LoginWithFacebook(String facebookId, String email, String firstName, String lastName) {
		HttpParams params = new HttpParams();
		params.addParam("id", facebookId);
		params.addParam("email", email);
		params.addParam("first_name", firstName);
		params.addParam("last_name", lastName);
//		if (!TextUtils.isEmpty(picture_url))
//			params.addParam("social_picture_url", picture_url);

		Log.v(Constants.LOG_TAG, ServerConfig.getUrl_UserLoginWithFacebook() + "\nparams -> " + params.getParams());
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserLoginWithFacebook(), params);

		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.RegisterModel result = gson.fromJson(response, ResponseModel.RegisterModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetCurrentUser(String token) {
		
		String response = HttpApi.sendGetRequest(ServerConfig.getUrl_CurrentUser(), token, null);
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CurrentUserResultModel result = gson.fromJson(response, ResponseModel.CurrentUserResultModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	public static Object Reset_Password(String username, String password) {
		HttpParams params = new HttpParams();
		params.addParam("email", username);
		params.addParam("password", password);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserResetPassword(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.ResetPasswordModel result = gson.fromJson(response, ResponseModel.ResetPasswordModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	public static Object Update_Profile(String usertoken, String login, String password, String fullname, 
			String email, String gender, String birthday, String hometown, String health_topics_array, 
			String diagnosed_with_array, int diagnosed_with_privacy, String medicated_array, int medicated_privacy, String about) {
		HttpParams params = new HttpParams();
		params.addParam("token", usertoken);
		params.addParam("name", login);
		params.addParam("email", email);
		params.addParam("password", password);
		params.addParam("full_name", fullname);
		params.addParam("birthday", birthday);
		params.addParam("address", hometown);
		params.addParam("health_topic_array", health_topics_array);
		params.addParam("diagnosed_with_array", diagnosed_with_array);
		params.addParam("diagnosed_with_privacy", Integer.toString(diagnosed_with_privacy));
		params.addParam("medicated_array", medicated_array);
		params.addParam("medicated_privacy", Integer.toString(medicated_privacy));
		params.addParam("about", about);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserUpdateProfile(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.GeneralModel result = gson.fromJson(response, ResponseModel.GeneralModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	public static Object Update_Avatar(String usertoken, String picture) {
		HttpParams params = new HttpParams();
		params.addParam("token", usertoken);
		params.addParam("picture", picture);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserUpdateAvatar(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.UpdateAvatarModel result = gson.fromJson(response, ResponseModel.UpdateAvatarModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	public static Object SetOnline(String token) {
		HttpParams params = new HttpParams();
		params.addParam("token", token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserSetOnline(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.GeneralModel result = gson.fromJson(response, ResponseModel.GeneralModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	public static Object SetUserLike(String token, int user_id) {
		HttpParams params = new HttpParams();
		params.addParam("token", token);
		params.addParam("member_id", Integer.toString(user_id));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserLike(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.UserLikedModel result = gson.fromJson(response, ResponseModel.UserLikedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetMyEvent(String token) {

		String response = HttpApi.sendGetRequest(ServerConfig.getUrl_MyEvents(), token, null);
		Log.v(Constants.LOG_TAG, ServerConfig.getUrl_MyEvents() + "\nmy event response -> " + response);
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.EventsListModel result = gson.fromJson(response, ResponseModel.EventsListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetEventHistory(String token) {

		HttpParams params = new HttpParams();
		params.addParam("status", "ended");

		String response = HttpApi.sendGetRequest(ServerConfig.getUrl_MyEvents(), token, params);
		Log.v(Constants.LOG_TAG, ServerConfig.getUrl_MyEvents() + "\nhistory response -> " + response);
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.EventsListModel result = gson.fromJson(response, ResponseModel.EventsListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetEventLists(String token) {

		String response = HttpApi.sendGetRequest(ServerConfig.getUrl_EventList(), token, null);
		Log.v(Constants.LOG_TAG, ServerConfig.getUrl_EventList() + "\nevent list response -> " + response);
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.EventsListModel result = gson.fromJson(response, ResponseModel.EventsListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

}