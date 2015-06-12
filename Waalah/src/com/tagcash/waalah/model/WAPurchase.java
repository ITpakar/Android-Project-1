package com.tagcash.waalah.model;

public class WAPurchase {
	public int id; // primary key
	public String price;
	public String title;

	
	private void init() {
		price = "";
		title = "";
	}

	public WAPurchase() {
		init();
	}

	public WAPurchase(WAPurchase purchase) {
		init();
		if (purchase == null)
			return;
		
		price = purchase.price;
		title = purchase.title;		
	}
}
