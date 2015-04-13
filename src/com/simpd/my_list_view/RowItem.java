package com.simpd.my_list_view;

public class RowItem {
	private String title; 
	private String subtitle; 
	private int iconId; 
	private String value; 
	private boolean hasRead = false;
	
	public RowItem() {
	}
	
	public RowItem(String title, String subtitle, int iconId, String value){
		this.title 		= title; 
		this.subtitle 	= subtitle;
		this.iconId 	= iconId; 
		this.value 		= value;
	}
	
	public void setTitle(String title){
		this.title = title; 
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setSubtitle(String subtitle){
		this.subtitle = subtitle;
	}
	
	public String getSubtitle(){
		return this.subtitle;
	}
	
	public void setIconId(int iconId){
		this.iconId = iconId;
	}
	
	public int getIconId(){
		return this.iconId;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public boolean hasRead(){
		return this.hasRead;
	}
	
	public void hasRead(boolean hasRead){
		if(hasRead){
			this.hasRead = true;
		}else{
			this.hasRead = false;
		}
	}
	
}
