package com.example.android_step.service;

import com.example.android_step.detector.StepDetectot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class StepCounterService extends Service {
	//服务运行状态
	public static boolean FLAG = false;
	
	//传感器服务
	private SensorManager mSensorManager;
	//传感器监听对象
	private StepDetectot mStepDetectot;
	//电池管理服务
	private PowerManager mPowerManager;
	//屏幕灯
	private WakeLock mWakeLock;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//标记服务为运行状态
		FLAG = true;
		//创建监听器
		mStepDetectot = new StepDetectot(this);
		//获取传感器的服务，初始化传感器
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		//注册传感器，注册监听器
		mSensorManager.registerListener(mStepDetectot, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_FASTEST);
		
		//电源管理服务
		mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP, 
				"s");
		mWakeLock.acquire();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FLAG = false;
		if(mStepDetectot != null){
			mSensorManager.unregisterListener(mStepDetectot);
		}
		if(mWakeLock != null){
			mWakeLock.release();
		}
	}
}
