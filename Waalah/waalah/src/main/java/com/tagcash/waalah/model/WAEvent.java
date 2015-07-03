package com.tagcash.waalah.model;

import java.util.Date;

public class WAEvent {
	public int id; // primary key
	public int event_id;
	public String event_owner;
	public Date event_date ;
	public int event_coin;
	public int event_rank;
	public String picture_url;

	
	private void init() {
		this.event_id = 0;
		this.event_owner = "";
		this.event_date = new Date();
		this.event_coin = 0;
		this.event_rank = 0;
		this.picture_url = "";
	}

	public WAEvent() {
		init();
	}

	public WAEvent(WAEvent event) {
		init();
		if (event == null)
			return;
		
		event_id = event.event_id;
		event_owner = event.event_owner;
		event_date = event.event_date;
		event_coin = event.event_coin;
		event_coin = event.event_rank;
		picture_url = event.picture_url;
	}
}
