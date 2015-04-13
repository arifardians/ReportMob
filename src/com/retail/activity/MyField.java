package com.retail.activity;

import java.util.ArrayList;

public class MyField {
	
	private long id;
	private String name; 
	private String placeholder;
	private int inputType; 
	private int dataType; 
	private ArrayList<String> options;
	
	public MyField(String name) {
		this.name = name; 
		this.inputType = MyConstant.INPUT_TEXT;
		this.dataType  = MyConstant.TYPE_STRING;
		this.options   = new ArrayList<String>();
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
	
	
	
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public int getInputType() {
		return inputType;
	}
	
	public void setInputType(int inputType) {
		this.inputType = inputType;
	} 
	
	public int getDataType() {
		return dataType;
	}
	
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	public ArrayList<String> getOptions() {
		return options;
	}
	
	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}
	
	@Override
	public String toString() {
		
		return "[field name : " + name + ", input type : " + MyConstant.TIPE_INPUT[inputType] 
			 + ", data type : " + MyConstant.TIPE_DATA[dataType] + "]";
	}
	
}
