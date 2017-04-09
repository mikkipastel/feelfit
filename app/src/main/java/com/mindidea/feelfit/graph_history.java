package com.mindidea.feelfit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
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


public class graph_history extends Activity{
	TableRow logo_graph;
	int weight;
	
	ImageView tabkmcal, tabfb;
	TextView print_result;
	
	String showall;

	boolean lastprocess;
	
	TableRow showgraph;
	Button goGraphPage;
	
	boolean haveFile;
	LinearLayout layout;
	
	TextView dateLabel1, dateLabel2, dateLabel3, dateLabel4, dateLabel5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graph);
		
		layout = (LinearLayout)findViewById(R.id.graphArea);
		
		/*dateLabel1 = (TextView)findViewById(R.id.labelDate1);
		dateLabel2 = (TextView)findViewById(R.id.labelDate2);
		dateLabel3 = (TextView)findViewById(R.id.labelDate3);
		dateLabel4 = (TextView)findViewById(R.id.labelDate4);
		dateLabel5 = (TextView)findViewById(R.id.labelDate5);*/
		
		//prepare x, y graph
		File feelfitDirectory = new File("/sdcard/Feelfit/");
        feelfitDirectory.mkdirs();
        //log file
        File log_timestamp5 = new File(feelfitDirectory, "log_timestamp5.log");
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
        	Toast.makeText(graph_history.this, "no user history record", Toast.LENGTH_SHORT).show();
        }
        
        if(haveFile == true){
        	String tmp[] = showall.split("\n");
        	//chk len
            int len = tmp.length;
            String test_len = String.format("%d", len);
            Log.d("length", test_len);
            //prepare store
            String test[];
            //String date1, date2, date3, date4, date5;
            double cal1 = 0.00, cal2 = 0.00, cal3 = 0.00, cal4 = 0.00, cal5 = 0.00;
            double[] cal = new double[5];
            //String[] cal_str = new String[5];
            String[] date = new String[5];
            String[] HorizontalLabels = new String[5];
            for(int i = 0; i < 5; i++){
            	cal[i] = 0.00;
            	date[i] = "";
            	HorizontalLabels[i] = String.valueOf(i+1);
            }           
            //String tmp1 = tmp[0];
    	    //String tmp2 = tmp[1];
            //int len_staff = tmp.length;
            for(int i = 0; i < tmp.length; i++){
            	test = tmp[i].split(" --- ");
            	//len = 2
            	//date
            	date[i] = test[0];
            	System.out.println(date[i]);
            	//cal
            	//cal_str[i] = test[1];
            	cal[i] = Double.parseDouble(test[1]);
            	System.out.println(cal[i]);
            }
    		//set graph area : MyDraw
            MyDraw mydraw = new MyDraw(this);            
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
    		mydraw.getWidth(screenWidth);
    		mydraw.getHeight(screenHeight);
    		mydraw.allCal(cal1, cal2, cal3, cal4, cal5);
    		//Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    		Canvas c = new Canvas();
    		mydraw.onDraw(c);
    		layout.addView(mydraw);
    		//set graph bar : GraphView
            GraphViewData gdata[] = new GraphViewData[5];
            //add data
            for(int i = 0; i < cal.length; i++){
            //GraphViewData(x, y) date[i], Cal[i]
            	gdata[i] = new GraphViewData(i, cal[i]);
            }
            GraphViewSeries exampleSeries = new GraphViewSeries(gdata);
    		/*GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[]{
    			new GraphViewData(1, cal1)
    			,new GraphViewData(2, cal2)
    			,new GraphViewData(3, cal3)
    			,new GraphViewData(4, cal4)
    			,new GraphViewData(5, cal5)
    		}) ;*/
    		GraphView graphView = new BarGraphView(this, "user history"); //context, heading
    		graphView.addSeries(exampleSeries); //data
    		//graphView.setVerticalLabels(cal_str);
    		graphView.setHorizontalLabels(date);   		
    		//graphView.setHorizontalLabels(HorizontalLabels); //1-5 date
    		/*graphView.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLACK, 
    				0xFF000000 + (255<<16) + (127<<8) + 0));*/
    		//int orange = 0xFF000000 + (255<<16) + (127<<8) + 0;
    		graphView.getGraphViewStyle().setVerticalLabelsWidth(20);
    		graphView.getGraphViewStyle().setTextSize(10);
    		graphView.setScalable(true);
    		//graphView.setDrawingCacheBackgroundColor(Color.rgb(255<<16, 127<<8, 0));
    		//graphView.isScrollable();
    		//layout.addView(graphView);
    		
    		/*dateLabel1.setText(date[0]); dateLabel1.setVerticalFadingEdgeEnabled(true);
    		dateLabel2.setText(date[1]); dateLabel2.setVerticalFadingEdgeEnabled(true);
    		dateLabel3.setText(date[2]); dateLabel3.setVerticalFadingEdgeEnabled(true);
    		dateLabel4.setText(date[3]); dateLabel4.setVerticalFadingEdgeEnabled(true);
    		dateLabel5.setText(date[4]); dateLabel5.setVerticalFadingEdgeEnabled(true);*/
        } else {
        	GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[]{
        			new GraphViewData(1, 0.00d)
        			,new GraphViewData(2, 0.00d)
        			,new GraphViewData(3, 0.00d)
        			,new GraphViewData(4, 0.00d)
        			,new GraphViewData(5, 0.00d)
        		}) ;
        		GraphView graphView = new BarGraphView(this, "user history"); //context, heading
        		graphView.addSeries(exampleSeries); //data
        		layout.addView(graphView);
        }
        
		//to contact us
		logo_graph = (TableRow)findViewById(R.id.toptab_graph);
		logo_graph.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goContact = new Intent(graph_history.this,contact.class);
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
					Toast.makeText(graph_history.this, "Please input your weight", Toast.LENGTH_SHORT).show();
					Intent goWeight = new Intent(graph_history.this,inputWeight.class);
					startActivity(goWeight);
				} else {
					if(lastprocess == true){
						onBackPressed();
					} else{
						//Toast.makeText(graph_history.this, "go to feelfit function", Toast.LENGTH_SHORT).show();
						Intent goContact = new Intent(graph_history.this,display.class);
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
			Toast.makeText(graph_history.this, "Please input your weight", Toast.LENGTH_SHORT).show();
			Intent goWeight = new Intent(graph_history.this,inputWeight.class);
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
