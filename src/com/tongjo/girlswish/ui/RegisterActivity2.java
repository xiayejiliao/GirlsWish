package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.TakePicturePopup.ChoicedItem;
import com.tongjo.girlswish.ui.TakePicturePopup.onChoiced;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * 注册完善个人资料，包括昵称，学校
 * 
 * @author 16ren
 *
 */
public class RegisterActivity2 extends BaseActivity implements OnClickListener {
	private EditText et_name;
	private EditText et_school;
	private Button bt_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		setCenterText("注册");
		et_name = (EditText) findViewById(R.id.et_register2_name);
		et_school = (EditText) findViewById(R.id.et_register2_school);
		bt_next = (Button) findViewById(R.id.bt_register2_next);
		bt_next.setOnClickListener(this);

		et_school.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Intent intent = new Intent(RegisterActivity2.this, RegisterSchollChooseActivity.class);
					startActivityForResult(intent, AppConstant.STARTFORCODE_REGISTER_SCHOOL);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_register2_next:
			String name = et_name.getText().toString();
			String school = et_school.getText().toString();
			if (StringUtils.isEmpty(name)) {
				ToastUtils.show(getApplicationContext(), "姓名空");
				return;
			}
			if (StringUtils.isEmpty(school)) {
				ToastUtils.show(getApplicationContext(), "学校空");
				return;
			}
			RequestParams requestParams = new RequestParams();
			requestParams.put("nickname", name);
			requestParams.put("schoolId", school);
			asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_PROFILE, requestParams, httpprofile);
			break;
		case R.id.et_register2_school:
			Intent intent = new Intent(RegisterActivity2.this, RegisterSchollChooseActivity.class);
			startActivityForResult(intent, AppConstant.STARTFORCODE_REGISTER_SCHOOL);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppConstant.STARTFORCODE_REGISTER_SCHOOL && resultCode == AppConstant.RESULTCODE_REGISTER_SCHOOL) {
			String schooname = data.getStringExtra("schoolname");
			et_school.setText(schooname);
		}
	}

	private TextHttpResponseHandler httpprofile = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				// 完善信息成功
				if (response.getResult().getCode() == 0) {

					TJUserInfo userInfo = response.getData();
					if (userInfo != null) {
						SpUtils.put(getApplicationContext(), AppConstant.USER_LOGINSTATE, 1);
						SpUtils.put(getApplicationContext(), AppConstant.USER_ID, userInfo.get_id().toString());
						SpUtils.put(getApplicationContext(), AppConstant.USER_NAME, userInfo.getRealname());
						SpUtils.put(getApplicationContext(), AppConstant.USER_NICKNAME, userInfo.getNickname());
						SpUtils.put(getApplicationContext(), AppConstant.USER_SEX, userInfo.getGender());
						SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, userInfo.getAvatarUrl());
						SpUtils.put(getApplicationContext(), AppConstant.USER_EMAIL, userInfo.getEmail());
						TJSchool userSchool = userInfo.getSchool();
						if (userSchool != null) {
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLID, userSchool.get_id().toString());
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME, userSchool.getName());
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLCOORDINATES, userSchool.getCoordinates());
						}
					}
					Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
					startActivity(intent);
					RegisterActivity2.this.finish();

				} else {
					Toast.makeText(getApplicationContext(), "完善信息失败:" + response.getResult().getMessage(), Toast.LENGTH_LONG).show();
				}
			} else {
				ToastUtils.show(getApplicationContext(), "完善信息失败" + arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(getApplicationContext(), "完善信息失败" + arg3.toString());
		}
	};
}
