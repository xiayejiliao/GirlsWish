package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;

import de.greenrobot.event.EventBus;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 女生心愿还没有被摘时修改心愿
 */
public class GirlUnpickedWishActivity extends BaseActivity {
	private UUID currentwishuuuid;
	private TJWish currentwish;
	private Button bt_del;
	private Button bt_update;
	private EditText et_wish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_girlunpickedwish);
		setCenterText("未摘心愿");
		bt_del = (Button) findViewById(R.id.bt_girlunpickedwish_del);
		bt_update = (Button) findViewById(R.id.bt_girlunpickedwish_update);
		et_wish = (EditText) findViewById(R.id.et_girlunpickedwish_wish);
		String uu = getIntent().getStringExtra("wishuuid");
		currentwishuuuid = UUID.fromString(uu);
		currentwish = DataContainer.mewishs.getByUUID(currentwishuuuid);
		if (currentwish != null) {
			et_wish.setText(currentwish.getContent());
			bt_del.setOnClickListener(onClickListener);
			bt_update.setOnClickListener(onClickListener);
		}else {
			et_wish.setText("-----");
		}

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_girlunpickedwish_del:
				RequestParams delParams = new RequestParams();
				delParams.put("_id", currentwishuuuid.toString());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_WISHDEL, delParams, httpdelwish);
				break;
			case R.id.bt_girlunpickedwish_update:
				RequestParams updateParams = new RequestParams();
				updateParams.put("_id", currentwishuuuid.toString());
				updateParams.put("content", et_wish.getText().toString());
				updateParams.put("isCompleted", currentwish.getIsCompleted());
				updateParams.put("isPicked", currentwish.getIsPicked());
				updateParams.put("backgroundColor", currentwish.getBackgroundColor());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_WISHUPDATE, updateParams, httpupdatewih);
				break;

			default:
				break;
			}
		}
	};
	private TextHttpResponseHandler httpdelwish = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJWish>>() {
				}.getType();
				TJResponse<TJWish> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {

					DataContainer.mewishs.deleteByUUID(currentwishuuuid);
					et_wish.getEditableText().clear();
					Message mes = new Message();
					mes.what = AppConstant.MESSAGE_WHAT_GIRLWISH_DEL;
					mes.obj = response.getData();
					EventBus.getDefault().post(mes);
					ToastUtils.show(getApplicationContext(), "删除成功");
				} else {
					ToastUtils.show(getApplicationContext(), "删除失败:" + response.getResult().getMessage());
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(getApplicationContext(), "删除失败:" + arg3.toString());
		}
	};
	private TextHttpResponseHandler httpupdatewih = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJWish>>() {
				}.getType();
				TJResponse<TJWish> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					currentwish.setContent(et_wish.getText().toString());
					DataContainer.mewishs.updateByUUID(currentwish);
					Message mes = new Message();
					mes.what = AppConstant.MESSAGE_WHAT_GIRLWISH_UPDATE;
					mes.obj = response.getData();
					EventBus.getDefault().post(mes);
					ToastUtils.show(getApplicationContext(), "修改成功");
				} else {
					ToastUtils.show(getApplicationContext(), "修改失败" + response.getResult().getMessage());
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			ToastUtils.show(getApplicationContext(), "修改失败:" + arg3.toString());
		}
	};
}
