package com.tongjo.bean;

import java.util.List;

public class TJWishList {
	private int total;
	private List<TJWish> wishes;

	public TJWishList(int total, List<TJWish> wishes) {
		super();
		this.total = total;
		this.wishes = wishes;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<TJWish> getWishes() {
		return wishes;
	}
	public void setWishes(List<TJWish> wishes) {
		this.wishes = wishes;
	}
	
}
