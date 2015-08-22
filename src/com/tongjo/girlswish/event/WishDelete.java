package com.tongjo.girlswish.event;

import com.tongjo.bean.TJWish;

public class WishDelete extends BaseEvent {
	private TJWish wish;

	public WishDelete(TJWish wish) {
		super();
		this.wish = wish;
	}

	public TJWish getWish() {
		return wish;
	}

	public void setWish(TJWish wish) {
		this.wish = wish;
	}
}
