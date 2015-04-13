package com.retail.activity;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.example.dbhelper.FieldOptionDAO;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.dbhelper.FormFieldDAO;
import com.example.dbhelper.Model;
import com.example.retail.R;
import com.simpd.my_list_view.CustomAdapter;
import com.simpd.my_list_view.RowItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FormListActivity extends Activity {
	
	private ImageButton btnBack;
	private Button btnAdd; 
	private ListView list;
	private TextView title;
	
	private long[] formIds;
	private String[] formNames;
	private static final String TAG = "[formlist-act]";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_list); 
		
		btnBack = (ImageButton) findViewById(R.id.form_list_button_back); 
		btnAdd	= (Button) findViewById(R.id.form_list_button_add);
		list	= (ListView) findViewById(R.id.form_list_listview); 
		title 	= (TextView) findViewById(R.id.form_list_title);
		
		btnBack.setOnClickListener(actionBack());
		btnAdd.setOnClickListener(actionAddForm());
		title.setText("Manage form");
		

		// set list view, insert data form tb_form in database
		// then saved in arraylis<model>
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
	
	private OnClickListener actionAddForm(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInputNewForm();
				
			}
		};
	}
	
	private void showInputNewForm(){
		LayoutInflater inflater = FormListActivity.this.getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(FormListActivity.this); 
		
		View dialogView = inflater.inflate(R.layout.dialog_new_form, null); 
		
		final EditText inputName = (EditText) dialogView.findViewById(R.id.dialog_add_new_form_input); 
		final EditText inputDesc = (EditText) dialogView.findViewById(R.id.dialog_add_new_form_description); 
		builder.setView(dialogView);
		
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String formName = inputName.getText().toString();
				String formDesc = inputDesc.getText().toString();
				
				long formId = insertForm(formName, formDesc);
				Intent intent = new Intent(FormListActivity.this, FormActivity.class);
				
				intent.putExtra("formId", formId);
				startActivity(intent);
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
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
				
				Intent intent = new Intent(FormListActivity.this, FormActivity.class);
				intent.putExtra("formId", formIds[position]);
				startActivity(intent);
			}
		});
		
		registerForContextMenu(list);
		
	}
	
	private long insertForm(String name, String description){
		FormData form = new FormData();
		form.setName(name);
		form.setDescription(description);
		form.setNumRows(3); 
		
		FormDataDAO formDao = new FormDataDAO(getApplicationContext()); 
		long formId = formDao.insert(form); 
		formDao.close();
		
		Log.d(TAG, "insert form with id : " + formId + ", form detail : " + form.toString());
		
		insertSampleField(formId);
		
		return formId;
	}
	
	private void insertSampleField(long formId){
		
		int[] exampleInputs = {MyConstant.INPUT_NUMBER, 
				MyConstant.INPUT_NUMBER, 
				MyConstant.INPUT_DATE};
		
		int[] exampleTypes = {MyConstant.TYPE_NUMBER,
				MyConstant.TYPE_NUMBER, 
				MyConstant.TYPE_DATE};
		
		String[] exampleNames = {"Jumlah pendapatan", "Jumlah pengeluaran", "Tanggal"};
		
		FormField field = null;
		FormFieldDAO fieldDao = new FormFieldDAO(getApplicationContext()); 
		long insertId = 0;		
		for (int i = 0; i < exampleNames.length; i++) {
			field = new FormField(); 
			field.setTitle(exampleNames[i]); 
			field.setName(exampleNames[i].toLowerCase(Locale.getDefault()));
			field.setDataType(exampleTypes[i]); 
			field.setType(exampleInputs[i]); 
			field.setColName(exampleNames[i].toLowerCase(Locale.getDefault()));
			field.setListOrder(i); 
			field.setFormId(formId);
			insertId = fieldDao.insert(field);
			Log.d(TAG, "inserting field with id : " + insertId + ", detail field : " + field.toString());
		}
		
		fieldDao.close();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		if (v.getId() == R.id.form_list_listview) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo; 
			menu.setHeaderTitle(formNames[info.position]);
			String[] menuItems = {"Edit", "Delete"};
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = {"Edit", "Delete"};
		final long formId = formIds[info.position];
		final String formName = formNames[info.position];
		
		if(menuItems[menuItemIndex].equalsIgnoreCase("edit")){
			// edit page
			Intent intent = new Intent(FormListActivity.this, FormActivity.class);
			intent.putExtra("formId", formId);
			startActivity(intent);
		}else {
			// delete
			SweetAlertDialog alert = new SweetAlertDialog(FormListActivity.this, 
					SweetAlertDialog.WARNING_TYPE); 
			
			alert.setTitleText("Warning..!!"); 
			alert.setContentText("Are you sure to delete this "+ formName + " form ? "); 
			alert.setConfirmText("Yes, Delete"); 
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
					
					FormDataDAO formDao = new FormDataDAO(getApplicationContext());
					formDao.delete(formId);
					formDao.close();
					updateListView(getRowItemData());
					sweetAlertDialog.dismiss();
				}
			});
			alert.show();
						
		}
		
		return true;
	}
	
	@Override
	protected void onResume() {
		updateListView(getRowItemData());
		super.onResume();
	}
	
}
