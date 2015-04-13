package com.arif.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class NoDefaultSpinner extends Spinner{
	
	OnItemSelectedListener listener; 
	int prevPost = -1; 
	
	public NoDefaultSpinner(Context context) {
		super(context);
	}
	
	public NoDefaultSpinner(Context context, AttributeSet attrs){
		super(context, attrs); 
	}
	
	public NoDefaultSpinner(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setSelection(int position) {
		super.setSelection(position);
		if(position == getSelectedItemPosition() && prevPost == position){
			getOnItemSelectedListener().onItemSelected(null, null, position, 0);
		}
		prevPost = position;
	}

}
