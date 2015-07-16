package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.RandomUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author 16ren 输入新密码
 */

public class ResetPassActivity2 extends BaseActivity {
	private final static String TAG = "ResetPassActivity2";
	private EditText et_pass;
	private EditText et_repass;
	private Button bt_sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpass2);
		et_pass = (EditText) findViewById(R.id.et_resetpass2_pass);
		et_repass = (EditText) findViewById(R.id.et_resetpass2_repass);
		bt_sure = (Button) findViewById(R.id.bt_resetpass2_sure);
		bt_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pass = et_pass.getText().toString();
				String repass = et_repass.getText().toString();
				if (StringUtils.isEmpty(pass) || StringUtils.isEmpty(repass)) {
					ToastUtils.show(getApplicationContext(), "信息不完整");
					return;
				}
				if (!pass.equals(repass)) {
					ToastUtils.show(getApplicationContext(), "密码不一致");
					return;
				}
				if (pass.length() < 6) {
					ToastUtils.show(getApplicationContext(), "密码小于6位");
					return;
				}
				RequestParams requestParams = new RequestParams();
				requestParams.put("password", et_pass.getText().toString());
				requestParams.put("authcode", RandomUtils.getRandom(1000, 9999));
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_RESETPASSWORD, requestParams, resetpass);
			}
		});
	}

	private TextHttpResponseHandler resetpass = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				if(response.getResult().getCode()==0){
					
				}else {
					ToastUtils.show(getApplicationContext(), "修改失败"+response.getResult().getMessage());
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};
}
