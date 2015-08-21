package com.tongjo.girlswish.ui;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;

public class AddWishActivity extends BaseActivity {
	private final String TAG = "AddWishActivity";
	private EditText mEditText = null;
	
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addwish);

		/*setRightBtn("完成");*/
		InitView();
	}

	public void InitView() {
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("添加心愿");
		toolbar.inflateMenu(R.menu.addwish);
		
		//设置导航按钮
		toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	AddWishActivity.this.finish();
		    }
		});
		//menu点击事件
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
		    @Override
		    public boolean onMenuItemClick(MenuItem item) {
		        int id = item.getItemId();
		        if (id == R.id.action_addwish) {
		        	String content = mEditText.getText().toString().trim();
					if(StringUtils.isEmpty(content)){
						ToastUtils.show(getApplicationContext(), "内容不能为空");
						return false;
					}
					
					addWish(content);
		        }
		        return false;
		    }
		});
		
		mEditText = (EditText)findViewById(R.id.wish_content);
	}

	public void addWish(String content){
		RequestParams requestParams = new RequestParams();
		requestParams.add("content", content);
		requestParams.add("backgroundColor", "9CCF98");
		asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_ADDWISH,
				requestParams, new TextHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						if (arg2 == null) {
							ToastUtils.show(getApplicationContext(), "发布心愿失败:");
							return;
						}
						Log.d(TAG, arg2);
						if (arg0 == 200) {
							Toast.makeText(getApplicationContext(), "发布心愿成功" + arg0,
									Toast.LENGTH_LONG).show();
						}	
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) { 
						Toast.makeText(getApplicationContext(), "发布心愿失败" + arg0,
								Toast.LENGTH_LONG).show();
					}
				});
	}
}
