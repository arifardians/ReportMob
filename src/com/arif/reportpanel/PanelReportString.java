package com.arif.reportpanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arif.helper.ColorHelper;
import com.arif.helper.ImageHelper;
import com.arif.helper.MyTableRow;
import com.example.dbhelper.FormField;
import com.example.dbhelper.FormFieldDAO;
import com.example.dbhelper.TransactionDetailDAO;
import com.example.retail.R;
import com.test.androidcharts.BarView;


public class PanelReportString {
	
	private long fieldId; 
	private LinearLayout container; 
	private Context context;
	private LinearLayout result;
	private LayoutInflater inflater; 
	
	private TextView textTitle; 
	private TextView textTitleTable;
	
	private LinearLayout chartLayout;
	private LinearLayout tableLayout;
	private LinearLayout chartPanel;
	private RelativeLayout chartHeader;
	
	private FormField field;
	
	private static final String TAG = "PanelReportString";
	
	public PanelReportString(Context context, long fieldId) {
		this.context = context;
		
		inflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		result 	 	= (LinearLayout) inflater.inflate(R.layout.panel_bar_chart, null);
		
		textTitle 	= (TextView) result.findViewById(R.id.panel_bar_chart_title);
		chartLayout = (LinearLayout) result.findViewById(R.id.panel_bar_chart_content);
		chartPanel	= (LinearLayout) result.findViewById(R.id.panel_bar_chart_panel);
		chartHeader = (RelativeLayout) result.findViewById(R.id.panel_bar_chart_header);
		
		textTitleTable = (TextView) result.findViewById(R.id.panel_bar_table_left_text);
		tableLayout	   = (LinearLayout) result.findViewById(R.id.panel_bar_table_content);
		
		setFieldId(fieldId);
	}

	public long getFieldId() {
		return fieldId;
	}

	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
		
		FormFieldDAO fieldDAO = new FormFieldDAO(context);
		field = (FormField) fieldDAO.findById(fieldId);
		fieldDAO.close();
	}

	public LinearLayout getContainer() {
		return container;
	}

	public void setContainer(LinearLayout container) {
		this.container = container;
	}
	
	private void drawChart(){
		textTitle.setText(field.getTitle());
		chartLayout.removeAllViews();
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context);
		ArrayList<StringDataRecord> records = detailDAO.getCountStringData(fieldId);
		detailDAO.close();
		
		ArrayList<String> bottomStringList = new ArrayList<String>();
		ArrayList<Integer> list = new ArrayList<Integer>();
	
		int maxTotal = 0;
		final int margin = 5;
		
		for (StringDataRecord record : records) {
			bottomStringList.add(record.getValue());
			list.add(record.getTotal());
			
			if(maxTotal < record.getTotal()){
				maxTotal = record.getTotal();
			}
		}
		
		BarView barView =  new BarView(context, ColorHelper.getStrokeColor(3));
		barView.setBottomTextList(bottomStringList);
		barView.setDataList(list, maxTotal + margin);
		chartLayout.addView(barView);
		
	}
	
	private void setTableData(){
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context);
		ArrayList<StringDataRecord> records = detailDAO.getCountStringData(fieldId);
		detailDAO.close();
		
		textTitleTable.setText(field.getTitle());
		int i = 0;
		int color;
		MyTableRow row = null;
		for (StringDataRecord record : records) {
			row = new MyTableRow(context);
			row.setContainer(tableLayout);
			row.setStrLeft(record.getValue());
			row.setStrRight(""+record.getTotal());
			if(i % 2 == 0){
				color = ColorHelper.WHITE;
			}else{
				color = ColorHelper.GREY_LIGHT;
			}
			row.setBackgroundColor(color);
			row.create();
			i++;
		}
	}
	
	private void hideChart(){
		chartPanel.setVisibility(View.GONE);
		chartHeader.setVisibility(View.GONE);
	}
	
	public void create(){
		hideChart();
		setTableData();
		container.addView(result);
	}
	
	

}
