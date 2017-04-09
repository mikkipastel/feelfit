package com.mindidea.feelfit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

/*import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;*/

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
//import android.graphics.Canvas;
//import android.graphics.Color;
import android.os.Bundle;
//import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class graph_myGraph extends Activity{
	TableRow logo_graph;
	int weight;
	
	ImageView tabkmcal, tabfb;
	TextView print_result;
	
	String showall;

	boolean lastprocess;
	
	TableRow showgraph;
	Button goGraphPage;
	
	boolean haveFile;
	LinearLayout layout, layout2;
	
	TextView dateLabel1, dateLabel2, dateLabel3, dateLabel4, dateLabel5;
	
	//test
	//LinearLayout vechLabel;
	//SharedPreferences
	SharedPreferences sp_calallday;
	float cal01, cal02, cal03, cal04, cal05;
	String date01, date02, date03, date04, date05;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graph);
		//setContentView(R.layout.graph__test);
		
		//for small screen
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		layout = (LinearLayout)findViewById(R.id.graphArea);
		if(screenWidth <= 400){
			layout.setPadding(1, 0, 1, 5);
			layout.setMinimumHeight(200);
		}
		
		layout2 = (LinearLayout)findViewById(R.id.dateArea);
		
		//test
		//vechLabel = (LinearLayout)findViewById(R.id.graphLabel);
		
		//get value for SharedPreferences
		/*sp_calallday = getSharedPreferences("calallday", MODE_PRIVATE);
		date01 = sp_calallday.getString("date01", "");
		date02 = sp_calallday.getString("date02", "");
		date03 = sp_calallday.getString("date03", "");
		date04 = sp_calallday.getString("date04", "");
		date05 = sp_calallday.getString("date05", "");
		cal01 = sp_calallday.getFloat("cal01", 0.00f);
		cal02 = sp_calallday.getFloat("cal02", 0.00f);
		cal03 = sp_calallday.getFloat("cal03", 0.00f);
		cal04 = sp_calallday.getFloat("cal04", 0.00f);
		cal05 = sp_calallday.getFloat("cal05", 0.00f);
		if (date01 == ""){
			float[] values = new float[] { 0f, 0f, 0f, 0f , 0f };
            String[] verlabels = new String[] { "great", "ok", "bad" };
            String[] horlabels = new String[] { "1", "2", "3", "4", "5" };
            myGraphView graphView = new myGraphView(this, values, "user history (x: date, y: calories)", horlabels, verlabels, myGraphView.BAR);
            layout.addView(graphView);
            Toast.makeText(graph_myGraph.this, "no user history record", Toast.LENGTH_SHORT).show();
		} else {
			Log.d("date01", date01);
			Log.d("date02", date02);
			Log.d("date03", date03);
			Log.d("date04", date04);
			Log.d("date05", date05);
			String[] date_day = {date01, date02, date03, date04, date05};
			float[] cal_day = {cal01, cal02, cal03, cal04, cal05};			
			String[] verlabels = new String[] { "great", "ok", "bad" };
            //String[] horlabels = date;
            myGraphView graphView = new myGraphView(this, cal_day, "user history (x: date, y: calories)", date_day, verlabels, myGraphView.BAR);
            layout.addView(graphView);
		}*/
		//prepare x, y graph
		File feelfitDirectory = new File("/sdcard/Feelfit/");
        feelfitDirectory.mkdirs();
        //log file
        File log_timestamp5 = new File(feelfitDirectory, "log_timestamp5.log");
        //File log_calallday = new File(feelfitDirectory, "log_calallday.log");
        //read file
        if (log_timestamp5.exists()){ //check if file exist
        	//read text from file
        	StringBuilder text_tmp = new StringBuilder();
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(log_timestamp5));
	        	String line;
	        	//String tmp[];
	        	while((line = br.readLine()) != null){
	        		text_tmp.append(line);
	        		text_tmp.append("\n");
	        		showall = text_tmp.toString(); 
	        		//Log.d("testline", line);
	        	}
        	} catch (IOException e) {
        		
        	}	
        	//Log.d("testfile", showall);
        	//print_result.setText(showall);
        	haveFile = true;
        } else {
        	Toast.makeText(graph_myGraph.this, "no user history record", Toast.LENGTH_SHORT).show();
        }
        //show graph
        if(haveFile == true){
        	String tmp[] = showall.split("\n");
        	//chk len
            int len = tmp.length;
            String test_len = String.format("%d", len);
            Log.d("length", test_len);
            //prepare store
            String test[], date_time[];
            float[] cal = new float[5];
            //String[] cal_str = new String[5];
            String[] date = new String[5];
            String[] HorizontalLabels = new String[5];
            String[] day = new String[5];
            String[] time = new String[5];
            for(int i = 0; i < 5; i++){
            	cal[i] = 0;
            	date[i] = "";
            	day[i] = "";
            	time[i] = "";
            	//timedate[i] = "";
            	HorizontalLabels[i] = String.valueOf(i+1);
            }           
            //String tmp1 = tmp[0];
    	    //String tmp2 = tmp[1];           
            //int len_staff = tmp.length;
            for(int i = 0; i < tmp.length; i++){
            	test = tmp[i].split(" --- "); //len = 2
            	//date
            	date[i] = test[0];
            	Log.d("date graph " + i, date[i]);
            	//test
            	date_time = date[i].split(" ");
            	day[i] = date_time[0];
            	time[i] = date_time[1];
            	Log.d("date_day graph " + i, day[i]);
            	Log.d("date_time graph " + i, time[i]);
            	//System.out.println(date[i]);
            	//cal
            	//cal_str[i] = test[1];
            	cal[i] = Float.parseFloat(test[1]);
            	String cal_str = String.format("%.2f", cal[i]);
            	Log.d("cal graph " + i, cal_str);
            	//System.out.println(cal[i]);
            }
    		//set graph area
    		//float[] values = new float[] { 2.0f, 1.5f, 2.5f, 10.0f , 3.0f };
            String[] verlabels = new String[] { "great", "ok", "bad" };
            //String[] horlabels = date;
            //myGraphView graphView = new myGraphView(this, cal, "user history (x: date, y: calories)", date, verlabels, myGraphView.BAR);
            myGraphView graphView = new myGraphView(this, cal, "user history (x: date, y: calories)", day, verlabels, myGraphView.BAR);
            layout.addView(graphView);
            
            timeLabel label = new timeLabel(this, time);
            layout2.addView(label);

        } else {
        	float[] values = new float[] { 0f, 0f, 0f, 0f , 0f };
            String[] verlabels = new String[] { "great", "ok", "bad" };
            String[] horlabels = new String[] { "1", "2", "3", "4", "5" };
            myGraphView graphView = new myGraphView(this, values, "user history (x: date, y: calories)", horlabels, verlabels, myGraphView.BAR);
            layout.addView(graphView);
        }
        
		//to contact us
		logo_graph = (TableRow)findViewById(R.id.toptab_graph);
		logo_graph.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goContact = new Intent(graph_myGraph.this,contact.class);
				startActivity(goContact);
			}});
		
		//chk weight
		weight = getIntent().getIntExtra("user_weight", 0);
		String weight_str = String.format("%d", weight);
		Log.d("user_weight", weight_str);
		
		//check fromkm/cal
		lastprocess = getIntent().getBooleanExtra("last_process", false);
		
		//chk first time
		tabkmcal = (ImageView)findViewById(R.id.buttom_kmcal);
		tabkmcal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (weight == 0){
					Toast.makeText(graph_myGraph.this, "Please input your weight", Toast.LENGTH_SHORT).show();
					Intent goWeight = new Intent(graph_myGraph.this,inputWeight.class);
					startActivity(goWeight);
				} else if ((weight < 20 || weight > 200)) {
					Toast.makeText(graph_myGraph.this, "Please new input your weight, feelfit is calculate 20-200 Kg.", Toast.LENGTH_SHORT).show();
					Intent goWeight = new Intent(graph_myGraph.this,inputWeight.class);
					startActivity(goWeight);
				} else {
					if(lastprocess == true){
						onBackPressed();
					} else{
						//Toast.makeText(graph_history.this, "go to feelfit function", Toast.LENGTH_SHORT).show();
						Intent goContact = new Intent(graph_myGraph.this,display.class);
					    goContact.putExtra("user_weight", weight);
					    startActivity(goContact);
					}
				} 
			}});

		//get result from file
		/*print_result = (TextView)findViewById(R.id.result_fromfile);
		//File feelfitDirectory = new File("/sdcard/Feelfit/");
        feelfitDirectory.mkdirs();
        //log file
        //File log_graph5 = new File(feelfitDirectory, "log_graph5.log");
        File log_timestamp5_test = new File(feelfitDirectory, "log_timestamp5.log");
        //read file
        if (log_timestamp5_test.exists()){ //check if file exist
        	//read text from file
        	StringBuilder text_tmp = new StringBuilder();
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(log_timestamp5_test));
	        	String line;
	        	while((line = br.readLine()) != null){
	        		text_tmp.append(line);
	        		text_tmp.append("\n");
	        		showall = text_tmp.toString(); 
	        	}
        	} catch (IOException e) {
        		
        	}	
        	//Log.d("test test file", showall);
        	print_result.setText(showall);
        } else {
        	Toast.makeText(graph_history.this, "no user history record", Toast.LENGTH_SHORT).show();
        }*/
        
		//shared result to facebook
		tabfb = (ImageView)findViewById(R.id.buttom_fb);
		tabfb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if (weight == 0){
					Toast.makeText(graph_history.this, "Please input your weight", Toast.LENGTH_SHORT).show();
					Intent goWeight = new Intent(graph_history.this,inputWeight.class);
					startActivity(goWeight);
				} else {
					if(lastprocess == true){
						onBackPressed();
					} else{
						//Intent goFacebook = new Intent(graph_history.this,fbSharedDialog.class);
						Intent goFacebook = new Intent(graph_history.this,TestConnect.class);
						startActivity(goFacebook);
					}
				} */
			}});
        
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if (weight == 0){
			Toast.makeText(graph_myGraph.this, "Please input your weight", Toast.LENGTH_SHORT).show();
			Intent goWeight = new Intent(graph_myGraph.this,inputWeight.class);
			startActivity(goWeight);
		} else {
			super.onBackPressed();
		}
	}
		
	
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.about, menu);
    	getMenuInflater().inflate(R.menu.activity_exit, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			//android.os.Process.killProcess(android.os.Process.myPid());
			//finish();
			moveTaskToBack(true);
			break;
		}
		return true;
	}*/
	
	//copy barChartDemo01View.java
	/*public barChartDemo01view(Context context) {
        super(context);

        CategoryDataset dataset = createDataset();
        AFreeChart chart = createChart(dataset);

        setChart(chart);
    }
	
	private static CategoryDataset createDataset() {

        // row keys... : sub category 
        String series1 = "First"; //calories_result

        // column keys... : x-axis
        String category1 = "Category 1"; //date1
        String category2 = "Category 2"; //date2
        String category3 = "Category 3"; //date3
        String category4 = "Category 4"; //date4
        String category5 = "Category 5"; //date5

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1); //cal, calories_result, date1
        dataset.addValue(4.0, series1, category2); //cal, calories_result, date2
        dataset.addValue(3.0, series1, category3); //cal, calories_result, date3
        dataset.addValue(5.0, series1, category4); //cal, calories_result, date4
        dataset.addValue(5.0, series1, category5); //cal, calories_result, date5

        return dataset;

    }
	
	private static AFreeChart createChart(CategoryDataset dataset) {

        // create the chart...
        AFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart Demo 01",      // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE)); //tran bg?, gray

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64)); //orange
        //GradientColor gp1 = new GradientColor(Color.GREEN, Color.rgb(0, 64, 0));
        //GradientColor gp2 = new GradientColor(Color.RED, Color.rgb(64, 0, 0));
        renderer.setSeriesPaintType(0, gp0);
        //renderer.setSeriesPaintType(1, gp1);
        //renderer.setSeriesPaintType(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }*/
	

}
