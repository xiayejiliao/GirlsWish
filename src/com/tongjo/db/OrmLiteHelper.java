package com.tongjo.db;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
/**
 * 数据持久工具。
 * @author 16ren
 * @data 2015/7/11
 * 
 */
public class OrmLiteHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	private static final String DATABASE_NAME = "parkshare.db";
	private static final int DATABASE_VERSION = 1;
	
	private Dao<TJUserInfo, UUID> tjuserinfoDao;
	private Dao<TJSchool, UUID> tjschoolDao;
	private Dao<TJWish, UUID> tjwishDao;
	private Dao<TJMessage, UUID> tjmessageDao;

	public OrmLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub

		try {
			TableUtils.createTableIfNotExists(arg1, TJUserInfo.class);
			TableUtils.createTableIfNotExists(arg1, TJSchool.class);
			TableUtils.createTableIfNotExists(arg1, TJWish.class);
			TableUtils.createTableIfNotExists(arg1, TJMessage.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(arg1, TJUserInfo.class, true);
			TableUtils.dropTable(arg1, TJSchool.class, true);
			TableUtils.dropTable(arg1, TJWish.class, true);
			TableUtils.dropTable(arg1, TJMessage.class, true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Dao<TJUserInfo, UUID> getTJUserInfoDao() {
		if(tjuserinfoDao==null){
			try {
				tjuserinfoDao=getDao(TJUserInfo.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("+++++++"+e.toString());
				e.printStackTrace();
				return null;
			}
		}
		return tjuserinfoDao;
	}
	public Dao<TJSchool, UUID> getTJSchoolDao() {
		if(tjschoolDao==null){
			try {
				tjschoolDao=getDao(TJSchool.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tjschoolDao;
	}
	public Dao<TJWish, UUID> getTJWishDao() {
		if(tjwishDao==null){
			try {
				tjwishDao=getDao(TJWish.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tjwishDao;
	}
	public Dao<TJMessage, UUID> getTJMessageDao() {
		if(tjmessageDao==null){
			try {
				tjmessageDao=getDao(TJMessage.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tjmessageDao;
	}
}
