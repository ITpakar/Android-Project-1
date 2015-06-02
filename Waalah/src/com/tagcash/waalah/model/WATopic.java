package com.tagcash.waalah.model;

import com.tagcash.waalah.database.DBConstant;


public class WATopic {

	public int id; // primary key
	public int topic_id;
	public int user_id;
	public String title;
	public String desc;
	public int post_count;
	public int type;
	public int like_id;
	public int flag_id;

	private void init() {
		topic_id = 0;
		user_id = 0;
		title = "";
		desc = "";
		post_count = 0;
		type = DBConstant.TYPE_ALL;
		like_id = 0;
		flag_id = 0;
	}

	public WATopic() {
		init();
	}

	public WATopic(WATopic topic) {
		init();
		if (topic == null)
			return;
		
		if (topic.topic_id != 0)
			this.topic_id = topic.topic_id;
		this.user_id = topic.user_id;
		if (topic.title != null)
			this.title = topic.title;
		if (topic.desc != null)
			this.desc = topic.desc;
		this.post_count = topic.post_count;
		this.type = topic.type;
		this.like_id = topic.like_id;
		this.flag_id = topic.flag_id;
	}

	public WATopic(int in_topic_id, int in_user_id, String in_title, String in_desc, int in_post_count, int in_type, int in_like_id, int in_flag_id) {
		init();
		
		if (in_topic_id != 0)
			this.topic_id = in_topic_id;
		this.user_id = in_user_id;
		if (in_title != null)
			this.title = in_title;
		if (in_desc != null)
			this.desc = in_desc;
		this.post_count = in_post_count;
		this.type = in_type;
		this.like_id = in_like_id;
		this.flag_id = in_flag_id;
	}
}
