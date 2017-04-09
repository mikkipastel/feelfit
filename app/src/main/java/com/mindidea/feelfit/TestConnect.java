//TestConnect.java

package com.mindidea.feelfit;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;

public class TestConnect extends Activity {
    private Facebook mFacebook;
    private CheckBox mFacebookBtn;
    private ProgressDialog mProgress;
     
    private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "offline_access"};
     
    private static final String APP_ID = "608296725924940"; //feelfit app id
    
    Button clicktopost;
    TableRow logo_testconnect;
    
    //String test_calsum, test_distance; //output
    float cal_sum, distance;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.fb);
        
        //get intent value : cal_sum, distance
        //test_calsum = getIntent().getStringExtra("test_calsum");
        //test_distance = getIntent().getStringExtra("test_distance");
        
        //to contact us
        logo_testconnect = (TableRow)findViewById(R.id.toptab_fb);
        logo_testconnect.setOnClickListener(new OnClickListener(){

      		@Override
      		public void onClick(View v) {
      			Intent goContact = new Intent(TestConnect.this,contact.class);
      			startActivity(goContact);
      		}});
        
        cal_sum = getIntent().getFloatExtra("cal_sum", 0);
        distance = getIntent().getFloatExtra("distance", 0);
        
        //facebook dialog 
        mFacebookBtn    = (CheckBox) findViewById(R.id.cb_facebook);
         
        mProgress       = new ProgressDialog(this);
        mFacebook       = new Facebook(APP_ID);
         
        SessionStore.restore(mFacebook, this);
         
        if (mFacebook.isSessionValid()) {
            mFacebookBtn.setChecked(true);
             
            String name = SessionStore.getName(this);
            name        = (name.equals("")) ? "Unknown" : name;
             
            mFacebookBtn.setText("  Facebook (" + name + ")");
            mFacebookBtn.setTextColor(Color.WHITE);
        }
         
        mFacebookBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookClick();
            }
        });
         
        clicktopost = (Button) findViewById(R.id.button1);
        clicktopost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent goPostFB = new Intent(TestConnect.this, TestPost.class);
            	//goPostFB.putExtra("test_calsum", test_calsum);
            	//goPostFB.putExtra("test_distance", test_distance);
            	goPostFB.putExtra("cal_sum", cal_sum);
            	goPostFB.putExtra("distance", distance);
                startActivity(goPostFB);
            }
        });
    }
     
    private void onFacebookClick() {
        if (mFacebook.isSessionValid()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
             
            builder.setMessage("disconnect Facebook ?")
                   .setCancelable(false)
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           fbLogout();
                       }
                   })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                             
                            mFacebookBtn.setChecked(true);
                       }
                   });
             
            final AlertDialog alert = builder.create();
             
            alert.show();
        } else {
            mFacebookBtn.setChecked(false);
             
            mFacebook.authorize(this, PERMISSIONS, -1, new FbLoginDialogListener());
        }
    }
     
    private final class FbLoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionStore.save(mFacebook, TestConnect.this);
            
            mFacebookBtn.setText("  Facebook (No Name)");
            mFacebookBtn.setChecked(true);
            mFacebookBtn.setTextColor(Color.WHITE);
              
            getFbName();
        }
 
        public void onFacebookError(FacebookError error) {
           Toast.makeText(TestConnect.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
            
           mFacebookBtn.setChecked(false);
        }
         
        public void onError(DialogError error) {
            Toast.makeText(TestConnect.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
             
            mFacebookBtn.setChecked(false);
        }
 
        public void onCancel() {
            mFacebookBtn.setChecked(false);
        }
    }
     
    private void getFbName() {
        mProgress.setMessage("Finalizing ...");
        mProgress.show();
         
        new Thread() {
            @Override
            public void run() {
                String name = "";
                int what = 1;
                 
                try {
                    String me = mFacebook.request("me");
                     
                    JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
                    name = jsonObj.getString("name");
                    what = 0;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                 
                mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
            }
        }.start();
    }
     
    private void fbLogout() {
        mProgress.setMessage("cancel connect to Facebook");
        mProgress.show();
             
        new Thread() {
            @Override
            public void run() {
                SessionStore.clear(TestConnect.this);
                        
                int what = 1;
                     
                try {
                    mFacebook.logout(TestConnect.this);
                          
                    what = 0;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                     
                mHandler.sendMessage(mHandler.obtainMessage(what));
            }
        }.start();
    }
     
    private Handler mFbHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();
             
            if (msg.what == 0) {
                String username = (String) msg.obj;
                username = (username.equals("")) ? "No Name" : username;
                     
                SessionStore.saveName(username, TestConnect.this);
                 
                mFacebookBtn.setText("  Facebook (" + username + ")");
                  
                Toast.makeText(TestConnect.this, "connect to Facebook user: " + username, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TestConnect.this, "connect to Facebook", Toast.LENGTH_SHORT).show();
            }
        }
    };
     
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();
             
            if (msg.what == 1) {
                Toast.makeText(TestConnect.this, "connect Facebook failed", Toast.LENGTH_SHORT).show();
            } else {
                mFacebookBtn.setChecked(false);
                mFacebookBtn.setText("  Facebook (Not connected)");
                mFacebookBtn.setTextColor(Color.GRAY);
                    
                Toast.makeText(TestConnect.this, "cancel to connect facebook", Toast.LENGTH_SHORT).show();
            }
        }
    };
}