package com.tongjo.girlswish.ui;

import org.apache.http.Header;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.LinkTextView;

import android.app.Activity;
import android.os.Bundle;
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
			requestParams.add("tel", "1234");
			requestParams.add("password", "123");
			asyncHttpClient.post("http://api.wish.tongjo.com/login", requestParams, new BaseJsonHttpResponseHandler<JsonObject>() {

				@Override
				public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, JsonObject arg4) {
					// TODO Auto-generated method stub
					System.out.println("----------"+arg3);
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2, JsonObject arg3) {
					// TODO Auto-generated method stub
					System.out.println("+++++++++++"+arg2);
				}

				@Override
				protected JsonObject parseResponse(String arg0, boolean arg1) throws Throwable {
					// TODO Auto-generated method stub
					System.out.println("!!!!!!!!"+arg0);
					return null;
				}

		
			});
			break;

		default:
			break;
		}
		
	}
	
}
