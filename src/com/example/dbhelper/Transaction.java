package com.example.dbhelper;

import com.example.retail.R.id;

public class Transaction extends Model {
	
	private long id;
	private long formId; 
	private long recordOrder;
	private String createdAt;
	private String updatedAt;
	private long createdBy; 
	private long updatedBy;
	
	@Override
	public long getId() {
		return id;
		
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}
	

	public long getFormId() {
		return formId;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}


	public long getRecordOrder() {
		return recordOrder;
	}

	public void setRecordOrder(long recordOrder) {
		this.recordOrder = recordOrder;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
