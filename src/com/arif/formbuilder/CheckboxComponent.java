package com.arif.formbuilder;

import java.util.ArrayList;

import com.example.retail.R;
import com.retail.activity.FormActivity;
import com.retail.activity.MyField;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CheckboxComponent extends FormComponent {
	
	private LayoutInflater inflater;
	private RelativeLayout result;
	private LinearLayout formContainer;
	private boolean isEditable;
	private Context context;
	private LinearLayout containerCheckbox;
	private TextView label;
	
	private Button btnEdit; 
	private Button btnRemove;
	private Button btnUp;
	private Button btnDown;
	
	private int order; 
	
	private boolean isTextFormat;
	private String fieldName;
	private String placeholder;
	private ArrayList<String> listOptions;
	private long id;
	 
	public CheckboxComponent(Context context) {
		this.context = context; 
		inflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		result		 = (RelativeLayout) inflater.inflate(R.layout.form_checkbox, null); 
		
		containerCheckbox = (LinearLayout) result.findViewById(R.id.form_checkbox_container); 
		label		 = (TextView) result.findViewById(R.id.form_checkbox_label); 
		btnEdit		 = (Button) result.findViewById(R.id.form_checkbox_button_edit); 
		btnRemove	 = (Button) result.findViewById(R.id.form_checkbox_button_delete); 
		btnUp		 = (Button) result.findViewById(R.id.form_checkbox_button_up); 
		btnDown		 = (Button) result.findViewById(R.id.form_checkbox_button_down);
		
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
					order = index+1;
					
					reOrderField(order, index);
				}
				
			}
		};
	}
	
	
	
	@Override
	protected void setActionButton() {
		btnEdit.setOnClickListener(actionEdit());
		btnRemove.setOnClickListener(actionRemove(id));
		btnUp.setOnClickListener(actionUp()); 
		btnDown.setOnClickListener(actionDown());
	}

	@Override
	public boolean isEditable() {
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

	@Override
	public void create() {
		showModifyButton(isEditable);
		CheckBox checkBox = null;
		Log.d("list size ", "ukuran list : "+listOptions.size());
		for (String option : listOptions) {
			checkBox = new CheckBox(context); 
			checkBox.setText(option); 
			checkBox.setTextColor(Color.BLACK); 
			containerCheckbox.addView(checkBox);
		}
		
		label.setText(fieldName);
		setActionButton();	

		formContainer.addView(result);
		order = formContainer.indexOfChild(result);
	}

	@Override
	public String getInputValue() {
		String inputValue = ""; 
		CheckBox checkBox = null;
		for (int i = 0; i < containerCheckbox.getChildCount(); i++) {
			checkBox = (CheckBox) containerCheckbox.getChildAt(i); 
			if(checkBox.isChecked()){
				inputValue += checkBox.getText().toString() +", ";
			}
		}
		inputValue = inputValue.substring(0, inputValue.length()-2);
		return inputValue;
	}
	
	@Override
	public void setInputValue(String values) {
		String[] arrayValues = values.split(", ");
		CheckBox checkBox = null;
		String dataValue = null; 
		String cbValue = null;
		for (int i = 0; i < arrayValues.length; i++) {
			for (int j = 0; j < containerCheckbox.getChildCount(); j++) {
				checkBox = (CheckBox) containerCheckbox.getChildAt(j);
				dataValue = arrayValues[i]; 
				cbValue	  = checkBox.getText().toString();
				if(dataValue.equalsIgnoreCase(cbValue)){
					checkBox.setChecked(true);
					break;
				}
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
		return result;
		
	}

	@Override
	public void update(int indexField) {
		showModifyButton(isEditable);
		CheckBox checkBox = null;
		Log.d("list size ", "ukuran list : "+listOptions.size());
		for (String option : listOptions) {
			checkBox = new CheckBox(context); 
			checkBox.setText(option); 
			checkBox.setTextColor(Color.BLACK); 
			containerCheckbox.addView(checkBox);
		}
		
		label.setText(fieldName);
		setActionButton();
		
		formContainer.removeViewAt(indexField);
		formContainer.addView(result, indexField);
		order = indexField;
	}

	@Override
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		
	}

	@Override
	public String getPlaceholder() {
		return placeholder;

	}

	@Override
	public int getOrder() {
		return this.order;
	}

			
}
