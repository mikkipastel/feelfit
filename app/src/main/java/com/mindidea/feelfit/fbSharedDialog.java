package com.mindidea.feelfit;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Facebook.DialogListener;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

public class fbSharedDialog extends Activity{
	//private UiLifecycleHelper uiHelper;
	TableRow logo_fb;
	ImageView tabkmcal;
	
	private Facebook mFacebook;
    private CheckBox mFacebookBtn, mFacebookCb;
    private ProgressDialog mProgress;
    
    private Handler mRunOnUi = new Handler();
     
    private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "offline_access"};
     
    private static final String APP_ID = "608296725924940"; //feelfit app id

    float cal_sum, distance;
    
    Button posttowall;
    String name;
    String post_cal, post_distance;
    //boolean connect_fb = false;
    boolean connect_fb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fb_post);
		
		//to contact us
		logo_fb = (TableRow)findViewById(R.id.toptab_fb);
		logo_fb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goContact = new Intent(fbSharedDialog.this,contact.class);
				startActivity(goContact);
			}});
		
		//go to kmcal
		tabkmcal = (ImageView)findViewById(R.id.buttom_kmcal);
		tabkmcal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
				onBackPressed();
			}});
		
		//value from display
		cal_sum = getIntent().getFloatExtra("cal_sum", 0);
        post_cal = String.format("%.2f", cal_sum);
        distance = getIntent().getFloatExtra("distance", 0);
        post_distance = String.format("%.2f", distance);
        
        //facebook dialog 
        mFacebookBtn    = (CheckBox) findViewById(R.id.cb_facebook);
         
        mProgress       = new ProgressDialog(this);
        mFacebook       = new Facebook(APP_ID);
         
        SessionStore.restore(mFacebook, this);
         
        if (mFacebook.isSessionValid()) {
            mFacebookBtn.setChecked(true);
             
            //String name = SessionStore.getName(this);
            name = SessionStore.getName(this);
            name = (name.equals("")) ? "Unknown" : name;
             
            mFacebookBtn.setText("  Facebook (" + name + ")");
            mFacebookBtn.setTextColor(Color.WHITE);
            
            connect_fb = true;
        }
         
        mFacebookBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookClick();
            }
        });
        
        //post facebook
        final EditText reviewEdit = (EditText) findViewById(R.id.revieew);
        mFacebookCb               = (CheckBox) findViewById(R.id.cb_facebook);
         
        mProgress   = new ProgressDialog(this);
         
        mFacebook   = new Facebook(APP_ID);
         
        SessionStore.restore(mFacebook, this);
 
        if (mFacebook.isSessionValid()) {
            mFacebookCb.setChecked(true);
                 
            name = SessionStore.getName(this);
            name = (name.equals("")) ? "Unknown" : name;
                 
            mFacebookCb.setText("  Facebook  (" + name + ")");
        }
         
        posttowall = (Button) findViewById(R.id.button1);
        posttowall.setOnClickListener(new OnClickListener() {
            public void onClick (View v) {
            	if (connect_fb == true){
            		String review = reviewEdit.getText().toString();
	                if (review.equals("")) return;
	                if (mFacebookCb.isChecked()) postToFacebook(review);
            	} else {
            		//Toast.makeText(fbSharedDialog.this, "please connect to your facebook", Toast.LENGTH_SHORT).show();
            		Toast.makeText(fbSharedDialog.this, "sign-in error, please come back again", Toast.LENGTH_SHORT).show();
            	}               
            }
        });
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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
            connect_fb = true; 
        }
    }
     
    private final class FbLoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionStore.save(mFacebook, fbSharedDialog.this);
            
            mFacebookBtn.setText("  Facebook (No Name)");
            mFacebookBtn.setChecked(true);
            mFacebookBtn.setTextColor(Color.WHITE);
              
            getFbName();
            //connect_fb = true;
        }
 
        public void onFacebookError(FacebookError error) {
           Toast.makeText(fbSharedDialog.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
            
           mFacebookBtn.setChecked(false);
        }
         
        public void onError(DialogError error) {
            Toast.makeText(fbSharedDialog.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
             
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
                SessionStore.clear(fbSharedDialog.this);
                        
                int what = 1;
                     
                try {
                    mFacebook.logout(fbSharedDialog.this);
                          
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
                     
                SessionStore.saveName(username, fbSharedDialog.this);
                 
                mFacebookBtn.setText("  Facebook (" + username + ")");
                  
                Toast.makeText(fbSharedDialog.this, "connect to Facebook user: " + username, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(fbSharedDialog.this, "connect to Facebook", Toast.LENGTH_SHORT).show();
            }
        }
    };
     
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();
             
            if (msg.what == 1) {
                Toast.makeText(fbSharedDialog.this, "connect Facebook failed", Toast.LENGTH_SHORT).show();
            } else {
                mFacebookBtn.setChecked(false);
                mFacebookBtn.setText("  Facebook (Not connected)");
                mFacebookBtn.setTextColor(Color.GRAY);
                    
                Toast.makeText(fbSharedDialog.this, "cancel to connect facebook", Toast.LENGTH_SHORT).show();
            }
        }
    };
    
    private void postToFacebook(String review) {    
        mProgress.setMessage("Posting ...");
        mProgress.show();
         
        AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
         
        Bundle params = new Bundle();
        
        params.putString("message", review);
        params.putString("caption", "feelfit"); //app name
        params.putString("name", name + " used feelfit"); //username used feelfit?
        params.putString("description", "calories: " + post_cal + ", distance : " + post_distance); //user result
        String url_app = "https://play.google.com/store/search?q=" + '"' + "feelfit" + '"' + "&c=apps";
        params.putString("link", url_app); //link to app feelfit in fb?
        //params.putString("link", "https://play.google.com/store/search?q=feelfit&c=apps"); //link to app feelfit in fb? 
        //params.putString("link", "https://play.google.com/store/apps/details?id=com.mindidea.feelfit"); //link to app feelfit in fb?              
        //params.putString("picture", "http://www.dropbox.com/s/x6d170qjg1yuf6w/logo144.png"); //feelfit logo
         
        mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
    }
 
    private final class WallPostListener extends BaseRequestListener {
        public void onComplete(final String response) {
            mRunOnUi.post(new Runnable() {
                @Override
                public void run() {
                    mProgress.cancel();                 
                    Toast.makeText(fbSharedDialog.this, "post to your wall", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

}
