package com.tagcash.waalah.model;

import com.tagcash.waalah.http.ResponseModel;
import com.tagcash.waalah.util.DateTimeUtil;

import java.util.Date;

public class WAEvent {

	public int id; // primary key
	public int event_id;
	public String event_name;
	public String event_description;
	public Date event_date;
	public int event_owner_id;
	public int duration;
	public float price;
	public String status;
	public int joined_count;
	public String image_url;
	public String thumb_url;
	public String video_url;
	public String audio_url;
	public String chat_url;
	public int coins_count;
	public int diamonds_count;
	public Date event_created;
//	public WAUser me;

//	public int event_rank;
//	public String picture_url;

	
	private void init() {
		this.event_id = 0;
		this.event_name = "";
		this.event_description = "";
		this.event_date = new Date();
		this.event_owner_id = 0;
		this.duration = 0;
		this.price = 0;
		this.status = "";
		this.joined_count = 0;
		this.image_url = "";
		this.thumb_url = "";
		this.video_url = "";
		this.audio_url = "";
		this.chat_url = "";
		this.coins_count=  0;
		this.diamonds_count = 0;
		this.event_created = new Date();
	}

	public WAEvent() {
		init();
	}

	public WAEvent(WAEvent event) {
		init();
		if (event == null)
			return;
		
		event_id 			= event.event_id;
		event_name 			= event.event_name;
		event_description 	= event.event_description;
		event_date 			= event.event_date;
		event_owner_id 		= event.event_owner_id;
		duration 			= event.duration;
		price 				= event.price;
		status 				= event.status;
		joined_count 		= event.joined_count;
		image_url 			= event.image_url;
		thumb_url 			= event.thumb_url;
		video_url 			= event.video_url;
		audio_url 			= event.audio_url;
		chat_url 			= event.chat_url;
		coins_count 		=  event.coins_count;
		diamonds_count		= event.diamonds_count;
		event_created 		= event.event_created;
	}

	public WAEvent(ResponseModel.EventModel event) {
		init();
		if (event == null)
			return;

		event_id 			= Integer.parseInt(event.id);
		event_name 			= event.event_name;
		event_description 	= event.event_description;
//		event_date 			= event.event_date;

		String format 		= "yyyy-MM-dd hh:mm:ss";
		event_date 			= DateTimeUtil.stringToDate(event.event_start_date, format);

		event_owner_id 		= Integer.parseInt(event.event_owner_id);
		duration 			= Integer.parseInt(event.duration);
		price 				= Integer.parseInt(event.price);
		status 				= event.status;
		joined_count 		= Integer.parseInt(event.joined_count);
		image_url 			= event.image_url;
		thumb_url 			= event.thumb_url;
		video_url 			= event.video_url;
		audio_url 			= event.audio_url;
		chat_url 			= event.chat_url;
		coins_count 		= Integer.parseInt(event.coins_count);
		diamonds_count		= Integer.parseInt(event.diamonds_count);
		event_created 		= DateTimeUtil.stringToDate(event.event_created, format);
	}
}
