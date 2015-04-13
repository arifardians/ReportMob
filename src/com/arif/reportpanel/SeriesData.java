package com.arif.reportpanel;

public class SeriesData {
	private String date; 
	private String value;
			
	private int year; 
	private int month; 
	private int day;
	
	public SeriesData() {
	}
	
	public SeriesData(String date, String value){
		this.date = date; 
		this.value = value; 
		date2Int(date);
	}

	public String getDate() {
		return date;
	}

	public boolean setDate(String date) {
		this.date = date;
		return date2Int(date);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	private boolean date2Int(String date){
		if(date.length() >= 10){
			date = date.substring(0, 10);
			String[] dates = date.split("-"); 
			year  = Integer.parseInt(dates[0]);
			month = Integer.parseInt(dates[1]) - 1;
			day   = Integer.parseInt(dates[2]);
			return true;
		}else{
			return false;
		}
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}
	
	public int getIntValue(){
		return Integer.parseInt(value);
		
	}
}
