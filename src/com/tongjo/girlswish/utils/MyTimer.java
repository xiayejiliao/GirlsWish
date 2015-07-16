package com.tongjo.girlswish.utils;

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
	private final int START = 0;
	private final int PROGRESS = 1;
	private final int FINISH = 2;
	private int times;
	private int remaining;
	private TimerProgress timerProgress;
	private Handler handler;

	public MyTimer(int times) {
		super();
		this.times = times;
		remaining = times;
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
						timerProgress.onProgress(msg.arg1, msg.arg2);;
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
		;
	}

	public MyTimer setTimerProgress(TimerProgress timerProgress) {
		this.timerProgress = timerProgress;
		return this;
	}

	public interface TimerProgress {
		void onStart();

		void onProgress(int toals, int remaining);

		void onFinish();
	}
}
