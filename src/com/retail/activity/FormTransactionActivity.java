package com.retail.activity;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;

public class FormTransactionActivity extends Activity{
	
	private TextView title; 
	private Button  btnAdd; 
	private ImageButton btnBack;
	private ListView list;
	
	private long[] formIds;
	private String[] formNames;
	private static final String TAG = "[FormTransc-act]";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_form_list);
		
		title 	= (TextView) findViewById(R.id.form_list_title); 
		btnAdd	= (Button) findViewById(R.id.form_list_button_add); 
		list 	= (ListView) findViewById(R.id.form_list_listview); 
		btnBack	= (ImageButton) findViewById(R.id.form_list_button_back);
		
		title.setText("List input Forms"); 
		btnAdd.setVisibility(View.GONE); 
		btnBack.setOnClickListener(actionBack());
		
		
		updateListView(getRowItemData());
		
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
		formNames 	= new String[datas.size()];
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
			formNames[i]	= form.getName();
			i++;
		}
		
		return rowItems;
	}

	private void updateListView(ArrayList<RowItem> rowItems){
		
		CustomAdapter adapter = new CustomAdapter(getApplicationContext(), rowItems); 
		adapter.setToReportStyle(true);
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(FormTransactionActivity.this, ListTransactionActivity.class);
				intent.putExtra("formId", formIds[position]);
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		updateListView(getRowItemData());
		super.onResume();
	}
	
}
