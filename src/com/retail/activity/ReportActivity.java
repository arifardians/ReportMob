package com.retail.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arif.reportpanel.PanelReportDetail;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.retail.R;

public class ReportActivity extends Activity{
	
	private final String TAG = "[ReportActivity]";
	
	private LinearLayout chartContainer; 
	private long formId;
	
	private TextView title; 
	private ImageButton btnBack; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	/*	getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown_gelap)));
	*/
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		
		chartContainer  = (LinearLayout) findViewById(R.id.general);
		
		
		formId = getIntent().getExtras().getLong("formId"); 
		PanelReportDetail panel = new PanelReportDetail(ReportActivity.this);
		panel.setFormId(formId);
		panel.setContainer(chartContainer);
		panel.created();
		
		FormDataDAO formDAO = new FormDataDAO(getApplicationContext()); 
		FormData form = (FormData) formDAO.findById(formId);
		formDAO.close();
		
		setTitle("Report " + form.getName());
		
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

	
	
	

