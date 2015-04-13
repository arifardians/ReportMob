package com.arif.helper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.retail.R;


public class DateHelper {
	
	public static final int LAST_WEEK 	= 0; 
	public static final int THIS_WEEK 	= 1; 
	public static final int LAST_MONTH 	= 2; 
	public static final int THIS_MONTH 	= 3;
	public static final int CUSTOM_DATE = 4; 
/*	public static final int TODAY = 0; 
	public static final int YESTERDAY = 1; */
	
	public static final String[] DAYS = {"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"};
	
	
	public static String getStringInterval(int type, Context context){
		String result = null;
		switch (type) {
		case LAST_WEEK: 
			result = getLastWeekInterval(); 
			break;
		case THIS_WEEK:
			result = getThisWeekInterval();
			break;
		case LAST_MONTH:
			result = getLastMonthInterval(); 
			break; 
		case THIS_MONTH: 
			result = getThisMonthInterval();
			break;
		
		default:
			result = getThisWeekInterval();
			break;
		}
		
		return result;
	}
	
	private static String getTodayInterval(){
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat df = DateFormat.getDateInstance();
		
		String result = df.format(date) + " - " + df.format(date);
		
		return result;
	}
	
	private static String getYesterdayInterval(){
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String result = df.format(date) + " - " + df.format(date);
		return result;
	}
	
	private static String getLastWeekInterval(){
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	
		cal.add(Calendar.DAY_OF_WEEK, -1 * 7);
		
		Date sunday = cal.getTime();
		cal.add(Calendar.DAY_OF_WEEK, 6);
		
		Date saturday = cal.getTime();
		DateFormat df = DateFormat.getDateInstance();
		
		String result = df.format(sunday) + " - " + df.format(saturday);
		return result;
	}
	
	private static String getThisWeekInterval(){
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		Date sunday = cal.getTime();
		cal.add(Calendar.DAY_OF_WEEK, 6); 
		
		Date saturday = cal.getTime();
		DateFormat df = DateFormat.getDateInstance();
		
		String result = df.format(sunday) + " - " + df.format(saturday);
		return result;		
	}
	
	private static String getLastMonthInterval(){
		Calendar cal = Calendar.getInstance();
		int currentMonth = cal.get(Calendar.MONTH);
		cal.set(Calendar.MONTH, currentMonth - 1);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date firstDate = cal.getTime();
		
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date lastDate = cal.getTime();
		
		DateFormat df = DateFormat.getDateInstance();
		
		String result = df.format(firstDate) + " - " + df.format(lastDate);
		return result;
	}
	
	private static String getThisMonthInterval(){
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date firstDate 	= cal.getTime(); 
		
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		Date lastDate	= cal.getTime();
		
		DateFormat df = DateFormat.getDateInstance(); 
		
		String result = df.format(firstDate) + " - " + df.format(lastDate);
		return result;
	}
	
	public static void getCustomDateInterval(Context context, final TextView text){
		Log.d("date interval", "this method is called!");
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customView = inflater.inflate(R.layout.multi_date_picker, null);
		
		// Define date picker
		final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.date_picker_start);
		final DatePicker dpEndDate	 = (DatePicker) customView.findViewById(R.id.date_picker_end); 
		
		// Build the dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(customView);
		builder.setTitle("Set date range"); 
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				long startDateTime = dpStartDate.getCalendarView().getDate();
				Date startDate 	= new Date(startDateTime);
				
				long endDateTime = dpEndDate.getCalendarView().getDate();
				Date endDate = new Date(endDateTime);
				
				DateFormat df = DateFormat.getDateInstance();
				String strDate = df.format(startDate) + " - " + df.format(endDate);
 				text.setText(strDate);
			}
		});
		
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		
		builder.create().show();
		
	}
	
	public static boolean isDateBetween(Date date, Date start, Date end){
		boolean isBetween = date.after(start) && date.before(end);
		boolean isSame  = date.equals(start) || date.equals(end);
		if(isBetween || isSame){
			return true;
		}
		return false;
	}
	
	
}
