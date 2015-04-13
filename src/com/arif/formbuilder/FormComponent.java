package com.arif.formbuilder;

import java.util.ArrayList;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class FormComponent {
	
	
	abstract public void setId(long id);
	abstract public long getId();
	abstract public void setFormContainer(LinearLayout formContainer);
	abstract public LinearLayout getFormContainer();
	abstract public void setFieldName(String fieldName);
	abstract public String getFieldName();
	abstract public void setPlaceholder(String placeholder);
	abstract public String getPlaceholder();
	abstract protected View.OnClickListener actionEdit(); 
	abstract protected View.OnClickListener actionRemove(long id); 
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
}
