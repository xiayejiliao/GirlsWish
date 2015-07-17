package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

/**
 * 注册性别选择
 * 
 * @author 16ren
 *
 */
public class RegisterSexChooseActivity extends BaseActivity implements OnClickListener, OnTouchListener {
	private final static int GIRL = 0;
	private final static int BOY = 1;
	private LinearLayout linearLayout;

	private int choose=-1;
	private int screenwith;
	private int screenheight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_sexchoose);
		setCenterText("性别选择");
		linearLayout = (LinearLayout) findViewById(R.id.ll_register_sexchose);
		linearLayout.setOnClickListener(this);
		linearLayout.setOnTouchListener(this);

	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ll_register_sexchose){
			Intent intent=new Intent(RegisterSexChooseActivity.this,RegisterActivity1.class);
			if(choose==GIRL){
				intent.putExtra("sex",GIRL);
				startActivity(intent);
			}
			if(choose==BOY){
				intent.putExtra("sex",BOY);
				startActivity(intent);
			}
		
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.ll_register_sexchose) {
			screenwith = linearLayout.getWidth();
			screenheight = linearLayout.getHeight();
			float x = event.getX();
			float y = event.getY();
			int i = checkpostion((int) x, (int) y);
			if (i <= 0) {
				choose = GIRL;
			} else {
				choose = BOY;
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (choose==GIRL) {
					linearLayout.setBackgroundResource(R.drawable.image_girlchoose);
				} else {
					linearLayout.setBackgroundResource(R.drawable.image_boychoose);
				}
				break;
			case MotionEvent.ACTION_UP:
				linearLayout.setBackgroundResource(R.drawable.image_sexchoose);
				break;
			default:
				break;
			}
		}
		return false;
	}

	private int checkpostion(int x, int y) {
		int result = 0;
		result = x * screenheight + screenwith * y - screenheight * screenwith;
		return result;
	}
}
