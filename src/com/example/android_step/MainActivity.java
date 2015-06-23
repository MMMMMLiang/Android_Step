package com.example.android_step;

import com.example.android_step.detector.StepDetectot;
import com.example.android_step.service.StepCounterService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv_step;
	private Button btn_start;
	private Button btn_stop;
	private Thread mThread;
	
	private int total_step = 0;
	
	Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0x00){
				countStep();
				tv_step.setText(total_step+"");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_step = (TextView) findViewById(R.id.tv_step);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		
		
	}
	
	public void btnClick(View view){
		Intent service = new Intent(this, StepCounterService.class);
		switch (view.getId()) {
		case R.id.btn_start:
			startService(service);
			btn_start.setEnabled(false);
			btn_stop.setEnabled(true);
			if(mThread == null){
				mThread = new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						int temp = 0;
						while (true) {
							try {
								sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(StepCounterService.FLAG){
								Log.i("istrue", "true");
								if (temp != StepDetectot.CURRENT_SETP) {
									temp = StepDetectot.CURRENT_SETP;
								}
								mHandler.sendEmptyMessage(0x00);
							}
						}
					}
				};
				mThread.start();
			}
			break;
		case R.id.btn_stop:
			stopService(service);
			btn_start.setEnabled(true);
			btn_stop.setEnabled(false);
			StepDetectot.CURRENT_SETP = 0;
			
			mHandler.removeCallbacks(mThread);
			break;

		default:
			break;
		}
	}
	
	private void countStep(){
		if(StepDetectot.CURRENT_SETP % 2 == 0){
			total_step = StepDetectot.CURRENT_SETP;
		}else{
			total_step = StepDetectot.CURRENT_SETP + 1;
		}
		Log.i("total_step", StepDetectot.CURRENT_SETP+"");
		total_step = StepDetectot.CURRENT_SETP;
	}

}
