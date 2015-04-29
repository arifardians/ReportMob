package com.arif.formbuilder;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.retail.R;
import com.retail.activity.FormActivity;


public class DropdownComponent extends FormComponent{
	
	private LayoutInflater inflater;
	private RelativeLayout result; 
	private LinearLayout formContainer;
	private boolean isEditable;
	private Context context;
	private Spinner spinner;
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
	
	public DropdownComponent(Context context) {
		this.context = context;
		inflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		result		 = (RelativeLayout) inflater.inflate(R.layout.form_dropdown, null); 
		
		spinner		 = (Spinner) result.findViewById(R.id.form_dropdown_spinner);
		label		 = (TextView) result.findViewById(R.id.form_dropdown_label); 
		btnEdit		 = (Button) result.findViewById(R.id.form_dropdown_button_edit); 
		btnRemove	 = (Button) result.findViewById(R.id.form_dropdown_button_delete); 
		btnUp		 = (Button) result.findViewById(R.id.form_dropdown_button_up);
		btnDown		 = (Button) result.findViewById(R.id.form_dropdown_button_down);
		
		textRequired = (Button) result.findViewById(R.id.form_dropdown_text_required);
		
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
					order = index + 1;
					reOrderField(order, index);
				}
				
			}
		};
	}
	
	@Override
	protected void setActionButton() {
		btnEdit.setOnClickListener(actionEdit());
		btnRemove.setOnClickListener(actionRemove(getId()));
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
		showModifyButton(isEditable);
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
		this.isTextFormat = true;
		
	}

	@Override
	public void create() {
		showModifyButton(isEditable); 
				
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
									   R.layout.spinner_item, 
									   R.id.spinner_item_text, 
									   listOptions);*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, listOptions);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		label.setText(fieldName); 
		setActionButton();
		
		formContainer.addView(result); 
		order = formContainer.indexOfChild(result);
	}

	@Override
	public String getInputValue() {
		return spinner.getSelectedItem().toString();
	
	}
	
	@Override
	public void setInputValue(String values) {
		int selectedIndex = listOptions.indexOf(values);
		spinner.setSelection(selectedIndex);
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
	public void setOptions(ArrayList<String> listOptions) {
		this.listOptions = listOptions;
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
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
									   R.layout.spinner_item, 
									   R.id.spinner_item_text, 
									   listOptions);
		spinner.setAdapter(adapter);
		
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
		return this.placeholder;
		
	}

	@Override
	public int getOrder() {
		return order;
	}

	
		
}
