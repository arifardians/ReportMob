package com.retail.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arif.reportpanel.PanelReportPreview;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.Model;
import com.example.retail.R;
import com.simpd.my_list_view.RowItem;

public class ReportListActivity extends Activity {
	
	private TextView title; 
	private ImageButton btnBack;
	private LinearLayout container;
	
	private long[] formIds;
	
	protected void onCreate(Bundle savedInstanceState) {
		//getActionBar().hide();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown_gelap)));

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_report_list); 
		title 	= (TextView) findViewById(R.id.report_list_title); 
		btnBack	= (ImageButton) findViewById(R.id.report_list_button_back);
		container = (LinearLayout) findViewById(R.id.report_list_container);
		title.setText("List Report"); 
		btnBack.setOnClickListener(actionBack());
		
		setTitle("Report List");
		
//		updateListView(getRowItemData());
		drawPanel(getFormData());
		
	};
	
	
	private void drawPanel(ArrayList<Model> formData){
		PanelReportPreview panel = null;
		for (Model model : formData) {
			panel = new PanelReportPreview(this);
			panel.setContainer(container);
			panel.setFormId(model.getId());
			panel.created();
		}
		
	} 
	
	private ArrayList<Model> getFormData(){
		FormDataDAO formDao = new FormDataDAO(getApplicationContext()); 
		ArrayList<Model> datas = formDao.getAll();
		formDao.close();
		return datas;
	}
		
	private OnClickListener actionBack(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		};
	}
	
	private ArrayList<RowItem> getRowItemData(){
		
		FormDataDAO formDao = new FormDataDAO(getApplicationContext()); 
		ArrayList<Model> datas = formDao.getAll();
		formDao.close();
		ArrayList<RowItem> rowItems = new ArrayList<RowItem>();
		RowItem rowItem = null;
		FormData form = null;
		String tanggal = null;
		
		formIds 	= new long[datas.size()];
		int i = 0;
		for (Model model : datas) {
			form = (FormData) model;
			rowItem = new RowItem(); 
			rowItem.setIconId(R.drawable.ic_news); 
			rowItem.setTitle(form.getName()); 
			rowItem.setSubtitle(form.getDescription());
			tanggal = form.getUpdatedAt().substring(0, 10);
			tanggal = tanggal.replace("-", "/");
			rowItem.setValue(tanggal);
			
			rowItems.add(rowItem); 
			
			formIds[i] 		= form.getId();
			i++;
		}
		
		return rowItems;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
}
