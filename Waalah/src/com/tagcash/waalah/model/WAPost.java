package com.tagcash.waalah.model;

import java.util.Date;

public class WAPost {

	public int id; // primary key
	public int post_id;
	public int user_id;
	public String user_full_name;
	public String user_picture_url;
	public int user_gender;
	public int topic_id;
	public String topic_title;
	public String message;
	public String image_url;
	public int comment_count;
	public int like_count;
	public int flag_count;
	public int like_id;
	public int comment_id;
	public int flag_id;
	public int shared;
	public String sharedtype;
	public Date created_time;

	private void init() {
		post_id = 0;
		user_id = 0;
		user_full_name = "";
		user_picture_url = "";
		user_gender = 0;
		topic_id = 0;
		topic_title = "";
		message = "";
		image_url = "";
		comment_count = 0;
		like_count = 0;
		flag_count = 0;
		like_id = 0;
		comment_id = 0;
		flag_id = 0;
		shared = 0;
		sharedtype = "";
		created_time = new Date();
	}

	public WAPost() {
		init();
	}

	public WAPost(WAPost post) {
		init();
		if (post == null)
			return;
		
		if (post.post_id != 0)
			this.post_id = post.post_id;
		this.user_id = post.user_id;
		if (post.user_full_name != null)
			this.user_full_name = post.user_full_name;
		if (post.user_picture_url != null)
			this.user_picture_url = post.user_picture_url;
		this.user_gender = post.user_gender;
		if (post.topic_id != 0)
			this.topic_id = post.topic_id;
		if (post.topic_title != null)
			this.topic_title = post.topic_title;
		if (post.message != null)
			this.message = post.message;
		if (post.image_url != null)
			this.image_url = post.image_url;
		this.comment_count = post.comment_count;
		this.like_count = post.like_count;
		this.flag_count = post.flag_count;
		this.like_id = post.like_id;
		this.comment_id = post.comment_id;
		this.flag_id = post.flag_id;
		this.shared = post.shared;
		this.sharedtype = post.sharedtype;
		if (post.created_time != null)
			this.created_time = post.created_time; 
	}

	public WAPost(int in_post_id, int in_user_id, String in_user_full_name, String in_user_picture_url, int in_user_gender, int in_topic_id, String in_topicTitle, String in_message, String in_image_url, 
			int in_comment_count, int in_like_count, int in_flag_count, 
			int in_like_id, int in_comment_id, int in_flag_id, int in_shared, String in_sharedtype, 
			Date in_created_time) {
		init();
		
		if (in_post_id != 0)
			this.post_id = in_post_id;
		this.user_id = in_user_id;
		if (in_user_full_name != null)
			this.user_full_name = in_user_full_name;
		if (in_user_picture_url != null)
			this.user_picture_url = in_user_picture_url;
		this.user_gender = in_user_gender;
		if (in_topic_id != 0)
			this.topic_id = in_topic_id;
		if (in_topicTitle != null)
			this.topic_title = in_topicTitle;
		if (in_message != null)
			this.message = in_message;
		if (in_image_url != null)
			this.image_url = in_image_url;
		this.comment_count = in_comment_count;
		this.like_count = in_like_count;
		this.flag_count = in_flag_count;
		this.like_id = in_like_id;
		this.comment_id = in_comment_id;
		this.flag_id = in_flag_id;
		this.shared = in_shared;
		this.sharedtype = in_sharedtype;
		if (in_created_time != null)
			this.created_time = in_created_time;
	}
}
