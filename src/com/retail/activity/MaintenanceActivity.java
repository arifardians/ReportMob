package com.retail.activity;

import com.example.retail.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MaintenanceActivity extends Activity{
	
	ImageButton btnBack;
	TextView 	textTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintenance); 
		
		btnBack   = (ImageButton) findViewById(R.id.maintenance_button_back); 
		textTitle = (TextView) findViewById(R.id.maintenance_title); 
		
		String title =  getIntent().getStringExtra("title");
		
		textTitle.setText(title);
		
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
