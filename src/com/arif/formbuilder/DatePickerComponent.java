package com.arif.formbuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retail.R;
import com.retail.activity.FormActivity;

public class DatePickerComponent extends FormComponent {
	
	private LayoutInflater inflater;
	private RelativeLayout result; 
	private LinearLayout formContainer;
	private boolean isEditable;
	private Context context;
	private TextView label;
	
	private EditText dateInput;
	private Button btnPickDate;
	
	private Button btnEdit; 
	private Button btnRemove;
	private Button btnUp; 
	private Button btnDown; 
	
	private int order;
	private boolean isTextFormat;
	private String fieldName;
	private String placeholder;
	private ArrayList<String> listOptions;
	private String values;
	private long id;
	
	public DatePickerComponent(Context context) {
		
		this.context = context;
		inflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		result		 = (RelativeLayout) inflater.inflate(R.layout.form_date, null); 
		
		dateInput  	 = (EditText) result.findViewById(R.id.form_date_input);
		btnPickDate  = (Button) result.findViewById(R.id.form_date_button_pick_date);
		label		 = (TextView) result.findViewById(R.id.form_date_label); 
		btnEdit		 = (Button) result.findViewById(R.id.form_date_button_edit); 
		btnRemove	 = (Button) result.findViewById(R.id.form_date_button_delete); 
		btnUp		 = (Button) result.findViewById(R.id.form_date_button_up);
		btnDown		 = (Button) result.findViewById(R.id.form_date_button_down);
		
		isEditable	 = true; 
		isTextFormat = true;
		setActionButton();
		
		btnPickDate.setOnClickListener(actionButtonPick());
		dateInput.setEnabled(false);
		listOptions = new ArrayList<String>();
		listOptions.add("default value");
		
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
		
	}

	@Override
	public long getId() {
		return id;
		
	}
	
	@Override
	public void setFormContainer(LinearLayout formContainer) {
		this.formContainer = formContainer;
		
	}

	@Override
	public LinearLayout getFormContainer() {
		return this.formContainer;

	}

	@Override
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		
	}

	@Override
	public String getFieldName() {
		return this.fieldName;
		
	}

	@Override
	protected OnClickListener actionEdit() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int indexField = formContainer.indexOfChild(result);
				FormActivity.showEditWindow(indexField, context);
			}
		};
	}

	@Override
	protected OnClickListener actionRemove(final long id) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int indexField = formContainer.indexOfChild(result);
				formContainer.removeView(result);
				FormActivity.fields.remove(indexField);
				FormActivity.deletedFieldIDs.add(id);
			}
		};
	}
	
	@Override
	protected OnClickListener actionUp() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = formContainer.indexOfChild(result);
				if(index > 0){
					formContainer.removeView(result);
					formContainer.addView(result, index-1);
					order = index - 1;
					
					reOrderField(index, order);
				}
				
			}
		};
	}

	@Override
	protected OnClickListener actionDown() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = formContainer.indexOfChild(result);
				if(index < formContainer.getChildCount() -1){
					formContainer.removeView(result);
					formContainer.addView(result, index+1);
					order = index + 1;
					
					reOrderField(order, index);
				}
				
			}
		};
	}
	
	protected void setActionButton(){
		btnEdit.setOnClickListener(actionEdit());
		btnRemove.setOnClickListener(actionRemove(id));
		btnUp.setOnClickListener(actionUp()); 
		btnDown.setOnClickListener(actionDown());
	}
	
	@Override
	public boolean isEditable() {
		// TODO Auto-generated method stub
		return this.isEditable;
	}

	@Override
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
		
	}

	@Override
	protected void showModifyButton(boolean isEditable) {
		if(isEditable){
			btnEdit.setVisibility(View.VISIBLE); 
			btnRemove.setVisibility(View.VISIBLE);
			btnUp.setVisibility(View.VISIBLE);
			btnDown.setVisibility(View.VISIBLE);
		}else{
			btnEdit.setVisibility(View.GONE); 
			btnRemove.setVisibility(View.GONE);
			btnUp.setVisibility(View.GONE);
			btnDown.setVisibility(View.GONE);
		}
		
	}

	@Override
	public boolean isNumberFormat() {
		return !this.isTextFormat;
	
	}

	@Override
	public void isNumberFormat(boolean isNumberFormat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTextFormat() {
		return this.isTextFormat;

	}

	@Override
	public void isTextFormat(boolean isTextFormat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOptions(ArrayList<String> listOptions) {
		this.listOptions = listOptions;
		
	}
	
	private OnClickListener actionButtonPick(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showPickdateDialog();
			}
		};
	}
	
		
	private void showPickdateDialog(){
		Calendar calendar = Calendar.getInstance();
		int mYear = calendar.get(Calendar.YEAR);
		int mMonth = calendar.get(Calendar.MONTH); 
		int mDay  = calendar.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog pickDialog = new DatePickerDialog(context, 
				new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String strDate = formatDate(year, monthOfYear, dayOfMonth);
						dateInput.setText(strDate);
						dateInput.setSelection(dateInput.getText().toString().length());
					}
				}, mYear, mMonth, mDay); 
		pickDialog.show();
	}
	
	private String formatDate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTimeInMillis(0);
		calendar.set(year, month, day); 
		Date date = calendar.getTime(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		
		return sdf.format(date);
	}
	
	@Override
	public void create() {
		showModifyButton(isEditable);
		
		label.setText(fieldName);
		setActionButton();
		
		if (placeholder == null || placeholder.equalsIgnoreCase("")) {
			placeholder = fieldName;
		}
		
		dateInput.setHint(placeholder);
		formContainer.addView(result);
		order = formContainer.indexOfChild(result);
	}

	@Override
	public void update(int indexField) {
		showModifyButton(isEditable);
		
		
		label.setText(fieldName);
		setActionButton(); 
		
		if (placeholder == null || placeholder.equalsIgnoreCase("")) {
			placeholder = fieldName;
		}
		
		dateInput.setHint(placeholder);
		formContainer.removeViewAt(indexField);
		formContainer.addView(result, indexField);
		order = indexField;
	}

	@Override
	public RelativeLayout getResultView() {
		return result;
	
	}

	@Override
	public String getInputValue() {
		return dateInput.getText().toString();
		
	}
	
	@Override
	public void setInputValue(String values) {
		this.values = values;
		dateInput.setText(values);
		Log.d("date values", values);
	}
	

	@Override
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		
	}

	@Override
	public String getPlaceholder() {
		return this.placeholder;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	
	
}
