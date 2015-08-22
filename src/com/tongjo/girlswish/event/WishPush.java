package com.tongjo.girlswish.event;

import com.tongjo.bean.TJWish;

public class WishPush extends BaseEvent {
	 TJWish wish;

	public TJWish getWish() {
		return wish;
	}

	public void setWish(TJWish wish) {
		this.wish = wish;
	}

	public WishPush(TJWish wish) {
		super();
		this.wish = wish;
	}
	 
}
