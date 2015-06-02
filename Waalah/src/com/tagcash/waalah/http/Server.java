package com.tagcash.waalah.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tagcash.waalah.model.WAUser;
import com.tagcash.waalah.ui.activity.MainActivity;

public class Server {
	/*
	 * for User module
	 */
	public static Object Register(String email, String username, String password, String picture_url) {
		HttpParams params = new HttpParams();
		params.addParam("email", email);
		params.addParam("name", username);
		params.addParam("password", password);
		if (!TextUtils.isEmpty(picture_url))
			params.addParam("social_picture_url", picture_url);
		
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

	public static Object IsRegister(String username) {
		HttpParams params = new HttpParams();
		params.addParam("name", username);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserIsRegister(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CheckRegisterModel result = gson.fromJson(response, ResponseModel.CheckRegisterModel.class);
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
		
		// TODO joseph
//		params.addParam("device_token", MainActivity.regid);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserLogin(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.LoginResultModel result = gson.fromJson(response, ResponseModel.LoginResultModel.class);
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
		params.addParam("gender", Integer.toString(WAUser.sGengerToiGender(gender)));
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
	
	public static Object SetUserUnlike(String token, int like_id) {
		HttpParams params = new HttpParams();
		params.addParam("token", token);
		params.addParam("lid", Integer.toString(like_id));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserUnlike(), params);
		
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

	public static Object GetOneUser(String user_token, int user_id) {
		HttpParams params = new HttpParams();
		params.addParam("token", user_token);
		params.addParam("uid", Integer.toString(user_id));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_UserGetOneUser(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.OneUserModel result = gson.fromJson(response, ResponseModel.OneUserModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	/*
	 * for Topic model
	 */

	public static Object AddTopic(String title, String description, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("title", title);
		params.addParam("desc", description);
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_TopicAdd(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.AddTopicModel result = gson.fromJson(response, ResponseModel.AddTopicModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetTopicList(int type, String token, int start_index, int count) {
		HttpParams params = new HttpParams();
		params.addParam("type", Integer.toString(type));
		params.addParam("token", token);
		params.addParam("offset", Integer.toString(start_index));
		params.addParam("limit", Integer.toString(count));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_TopicGetList(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.TopicListModel result = gson.fromJson(response, ResponseModel.TopicListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object TopicLike(int topic_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("topic_id", Integer.toString(topic_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_TopicLike(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.TopicLikedModel result = gson.fromJson(response, ResponseModel.TopicLikedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object TopicUnlike(int like_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("lid", Integer.toString(like_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_TopicUnlike(), params);
		
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

	public static Object TopicFlag(int topic_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("topic_id", Integer.toString(topic_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_TopicFlag(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.TopicFlagedModel result = gson.fromJson(response, ResponseModel.TopicFlagedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object TopicUnflag(int flag_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("fid", Integer.toString(flag_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_TopicUnflag(), params);
		
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
	
	/*
	 * for Post model
	 */

	public static Object AddPost(int topic_id, String title, String picture, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("topic_id", Integer.toString(topic_id));
		params.addParam("title", title);
		params.addParam("picture", picture);
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostAdd(), params);
		
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

	public static Object GetPostList(int topic_id, String token, int start_index, int count) {
		HttpParams params = new HttpParams();
		params.addParam("topic_id", Integer.toString(topic_id)); // if topic_id == 0, it is feed.
		params.addParam("token", token);
		params.addParam("offset", Integer.toString(start_index));
		params.addParam("limit", Integer.toString(count));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostGetList(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostListModel result = gson.fromJson(response, ResponseModel.PostListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostLike(int post_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostLike(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostLikedModel result = gson.fromJson(response, ResponseModel.PostLikedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostUnlike(int like_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("lid", Integer.toString(like_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostUnlike(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostLikedModel result = gson.fromJson(response, ResponseModel.PostLikedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostFlag(int post_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostFlag(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostFlagedModel result = gson.fromJson(response, ResponseModel.PostFlagedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostUnflag(int flag_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("fid", Integer.toString(flag_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostUnflag(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostFlagedModel result = gson.fromJson(response, ResponseModel.PostFlagedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostShare(int post_id, int shared, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("shared", Integer.toString(shared));
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostShare(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostSharedModel result = gson.fromJson(response, ResponseModel.PostSharedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostEdit(int post_id, String title, String picture, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("title", title);
		params.addParam("picture", picture);
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostEdit(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostEditModel result = gson.fromJson(response, ResponseModel.PostEditModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostDelete(int post_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostDelete(), params);
		
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

	public static Object GetPostOne(int post_id, String token) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostGetOne(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostOneModel result = gson.fromJson(response, ResponseModel.PostOneModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object PostLikelist(int post_id, String user_token, int start_index, int count) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", user_token);
		params.addParam("offset", Integer.toString(start_index));
		params.addParam("limit", Integer.toString(count));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_PostLikelist(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.PostLikelistModel result = gson.fromJson(response, ResponseModel.PostLikelistModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
	
	/*
	 * for Comment model
	 */

	public static Object AddComment(int post_id, String title, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("title", title);
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentAdd(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.AddCommentModel result = gson.fromJson(response, ResponseModel.AddCommentModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object GetCommentList(int post_id, String token, int start_index, int count) {
		HttpParams params = new HttpParams();
		params.addParam("post_id", Integer.toString(post_id));
		params.addParam("token", token);
		params.addParam("offset", Integer.toString(start_index));
		params.addParam("limit", Integer.toString(count));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentGetList(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CommentListModel result = gson.fromJson(response, ResponseModel.CommentListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object CommentLike(int comment_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("comment_id", Integer.toString(comment_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentLike(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CommentLikedModel result = gson.fromJson(response, ResponseModel.CommentLikedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object CommentUnlike(int like_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("lid", Integer.toString(like_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentUnlike(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CommentLikedModel result = gson.fromJson(response, ResponseModel.CommentLikedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object CommentFlag(int comment_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("comment_id", Integer.toString(comment_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentFlag(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CommentFlagedModel result = gson.fromJson(response, ResponseModel.CommentFlagedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object CommentUnflag(int flag_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("fid", Integer.toString(flag_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentUnflag(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.CommentFlagedModel result = gson.fromJson(response, ResponseModel.CommentFlagedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object CommentEdit(int comment_id, String title, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("comment_id", Integer.toString(comment_id));
		params.addParam("title", title);
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentEdit(), params);
		
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

	public static Object CommentDelete(int comment_id, String user_token) {
		HttpParams params = new HttpParams();
		params.addParam("comment_id", Integer.toString(comment_id));
		params.addParam("token", user_token);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_CommentDelete(), params);
		
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
	
	/*
	 * for Message Model
	 */

	public static Object MessageConnected(String user_token, int receiver_id) {
		HttpParams params = new HttpParams();
		params.addParam("token", user_token);
		params.addParam("receiver_id", Integer.toString(receiver_id));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_MessageConnected(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.MessageConnectedModel result = gson.fromJson(response, ResponseModel.MessageConnectedModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object MessageSend(String user_token, int receiver_id, String base64Message) {
		HttpParams params = new HttpParams();
		params.addParam("token", user_token);
		params.addParam("receiver_id", Integer.toString(receiver_id));
		params.addParam("message", base64Message);
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_MessageSend(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.MessageSendModel result = gson.fromJson(response, ResponseModel.MessageSendModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}

	public static Object MessageSearch(String user_token, int receiver_id, int start_index, int count) {
		HttpParams params = new HttpParams();
		params.addParam("token", user_token);
		params.addParam("receiver_id", Integer.toString(receiver_id));
		params.addParam("offset", Integer.toString(start_index));
		params.addParam("limit", Integer.toString(count));
		
		String response = HttpApi.sendPostRequest(ServerConfig.getUrl_MessageSearch(), params);
		
		if (response != null) {
			try {
				Gson gson = new Gson();
				ResponseModel.MessageListModel result = gson.fromJson(response, ResponseModel.MessageListModel.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return response;
	}
}
