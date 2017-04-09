//TestPost.java

package com.mindidea.feelfit;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.SessionStore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

public class TestPost extends Activity{
    private Facebook mFacebook;
    private CheckBox mFacebookCb;
    private ProgressDialog mProgress;
     
    private Handler mRunOnUi = new Handler();
     
    private static final String APP_ID = "608296725924940"; //feelfit app id
    
    Button posttowall;
    String name;
    
    float cal_sum, distance;
    String post_cal, post_distance;
    //String test_calsum, test_distance; //output
    
    TableRow logo_testpost;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.post);
        
        //to contact us
        logo_testpost = (TableRow)findViewById(R.id.toptab_fbpost);
        logo_testpost.setOnClickListener(new OnClickListener(){

      		@Override
      		public void onClick(View v) {
      			Intent goContact = new Intent(TestPost.this,contact.class);
      			startActivity(goContact);
      		}});
        
        //get intent value : cal_sum, time_total
        //test_calsum = getIntent().getStringExtra("test_calsum");
        //test_distance = getIntent().getStringExtra("test_distance");
        cal_sum = getIntent().getFloatExtra("cal_sum", 0);
        post_cal = String.format("%.2f", cal_sum);
        distance = getIntent().getFloatExtra("distance", 0);
        post_distance = String.format("%.2f", distance);
        
        //post facebook
        final EditText reviewEdit = (EditText) findViewById(R.id.revieew);
        mFacebookCb               = (CheckBox) findViewById(R.id.cb_facebook);
         
        mProgress   = new ProgressDialog(this);
         
        mFacebook   = new Facebook(APP_ID);
         
        SessionStore.restore(mFacebook, this);
 
        if (mFacebook.isSessionValid()) {
            mFacebookCb.setChecked(true);
                 
            name = SessionStore.getName(this);
            name        = (name.equals("")) ? "Unknown" : name;
                 
            mFacebookCb.setText("  Facebook  (" + name + ")");
        }
         
        posttowall = (Button) findViewById(R.id.button1);
        posttowall.setOnClickListener(new OnClickListener() {
            public void onClick (View v) {
                String review = reviewEdit.getText().toString();
                 
                if (review.equals("")) return;
             
                if (mFacebookCb.isChecked()) postToFacebook(review);
            }
        });
    }
     
    private void postToFacebook(String review) {    
        mProgress.setMessage("Posting ...");
        mProgress.show();
         
        AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
         
        Bundle params = new Bundle();
             
        params.putString("message", review);
        params.putString("caption", "feelfit"); //app name
        params.putString("name", name + " used feelfit"); //username used feelfit?
        params.putString("description", "calories: " + post_cal + ", distance : " + post_distance); //user result
        params.putString("link", "https://play.google.com/store/search?q=feelfit&c=apps"); //link to app feelfit in fb? 
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
                    Toast.makeText(TestPost.this, "post to your wall", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}