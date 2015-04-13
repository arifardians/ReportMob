package com.arif.formbuilder;

import java.util.ArrayList;

import com.example.retail.R;
import com.retail.activity.FormActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RadioGroupComponent extends FormComponent{
	
	private LayoutInflater inflater;
	private RelativeLayout result; 
	private LinearLayout formContainer;
	private boolean isEditable;
	private Context context;
	private RadioGroup radioGroup;
	private TextView label;
	
	private Button btnEdit; 
	private Button btnRemove;
	
	private boolean isTextFormat;
	private String fieldName;
	private String placehoder;
	private ArrayList<String> listOptions;
	private long id;
	
	public RadioGroupComponent(Context context) {
		this.context = context;
		inflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		result		 = (RelativeLayout) inflater.inflate(R.layout.form_radio, null); 
		
		radioGroup	 = (RadioGroup) result.findViewById(R.id.form_radio_radio_group);
		label		 = (TextView) result.findViewById(R.id.form_radio_label); 
		btnEdit		 = (Button) result.findViewById(R.id.form_radio_button_edit); 
		btnRemove	 = (Button) result.findViewById(R.id.form_radio_button_delete); 
		
		isEditable	 = true; 
		isTextFormat = true;
		
		setActionButton();		
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
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		
	}

	@Override
	public String getFieldName() {
		return fieldName;
		
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
	protected void setActionButton() {
		btnEdit.setOnClickListener(actionEdit());
		btnRemove.setOnClickListener(actionRemove(getId()));

	}

	@Override
	public boolean isEditable() {
		return isEditable;
		
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
		}else{
			btnEdit.setVisibility(View.GONE); 
			btnRemove.setVisibility(View.GONE);
		}
		
	}

	@Override
	public boolean isNumberFormat() {
		return !this.isTextFormat;
		
	}

	@Override
	public void isNumberFormat(boolean isNumberFormat) {
		// do nothing
		
	}

	@Override
	public boolean isTextFormat() {
		return this.isTextFormat;

	}

	@Override
	public void isTextFormat(boolean isTextFormat) {
		// do nothing!
		
	}

	@Override
	public void setOptions(ArrayList<String> listOptions) {
		this.listOptions = listOptions;
	
	}

	@Override
	public void create() {
		showModifyButton(isEditable); 
		
		RadioButton radio = null; 
		for (String option : listOptions) {
			radio = new RadioButton(context);
			radio.setText(option); 
			radio.setTextColor(Color.BLACK);
			
			radioGroup.addView(radio);
		}
		
		label.setText(fieldName);
		setActionButton();
				
		formContainer.addView(result);
	}

	@Override
	public String getInputValue() {
		String inputValue = ""; 
		RadioButton radio = null;
		
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			radio = (RadioButton) radioGroup.getChildAt(i);
			if(radio.isChecked()){
				inputValue += radio.getText().toString();
				break;
			}
		}
		
		return inputValue ;
	
	}
	
	@Override
	public void setInputValue(String values) {
		RadioButton radio = null;
		
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			radio = (RadioButton) radioGroup.getChildAt(i);
			if(radio.getText().toString().equalsIgnoreCase(values)){
				radio.setChecked(true);
				break;
			}
		}
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
	public RelativeLayout getResultView() {
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public void update(int indexField) {
		showModifyButton(isEditable); 
		
		RadioButton radio = null; 
		for (String option : listOptions) {
			radio = new RadioButton(context);
			radio.setText(option); 
			radio.setTextColor(Color.BLACK);
			
			radioGroup.addView(radio);
		}
		
		label.setText(fieldName);
		setActionButton(); 
		
		formContainer.removeViewAt(indexField); 
		formContainer.addView(result, indexField);
	}

	@Override
	public void setPlaceholder(String placeholder) {
		this.placehoder = placeholder;
		
	}

	@Override
	public String getPlaceholder() {
		return placehoder;
		
	}
		
}
