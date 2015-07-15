package com.tongjo.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TJWishList {
	private int total;
	private List<TJWish> wishes;

	public TJWishList(int total, List<TJWish> wishes) {
		super();
		this.total = total;
		this.wishes = wishes;
	}

	public TJWishList(List<TJWish> wishes) {
		this.wishes = wishes;
		this.total = size();
	}

	public TJWishList() {
		this.wishes = new ArrayList<TJWish>();
		this.total = size();
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

	public int size() {
		if (wishes == null) {
			return 0;
		} else {
			return wishes.size();
		}
	}

	public void clear() {
		wishes.clear();
		total = 0;
	}

	public TJWish getByUUID(UUID uuid) {
		if (wishes == null) {
			return null;
		}
		if (wishes.size() == 0) {
			return null;
		}
		for (int i = 0; i < wishes.size(); i++) {
			TJWish tjWish = wishes.get(i);
			if (tjWish.get_id().equals(uuid))
				return tjWish;
		}
		return null;
	}

	public TJWish getByUUID(String uuid) {
		UUID temp;
		try {
			temp = UUID.fromString(uuid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return getByUUID(temp);
	}

	public List<TJWish> getAll() {
		return wishes;
	}

	public void deleteByUUID(UUID uuid) {
		for (int i = 0; i < wishes.size(); i++) {
			if (wishes.get(i).get_id().equals(uuid)) {
				wishes.remove(i);
			}
		}
	}

	public void updateByUUID(TJWish tjWish) {
		for (int i = 0; i < wishes.size(); i++) {
			if (wishes.get(i).get_id().equals(tjWish.get_id())) {
				wishes.set(i, tjWish);
			}
		}
	}
}
