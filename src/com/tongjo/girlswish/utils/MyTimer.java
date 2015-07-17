package com.tongjo.girlswish.utils;

import android.R.integer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

/**
 * 定时器
 * 
 * @author 16ren
 *
 */

public class MyTimer extends Thread {
	private final int START = 3256;
	private final int PROGRESS = 3257;
	private final int FINISH = 3258;
	private int times;
	private int remaining;
	private TimerProgress timerProgress;
	private Handler handler;

	public MyTimer() {
		super();
		handler = new Handler(Looper.myLooper(), new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case START:
					if (timerProgress != null)
						timerProgress.onStart();
					break;
				case PROGRESS:
					if (timerProgress != null)
						timerProgress.onProgress(msg.arg1, msg.arg2);
					break;
				case FINISH:
					if (timerProgress != null) {
						timerProgress.onFinish();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	public void begin(int times, TimerProgress timerProgress) {
		this.times = times;
		remaining = times;
		this.timerProgress = timerProgress;
		this.start();
	}

	public void end() {
		handler.obtainMessage(FINISH).sendToTarget();
		this.stop();
	}
	
	@Override
	public void run() {

		handler.obtainMessage(START).sendToTarget();
		while (remaining > -1) {
			handler.obtainMessage(PROGRESS, times, remaining).sendToTarget();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.obtainMessage(FINISH).sendToTarget();
				;
			}
			remaining--;
		}
		handler.obtainMessage(FINISH).sendToTarget();
	}

	public interface TimerProgress {
		void onStart();

		void onProgress(int toals, int remaining);

		void onFinish();
	}
}
