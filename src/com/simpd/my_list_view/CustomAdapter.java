package com.simpd.my_list_view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.retail.R;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	Context context;
	List<RowItem> rowItems; 
	List<RowItem> arraylist;
	boolean isDefaultStyle = true;

	final int offsetSubtitleReport = 0;
	private int offsetSubtitleDefault = 90;

	public CustomAdapter(Context context, List<RowItem> rowItems) {
		this.context 	= context; 
		this.rowItems 	= rowItems;
		
		this.arraylist  = new ArrayList<RowItem>(); 
		this.arraylist.addAll(rowItems);
	}

	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}
	
	public void setItem(int position, RowItem item){
		this.rowItems.set(position, item);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(getItem(position));
	}

	public void setToReportStyle(boolean isReportStyle){
		this.isDefaultStyle = false;
	}

	/* private holder class*/
	private class ViewHolder{
		ImageView icon; 
		TextView title; 
		TextView subtitle; 
		TextView value; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = null;
		result = useDefaultStyle(position, convertView, parent);

		return result;
	}

	private View useDefaultStyle(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null; 

		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		if(convertView == null){
			convertView = (View) layoutInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();

			holder.icon 	= (ImageView) convertView.findViewById(R.id.list_item_icon);
			holder.title 	= (TextView) convertView.findViewById(R.id.list_item_title); 
			holder.subtitle = (TextView) convertView.findViewById(R.id.list_item_subtitle);
			holder.value 	= (TextView) convertView.findViewById(R.id.list_item_value); 

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics(); 
		float dpWidth =  displayMetrics.widthPixels/displayMetrics.density;
		int maxWidthLabel = (int) (dpWidth - offsetSubtitleDefault);

		RelativeLayout.LayoutParams params = (LayoutParams) holder.title.getLayoutParams();
		params.width = maxWidthLabel;
		holder.title.setLayoutParams(params);
		/*Log.d("[subtitle-width]", "width subtitle : "+maxWidthLabel+", screen width : "+dpWidth);

		Log.d("[holder]", "is holder = null ? "+(holder == null));*/		
		RowItem row_pos = rowItems.get(position);
		holder.icon.setBackgroundResource(row_pos.getIconId()); 
		holder.title.setText(row_pos.getTitle()); 
		holder.subtitle.setText(row_pos.getSubtitle()); 
		holder.value.setText(row_pos.getValue()); 
		holder.title.setTypeface(Typeface.DEFAULT_BOLD);
		
		if(row_pos.hasRead()){
			holder.title.setTextColor(Color.GRAY);
			holder.subtitle.setTextColor(Color.GRAY);
		}else{
			holder.title.setTextColor(Color.BLACK);
			holder.subtitle.setTextColor(Color.BLACK);
		}

		return convertView;
	}
	
	// Filter class 
	public void filter(String charText){
		charText = charText.toLowerCase(Locale.getDefault());
		Log.d("[filtering-list]", "filter list with parameter : "+charText);
		rowItems.clear(); 
		if(charText.length() == 0){
			rowItems.addAll(arraylist);
		}else{
			for (RowItem item : arraylist) {
				boolean isSelected = item.getTitle().toLowerCase(Locale.getDefault()).contains(charText);
				if(isSelected == true){
					Log.d("[filtering]", ""+charText+" : "+item.getTitle().toLowerCase(Locale.getDefault())+" => "+isSelected);
					rowItems.add(item);
				}
			}
		}
		
		notifyDataSetChanged();
	}
	
	/**
	 * filter by title skpd
 	 * @param hasRead
	 */
	
	public void filterByTitle(String title){
		title = title.toLowerCase(Locale.getDefault());
		Log.d("[filtering - title]", "filter list with parameter : "+title);
		rowItems.clear(); 
		for (RowItem item : arraylist) {
			boolean isSelected = item.getTitle().equalsIgnoreCase(title);
			if(isSelected){
				Log.d("[filtering - title]", ""+title+" : "+item.getTitle().toLowerCase(Locale.getDefault())+" => "+isSelected);
				rowItems.add(item);
			}
		}
		notifyDataSetChanged();
	}
	
	
	// Filter by read notifications 
	public void filterHasRead(boolean hasRead){
				
		rowItems.clear();
		for (RowItem item : arraylist) {
			if(item.hasRead() == hasRead){
				rowItems.add(item);
			}
		}
		notifyDataSetChanged();
	}
	
	public void setOffsetDefault(int offset){
		this.offsetSubtitleDefault = offset;
	}

}


















