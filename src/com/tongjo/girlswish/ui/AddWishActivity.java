package com.tongjo.girlswish.ui;

import org.apache.http.Header;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private ViewGroup bg = null;
	private EditText mEditText = null;
	private View mBtn1 = null;
	private View mBtn2 = null;
	private View mBtn3 = null;
	private View mBtn4 = null;

	private final String[] colors = new String[] { "9CCF98", "F1F1BC",
			"EBD0E6", "9AB8D8" };
	private final int[] colorsId = new int[] { R.color.addwish_greenColor,
			R.color.addwish_yellowColor, R.color.addwish_redColor,
			R.color.addwish_blueColor };
	
	private int currentColor = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addwish);

		setRightBtn("完成");
		InitView();
	}

	public void InitView() {
		mEditText = (EditText) findViewById(R.id.addwish_edit);
		mBtn1 = (View) findViewById(R.id.addwish_btn1);
		mBtn2 = (View) findViewById(R.id.addwish_btn2);
		mBtn3 = (View) findViewById(R.id.addwish_btn3);
		mBtn4 = (View) findViewById(R.id.addwish_btn4);
		bg = (ViewGroup) findViewById(R.id.addwish_bg);

		mBtn1.setOnClickListener(new ColorClickListener(0));
		mBtn2.setOnClickListener(new ColorClickListener(1));
		mBtn3.setOnClickListener(new ColorClickListener(2));
		mBtn4.setOnClickListener(new ColorClickListener(3));
		
		getRightBtnTextView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String content = mEditText.getText().toString().trim();
				if(StringUtils.isEmpty(content)){
					ToastUtils.show(getApplicationContext(), "内容不能为空");
					return;
				}
				
				addWish(content);
			}
		});
	}

	public void addWish(String content){
		RequestParams requestParams = new RequestParams();
		requestParams.add("content", content);
		requestParams.add("backgroundColor", colors[currentColor]);
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
	
	class ColorClickListener implements OnClickListener {
		private int mPosition = 0;

		public ColorClickListener(int position) {
			if(position < 0 || position > 3){
				mPosition = 0;
			}
			mPosition = position;
			currentColor = mPosition;
		}

		@Override
		public void onClick(View v) {
			bg.setBackgroundResource(colorsId[mPosition]);
		}
	}
}
