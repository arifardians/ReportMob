package com.example.dbhelper;

import java.io.Serializable;

public class FormData extends Model implements Serializable { 
	
	
	private static final long serialVersionUID = 0L;
	private long id 		= 0; 
	private long ownerId 	= 0; 
	private int ownerType 	= 0;
	private String name 	= ""; 
	private String description = "";
	private int numRows 	= 0;
	private String slug 	= "";
	
	private boolean isActivated;
	
	private String createdAt; 
	private String updatedAt;
	private long createdBy; 
	private long updatedBy;
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
		
	}
	
	public long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public int getOwnerType() {
		return ownerType;
	}
	
	public void setOwnerType(int ownerType) {
		this.ownerType = ownerType;
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumRows() {
		return numRows;
	}
	
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public String getSlug() {
		return slug;
	}
	
	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public boolean isActivated() {
		return isActivated;
	}
	
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	@Override
	public String toString() {
		String formData = "Form data [id : " + id 
					+ ", owner_id: " + ownerId
					+ ", owner_type : " + ownerType 
					+ ", form_name : " + name
					+ ", form_row: " + numRows 
					+ ", slug : " + slug
					+ ", is_activated : " + isActivated 
					+ ", created_at : " + createdAt 
					+ ", created_by : " + createdBy
					+ ", updated_at : " + updatedAt
					+ ", updated_by : " + updatedBy
					+ "]";
		return formData;
	}

		
}
