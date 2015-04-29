package com.retail.activity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.Model;
import com.example.dbhelper.Transaction;
import com.example.dbhelper.TransactionDAO;
import com.example.retail.R;
import com.simpd.my_list_view.CustomAdapter;
import com.simpd.my_list_view.RowItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;

public class ListTransactionActivity extends Activity{
	
	private Button btnAdd; 
	private ImageButton btnBack; 
	private ListView list; 
	private TextView title; 
	private TextView subTitle;
	
	private static final int DEFAULT_ID = 0;
	
	private long formId;
	private long[] transactionIds;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//getActionBar().hide();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown_gelap)));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_list);
		
		title 	= (TextView) findViewById(R.id.form_list_title); 
		btnAdd	= (Button) findViewById(R.id.form_list_button_add); 
		list 	= (ListView) findViewById(R.id.form_list_listview); 
		btnBack	= (ImageButton) findViewById(R.id.form_list_button_back);
		subTitle = (TextView) findViewById(R.id.form_preview_form_name);
		
		formId 	= getIntent().getLongExtra("formId", DEFAULT_ID); 
		
		FormDataDAO formDAO = new FormDataDAO(getApplicationContext()); 
		FormData form = (FormData) formDAO.findById(formId); 
		formDAO.close();
		
		title.setText("" + form.getName());
		subTitle.setText("Transactions data");
		btnAdd.setText("Add Data");
		btnAdd.setOnClickListener(actionAdd());
		btnBack.setOnClickListener(actionBack());
		
		setTitle(form.getName()+" transactions");
		updateListView(getRowItemData());
	}
	
	private OnClickListener actionAdd(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListTransactionActivity.this, TransactionActivity.class); 
				intent.putExtra("formId", formId);
				intent.putExtra("transactionId", 0); 
				
				startActivity(intent);
				
			}
		};
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
		TransactionDAO transactionDAO = new TransactionDAO(getApplicationContext()); 
		ArrayList<Model> allData =  transactionDAO.findByFormId(formId);
		transactionDAO.close();
		
		ArrayList<RowItem> rowItems = new ArrayList<RowItem>();
		RowItem rowItem = null;
		
		Transaction transaction = null;
		int i = 0;
		transactionIds = new long[allData.size()];
		
		for (Model model : allData) {
			transaction = (Transaction) model;
			rowItem = new RowItem(); 
			rowItem.setIconId(R.drawable.ic_news); 
			rowItem.setTitle("Data ke : " + (i+1));
			rowItem.setSubtitle("date modified : " + dateTimeToDate(transaction.getUpdatedAt()));
			rowItem.setValue(dateTimeToDate(transaction.getCreatedAt()));
			
			rowItems.add(rowItem);
			transactionIds[i] = transaction.getId();
			i++;
		}
		
		return rowItems;
	}
	
	private void updateListView(ArrayList<RowItem> rowItems){
		CustomAdapter adapter = new CustomAdapter(ListTransactionActivity.this, rowItems); 
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter); 
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ListTransactionActivity.this, TransactionActivity.class);
				intent.putExtra("formId", formId); 
				intent.putExtra("transactionId", transactionIds[position]);
				startActivity(intent);
			}
		});
		
		registerForContextMenu(list);
	}
	
	private String dateTimeToDate(String dateTime){
		String date = dateTime.substring(0, 10); 
		date = date.replace("-", "/"); 
		
		return date;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		if (v.getId() == R.id.form_list_listview) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo; 
			menu.setHeaderTitle("Data ke : " + (info.position+1));
			String[] menuItems = {"Edit", "Delete"};
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = {"Edit", "Delete"}; 
		
		if(menuItems[menuItemIndex].equalsIgnoreCase("edit")){
			Intent intent = new Intent(ListTransactionActivity.this, TransactionActivity.class);
			intent.putExtra("formId", formId); 
			intent.putExtra("transactionId", transactionIds[info.position]);
			startActivity(intent);
		}else{
			showWarningBeforeRemove(transactionIds[info.position]);
		}
		
		return true;
	}
	
	
	private void showWarningBeforeRemove(final long transactionId){
		SweetAlertDialog alert = new SweetAlertDialog(ListTransactionActivity.this, 
				SweetAlertDialog.WARNING_TYPE);
		alert.setTitleText("Warning...!"); 
		alert.setContentText("Are you sure to delete this record ?");
		alert.setConfirmText("Yes, delete"); 
		alert.setCancelText("Cancel");
		alert.showCancelButton(true); 
		alert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.dismiss();
				
			}
		});
		
		alert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.dismiss();
				deleteTransaction(transactionId);
				showSuccessDialog("Congrats..!", "The record has been deleted!");
				updateListView(getRowItemData());
			}
		});
		
		alert.show();
		
	}
	
	private void deleteTransaction(long transactionId){
		TransactionDAO transactionDAO = new TransactionDAO(getApplicationContext()); 
		transactionDAO.delete(transactionId);
		transactionDAO.close();
	}
	
	private void showSuccessDialog(String title, String message){
		
		SweetAlertDialog alert = new SweetAlertDialog(ListTransactionActivity.this, SweetAlertDialog.SUCCESS_TYPE);
		alert.setTitleText(title); 
		alert.setContentText(message); 
		alert.show();
	}
	
	@Override
	protected void onResume() {
		updateListView(getRowItemData());
		super.onResume();
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
