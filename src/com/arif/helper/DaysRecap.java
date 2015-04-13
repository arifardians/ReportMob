package com.arif.helper;

import java.util.Calendar;
import java.util.Date;

public class DaysRecap {
	private static final int DAYS_OF_WEEK = 7;
	public int[] TOTAL_VALUES = new int[DAYS_OF_WEEK];		
			
	private String title;
	
	public static final int SUNDAY 		= 0; 
	public static final int MONDAY 		= 1; 
	public static final int TUESDAY 	= 2; 
	public static final int WEDNESDAY 	= 3; 
	public static final int THURSDAY 	= 4; 
	public static final int FRIDAY 		= 5; 
	public static final int SATURDAY 	= 6;
	
	public static final String[] STR_DAYS = {"Sunday", "Monday", "Tuesday", 
											 "Wednesday", "Thursday", "Friday", 
											 "Saturday"};
		
	public void addData(Date date, int value){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			TOTAL_VALUES[SUNDAY] += value; 
			break;
		case Calendar.MONDAY: 
			TOTAL_VALUES[MONDAY] += value;  
			break; 
		case Calendar.TUESDAY: 
			TOTAL_VALUES[TUESDAY] += value; 
			break; 
		case Calendar.WEDNESDAY: 
			TOTAL_VALUES[WEDNESDAY] += value; 
			break; 
		case Calendar.THURSDAY: 
			TOTAL_VALUES[THURSDAY] += value; 
			break; 
		case Calendar.FRIDAY: 
			TOTAL_VALUES[FRIDAY] += value; 
			break; 
		case Calendar.SATURDAY: 
			TOTAL_VALUES[SATURDAY] += value; 
			break; 
		
		default:
			break;
		}
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
