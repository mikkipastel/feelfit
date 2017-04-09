package com.mindidea.feelfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class display extends Activity {
	TableRow logo_display;
	TextView show_userresult;
	TextView km_touch, cal_touch; 
	ImageView stop_to;
	
	SensorManager mSensorManager;
	Sensor mSensor;
	
	int weight;
	String weight_str;
	String test_calsum, test_distance; //output
	
	float a1 = 0, a2 = 0, a3 = 0, a_Avg_Last = 0, a_Avg_This = 0, a_Delta = 0;
	float cal_this, cal_sum = 0, distance;
	int v = 0;
	int time_total;
	float time_k;
	
	ImageView tabgraph, tabfb, tabkmcal;
	boolean d_id, c_id = true;
	long time_begin, time_end, time_tmp;
	
	int cal_sum_int, distance_int;
	
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.display);
		
		//show km/cal
		show_userresult = (TextView)findViewById(R.id.show_result);
		
		//intent weight from graph
		weight = getIntent().getIntExtra("user_weight", 0);
		weight_str = String.format("%d", weight);
		Log.d("user_weight", weight_str);
		
		//start time
		time_begin = System.currentTimeMillis();
		
		//to contact us
		logo_display = (TableRow)findViewById(R.id.toptab_display);
		logo_display.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goContact = new Intent(display.this,contact.class);
				startActivity(goContact);
			}});
		
		//touch km / cal	
		km_touch = (TextView)findViewById(R.id.km_view);
		km_touch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) { //d_id = true
				d_id = true;
				c_id = false;
				km_touch.setTextColor(getResources().getColor(R.color.black));
				cal_touch.setTextColor(getResources().getColor(R.color.gray));
				show_userresult.setText(test_distance);
			}});
		cal_touch = (TextView)findViewById(R.id.cal_view);
		cal_touch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) { //c_id = true
				c_id = true;
				d_id = false;
				km_touch.setTextColor(getResources().getColor(R.color.gray));
				cal_touch.setTextColor(getResources().getColor(R.color.black));
				show_userresult.setText(test_calsum);
			}});
		
		//stop_process
		stop_to = (ImageView)findViewById(R.id.stop_process);
		stop_to.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent goPauseProcess = new Intent(display.this,comfirm_stop.class);
				goPauseProcess.putExtra("distance", distance);
				goPauseProcess.putExtra("cal_sum", cal_sum);
				goPauseProcess.putExtra("time_total", time_total);
				goPauseProcess.putExtra("user_weight", weight);
				startActivity(goPauseProcess);
			}});
		
		
		//connect accelerometer
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		//go graph
		tabgraph = (ImageView)findViewById(R.id.buttom_graph);
		tabgraph.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(display.this, "feelfit running", Toast.LENGTH_SHORT).show();
				//Intent goGraph = new Intent(display.this,graph_history.class);
				Intent goGraph = new Intent(display.this,graph_myGraph.class);
				goGraph.putExtra("user_weight", weight);
				goGraph.putExtra("last_process", true);
				startActivity(goGraph);
			}});
		
		//shared result to facebook
		tabfb = (ImageView)findViewById(R.id.buttom_fb);
		tabfb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goFacebook = new Intent(display.this,fbSharedDialog.class);
				//Intent goFacebook = new Intent(display.this,TestConnect.class);
				//goFacebook.putExtra("test_calsum", test_calsum);
				//goFacebook.putExtra("test_distance", test_distance);
				goFacebook.putExtra("distance", distance);
				goFacebook.putExtra("cal_sum", cal_sum);
				//goFacebook.putExtra("time_total", time_total);
				startActivity(goFacebook);
			}});
		
		//change km <-/-> cal
		tabkmcal = (ImageView)findViewById(R.id.buttom_kmcal);
		tabkmcal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if (d_id == true){ //show km
					d_id = false;
					c_id = true	;	
					km_touch.setTextColor(getResources().getColor(R.color.black));
					cal_touch.setTextColor(getResources().getColor(R.color.gray));
					show_userresult.setText(test_distance);
				} 
				if (c_id == true) { //show cal
					c_id = false;
					d_id = true;
					km_touch.setTextColor(getResources().getColor(R.color.gray));
					cal_touch.setTextColor(getResources().getColor(R.color.black));
					show_userresult.setText(test_calsum);
				}*/
			}});
	}
	
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(accelListener, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}
 
	public void onStop() {
		super.onStop();
		mSensorManager.unregisterListener(accelListener);
	}
	
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(accelListener);
	}
	
	SensorEventListener accelListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int acc) { }
 
		public void onSensorChanged(SensorEvent event) {
			
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			calculateCal(x,y,z);

		}
	};
	
	void calculateCal (float x, float y, float z) {
		float a_New = (float) Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
		String test_a = String.format("%f", a_New);
		Log.d("test a_New", test_a);
		
		//calculate calories and distance
		int t = 40;
		
		if (a1 == 0){
			a1 = a_New;
			String test_a1 = String.format("%f", a1);
			Log.d("a1", test_a1);
		} else if (a2 == 0 && a1 != 0){
			a2 = a_New;
			String test_a2 = String.format("%f", a2);
			Log.d("a2", test_a2);
		} else if (a3 == 0 && a2 != 0 && a1 != 0){
			a3 = a_New;
			String test_a3 = String.format("%f", a3);
			Log.d("a3", test_a3);
			
			a_Avg_Last = (a1+a2+a3)/3;
		} else {
		    a1 = a2;
		    a2 = a3;
		    a3 = a_New;
		    a_Avg_This = (a1+a2+a3)/3;
		    
		    a_Delta = a_Avg_This - a_Avg_Last;
		    if (a_Delta < 0){
		        a_Delta = -a_Delta;
		    }
		    v = (int) (v + (a_Delta*t));
		    //calculate calories from formula
		    float cal_formular = (float) (238*weight*((a_Delta*v*t) + 
		    		(((Math.pow(a_Delta,2))*(Math.pow(t,2)))/2)));
		    //new k
		    float k_float;
		    time_tmp = System.currentTimeMillis();
		    //time_tmp = time_tmp - time_begin;
		    //new code, new k
		    time_k = ((float) (time_tmp - time_begin))/60000; //time in ms to minute
		    /*k_float = (float) ((14.09 * Math.exp(-Math.pow(((time_tmp/60+46.28)/105.2),2))) +
		    		  (10.05 * Math.exp(-Math.pow(((time_tmp/60-258.6)/469.9),2))));*/
		    k_float = (float) ((14.09 * Math.exp(-Math.pow(((time_k/60+46.28)/105.2),2))) +
		    		  (10.05 * Math.exp(-Math.pow(((time_k/60-258.6)/469.9),2))));
		    cal_this = cal_formular * k_float / 50;
		    
		    //test output
		    String time_k_str = String.format("%f", time_k);
		    String k_float_str = String.format("%f", k_float); 
		    Log.d("time_k", time_k_str);
		    Log.d("k_float", k_float_str);
		    
		    //cal_this = (float) ((cal_k/Math.pow(10,12)));
		    String test_calthis = String.format("%f", cal_this);
			Log.d("test cal_this", test_calthis);
		    
		    cal_sum = (float) (cal_sum + cal_this);
		    //to %.2f
		    test_calsum = String.format("%.1f", cal_sum); //%.2f
		    //check cal_sum result
		    String test_calsum_f = String.format("%f", cal_sum);
			Log.d("test cal_sum", test_calsum_f);
			
		    a_Avg_Last = a_Avg_This;
		    
		    distance = (float) (cal_sum / 62.5);
		    //to %.2f
		    test_distance = String.format("%.1f", distance); //%.2f
		    //check distance result
		    String test_distance_f = String.format("%f", distance);
			Log.d("test distance", test_distance_f);
			
			//time_total = time_total + 40;
			//should be save time_k
			time_end = System.currentTimeMillis();
			time_total = (int) (time_end - time_begin);
			String timing = String.format("%d", time_total);
			Log.d("time_total", timing);
			
			if (d_id == true){ //show km
				show_userresult.setText(test_distance);
			} 
			if (c_id == true) { //show cal
				show_userresult.setText(test_calsum);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		//old use background service
		/*new AlertDialog.Builder(display.this)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface dialog, int which) {
				moveTaskToBack(true);
				//android.os.Process.killProcess(android.os.Process.myPid());
				//startService(new Intent(display.this, MyService.class));
				createNotification();
				//System.exit(0);
				//android.os.Process.killProcess(android.os.Process.myPid());
				//finish();
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.setMessage("feelfit is running. You exit from feelfit page?")
		.show();*/
		//new cant exit feelfit
		new AlertDialog.Builder(display.this)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface dialog, int which) {
				moveTaskToBack(true);
				//android.os.Process.killProcess(android.os.Process.myPid());
				//startService(new Intent(display.this, MyService.class));
				//createNotification();
				//System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
				finish();
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.setMessage("Do you want to exit feelfit?")
		.show();
		//Toast.makeText(display.this, "feelfit is running. You can't exit from feelfit.", Toast.LENGTH_LONG);
	}
	
	public void createNotification() {//View view
		context = this.getApplicationContext();
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.logo_noti,
				"feelfit is running", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		///NotificationCompat.Builder builder = new NotificationCompat.Builder(this); //
		//Intent intent = new Intent(this, NotificationReceiver.class);
		Intent intent = new Intent(context, NotificationReceiver.class);
		/*intent.putExtra("distance", distance);
		intent.putExtra("cal_sum", cal_sum);
		intent.putExtra("time_total", time_total);
		intent.putExtra("user_weight", weight);
		intent.putExtra("a_Avg_This", a_Avg_This);*/
		//intent.setAction(Intent.ACTION_MAIN);
		///intent.setFlags();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
		PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
		//PendingIntent activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//PendingIntent activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		///builder.setContentIntent(activity); //
		notification.setLatestEventInfo(this, "feelfit","feelfit is running", activity);
		//notification.flags = Notification.FLAG_ONGOING_EVENT;
		//notification.number += 1;
		notification.defaults = Notification.DEFAULT_SOUND;
		notificationManager.notify(0, notification); //0

	}
		
	/*public void progressUpdate(int caloriesRunning){
		CharSequence contentText = caloriesRunning + "Cal";
		//notification.set
	}*/  

}
