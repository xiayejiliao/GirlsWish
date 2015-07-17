package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.TakePicturePopup.ChoicedItem;
import com.tongjo.girlswish.ui.TakePicturePopup.onChoiced;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class RegisterActivity2 extends BaseActivity implements OnClickListener {
	private ImageButton iv_personicon;
	private Button bt_sure;
	private TakePicturePopup takePicturePopup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		setCenterText("注册");
		iv_personicon = (ImageButton) findViewById(R.id.iv_register2_personico);
		bt_sure = (Button) findViewById(R.id.bt_register2_sure);
		iv_personicon.setOnClickListener(this);
		bt_sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_register2_sure:
			//takePicturePopup.dismiss();
			break;
		case R.id.iv_register2_personico:
			takePicturePopup =new TakePicturePopup(this);
			takePicturePopup.showAsDropDown(v,0,-takePicturePopup.getHeight());
			takePicturePopup.setOutsideTouchable(true);
			break;
		default:
			break;
		}
	}
}
