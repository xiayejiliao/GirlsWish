package com.tongjo.girlswish.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RegisterSchollChooseActivity extends BaseActivity {
	private enum CHOOSE {
		COUNTRY, PROVINCE, SHOOL;
	}

	private String school;
	private ListView listView;
	private CHOOSE choose = CHOOSE.COUNTRY;
	private JSONArray tempjsonArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCenterText("选择国籍");
		setContentView(R.layout.activity_register_schoolchoose);
		listView = (ListView) findViewById(R.id.lv_register_school);

		try {
			InputStream in = getAssets().open("school.txt");
			school = readSchool(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			tempjsonArray = new JSONArray(school);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listView.setAdapter(new CountryAdapter(tempjsonArray));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (choose == CHOOSE.COUNTRY) {
					JSONObject jsonObject = (JSONObject) arg0.getItemAtPosition(arg2);
					try {
						String provs=jsonObject.getString("provs");
						String univs=jsonObject.getString("univs");
					
			
						System.out.println(provs);
						System.out.println(univs);
						if (!StringUtils.isEmpty(provs) && StringUtils.isEmpty(univs)) {
							JSONArray provsJsonArray = new JSONArray(provs);
							listView.setAdapter(new ProvinceAdapter(provsJsonArray));
							choose = CHOOSE.PROVINCE;
							setCenterText("选择地区");
							return;
						}
						if (StringUtils.isEmpty(provs) &&!StringUtils.isEmpty(univs)) {
							JSONArray univsJsonArray = new JSONArray(univs);
							listView.setAdapter(new SchoolAdapter(univsJsonArray));
							choose = CHOOSE.SHOOL;
							setCenterText("选择学校");
							return;
						}
						return;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (choose == CHOOSE.PROVINCE) {
					JSONObject jsonObject = (JSONObject) arg0.getItemAtPosition(arg2);

					try {
						JSONArray univsJsonArray = jsonObject.getJSONArray("univs");
						listView.setAdapter(new SchoolAdapter(univsJsonArray));
						choose = CHOOSE.SHOOL;
						setCenterText("选择学校");
						return;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (choose == CHOOSE.SHOOL) {
					JSONObject jsonObject = (JSONObject) arg0.getItemAtPosition(arg2);
					try {
						String name= jsonObject.getString("name");
						Intent intent=new Intent();
						intent.putExtra("shoolname", name);
						setResult(AppConstant.RESULTCODE_REGISTER_SCHOOL, intent);
						RegisterSchollChooseActivity.this.finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	private String readSchool(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer stringBuffer = new StringBuffer();
		int line = 1;
		String tempString;
		try {
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				stringBuffer.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return stringBuffer.toString();
	}

	private class CountryAdapter extends BaseAdapter {
		private JSONArray countryArray;

		public CountryAdapter(JSONArray countryArray) {
			super();
			this.countryArray = countryArray;
		}

		@Override
		public int getCount() {
			if (countryArray == null) {
				return 0;
			} else {
				return countryArray.length();
			}
		}

		@Override
		public Object getItem(int position) {
			if (countryArray == null) {
				return null;
			} else {
				try {

					return countryArray.getJSONObject(position);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHandle viewhandle = null;
			if (convertView == null) {
				viewhandle = new ViewHandle();
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				convertView = inflater.inflate(R.layout.listitem_school_area, null);
				viewhandle.text = (TextView) convertView.findViewById(R.id.tv_listshool_area);
				convertView.setTag(viewhandle);
			} else {
				viewhandle = (ViewHandle) convertView.getTag();
			}
			try {
				JSONObject countryObject = countryArray.getJSONObject(position);
				String countryname = countryObject.getString("name");
				viewhandle.text.setText(countryname);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

	}

	private class ProvinceAdapter extends BaseAdapter {
		private JSONArray provinceArray;

		public ProvinceAdapter(JSONArray provinceArray) {
			super();
			this.provinceArray = provinceArray;
		}

		@Override
		public int getCount() {
			if (provinceArray == null) {
				return 0;
			} else {
				return provinceArray.length();
			}
		}

		@Override
		public Object getItem(int position) {
			if (provinceArray == null) {
				return null;
			} else {
				try {
					return provinceArray.getJSONObject(position);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHandle viewhandle = null;
			if (convertView == null) {
				viewhandle = new ViewHandle();
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				convertView = inflater.inflate(R.layout.listitem_school_area, null);
				viewhandle.text = (TextView) convertView.findViewById(R.id.tv_listshool_area);
				convertView.setTag(viewhandle);
			} else {
				viewhandle = (ViewHandle) convertView.getTag();
			}
			try {
				JSONObject provincObject = provinceArray.getJSONObject(position);
				String provincname = provincObject.getString("name");
				viewhandle.text.setText(provincname);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

	}

	private class SchoolAdapter extends BaseAdapter {
		private JSONArray schoolArray;

		public SchoolAdapter(JSONArray schoolArray) {
			super();
			this.schoolArray = schoolArray;
		}

		@Override
		public int getCount() {
			if (schoolArray == null) {
				return 0;
			} else {
				return schoolArray.length();
			}
		}

		@Override
		public Object getItem(int position) {
			if (schoolArray == null) {
				return null;
			} else {
				try {
					return schoolArray.getJSONObject(position);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHandle viewhandle = null;
			if (convertView == null) {
				viewhandle = new ViewHandle();
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				convertView = inflater.inflate(R.layout.listitem_school_area, null);
				viewhandle.text = (TextView) convertView.findViewById(R.id.tv_listshool_area);
				viewhandle.iv = (ImageView) convertView.findViewById(R.id.iv_listitem_into);
				convertView.setTag(viewhandle);
			} else {
				viewhandle = (ViewHandle) convertView.getTag();
			}
			try {
				JSONObject provinceObject = schoolArray.getJSONObject(position);
				String provincename = provinceObject.getString("name");
				viewhandle.text.setText(provincename);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewhandle.iv.setVisibility(View.INVISIBLE);
			return convertView;
		}

	}

	private class ViewHandle {
		private TextView text;
		private ImageView iv;
	}

}
