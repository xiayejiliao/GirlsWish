package com.tongjo.girlswish.ui;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.LinkTextView;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends BaseActivity implements OnClickListener{
	
	private Button bt_login;
	private EditText et_phone;
	private EditText et_pass;
	private ImageButton iv_personico;
	private LinkTextView ltv_forgetpass;
	private LinkTextView ltv_register;
	private SyncHttpClient syncHttpClient;
	private AsyncHttpClient asyncHttpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		bt_login=(Button)findViewById(R.id.bt_login_sure);
		et_phone=(EditText)findViewById(R.id.et_login_name);
		et_pass=(EditText)findViewById(R.id.et_login_pass);
		ltv_forgetpass=(LinkTextView)findViewById(R.id.ltv_login_forget);
		ltv_register=(LinkTextView)findViewById(R.id.ltv_login_register);
		iv_personico=(ImageButton)findViewById(R.id.iv_login_personico);
		
		bt_login.setOnClickListener(this);
		iv_personico.setOnClickListener(this);
		ltv_forgetpass.setOnClickListener(this);
		ltv_register.setOnClickListener(this);
		syncHttpClient=((BaseApplication)getApplication()).getSyncHttpClient();
		asyncHttpClient=((BaseApplication)getApplication()).getAsyncHttpClient();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_sure:
			RequestParams requestParams=new RequestParams();
			requestParams.add("tel", et_phone.getText().toString());
			requestParams.add("password", et_pass.getText().toString());
			asyncHttpClient.post("http://api.wish.tongjo.com/login", requestParams, new BaseJsonHttpResponseHandler<TJResponse<TJUserInfo>>() {

				@Override
				public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, TJResponse<TJUserInfo> arg4) {
					
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2, TJResponse<TJUserInfo> arg3) {
					System.out.println(arg3.getResult().getCode());
				}

				@Override
				protected TJResponse<TJUserInfo> parseResponse(String arg0, boolean arg1) throws Throwable {
					Type type = new TypeToken<TJResponse<TJUserInfo>>(){}.getType();
					return new Gson().fromJson(arg0, type);
				}
			});
			break;

		default:
			break;
		}
		
	}
	
}
