package com.arif.formbuilder;

import java.util.ArrayList;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retail.R;
import com.retail.activity.FormActivity;

public class TextComponent extends FormComponent {
	
	private LayoutInflater inflater;
	private RelativeLayout result; 
	private LinearLayout formContainer;
	private boolean isEditable;
	private Context context;
	private EditText editText;
	private TextView label;
	
	private Button btnEdit; 
	private Button btnRemove;
	private Button btnUp; 
	private Button btnDown;
	
	private int order;
	private boolean isTextFormat;
	private String fieldName;
	private String placeholder;
	private long id;
	
	public TextComponent(Context context) {
		this.context 		= context; 
		this.isEditable 	= false; 
		this.isTextFormat 	= true;
		
		inflater			= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.result  		= (RelativeLayout) inflater.inflate(R.layout.form_text, null);
		
		editText	= (EditText) result.findViewById(R.id.form_text_input);
		btnEdit 	= (Button) result.findViewById(R.id.form_text_button_edit); 
		btnRemove 	= (Button) result.findViewById(R.id.form_text_button_delete);
		btnUp		= (Button) result.findViewById(R.id.form_text_button_up); 
		btnDown		= (Button) result.findViewById(R.id.form_text_button_down);
		label		= (TextView) result.findViewById(R.id.form_text_label);
		
		textRequired = (TextView) result.findViewById(R.id.form_text_required);
		setActionButton();
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
	protected OnClickListener actionUp(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = formContainer.indexOfChild(result);
				if(index > 0){
					formContainer.removeView(result);
					formContainer.addView(result, index-1);
					order = index -1;
					
					reOrderField(index, order);
				}
				
			}
		};
	}
	
	@Override
	protected OnClickListener actionDown(){
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

	@Override
	public boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
		showModifyButton(isEditable);
	}
	
	@Override
	protected void showModifyButton(boolean isEditable){
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
	public void create() {
		showModifyButton(isEditable);
				
		if(isTextFormat)
			editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		else
			editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		if (placeholder == null || placeholder.equalsIgnoreCase("")) {
			placeholder = fieldName;
		}
		
		editText.setHint(placeholder);
		label.setText(fieldName);
		
		setActionButton();
		
		formContainer.addView(result);
		order = formContainer.indexOfChild(result);
	}

	@Override
	public String getInputValue() {
		String inputValue = editText.getText().toString();
		return inputValue;
	}
	
	@Override
	public boolean isTextFormat(){
		return this.isTextFormat;
	}
	
	@Override
	public void isTextFormat(boolean isTextFormat){
		this.isTextFormat = isTextFormat; 
	}
	
	@Override
	public void isNumberFormat(boolean isNumberFormat){
		this.isTextFormat = !isNumberFormat;
	}
	
	@Override
	public boolean isNumberFormat(){
		return !this.isTextFormat;
	}


	@Override
	public void setOptions(ArrayList<String> listOptions) {
		// text component has not list options 
		
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
		
		if(isTextFormat)
			editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		else
			editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		
		editText.setHint(placeholder);
		label.setText(fieldName);
		setActionButton();
		
		formContainer.removeViewAt(indexField);
		formContainer.addView(result, indexField);
		order = indexField;
	}
	
	protected void setActionButton(){
		btnEdit.setOnClickListener(actionEdit());
		btnRemove.setOnClickListener(actionRemove(getId()));
		btnUp.setOnClickListener(actionUp());
		btnDown.setOnClickListener(actionDown());
	}


	@Override
	public void setPlaceholder(String placeholder) {
		 this.placeholder =  placeholder;
		
	}


	@Override
	public String getPlaceholder() {
		return placeholder;
		
	}

	@Override
	public void setInputValue(String values) {
		editText.setText(values);
		
	}

	@Override
	public int getOrder() {
		return this.order;
	}
	
}
