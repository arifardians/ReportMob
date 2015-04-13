package com.retail.activity;

import java.util.ArrayList;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arif.reportpanel.PanelReportDetail;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.retail.R;

public class ReportActivity extends Activity{
	
	private final String TAG = "[ReportActivity]";
	
	private LinearLayout chartContainer; 
	private GraphicalView mChart;
	private long formId;
	
	private TextView title; 
	private ImageButton btnBack; 
	
	private ArrayList<FormField> numerikFields; 
	private AlertDialog alertDialog; 
	private FormField dateField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	/*	getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown_gelap)));
	*/
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		
		title 			= (TextView) findViewById(R.id.report_title);
		btnBack			= (ImageButton) findViewById(R.id.report_button_back);
		chartContainer  = (LinearLayout) findViewById(R.id.report_chart_view);
		
		
		formId = getIntent().getExtras().getLong("formId"); 
		PanelReportDetail panel = new PanelReportDetail(ReportActivity.this);
		panel.setFormId(formId);
		panel.setContainer(chartContainer);
		panel.created();
		
		FormDataDAO formDAO = new FormDataDAO(getApplicationContext()); 
		FormData form = (FormData) formDAO.findById(formId);
		formDAO.close();
		
		setTitle("Report " + form.getName());
		
		title.setText("Report "+ form.getName()); 
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

		
	}
	
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

	
	
	

