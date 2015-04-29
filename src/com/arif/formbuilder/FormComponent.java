package com.arif.formbuilder;

import java.util.ArrayList;

import com.retail.activity.FormActivity;
import com.retail.activity.MyField;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class FormComponent {
	
	protected boolean isRequired;
	protected TextView textRequired; 
	
	abstract public void setId(long id);
	abstract public long getId();
	abstract public void setFormContainer(LinearLayout formContainer);
	abstract public LinearLayout getFormContainer();
	abstract public void setFieldName(String fieldName);
	abstract public String getFieldName();
	abstract public void setPlaceholder(String placeholder);
	abstract public String getPlaceholder();
	abstract public int getOrder();
	abstract protected View.OnClickListener actionEdit(); 
	abstract protected View.OnClickListener actionRemove(long id); 
	abstract protected View.OnClickListener actionUp();
	abstract protected View.OnClickListener actionDown();
	abstract protected void setActionButton();
	
	abstract public boolean isEditable();
	abstract public void setEditable(boolean isEditable);
	abstract protected void showModifyButton(boolean isEditable);
	
	abstract public boolean isNumberFormat();
	abstract public void isNumberFormat(boolean isNumberFormat);
	abstract public boolean isTextFormat();
	abstract public void isTextFormat(boolean isTextFormat);
	
	abstract public void setOptions(ArrayList<String> listOptions);
	
	abstract public void create();
	abstract public void update(int indexField);
	abstract public RelativeLayout getResultView();
	abstract public String getInputValue();
	abstract public void setInputValue(String values);
	
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
		if(isRequired){
			textRequired.setVisibility(View.VISIBLE);
		}else{
			textRequired.setVisibility(View.GONE);
		}
	}
	protected void reOrderField(int upIndex, int downIndex){
		MyField fieldUp = FormActivity.fields.get(upIndex);
		fieldUp.setOrder(downIndex);
		
		MyField fieldDown = FormActivity.fields.get(downIndex);
		fieldDown.setOrder(upIndex);
		
		FormActivity.fields.set(downIndex, fieldUp);
		FormActivity.fields.set(upIndex, fieldDown);
	}
}
