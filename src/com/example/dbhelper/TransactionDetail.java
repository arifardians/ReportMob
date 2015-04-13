package com.example.dbhelper;

public class TransactionDetail extends Model{
	
	private long id; 
	private long transactionId;
	private long fieldId; 
	private String fieldValue;
	private String createdAt; 
	private String updatedAt; 
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}
	
	
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getFieldId() {
		return fieldId;
	}

	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
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

	@Override
	public String toString() {
		return  "Transaction detail : [ id: " + id +", transaction id: " + transactionId + 
				", field id: " + fieldId + ", field value : " + fieldValue + 
				", created at: " + createdAt + ", updatedAt: " + updatedAt + " ]";
		
	}

}
