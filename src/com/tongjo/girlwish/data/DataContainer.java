package com.tongjo.girlwish.data;

import java.util.ArrayList;
import java.util.List;

import com.tongjo.bean.TJWish;

/**
 * 用于在内存在存储临时数据
 * 新建一个存储的时候，记得在clear中写清除方法
 * @author fuzhen
 *
 */
public class DataContainer {
	public final static List<TJWish> WishList = new ArrayList<TJWish>();
	
	public void clear(){
		if(WishList != null){
			WishList.clear();
		}
	}
} 
