package com.retail.activity;

import java.util.ArrayList;

import com.arif.reportpanel.PanelReportPreview;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.Model;
import com.example.retail.R;
import com.simpd.my_list_view.CustomAdapter;
import com.simpd.my_list_view.RowItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReportListActivity extends Activity {
	
	private TextView title; 
	private ImageButton btnBack;
	private LinearLayout container;
	
	private long[] formIds;
	
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_report_list); 
		title 	= (TextView) findViewById(R.id.report_list_title); 
		btnBack	= (ImageButton) findViewById(R.id.report_list_button_back);
		container = (LinearLayout) findViewById(R.id.report_list_container);
		title.setText("List Report"); 
		btnBack.setOnClickListener(actionBack());
		
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
	
	/*private void updateListView(ArrayList<RowItem> rowItems){
		
		CustomAdapter adapter = new CustomAdapter(getApplicationContext(), rowItems); 
		adapter.setToReportStyle(true);
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(ReportListActivity.this, ReportActivity.class);
				intent.putExtra("formId", formIds[position]);
				startActivity(intent);
			}
		});
		
	}*/
	
}
