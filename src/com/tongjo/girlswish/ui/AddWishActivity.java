package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJWishList;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.WishPush;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.CustomeProgressDialog;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class AddWishActivity extends BaseActivity {
	private final String TAG = "AddWishActivity";
	private EditText mEditText = null;
	private TextView mInputNumber = null;

	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addwish);

		/* setRightBtn("完成"); */
		InitView();
	}

	public void InitView() {

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("添加心愿");
		toolbar.inflateMenu(R.menu.addwish);

		// 设置导航按钮
		toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddWishActivity.this.finish();
			}
		});
		// menu点击事件
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();
				if (id == R.id.action_addwish) {
					String content = mEditText.getText().toString().trim();
					if (StringUtils.isEmpty(content)) {
						ToastUtils.show(getApplicationContext(), "内容不能为空");
						return false;
					}

					addWish(content);
				}
				return false;
			}
		});

		mInputNumber = (TextView)findViewById(R.id.text_number);
		
		mEditText = (EditText) findViewById(R.id.wish_content);
		mEditText.setFocusable(true);
		mEditText.setFocusableInTouchMode(true);
		mEditText.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);

		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (start - count + after > 49) {
					ToastUtils.show(AddWishActivity.this, "字数不能超过50个");
				}

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				mInputNumber.setText("已输入："+s.length()+"/50");
			}

		});

	}
	
	
	/**
	 * 当前用户信息是否完整
	 * @return
	 */
	public boolean isUserInfoComplete(){
		String name = SpUtils.get(getApplication(), AppConstant.USER_NICKNAME, "").toString();
		String school = SpUtils.get(getApplication(), AppConstant.USER_SCHOOLNAME, "").toString();
		int sex = (Integer) SpUtils.get(getApplication(), AppConstant.USER_SEX, -1);
		String phone = SpUtils.get(getApplication(), AppConstant.USER_PHONE, "").toString();
		String iconurl = SpUtils.get(getApplication(), AppConstant.USER_ICONURL, "").toString();
		
		if(StringUtils.isBlank(name) || StringUtils.isBlank(school) || StringUtils.isBlank(phone) 
				|| StringUtils.isBlank(iconurl) || sex == -1){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 发布心愿
	 * @param content
	 */
	public void addWish(String content) {
		
		if(!isUserInfoComplete()){
			ToastUtils.show(getApplication(), "对不起，请先完善你的个人信息，然后再发布心愿哦");
			Intent intent = new Intent(AddWishActivity.this,
					MyinfoActivity.class);
			startActivity(intent);
			return;
		}
		
		if(AddWishActivity.this == null){
			return;
		}
		
		final CustomeProgressDialog dialog = new CustomeProgressDialog(AddWishActivity.this,"正在努力发布中!");
		dialog.show();
		
		RequestParams requestParams = new RequestParams();
		requestParams.add("content", content);
		requestParams.add("backgroundColor", "9CCF98");
		asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_ADDWISH, requestParams, new TextHttpResponseHandler("UTF-8") {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				if (arg2 == null) {
					ToastUtils.show(getApplicationContext(), "发布心愿失败:");
					return;
				}
				Log.d(TAG, arg2);
				if (arg0 == 200) {
					Type type = new TypeToken<TJResponse<Object>>() {
					}.getType();
					TJResponse<Object> response = null;
					try {
						response = new Gson().fromJson(arg2, type);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (response == null || response.getResult() == null) {
						ToastUtils.show(AddWishActivity.this, "发布失败");
						return;
					}

					// 0成功
					if (response.getResult().getCode() == 0) {
						Toast.makeText(getApplicationContext(), "恭喜您，发布心愿成功了！", Toast.LENGTH_LONG).show();
						EventBus.getDefault().post(new WishPush(null));
						AddWishActivity.this.finish();
					}
					// 1没有登录
					else if (response.getResult().getCode() == 1) {
						ToastUtils.show(AddWishActivity.this, "对不起，您没有登录，请重新登录");
					}
					// 2次数超过限制
					else if (response.getResult().getCode() == 2) {
						ToastUtils.show(AddWishActivity.this, "对不起，发布心愿失败了:" + response.getResult().getMessage());
					}

					// 3信息不完善
					else if (response.getResult().getCode() == 3) {
						ToastUtils.show(AddWishActivity.this, "对不起，发布心愿失败了:" + response.getResult().getMessage());

						Intent intent = new Intent(AddWishActivity.this, MyinfoActivity.class);
						startActivity(intent);
						AddWishActivity.this.finish();
					} else {
						ToastUtils.show(AddWishActivity.this, "发布心愿失败了:" + response.getResult().getMessage());
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), "发布心愿失败" + arg0, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		//友盟页面统计
		MobclickAgent.onPageStart("添加心愿");
		//友盟用户活跃度统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("添加心愿");
		MobclickAgent.onPause(this);
	}
}
