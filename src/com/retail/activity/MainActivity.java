package com.retail.activity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.example.retail.R;
import com.simpd.my_list_view.FlatListAdapter;
import com.simpd.my_list_view.RowItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	Button btnAddForm; 
	ListView listAction; 
	Button btnLogout; 	
	
	ArrayList<RowItem> rowItems; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().hide();	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnAddForm = (Button) findViewById(R.id.main_buttonAdd); 
		btnLogout  = (Button) findViewById(R.id.main_button_logout); 
		
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionButtonKeluar();
				
			}
		});
		
		listAction = (ListView) findViewById(R.id.main_list);
		
		btnAddForm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FormActivity.class);
				startActivity(intent);
				
			}
		});
			
		final String[] titles = {"Manage Form", "Input Transaction", 
						   "Manage Workflow", "Report"};
		int[] icons = {R.drawable.ic_setting, R.drawable.ic_help,
					  R.drawable.ic_exit, R.drawable.ic_notification};
		
		rowItems = new ArrayList<RowItem>(); 
		RowItem item = null; 
		for (int i = 0; i < titles.length; i++) {
			item = new RowItem(); 
			item.setTitle(titles[i]); 
			item.setIconId(icons[i]); 
			item.setSubtitle("");
			rowItems.add(item);
		}
		
		FlatListAdapter adapter = new FlatListAdapter(this, rowItems);
		
		listAction.setAdapter(adapter);
		listAction.setVisibility(View.VISIBLE);
		
		listAction.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null; 
				switch (position) {
				case 0:
					intent = new Intent(MainActivity.this, FormListActivity.class);
					startActivity(intent);
					break;
				case 1: 
					intent = new Intent(MainActivity.this, FormTransactionActivity.class);
					startActivity(intent); 
					break;
				case 2:
					intent = new Intent(MainActivity.this, TestActivity.class);
					startActivity(intent);
					break;
				case 3: 
					intent = new Intent(MainActivity.this, ReportListActivity.class);
					startActivity(intent);
					break;
				default:
					intent = new Intent(MainActivity.this, MaintenanceActivity.class);
					intent.putExtra("title", titles[position]);
					startActivity(intent);
					break;
				}
				
			}
		});
		
	}
	
	
	
	private void actionButtonKeluar(){
		SweetAlertDialog alert = new SweetAlertDialog(MainActivity.this, 
				SweetAlertDialog.WARNING_TYPE);
		alert.setTitleText("Warning..!"); 
		alert.setContentText("Are you sure to logout?");
		alert.setConfirmText("Yes, Logout"); 
		alert.setCancelText("Cancel"); 
		alert.showCancelButton(true); 
		alert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.cancel();
			}
		});
		
		alert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent); 
				finish();
			}
		});
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
