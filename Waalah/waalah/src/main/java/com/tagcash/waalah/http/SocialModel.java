package com.tagcash.waalah.http;

import java.util.ArrayList;

public class SocialModel {
	/*
	 * Facebook model
	 */
	public class commonModel {
		public String id;
		public String name;
	}
	
	public class schoolModel {
		public String type;
		public commonModel school;
	}
	
	public class employModel {
		public commonModel employer;
	}
	
	public class FacebookModel {
		public String id;
		public String birthday;
		public String name;
		public String gender;
		public commonModel location;
		public commonModel hometown;
		public ArrayList<employModel> work;
		public ArrayList<schoolModel> education;
	}
	
	/*
	 * twitter model
	 */
	
	public class TwitterModel {
		public long id;
		public String name;
		public String location; // hometown
		public String profile_image_url;
		public String description; // about
	}
}
