package com.mindidea.feelfit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
	//private static final String TAG = "MyService";
	
	@Override
	public IBinder onBind(Intent arg0){
		return null;
	}
	
	@Override
	public void onCreate(){
		
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		
	}
	
	@Override
	public void onDestroy(){
		
	}

}
