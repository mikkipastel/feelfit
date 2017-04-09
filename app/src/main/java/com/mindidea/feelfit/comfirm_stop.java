package com.mindidea.feelfit;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

public class comfirm_stop extends Activity{
	TableRow logo_comfirm;
	ImageView saveto, stopto, pause;
	
	String timestamp_end, timestamp_last;
	float cal_sum, distance, result_min;
	int time_total;
	String cal_sum_str, distance_str, time_total_str; 
	String cal_sum_f, distance_f, time_total_f;
	
	ImageView tabgraph, tabfb, tabkmcal;
	int weight;
	String tofile_old;
	String read_tmp, read_tmp_result, read_time_result;
	String result[];
	
	//new
	String todayDate, read_time_resultall, read_tmpall;
	String datacaltoday = "";
	int save_cnt = 0;
	float lasted_cal = 0f;
	
	//SharedPreferences
	SharedPreferences sp_calallday;
	//float cal01 = 0.00f, cal02 = 0.00f, cal03 = 0.00f, cal04 = 0.00f, cal05 = 0.00f;
	float cal01, cal02, cal03, cal04, cal05;
	//String date01 = "", date02 = "", date03 = "", date04 = "", date05 = "";
	String date01, date02, date03, date04, date05;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stopto);
		
		//timestamp_end = getIntent().getStringExtra(timestamp_end);
		weight = getIntent().getIntExtra("user_weight", 0);
		
		cal_sum = getIntent().getFloatExtra("cal_sum", 0);		
		distance = getIntent().getFloatExtra("distance", 0);
		time_total = getIntent().getIntExtra("time_total", 0);
		result_min = time_total/(1000*60); //ms to second to minute
		
		cal_sum_str = String.format("%f", cal_sum);
		distance_str = String.format("%f", distance);
		time_total_str = String.format("%f", result_min);
		
		//int cal_sum_tmp = (int)cal_sum;
		//int distance_tmp = (int)distance;
		//int result_min_tmp = (int) result_min;
		
		cal_sum_f = String.format("%.2f", cal_sum);
		distance_f = String.format("%.2f", distance);
		time_total_f = String.format("%.2f", result_min);
		
		//to contact us
		logo_comfirm = (TableRow)findViewById(R.id.toptab_stopto);
		logo_comfirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent goContact = new Intent(comfirm_stop.this,contact.class);
				startActivity(goContact);
			}});
		
		//save result to text file
		saveto = (ImageView)findViewById(R.id.button_saveto);
		saveto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savedata_all();
				//saveallday5result_sp();
				//savecaldataallday(save_cnt);
				finish();

			}});
		
		//reset all result and weight
		stopto = (ImageView)findViewById(R.id.button_stopto);
		stopto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goReset = new Intent(comfirm_stop.this,inputWeight.class);
				startActivity(goReset);
			}});
		
		//back to state
		pause = (ImageView)findViewById(R.id.button_cancelto);
		pause.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}});
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	void saveallday5result_sp() {
		todayDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		System.out.println(todayDate);
		//check date
		if ( date01 == todayDate)
		{
			cal01 = cal01 + cal_sum;
		}
		else
		{
			date05 = date04; cal05 = cal04;
			date04 = date03; cal04 = cal03;
			date03 = date02; cal03 = cal02;
			date02 = date01; cal02 = cal01;
			date01 = todayDate; cal01 = cal_sum;
		}
	}
	
	/*void savecaldataallday(int save_cnt){
		File feelfitDirectory = new File("/sdcard/Feelfit/");
		//new save calories all day
        todayDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String datecal_allday5 = todayDate + " --- " + cal_sum_f + "\n";
        File log_calallday = new File(feelfitDirectory, "log_calallday.log");
        //File log_feelfitallday = new File(feelfitDirectory, "log_feelfitallday.log");
        //save calories all day
        if (log_calallday.exists()){ //check if file exist
        	StringBuilder text_tmpall = new StringBuilder();
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(log_calallday));
        	    String line;
        	    int k=0;
				while((line = br.readLine()) != null){
					text_tmpall.append(line);
					text_tmpall.append("\n");
					read_time_resultall = text_tmpall.toString();
					//Log.d("test file" + k, read_time_resultall);
					k++;
				}
				//add cal day
				String temp_cal[] = read_time_resultall.split("\n");
	            String test[];
	            float[] cal = new float[5];
	            String[] date = new String[5];
	            String[] allcalresult = new String[5];
	            for(int i = 0; i < 5; i++){ //build null string
	            	cal[i] = 0;
	            	date[i] = "";
	            }
	            //chk len
	            int len = temp_cal.length;
	            String test_len = String.format("%d", len);
	            String save_cnt_str =  String.format("%d", save_cnt);
	            Log.d("length", test_len);
	            Log.d("save_cnt", save_cnt_str);
	            //add value to string
	            float tmp_cal = 0f;
	            for(int i = 0; i < temp_cal.length; i++){ 
	            	test = temp_cal[i].split(" --- "); //len = 2
	            	date[i] = test[0]; //date
	            	Log.d("date file " + i, date[i]);
	            	//System.out.println(date[i]);
	            	cal[i] = Float.parseFloat(test[1]); //cal
	            	String cal_str = String.format("%.2f", cal[i]);
	            	Log.d("cal file " + i, cal_str);
	            	//System.out.println(cal[i]);
	            	if (date[i] == todayDate){
	            		//if (save_cnt == 0) {
	            		tmp_cal = cal[i];
	            		cal[i] = tmp_cal + cal_sum;
	            		//} else {
	            		//	cal[i] = cal[i] - lasted_cal + cal_sum;
	            		//}  			            		
	            	}
	            	allcalresult[i] = date[i] + " --- " + cal[i] + "\n";
	            	if (i == temp_cal.length - 1){
	            		datacaltoday = allcalresult[i];
	            	}
	            	lasted_cal = cal_sum;
	            }
	            save_cnt++;
				//record to file
				if(k == 5){ //full
					//swap value
					//String result[] = read_time_resultall.split("\n");
					//check len
					int len = result.length;
					String len_str = String.format("%d", len);
					Log.d("length", len_str);					
					result[0] = result[1]+"\n";
					result[1] = result[2]+"\n";
					result[2] = result[3]+"\n";
					result[3] = result[4]+"\n";
					result[4] = datacaltoday; //new
					//Log.d("test result[4]", result[4]);
					//write file
					FileOutputStream fos_full = null;
		        	try {
		        		fos_full = new FileOutputStream(log_calallday);
		        		for (int j = 1; j <= temp_cal.length; j++){
		        			//String line_tmp = date[j] + " --- " + cal[j] + "\n";
		        			fos_full.write(allcalresult[j].getBytes());
						}
		        		fos_full.write(result[0].getBytes());
		        		fos_full.write(result[1].getBytes());
		        		fos_full.write(result[2].getBytes());
		        		fos_full.write(result[3].getBytes());
		        		fos_full.write(result[4].getBytes());
		        		//fos_full.write(new_result.getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if (fos_full != null) {
							try {
								fos_full.flush();
								fos_full.close();
							} catch (IOException e) {
								// swallow
							}
						}
					}
				} else { //type a+
					//write file
					FileOutputStream fos_notfull = null;
		        	try {
		        		fos_notfull = new FileOutputStream(log_calallday);
		        		for (int j = 0; j <= allcalresult.length; j++){
		        			//String line_tmp = date[j] + " --- " + cal[j] + "\n";
		        			fos_notfull.write(allcalresult[j].getBytes()); //
						}
		        		//fos_notfull.write(read_time_resultall.getBytes());
		        		//fos_notfull.write(datacaltoday.getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if (fos_notfull != null) {
							try {
								fos_notfull.flush();
								fos_notfull.close();
							} catch (IOException e) {
								// swallow
							}
						}
					}
				}
        	} catch (IOException e) {

        	}
        } else { //no file - build new text file 
        	FileOutputStream fos_allday = null;
        	try {
        		fos_allday = new FileOutputStream(log_calallday);
        		fos_allday.write(datecal_allday5.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (fos_allday != null) {
					try {
						fos_allday.flush();
						fos_allday.close();
					} catch (IOException e) {
						// swallow
					}
				}
			}
        }
        
        if (log_feelfitallday.exists()){ //check if file exist
        	//read text from file
        	StringBuilder text_tmpall = new StringBuilder();
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(log_feelfitallday));
	        	String line;
	        	while((line = br.readLine()) != null){
	        		text_tmpall.append(line);
	        		text_tmpall.append("\n");
	        		read_tmpall = text_tmpall.toString(); 
	        	}
	        	//write new result
	        	FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(log_feelfitallday);
					fos.write(read_tmpall.getBytes());
					fos.write(datacaltoday.getBytes());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							// swallow
						}
					}
				}
	        	
        	} catch (IOException e) {
        		
        	}
        } else {
        	FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(log_feelfitallday);
				fos.write(datecal_allday5.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (fos != null) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						// swallow
					}
				}
			}
        } 
        //Toast.makeText(comfirm_stop.this, "prepare graph data", Toast.LENGTH_SHORT).show();
	}*/
	
	void savedata_all(){
		timestamp_end  = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		//timestamp_last = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		timestamp_last = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		//bulid app folder
		File feelfitDirectory = new File("/sdcard/Feelfit/");
        feelfitDirectory.mkdirs();
		//string result + log file (detail)
        String header = "datetime  ---  calories|distance|time_total\n";
        String toFile = timestamp_end + "  ---  " + cal_sum_str + "Cal|" + distance_str + "km|" + time_total_str + "min\n"; 
        File log_feelfit = new File(feelfitDirectory, "log_feelfit.log");
        //string result last 5 results
        //String new_result = timestamp_last + "  ---  " + cal_sum_f + "Cal|" + distance_f + "km|" + time_total_f + "min\n"; 
        //File log_graph5 = new File(feelfitDirectory, "log_graph5.log");
        //save timestamp and cal_sum only
        String pregraph = timestamp_last + " --- " + cal_sum_f + "\n";
        File log_timestamp5 = new File(feelfitDirectory, "log_timestamp5.log");       
        //read file detail
        if (log_feelfit.exists()){ //check if file exist
        	//read text from file
        	StringBuilder text_tmp = new StringBuilder();
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(log_feelfit));
	        	String line;
	        	while((line = br.readLine()) != null){
	        		text_tmp.append(line);
	        		text_tmp.append("\n");
	        		read_tmp = text_tmp.toString(); 
	        	}
	        	//write new result
	        	FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(log_feelfit);
					fos.write(read_tmp.getBytes());
					fos.write(toFile.getBytes());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							// swallow
						}
					}
				}
	        	
        	} catch (IOException e) {
        		
        	}
        	//show text to log
        	//Log.d("test test file", read_tmp);
        } else {
        	//build new text file result (header + result)
        	FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(log_feelfit);
				fos.write(header.getBytes());
				fos.write(toFile.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (fos != null) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						// swallow
					}
				}
			}
        } //end read text from file
		//save to last 5 result to file
        /*if (log_graph5.exists()){ //check if file exist
        	//read file from file
        	StringBuilder text_tmp_5 = new StringBuilder();
        	try {
        	    BufferedReader br = new BufferedReader(new FileReader(log_graph5));
        	    String line;
        	    int k=0;
				while((line = br.readLine()) != null){
					text_tmp_5.append(line);
					text_tmp_5.append("\n");
					read_tmp_result = text_tmp_5.toString();
					Log.d("test file" + k, read_tmp_result);
					k++;
				}
				if(k == 5){ //full
					//swap value
					String result[] = read_tmp_result.split("\n");
					//check len
					int len = result.length;
					String len_str = String.format("%d", len);
					Log.d("length", len_str);
					
					result[0] = result[1]+"\n";
					result[1] = result[2]+"\n";
					result[2] = result[3]+"\n";
					result[3] = result[4]+"\n";//
					result[4] = new_result;
					Log.d("test result[4]", result[4]);
					//write file
					FileOutputStream fos_full = null;
		        	try {
		        		fos_full = new FileOutputStream(log_graph5);
		        		//fos_full.write(read_tmp_result.getBytes());//
		        		fos_full.write(result[0].getBytes());
		        		fos_full.write(result[1].getBytes());
		        		fos_full.write(result[2].getBytes());
		        		fos_full.write(result[3].getBytes());
		        		fos_full.write(result[4].getBytes());
		        		//fos_full.write(new_result.getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if (fos_full != null) {
							try {
								fos_full.flush();
								fos_full.close();
							} catch (IOException e) {
								// swallow
							}
						}
					}
				} else { //type a+
					//write file
					FileOutputStream fos_notfull = null;
		        	try {
		        		fos_notfull = new FileOutputStream(log_graph5);
		        		fos_notfull.write(read_tmp_result.getBytes());
		        		fos_notfull.write(new_result.getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if (fos_notfull != null) {
							try {
								fos_notfull.flush();
								fos_notfull.close();
							} catch (IOException e) {
								// swallow
							}
						}
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        } else { //no file - build new text file 
        	FileOutputStream fos1 = null;
        	try {
        		fos1 = new FileOutputStream(log_graph5);
				fos1.write(new_result.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (fos1 != null) {
					try {
						fos1.flush();
						fos1.close();
					} catch (IOException e) {
						// swallow
					}
				}
			}
        } //end save to last 5 result to file
        Toast.makeText(comfirm_stop.this, "save result ", Toast.LENGTH_SHORT).show();*/
        //save prepare graph data
        if (log_timestamp5.exists()){ //check if file exist
        	//read file from file
        	StringBuilder text_time_5 = new StringBuilder();
        	try {
        	    BufferedReader br = new BufferedReader(new FileReader(log_timestamp5));
        	    String line;
        	    int k=0;
				while((line = br.readLine()) != null){
					text_time_5.append(line);
					text_time_5.append("\n");
					read_time_result = text_time_5.toString();
					//Log.d("test file" + k, read_time_result);
					k++;
				}
				if(k == 5){ //full
					//swap value
					String result[] = read_time_result.split("\n");
					//check len
					int len = result.length;
					String len_str = String.format("%d", len);
					//Log.d("length", len_str);					
					result[0] = result[1]+"\n";
					result[1] = result[2]+"\n";
					result[2] = result[3]+"\n";
					result[3] = result[4]+"\n";//
					result[4] = pregraph;
					//Log.d("test result[4]", result[4]);
					//write file
					FileOutputStream fos_full = null;
		        	try {
		        		fos_full = new FileOutputStream(log_timestamp5);
		        		//fos_full.write(read_tmp_result.getBytes());//
		        		fos_full.write(result[0].getBytes());
		        		fos_full.write(result[1].getBytes());
		        		fos_full.write(result[2].getBytes());
		        		fos_full.write(result[3].getBytes());
		        		fos_full.write(result[4].getBytes());
		        		//fos_full.write(new_result.getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if (fos_full != null) {
							try {
								fos_full.flush();
								fos_full.close();
							} catch (IOException e) {
								// swallow
							}
						}
					}
				} else { //type a+
					//write file
					FileOutputStream fos_notfull = null;
		        	try {
		        		fos_notfull = new FileOutputStream(log_timestamp5);
		        		fos_notfull.write(read_time_result.getBytes());
		        		fos_notfull.write(pregraph.getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if (fos_notfull != null) {
							try {
								fos_notfull.flush();
								fos_notfull.close();
							} catch (IOException e) {
								// swallow
							}
						}
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        } else { //no file - build new text file 
        	FileOutputStream fos2 = null;
        	try {
        		fos2 = new FileOutputStream(log_timestamp5);
				fos2.write(pregraph.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (fos2 != null) {
					try {
						fos2.flush();
						fos2.close();
					} catch (IOException e) {
						// swallow
					}
				}
			}
        } //end prepare graph data
		Toast.makeText(comfirm_stop.this, "prepare graph data", Toast.LENGTH_SHORT).show();
		tofile_old = toFile;
	}

}
