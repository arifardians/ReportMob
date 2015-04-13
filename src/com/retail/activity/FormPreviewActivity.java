package com.retail.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.arif.formbuilder.CheckboxComponent;
import com.arif.formbuilder.DatePickerComponent;
import com.arif.formbuilder.DropdownComponent;
import com.arif.formbuilder.FormComponent;
import com.arif.formbuilder.RadioGroupComponent;
import com.arif.formbuilder.TextComponent;
import com.example.dbhelper.FieldOption;
import com.example.dbhelper.FieldOptionDAO;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.dbhelper.FormFieldDAO;
import com.example.dbhelper.Model;
import com.example.dbhelper.Transaction;
import com.example.dbhelper.TransactionDAO;
import com.example.dbhelper.TransactionDetail;
import com.example.dbhelper.TransactionDetailDAO;
import com.example.retail.R;

public class FormPreviewActivity extends Activity {
	
	private static final String TAG = "[FormPreview]";
	
	private LinearLayout container; 
	private ImageButton btnBack;
	private Button btnSubmit; 
	private ArrayList<FormComponent> components;
	
	private TextView textTitle;
	private TextView textFormName; 
	private TextView textFormDesc;
	
	private long formId;
	private long transactionId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		
		formId 			= getIntent().getExtras().getLong("formId");
		transactionId 	= getIntent().getExtras().getLong("transactionId"); 
				
		container 	= (LinearLayout) findViewById(R.id.preview_container);
		btnSubmit 	= (Button) findViewById(R.id.preview_button_submit);
		btnBack		= (ImageButton) findViewById(R.id.form_preview_button_back);
		textTitle	= (TextView) findViewById(R.id.form_preview_title); 
		textFormName= (TextView) findViewById(R.id.form_preview_form_name);
		textFormDesc= (TextView) findViewById(R.id.form_preview_form_desc);
		
		
		FormDataDAO formDao = new FormDataDAO(getApplicationContext());
		FormData form = (FormData) formDao.findById(formId);
		formDao.close();
		
		TransactionDAO transactionDAO = new TransactionDAO(getApplicationContext()); 
		Transaction transaction = (Transaction) transactionDAO.findById(transactionId);
		transactionDAO.close();
		
		ArrayList<Model> details  = null;
		
		if(transactionId != 0 && transaction != null){
			TransactionDetailDAO detailDAO = new TransactionDetailDAO(getApplicationContext());
			details = detailDAO.findByTransactionId(transaction.getId());
			detailDAO.close();
		}
		
		textTitle.setText(form.getName()); 
		textFormName.setText("Form " + form.getName()); 
		textFormDesc.setText(form.getDescription()); 
		
		FormFieldDAO fieldDAO = new FormFieldDAO(getApplicationContext()); 
		
		ArrayList<FormField> fields = fieldDAO.findFieldByFormId(formId); 
		fieldDAO.close();
		components = new ArrayList<FormComponent>();
		TransactionDetail detail = null;
		
		long[] detailIds = null;
		if(details != null && details.size() >0){
			detailIds = new long[details.size()];
			int i =0;
			for (Model model : details) {
				detailIds[i] = model.getId();
				i++;
			}
		}
		
		
		int count = 0;
		for (FormField field : fields) {
			if(details != null && details.size() >0){
				count = 0;
				for (Model model : details) {
					detail = (TransactionDetail) model;
					Log.d(TAG, "field id : " + field.getId() + ", detail field id " + detail.getFieldId());
					if(field.getId() == detail.getFieldId()){
						Log.d(TAG, "field id : " + field.getId()+", type : " 
									+ MyConstant.TIPE_INPUT[field.getType()] +", values : " + detail.getFieldValue());
						createForm(field, detail.getFieldValue());
						break;
					}else{
						count++;
						if(count == details.size()){
							createForm(field, null);
							break;
						}
					}
				}
			}else
				createForm(field, null);
		}

		btnBack.setOnClickListener(actionBack());
		btnSubmit.setOnClickListener(actionSubmit());
		/*btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String result = "";
				
				for (FormComponent item : components) {
					result += item.getFieldName() + ": " + item.getInputValue()+"\n";
				}
				
				Toast.makeText(FormPreviewActivity.this, result, Toast.LENGTH_LONG).show();
				
			}
		});*/
		
	}
	
	
	private OnClickListener actionSubmit(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					saveTransaction();
					showSuccessDialog("Congrats..!", "Transaction data successfully saved!");
					
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
	
	private void saveTransaction(){
		
		if(transactionId == 0){
			insertNewTransaction();
		}else{
			updateTransaction(transactionId);
		}
		
	}
	
	private void insertNewTransaction(){
		Transaction transaction = new Transaction(); 
		transaction.setFormId(formId);
		
		TransactionDAO transactionDAO = new TransactionDAO(getApplicationContext()); 
		transactionId =  transactionDAO.insert(transaction);
		transactionDAO.close(); 
		
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(getApplicationContext());
		TransactionDetail detail = null;
		long insertId = 0 ;
		for (FormComponent component : components) {
			detail = new TransactionDetail(); 
			detail.setTransactionId(transactionId); 
			detail.setFieldId(component.getId());
			detail.setFieldValue(component.getInputValue());
			insertId = detailDAO.insert(detail);
			Log.d(TAG, "insert detail transaction, id: " + insertId 
					+ ", value : " + component.getInputValue());
		}
		detailDAO.close();
	}
	
	private void updateTransaction(long transactionId){
		TransactionDAO transactionDAO = new TransactionDAO(getApplicationContext()); 
		Transaction transaction = (Transaction) transactionDAO.findById(transactionId);
		transactionDAO.close();
		
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(getApplicationContext()); 
		ArrayList<Model> details = detailDAO.findByTransactionId(transaction.getId()); 
		TransactionDetail detail = null;
		
		
		if(details.size() > 0){
			String strDetails = " details : ";
			for (Model model : details) {
				detail = (TransactionDetail) model; 
				strDetails += detail.getFieldId()+", ";
			}
			
			Log.d(TAG, strDetails);
			
			String strComponents = " components : ";
			for (FormComponent component : components) {
				strComponents += ", " + component.getId();
			}
			Log.d(TAG, strComponents);
			
			long insertId = 0;
			
			for (FormComponent component : components) {
				if(components.indexOf(component) < details.size()){
					for (Model model : details) {
						detail = (TransactionDetail) model; 
						if(component.getId() == detail.getFieldId()){
							detail.setFieldValue(component.getInputValue());
							detailDAO.update(detail);
							Log.d(TAG, "update detail transaction, id: " + detail.getId() 
									+ ", value : " + component.getInputValue() + ", field id : " + detail.getFieldId());
							
						}
					}
				}else if(components.indexOf(component) >= details.size()){
					detail = new TransactionDetail();
					detail.setTransactionId(transactionId);
					detail.setFieldId(component.getId());
					detail.setFieldValue(component.getInputValue());
					insertId = detailDAO.insert(detail);
					Log.d(TAG, "insert detail transaction, id: " + insertId 
							+ ", value : " + component.getInputValue());
				}
			}
		}
		
		detailDAO.close();
		
	}
	
	private void createForm(FormField field, String values){
		FormComponent component = null;
		
		FieldOptionDAO optionDAO = new FieldOptionDAO(getApplicationContext());
		ArrayList<Model> optionDatas = optionDAO.findByFieldID(field.getId());
		optionDAO.close();
		
		Log.d(TAG, "number of options : " + optionDatas.size());
		
		ArrayList<String> listOptions = new ArrayList<String>();
		FieldOption option = null;
		if(optionDatas.size() > 0){
			for (Model model : optionDatas) {
				option = (FieldOption) model;
				Log.d(TAG, "inserting "+option.getName() + " to list Options");
				listOptions.add(option.getName());
			}
		}
		
		
		switch (field.getType()) {
		case MyConstant.INPUT_TEXT:
			component = new TextComponent(this);			
			break;
		case MyConstant.INPUT_NUMBER: 
			component = new TextComponent(this);
			component.isNumberFormat(true);
			break;
		case MyConstant.INPUT_RADIO_BUTTON: 
			component = new RadioGroupComponent(this);
			break;
		case MyConstant.INPUT_CHECK_BOX: 
			component = new CheckboxComponent(this);
			break;
		case MyConstant.INPUT_DROPDOWN:
			component = new DropdownComponent(this);
			break;
		case MyConstant.INPUT_DATE: 
			component = new DatePickerComponent(this); 
			break;
		default:
			component = new TextComponent(this);
			break;
		}
		
				
		component.setId(field.getId());
		component.setFieldName(field.getTitle()); 
		component.setEditable(false);
		component.setPlaceholder(field.getPlaceholder());
		component.setOptions(listOptions);
		component.setFormContainer(container);		
		component.create();
		
		Log.d(TAG, "field id : " + field.getId()+", type : " 
				+ MyConstant.TIPE_INPUT[field.getType()] +", values : " + values);
		if(values != null){
			component.setInputValue(values);
		}
		components.add(component);
	}
	
	private void showSuccessDialog(String title, String message){
		
		SweetAlertDialog alert = new SweetAlertDialog(FormPreviewActivity.this, SweetAlertDialog.SUCCESS_TYPE);
		alert.setTitleText(title); 
		alert.setContentText(message); 
		alert.show();
	}
}

