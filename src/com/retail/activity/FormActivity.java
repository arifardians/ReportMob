package com.retail.activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.arif.formbuilder.CheckboxComponent;
import com.arif.formbuilder.DatePickerComponent;
import com.arif.formbuilder.DropdownComponent;
import com.arif.formbuilder.FormComponent;
import com.arif.formbuilder.RadioGroupComponent;
import com.arif.formbuilder.TextComponent;
import com.arif.helper.ImageHelper;
import com.example.dbhelper.FieldOption;
import com.example.dbhelper.FieldOptionDAO;
import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.dbhelper.FormFieldDAO;
import com.example.dbhelper.Model;
import com.example.retail.R;

public class FormActivity extends Activity {
	
	private static final String TAG = "[FormActivity]";
	
	Button btnAddField;
	Button btnSimpan;
	PopupWindow popupWindow;
	ImageButton btnBack;
	
	private static LinearLayout parentLayout;
	public static ArrayList<MyField> fields;
	public static ArrayList<Long> deletedFieldIDs;
	
	public static Activity activity;

	private ArrayList<String> listOptions;
	private long formId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_form);
		activity = this;
		
		formId = getIntent().getLongExtra("formId", 0);
		FormDataDAO formDao = new FormDataDAO(getApplicationContext()); 
		
		FormData form = (FormData) formDao.findById(formId);
		formDao.close();
		
		TextView title = (TextView) findViewById(R.id.add_form_title);
		btnBack	= (ImageButton) findViewById(R.id.add_form_button_back);
		
		title.setText(form.getName());
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		parentLayout = (LinearLayout) findViewById(R.id.add_form_container);
		
		fields = new ArrayList<MyField>();
		deletedFieldIDs = new ArrayList<Long>();
		
		drawFieldForm(form.getId());
	
		
		btnAddField = (Button) findViewById(R.id.add_form_tambah_input);
		
		btnSimpan   = (Button) findViewById(R.id.add_form_button_save);
		
		
		btnSimpan.setOnClickListener(new View.OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				// save current form	
				if(isFormValid()){
					actionSaveForm();
					showSuccessDialog("Congrats..!", "Form was successfully updated!");
				}else{
					SweetAlertDialog alert = new SweetAlertDialog(FormActivity.this, SweetAlertDialog.ERROR_TYPE);
					alert.setTitle("Oops..!");
					alert.setContentText("Form must have date field!");
					alert.show();
				}
			}
		});
		 
		btnAddField.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// add new field to current form			
				actionAddField();
			}
		});
	}
	
	private void actionSaveForm(){
		FormDataDAO formDAO = new FormDataDAO(getApplicationContext()); 
		FormData form = (FormData) formDAO.findById(formId);
		form.setNumRows(fields.size());
		formDAO.update(form);
		formDAO.close();
		
		FormField formField = null;
		FormFieldDAO fieldDAO = new FormFieldDAO(getApplicationContext());
		// fieldDAO.deleteFieldByFormId(formId);
		String fieldName = null;
		String colName	= null;
		long fieldID = 0;
		
		for (MyField field : fields) {
			fieldName = field.getName();
			colName	  = fieldName.toLowerCase(Locale.getDefault());
			colName	  = colName.replace(" ", "_");
			formField = new FormField();
			formField.setId(field.getId());
			formField.setFormId(formId);
			formField.setTitle(field.getName());
			formField.setName(colName);
			formField.setColName(colName);
			formField.setPlaceholder(field.getPlaceholder());
			formField.setDataType(field.getDataType());
			formField.setType(field.getInputType());
			formField.setListOrder(field.getOrder());
			Log.d(TAG, field.toString());
			if(field.getId()== 0){
				fieldID = fieldDAO.insert(formField);
			}else{
				fieldDAO.update(formField);
			}
			
			if(field.getInputType() == MyConstant.INPUT_CHECK_BOX || 
			   field.getInputType() == MyConstant.INPUT_DROPDOWN ||
			   field.getInputType() == MyConstant.INPUT_RADIO_BUTTON){
				
				if(field.getOptions().size() > 0 ){
					FieldOptionDAO optionDAO = new FieldOptionDAO(getApplicationContext());
					optionDAO.deleteByFieldID(fieldID);
					FieldOption option = null;
					int i = 0;
					Log.d(TAG, "number of option : " + field.getOptions().size());
					for (String strOption : field.getOptions()) {
						Log.d(TAG, "insert option : " + strOption + " to database");
						option = new FieldOption(); 
						option.setFieldID(fieldID);
						option.setName(strOption);
						option.setValue(strOption);
						option.setOrder(i); 
						optionDAO.insert(option);
						i++;
					}
					
					optionDAO.close();
				}
				
			}
		}
		Log.d(TAG, "number of deleted id : " + deletedFieldIDs.size());
		for (long deletedID : deletedFieldIDs) {
			fieldDAO.delete(deletedID);
			Log.d(TAG, "deleted id : "+deletedID);
		}
		
		fieldDAO.close();
	}
	
	private boolean isFormValid(){
		boolean formValid = false;
		boolean isDate;
		for (MyField field : fields) {
			isDate = field.getInputType() == MyConstant.INPUT_DATE && field.getDataType() == MyConstant.TYPE_DATE;
			if(isDate){
				formValid = true; 
				break;
			}
		}
		return formValid;
	}
	
	private void actionAddField(){
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE); 
		View popupView = inflater.inflate(R.layout.popup_field, null); 
		popupWindow = new PopupWindow(popupView,    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true); 
		popupWindow.setAnimationStyle(R.style.PopupAnimaitonBottom);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true); 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		

		final EditText inputName = (EditText) popupView.findViewById(R.id.popup_input_nama); 
		final EditText inputPlaceholder = (EditText) popupView.findViewById(R.id.popup_input_placeholder);
		final Spinner  spinnerTipeInput = (Spinner) popupView.findViewById(R.id.popup_spinner_tipe_input);
		final Spinner  spinnerTipeData  = (Spinner) popupView.findViewById(R.id.popup_spinner_tipe_data); 
		
		
		Button   btnSubmit = (Button) popupView.findViewById(R.id.popup_button_submit);
		Button   btnCancel = (Button) popupView.findViewById(R.id.popup_button_cancel);
		
		final TextView labelOption = (TextView) popupView.findViewById(R.id.popup_input_label_opsi);
		final EditText inputOpsi = (EditText) popupView.findViewById(R.id.popup_input_text_opsi);
		
		labelOption.setVisibility(View.GONE);
		inputOpsi.setVisibility(View.GONE);
		
		ArrayAdapter<CharSequence> adapterTipeInput = new ArrayAdapter<CharSequence>(
														  getApplicationContext(), 
														  R.layout.spinner_item, 
														  MyConstant.TIPE_INPUT);
		spinnerTipeInput.setAdapter(adapterTipeInput);
		
		spinnerTipeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				
				String selectedItem = MyConstant.TIPE_INPUT[position];
				if(selectedItem.equalsIgnoreCase("check box") || 
				   selectedItem.equalsIgnoreCase("combo box") || 
				   selectedItem.equalsIgnoreCase("radio button")){
					
					labelOption.setVisibility(View.VISIBLE);
					inputOpsi.setVisibility(View.VISIBLE);
				}else{
					labelOption.setVisibility(View.GONE); 
					inputOpsi.setVisibility(View.GONE);
				}
				
				switch (position) {
				case MyConstant.INPUT_TEXT:
					spinnerTipeData.setSelection(MyConstant.TYPE_STRING);
					break;
				case MyConstant.INPUT_NUMBER: 
					spinnerTipeData.setSelection(MyConstant.TYPE_NUMBER);
					break;
				case MyConstant.INPUT_DATE:
					spinnerTipeData.setSelection(MyConstant.TYPE_DATE);
					break;
				default:
					spinnerTipeData.setSelection(MyConstant.TYPE_STRING);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ArrayAdapter<CharSequence> adapterTipeData = new ArrayAdapter<CharSequence>(
														 getApplicationContext(), 
														 R.layout.spinner_item, 
														 MyConstant.TIPE_DATA); 
		
		spinnerTipeData.setAdapter(adapterTipeData);
						
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int inputType = spinnerTipeInput.getSelectedItemPosition(); 
				
				if(inputType == MyConstant.INPUT_CHECK_BOX || 
				   inputType == MyConstant.INPUT_DROPDOWN ||
				   inputType == MyConstant.INPUT_RADIO_BUTTON){
					listOptions = new ArrayList<String>(); 
					String textOptions = inputOpsi.getText().toString(); 
					String[] optionItems = textOptions.split("\n"); 
					for (String item : optionItems) {
						listOptions.add(item);
						Log.d("[add item]", "add item : "+item+" to listOptions");
					}
				}
				
				FormField field = new FormField();
				field.setName(inputName.getText().toString().toLowerCase(Locale.getDefault())); 
				field.setTitle(inputName.getText().toString());
				field.setPlaceholder(inputPlaceholder.getText().toString());
				field.setType(spinnerTipeInput.getSelectedItemPosition()); 
				field.setDataType(spinnerTipeData.getSelectedItemPosition());
				
				FormFieldDAO fieldDAO = new FormFieldDAO(getApplicationContext());
				long fieldId = fieldDAO.insert(field);
				fieldDAO.close();
				field.setId(fieldId);
				
				if((field.getType() == MyConstant.INPUT_DROPDOWN ||
					field.getType() == MyConstant.INPUT_RADIO_BUTTON ||
					field.getType() == MyConstant.INPUT_CHECK_BOX) && listOptions.size() > 0){
					
					FieldOptionDAO optionDAO = new FieldOptionDAO(getApplicationContext());
					FieldOption option = null; 
					
					for (int j = 0; j < listOptions.size(); j++) {
						option = new FieldOption(); 
						option.setFieldID(fieldId);
						option.setName(listOptions.get(j));
						option.setValue(listOptions.get(j));
						option.setOrder(j); 
						optionDAO.insert(option);
						
					}
					optionDAO.close();
					
				}
				createForm(field);
				
				// saving form
				actionSaveForm();
				popupWindow.dismiss();
			}
		});
		
		
		
		View view = new View(FormActivity.this);
		int dp = 60;
		int px = ImageHelper.dpToPx(dp, this);
		popupWindow.showAtLocation(view, Gravity.TOP, 0, px);
		
		
	}
	
	private void showSuccessDialog(String title, String message){
		
		SweetAlertDialog alert = new SweetAlertDialog(FormActivity.this, SweetAlertDialog.SUCCESS_TYPE);
		alert.setTitleText(title); 
		alert.setContentText(message); 
		alert.show();
	}
	
	private void drawFieldForm(long formId){
		
		FormFieldDAO fieldDao = new FormFieldDAO(getApplicationContext());
		
		ArrayList<FormField> formFields = fieldDao.findFieldByFormId(formId);
		fieldDao.close(); 
		
		for (FormField field : formFields) {
			Log.d(TAG, field.toString());
			createForm(field);
		}
		
	}
	
		
	private void createForm(FormField field){
		MyField myField = new MyField(field.getTitle());
		myField.setId(field.getId());
		myField.setInputType(field.getType());
		myField.setDataType(field.getDataType()); 
		myField.setPlaceholder(field.getPlaceholder());
		
		
		if(field.getId() > 0 ){
			FieldOptionDAO optionDAO = new FieldOptionDAO(getApplicationContext());
			ArrayList<Model> optionDatas = optionDAO.findByFieldID(field.getId());
			optionDAO.close();
			
			Log.d(TAG, "number of options : " + optionDatas.size());
			
			listOptions = new ArrayList<String>();
			FieldOption option = null;
			if(optionDatas.size() > 0){
				for (Model model : optionDatas) {
					option = (FieldOption) model;
					Log.d(TAG, "inserting "+option.getName() + " to list Options");
					listOptions.add(option.getName());
				}
			}
			
		}
		
		Log.d(TAG, "list options size : " + listOptions.size());
		myField.setOptions(listOptions);
		
		FormComponent component = null;
		
		
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
		component.setPlaceholder(field.getPlaceholder());
		component.setEditable(true);
		component.setOptions(listOptions);
		component.setFormContainer(parentLayout);		
		component.create();
		
		myField.setOrder(component.getOrder());
		fields.add(myField);
	}
	
	public static void showEditWindow(final int indexField, final Context context){
		final MyField myField = fields.get(indexField);
		final long editedID   = myField.getId();
		Log.d(TAG, "want to edit field id : " + editedID + ", input type : "+ MyConstant.TIPE_INPUT[myField.getInputType()]);
		Log.d("activity", ""+activity);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE); 
		
		View popupView = inflater.inflate(R.layout.popup_field, null); 
		
		final PopupWindow popupEdit = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true); 
		popupEdit.setAnimationStyle(R.style.PopupAnimaitonBottom);
		popupEdit.setOutsideTouchable(true);
		popupEdit.setTouchable(true); 
		popupEdit.setBackgroundDrawable(new BitmapDrawable());
		
		final EditText inputName 		= (EditText) popupView.findViewById(R.id.popup_input_nama); 
		final EditText inputPlaceholder	= (EditText) popupView.findViewById(R.id.popup_input_placeholder);
		final Spinner  spinnerTipeInput = (Spinner) popupView.findViewById(R.id.popup_spinner_tipe_input);
		final Spinner  spinnerTipeData  = (Spinner) popupView.findViewById(R.id.popup_spinner_tipe_data); 
		
		inputName.setText(myField.getName()); 
		inputPlaceholder.setText(myField.getPlaceholder());	
		
		Button   btnSubmit = (Button) popupView.findViewById(R.id.popup_button_submit);
		Button   btnCancel = (Button) popupView.findViewById(R.id.popup_button_cancel);
		
		final TextView labelOption = (TextView) popupView.findViewById(R.id.popup_input_label_opsi);
		final EditText inputOpsi = (EditText) popupView.findViewById(R.id.popup_input_text_opsi);
		
		labelOption.setVisibility(View.GONE);
		inputOpsi.setVisibility(View.GONE);
		
		ArrayAdapter<CharSequence> adapterTipeInput = new ArrayAdapter<CharSequence>(
				  context, 
				  R.layout.spinner_item, 
				  MyConstant.TIPE_INPUT);
		
		spinnerTipeInput.setAdapter(adapterTipeInput);
		
		spinnerTipeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				
				String selectedItem = MyConstant.TIPE_INPUT[position];
				if(selectedItem.equalsIgnoreCase("check box") || 
				   selectedItem.equalsIgnoreCase("combo box") || 
				   selectedItem.equalsIgnoreCase("radio button")){
					
					labelOption.setVisibility(View.VISIBLE);
					inputOpsi.setVisibility(View.VISIBLE);
					
					if(myField.getOptions() != null && myField.getOptions().size() > 1){
						String option2String = "";
						for (String option : myField.getOptions()) {
							option2String += option + "\n";
						}
						inputOpsi.setText(option2String);
					}
				}else{
					labelOption.setVisibility(View.GONE); 
					inputOpsi.setVisibility(View.GONE);
				}
				
				switch (position) {
				case MyConstant.INPUT_TEXT:
					spinnerTipeData.setSelection(MyConstant.TYPE_STRING);
					break;
				case MyConstant.INPUT_NUMBER: 
					spinnerTipeData.setSelection(MyConstant.TYPE_NUMBER);
					break;
				case MyConstant.INPUT_DATE:
					spinnerTipeData.setSelection(MyConstant.TYPE_DATE);
					break;
				default:
					spinnerTipeData.setSelection(MyConstant.TYPE_STRING);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinnerTipeInput.setSelection(myField.getInputType());
		
		ArrayAdapter<CharSequence> adapterTipeData = new ArrayAdapter<CharSequence>(
				 context, 
				 R.layout.spinner_item, 
				 MyConstant.TIPE_DATA); 

		spinnerTipeData.setAdapter(adapterTipeData);
		
		spinnerTipeData.setSelection(myField.getDataType());

		btnCancel.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				popupEdit.dismiss();
			}
		});
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyField myField = new MyField(inputName.getText().toString()); 
				myField.setId(editedID);
				myField.setInputType(spinnerTipeInput.getSelectedItemPosition()); 
				myField.setDataType(spinnerTipeData.getSelectedItemPosition()); 
				myField.setPlaceholder(inputPlaceholder.getText().toString());
				Log.d(TAG, "done edit field id : " + editedID + ", input type : "+ MyConstant.TIPE_INPUT[myField.getInputType()]);
				
				int inputType = spinnerTipeInput.getSelectedItemPosition(); 
				if(inputType == MyConstant.INPUT_CHECK_BOX || 
				   inputType == MyConstant.INPUT_DROPDOWN ||
				   inputType == MyConstant.INPUT_RADIO_BUTTON){
					ArrayList<String> listOptions = new ArrayList<String>(); 
					String textOptions = inputOpsi.getText().toString(); 
					String[] optionItems = textOptions.split("\n"); 
					for (String item : optionItems) {
						listOptions.add(item);
						Log.d("[add item]", "add item : "+item+" to listOptions");
					}
					myField.setOptions(listOptions);
				}
				
				editForm(myField, context, indexField);
				popupEdit.dismiss();
			}
		});
		int dp = 60;
		int px = ImageHelper.dpToPx(dp, context);
		popupEdit.showAtLocation(new View(context), Gravity.TOP, 0, px);
			
	}
	
	private static void editForm(MyField myField, Context context, int indexField){
		FormComponent component = null;
				
		
		switch (myField.getInputType()) {
		case MyConstant.INPUT_TEXT:
			component = new TextComponent(context);			
			break;
		case MyConstant.INPUT_NUMBER: 
			component = new TextComponent(context);
			component.isNumberFormat(true);
			break;
		case MyConstant.INPUT_RADIO_BUTTON: 
			component = new RadioGroupComponent(context);
			break;
		case MyConstant.INPUT_CHECK_BOX: 
			component = new CheckboxComponent(context);
			break;
		case MyConstant.INPUT_DROPDOWN:
			component = new DropdownComponent(context);
			break;
		case MyConstant.INPUT_DATE: 
			component = new DatePickerComponent(context);
			break;
		default:
			component = new TextComponent(context);
			break;
		}
		
		component.setId(myField.getId());
		component.setFieldName(myField.getName()); 
		component.setEditable(true);
		component.setPlaceholder(myField.getPlaceholder());
		component.setOptions(myField.getOptions());
		component.setFormContainer(parentLayout);		
		component.update(indexField);
		
		myField.setOrder(component.getOrder());
		fields.set(indexField, myField);
	}

}
