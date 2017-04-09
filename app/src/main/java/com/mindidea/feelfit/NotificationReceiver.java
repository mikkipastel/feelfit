package com.mindidea.feelfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NotificationReceiver extends Activity {
	int weight, time_total;
	float cal_sum, distance, a_Avg_This;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//weight = getIntent().getIntExtra("user_weight", 0);
		//cal_sum = getIntent().getFloatExtra("cal_sum", 0);
		//distance = getIntent().getFloatExtra("distance", 0);
		//time_total = getIntent().getIntExtra("time_total", 0);
		//a_Avg_This = getIntent().getFloatExtra("a_Avg_This", 0);
		
		moveTaskToBack(false);
		onBackPressed();
		//Intent goWork = new Intent(getApplicationContext(),display.class);
		//Intent goWork = new Intent(getApplicationContext(),null);
		//goWork.putExtra("distance", distance);
		//goWork.putExtra("cal_sum", cal_sum);
		//goWork.putExtra("time_total", time_total);
		//goWork.putExtra("user_weight", weight);
		//goWork.putExtra("a_Avg_This", a_Avg_This);
		//startActivity(goWork);		

	}
}
