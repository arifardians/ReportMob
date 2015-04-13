package com.example.dbhelper;

public class FieldOption extends Model {
	
	private long id; 
	private String name; 
	private String value; 
	private int order;
	private long fieldID;
	
	@Override
	public long getId() {
		return this.getId();
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}
		

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getFieldID() {
		return fieldID;
	}

	public void setFieldID(long fieldID) {
		this.fieldID = fieldID;
	}

	@Override
	public String toString() {
		return "field options:  {id : "+id+", name : "+ name 
				+ ", value : " + value + ", order : " + order 
				+ ", field id : " + fieldID;
	}

}
