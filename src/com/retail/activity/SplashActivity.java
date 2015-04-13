package com.retail.activity;


import com.example.retail.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	
	private final static int SPLASH_DELAY_LENGTH = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide(); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_DELAY_LENGTH);
		
	}
}
