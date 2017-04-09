package com.mindidea.feelfit;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class inputWeight extends Activity{
	ImageView ok, del, graph;
	ImageView one, two, three, four, five, six, seven, eight, nine, zero;
	TableRow logo;
	int weight_in, tmp;
	TextView userweight;
	String weight_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userweight);
		
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		System.out.println(screenWidth);
		
		//build directory and delete necessary log file 
		File feelfitDirectory = new File("/sdcard/Feelfit/");
		File log_graph5 = new File(feelfitDirectory, "log_graph5.log");
		if (log_graph5.exists()) log_graph5.delete(); //delete necessary log file
		//File log_timestamp5 = new File(feelfitDirectory, "log_timestamp5.log");       
		//if (log_timestamp5.exists()) log_timestamp5.delete();
		
		//to contact us
		logo = (TableRow)findViewById(R.id.toptab);
		logo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent goContact = new Intent(inputWeight.this,contact.class);
				startActivity(goContact);
			}});
		
		//go to display
		ok = (ImageView)findViewById(R.id.ok_button);
		ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				weight_str = userweight.getText().toString();
				weight_in = Integer.parseInt(weight_str);
				if (weight_in == 0){
					Toast.makeText(inputWeight.this, "Please input your weight", Toast.LENGTH_SHORT).show();
				} else if (weight_in < 20 || weight_in > 200){
					Toast.makeText(inputWeight.this, "feelfit is calculate 20-200 Kg.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(inputWeight.this, "your weight = " + weight_str + " Kg.", Toast.LENGTH_SHORT).show();
					Intent goDisplay = new Intent(inputWeight.this,display.class);
					goDisplay.putExtra("user_weight", weight_in);
				    startActivity(goDisplay);
				}				
			}});
		
		//go to graph
		graph = (ImageView)findViewById(R.id.graph_button);
		graph.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				weight_str = userweight.getText().toString();
				weight_in = Integer.parseInt(weight_str);
				//Intent goGraph = new Intent(inputWeight.this,graph_history.class);
				Intent goGraph = new Intent(inputWeight.this,graph_myGraph.class);
				goGraph.putExtra("user_weight", weight_in);
				startActivity(goGraph);
			}});
		
		//get user weight
		del = (ImageView)findViewById(R.id.del_value);
		zero = (ImageView)findViewById(R.id.button_0);
		one = (ImageView)findViewById(R.id.button_1);
		two = (ImageView)findViewById(R.id.button_2);
		three = (ImageView)findViewById(R.id.button_3);
		four = (ImageView)findViewById(R.id.button_4);
		five = (ImageView)findViewById(R.id.button_5);
		six = (ImageView)findViewById(R.id.button_6);
		seven = (ImageView)findViewById(R.id.button_7);
		eight = (ImageView)findViewById(R.id.button_8);
		nine = (ImageView)findViewById(R.id.button_9);
		userweight = (TextView)findViewById(R.id.show_weight);
		
		//delete value
		del.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = -1;
				setWeight(tmp);
			}});
		
		//input weight value
		zero.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 0;
				setWeight(tmp);
			}});
		one.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 1;
				setWeight(tmp);
			}});
		two.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 2;
				setWeight(tmp);
			}});
		three.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 3;
				setWeight(tmp);
			}});
		four.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 4;
				setWeight(tmp);
			}});
		five.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 5;
				setWeight(tmp);
			}});
		six.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 6;
				setWeight(tmp);
			}});
		seven.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 7;
				setWeight(tmp);
			}});
		eight.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 8;
				setWeight(tmp);
			}});
		nine.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tmp = 9;
				setWeight(tmp);
			}});
		
	}
	
	void setWeight (int tmp){
		int weight_fn = 0;
		int tail, event;
		String w_display, str_chk;
	    //check event
		int getTextview = Integer.parseInt(userweight.getText().toString());
		if (getTextview == 0) {
			event = 0;
		} else {
			str_chk = userweight.getText().toString();
			event = str_chk.length();
		}
		//display weight
		if (event == 0){ 
			if (tmp != 0){
				weight_fn = tmp;
				w_display = String.format("%d", weight_fn);
				userweight.setText(w_display);
			}
		} else if (event >= 1 && event < 3){ 
			weight_fn = (getTextview*10) + tmp;
			w_display = String.format("%d", weight_fn);
			userweight.setText(w_display);	
		} else {
			//Toast.makeText(inputWeight.this, "you cannot input your weight", Toast.LENGTH_SHORT).show();
		}
		//delete weight
		if (tmp == -1 ){
			tail = getTextview % 10;
			weight_fn = (getTextview - tail) / 10;
			w_display = String.format("%d", weight_fn);
			userweight.setText(w_display);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
		System.exit(0);
		finish();
		//android.os.Process.killProcess(android.os.Process.myPid()); //runing in bg
		//super.onBackPressed();
	}

}
