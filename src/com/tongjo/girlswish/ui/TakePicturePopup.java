package com.tongjo.girlswish.ui;

import java.text.ChoiceFormat;

import com.tongjo.girlswish.R;

import android.content.Context;
import android.provider.Telephony.Sms.Conversations;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class TakePicturePopup extends PopupWindow implements OnClickListener {

	public enum ChoicedItem {
		CAMERA, LOCAL, CANCEL
	}

	private Button bt_camera;
	private Button bt_local;
	private Button bt_cancel;
	private Context mycontext;
	private onChoiced myonchoiced;

	public TakePicturePopup(Context context) {
		mycontext = context;
		View view = LayoutInflater.from(mycontext).inflate(R.layout.popupwindow_takepicture, null);
		bt_camera = (Button) view.findViewById(R.id.bt_takepicturepopup_camera);
		bt_local = (Button) view.findViewById(R.id.bt_takepicturepopup_local);
		bt_cancel = (Button) view.findViewById(R.id.bt_takepicturepopup_cancel);
		bt_camera.setOnClickListener(this);
		bt_local.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		setContentView(view);
		super.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	public void setOnChoiced(onChoiced onChoiced) {
		myonchoiced = onChoiced;
	}

	@Override
	public void onClick(View v) {
		if (myonchoiced == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.bt_takepicturepopup_camera:	
			myonchoiced.Choiced(ChoicedItem.CAMERA);
			break;
		case R.id.bt_takepicturepopup_local:
			myonchoiced.Choiced(ChoicedItem.LOCAL);
			break;
		case R.id.bt_takepicturepopup_cancel:
			myonchoiced.Choiced(ChoicedItem.CANCEL);
			break;

		default:
			break;
		}
	}

	interface onChoiced {
		public void Choiced(ChoicedItem item);
	}
}
