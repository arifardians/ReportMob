package com.arif.helper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retail.R;

public class MyTableRow {
	private String strLeft; 
	private String strRight; 
	
	private LayoutInflater inflater; 
	private LinearLayout container; 
	private LinearLayout result; 
	private RelativeLayout tableRowContent;
	private TextView textLeft; 
	private TextView textRight; 
	private int backgroundColor;
	
	public MyTableRow(Context context) {
		inflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		result 		= (LinearLayout) inflater.inflate(R.layout.table_row, null); 
		
		tableRowContent = (RelativeLayout) result.findViewById(R.id.table_row_content);
		textLeft 	= (TextView) result.findViewById(R.id.table_row_text_left);
		textRight 	= (TextView) result.findViewById(R.id.table_row_text_right); 
		backgroundColor = Color.WHITE;
	}


	public String getStrLeft() {
		return strLeft;
	}


	public void setStrLeft(String strLeft) {
		this.strLeft = strLeft;
	}


	public String getStrRight() {
		return strRight;
	}


	public void setStrRight(String strRight) {
		this.strRight = strRight;
	}


	public LinearLayout getContainer() {
		return container;
	}


	public void setContainer(LinearLayout container) {
		this.container = container;
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void create(){
		textLeft.setText(strLeft); 
		textRight.setText(strRight); 
		tableRowContent.setBackgroundColor(backgroundColor);
		
		container.addView(result);
	}
	
}
