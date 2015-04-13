package com.retail.activity;

import java.util.ArrayList;
import java.util.Locale;

import com.arif.formbuilder.FormPanel;
import com.example.dbhelper.DBHelper;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.dbhelper.FormFieldDAO;
import com.example.dbhelper.Model;
import com.example.dbhelper.TransactionDAO;
import com.example.retail.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NewFormActivity extends Activity{
	
	private LinearLayout container;
	private static final String TAG ="[NewFormAct]";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_new);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown_gelap)));
		container = (LinearLayout) findViewById(R.id.new_form_container);
	
		drawListForm();
		
	}
	
	private void drawListForm(){
		container.removeAllViews();
		FormDataDAO formDAO = new FormDataDAO(getApplicationContext());
		ArrayList<Model> forms = formDAO.getAll();
		formDAO.close();
		
		TransactionDAO transactionDAO = new TransactionDAO(getApplicationContext());
		int totalRecord = 0;
		
		FormData form = null;
		FormPanel formPanel = null;
		
		for (Model model : forms) {
			form = (FormData) model;
			formPanel = new FormPanel(this);
			formPanel.setFormId(form.getId());
			formPanel.setTitle(form.getName());
			formPanel.setSubtitle(form.getDescription());
			
			totalRecord = transactionDAO.findByFormId(form.getId()).size();
			
			formPanel.setNumberField(totalRecord);
			formPanel.setContainer(container);
			formPanel.create();
		}
		transactionDAO.close();
	}
	
	@Override
	protected void onResume() {
		drawListForm();
		super.onResume();
	}
	
	private void showInputNewForm(){
		LayoutInflater inflater = this.getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		
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
				Intent intent = new Intent( NewFormActivity.this, FormActivity.class);
				
				intent.putExtra("formId", formId);
				startActivity(intent);
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
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
		
		int[] exampleInputs = {	MyConstant.INPUT_DATE};
		
		int[] exampleTypes 	= { MyConstant.TYPE_DATE};
		
		String[] exampleNames = {"Date"};
		
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_add_form:
			showInputNewForm();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_form_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
