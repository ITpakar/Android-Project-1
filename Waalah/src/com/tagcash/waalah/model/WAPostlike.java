package com.tagcash.waalah.model;

public class WAPostlike {

	public int id; // primary key
	public int lid;
	public int user_id;
	public int post_id;
	public String user_full_name;
	public String user_picture_url;
	public String user_birthday;
	public int user_gender;

	private void init() {
		lid = 0;
		user_id = 0;
		post_id = 0;
		user_full_name = "";
		user_picture_url = "";
		user_birthday = "";
		user_gender = 0;
	}

	public WAPostlike() {
		init();
	}

	public WAPostlike(WAPostlike postlike) {
		init();
		if (postlike == null)
			return;
		
		if (postlike.lid != 0)
			this.lid = postlike.lid;
		this.user_id = postlike.user_id;
		if (postlike.post_id != 0)
			this.post_id = postlike.post_id;
		if (postlike.user_full_name != null)
			this.user_full_name = postlike.user_full_name;
		if (postlike.user_picture_url != null)
			this.user_picture_url = postlike.user_picture_url;
		this.user_birthday = postlike.user_birthday;
		this.user_gender = postlike.user_gender;
	}

	public WAPostlike(int in_lid, int in_user_id, int in_post_id, String in_user_full_name, String in_user_picture_url, String in_birthday, int in_user_gender) {
		init();
		
		if (in_lid != 0)
			this.lid = in_lid;
		this.user_id = in_user_id;
		if (in_post_id != 0)
			this.post_id = in_post_id;
		if (in_user_full_name != null)
			this.user_full_name = in_user_full_name;
		if (in_user_picture_url != null)
			this.user_picture_url = in_user_picture_url;
		this.user_birthday = in_birthday;
		this.user_gender = in_user_gender;
	}
}
