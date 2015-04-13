package com.simpd.my_list_view;

import java.util.List;

import com.example.retail.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class FlatListAdapter extends BaseAdapter {
	
	public static int LARGE_STYLE = 1; 
	public static int SMALL_STYLE = 2;
	private Context context; 
	private List<RowItem> rowItems;
	
	private int currentStyle = SMALL_STYLE; 
	
	public FlatListAdapter(Context context, List<RowItem> rowItems) {
		this.context  = context; 
		this.rowItems = rowItems;
	}
	
	@Override
	public int getCount() {
		return this.rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(getItem(position));
	}
	
	public void setItem(int position, RowItem item){
		this.rowItems.add(position, item);
	}
	
	public void setStyle(int style){
		this.currentStyle = style; 
	}
	
	private class ViewHolder{
		ImageView icon; 
		TextView title; 
		TextView subtitle;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null; 
		LayoutInflater layoutInflater = (LayoutInflater) context.
										getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			holder 	= new ViewHolder(); 
			if(currentStyle == SMALL_STYLE){
				convertView = (View) layoutInflater.inflate(R.layout.flat_list_item, null); 
				
				holder.icon 	= (ImageView) convertView.findViewById(R.id.flat_item_icon); 
				holder.title	= (TextView) convertView.findViewById(R.id.flat_item_title); 
				holder.subtitle	= (TextView) convertView.findViewById(R.id.flat_item_subtitle);
			}else if(currentStyle == LARGE_STYLE){
				convertView = (View) layoutInflater.inflate(R.layout.flat_list_item_large, null); 
				
				holder.icon 	= (ImageView) convertView.findViewById(R.id.flat_item_icon_large); 
				holder.title	= (TextView) convertView.findViewById(R.id.flat_item_title_large); 
				holder.subtitle	= (TextView) convertView.findViewById(R.id.flat_item_subtitle_large);
			}
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		RowItem item = rowItems.get(position);
		holder.icon.setBackgroundResource(item.getIconId()); 
		holder.title.setText(item.getTitle()); 
		holder.title.setTypeface(Typeface.DEFAULT_BOLD);
		holder.subtitle.setText(item.getSubtitle());
		
		return convertView;
	}
	
	

}
