package com.tongjo.girlswish.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import u.aly.v;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 选择学校界面 学校资源放在assets文件夹里的school文件里面，格式是json的
 * 
 * @author dell
 *
 */
public class RegisterSchollChooseActivity extends AppCompatActivity {

	private ListView mListView;
	private Context mContext;
	private TextView mTextView;
	private AsyncHttpClient mAsyncHttpClient;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_schoolchoose);
		mContext = getApplicationContext();
		mActionBar=getSupportActionBar();
		mActionBar.setTitle("选择学校");
		mListView = (ListView) findViewById(R.id.lv_register_school);
		mTextView = (TextView) findViewById(R.id.tv_schoolchoose_info);
		mTextView.setVisibility(View.GONE);
		mAsyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();
		mAsyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_GETSCHOOL, getschool);
	}

	private TextHttpResponseHandler getschool = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Schools>>() {
				}.getType();
				TJResponse<Schools> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					if (response.getData() != null) {
						mListView.setAdapter(new SchoolAdapter(response.getData().getSchoolList()));
					} else {
						mTextView.setVisibility(View.VISIBLE);
					}
				} else {
					mTextView.setVisibility(View.VISIBLE);
				}
			} else {
				mTextView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			mTextView.setVisibility(View.VISIBLE);
		}
	};

	class SchoolAdapter extends BaseAdapter {

		private ArrayList<TJSchool> schools;

		public SchoolAdapter(ArrayList<TJSchool> schools) {
			super();
			this.schools = schools;
		}

		@Override
		public int getCount() {
			if (schools == null) {
				return 0;
			}
			return schools.size();
		}

		@Override
		public Object getItem(int position) {
			if (schools == null) {
				return null;
			}
			return schools.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view= LayoutInflater.from(mContext).inflate(R.layout.listitem_school_area, null);
			TextView textView=(TextView) view.findViewById(R.id.tv_listshool_area);
			textView.setText(schools.get(position).getName());
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("schoolid", schools.get(position).get_id().toString());
					intent.putExtra("schoolname", schools.get(position).getName());
					setResult(RESULT_OK, intent);
					RegisterSchollChooseActivity.this.finish();
				}
			});
			return view;
		}

	}

	class Schools {
		ArrayList<TJSchool> schoolList;

		public Schools() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ArrayList<TJSchool> getSchoolList() {
			return schoolList;
		}

		public void setSchoolList(ArrayList<TJSchool> schoolList) {
			this.schoolList = schoolList;
		}

	}
}
