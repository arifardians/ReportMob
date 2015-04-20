package com.example.dbhelper;

import com.retail.activity.MyConstant;

public class FormField extends Model {
	
	private long id;
	private long formId; 
	private String title;
	private String name; 
	private int type; 
	private int size; 
	private String colName; 
	private int dataType; 
	private int listOrder;
	private boolean isFieldSystem;
	private String optionValues;
	private String placeholder;
	
	public FormField() {
		this.id = 0;
		this.formId = 0; 
		this.size = 25;
		this.isFieldSystem = false;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isFieldSystem() {
		return isFieldSystem;
	}
	
	public void setFieldSystem(boolean isFieldSystem) {
		this.isFieldSystem = isFieldSystem;
	}
	
	public long getFormId() {
		return formId;
	}
	
	public void setFormId(long formId) {
		this.formId = formId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getColName() {
		return colName;
	}
	
	public void setColName(String colName) {
		this.colName = colName;
	}
	
	public int getDataType() {
		return dataType;
	}
	
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	public int getListOrder() {
		return listOrder;
	}
	
	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}
	
	

	public String getOptionValues() {
		return optionValues;
	}

	public void setOptionValues(String optionValues) {
		this.optionValues = optionValues;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public String toString() {
		String fieldData = "Field data [ id : " + id
				+ ", field_title : " + title
				+ ", field_type : " + MyConstant.TIPE_INPUT[type]
				+ ", order : " + listOrder+"]"; 
		
		return fieldData;
	}


	
	
}
