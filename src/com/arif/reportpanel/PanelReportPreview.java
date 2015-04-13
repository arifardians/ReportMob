package com.arif.reportpanel;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dbhelper.FormData;
import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.dbhelper.FormFieldDAO;
import com.example.dbhelper.Model;
import com.example.dbhelper.Transaction;
import com.example.dbhelper.TransactionDAO;
import com.example.dbhelper.TransactionDetail;
import com.example.dbhelper.TransactionDetailDAO;
import com.example.retail.R;
import com.retail.activity.MyConstant;
import com.retail.activity.ReportActivity;
 
public class PanelReportPreview extends AbstractPanel{
	
	private static final String TAG = "[PanelReport]";
	
	private long formId;
	private Context context; 
	private FormData form;
	private FormField numerikField; 
	private FormField dateField; 
	
	private AlertDialog alertDialog;
	
	private LinearLayout container; 
	private TextView textDimension; 
	private ImageButton btnOptions; 
	private LinearLayout result;
	private LinearLayout chartLayout;
	private LayoutInflater inflater;
	
	public PanelReportPreview(Context context) {
		this.context = context;
		
		inflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		result		= (LinearLayout) inflater.inflate(R.layout.panel_preview, null);
		
		textDimension = (TextView) result.findViewById(R.id.panel_preview_title); 
		btnOptions	  = (ImageButton) result.findViewById(R.id.panel_preview_button_option);
		chartLayout	  = (LinearLayout) result.findViewById(R.id.panel_preview_chart);
	}
	
	@Override
	public long getFormId() {
		return formId;
		
	}

	@Override
	public void setFormId(long formId) {
		this.formId = formId;
		
		FormDataDAO formDao = new FormDataDAO(context);
		form = (FormData) formDao.findById(formId);
		formDao.close();
		setNumerikNDateField();
		
		btnOptions.setOnClickListener(actionOptions());
		if(numerikField != null){
			updateChart();
		}
		
	}
	
	private void updateChart(){
		textDimension.setText(form.getName());
		chartLayout.removeAllViews(); 
		GraphicalView chart = getChart(formId, numerikField.getId());
		chartLayout.addView(chart);
	}
	
	@Override
	protected GraphicalView getChart(long formId, long fieldId) {
		FormFieldDAO fieldDAO = new FormFieldDAO(context); 
		FormField field = (FormField) fieldDAO.findById(fieldId);
		fieldDAO.close();
		
		TimeSeries series = getSeries(formId, field.getId());
		
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); 
		
		// adding values to dataset 
		dataset.addSeries(series);
		
		// Creating XYSeriesRenderer to customize visitsSeries
		XYSeriesRenderer mRenderer = new XYSeriesRenderer();
		mRenderer.setColor(Color.BLUE);
		mRenderer.setPointStyle(PointStyle.CIRCLE);
		mRenderer.setPointStrokeWidth(10);
		mRenderer.setFillPoints(true);
		mRenderer.setLineWidth(1);
		mRenderer.setDisplayChartValues(true);
        
        
        FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BELOW);
        fill.setColor(Color.CYAN);
        
        mRenderer.addFillOutsideLine(fill);
        
     // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setChartTitle("" + field.getTitle());
        multiRenderer.setXTitle("Days");
        multiRenderer.setYTitle("Count");
        multiRenderer.addSeriesRenderer(mRenderer);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setLabelsTextSize(12);
        multiRenderer.setZoomButtonsVisible(false); 
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        
        GraphicalView chart = (GraphicalView) ChartFactory
        		.getTimeChartView(context,
        				dataset, 
        				multiRenderer,
        				"dd-MMM-yyyy");
        
        return chart;
	}

	@Override
	protected TimeSeries getSeries(long formId, long fieldId) {
		TransactionDAO transactionDAO = new TransactionDAO(context); 
		ArrayList<Model> transactions = transactionDAO.findByFormId(formId);
		transactionDAO.close(); 
		
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context); 
		ArrayList<SeriesData> resultData = new ArrayList<SeriesData>(); 
		SeriesData data = null;
		
		Transaction transaction  = null;
		TransactionDetail detail = null;
		TransactionDetail dateDetail = null;
		boolean isDateValid = false;
		for (Model model : transactions) {
			transaction = (Transaction) model;
			detail = (TransactionDetail) detailDAO.findByTransactionAndFieldID(transaction.getId(), fieldId);
			if(dateField != null){
				dateDetail = (TransactionDetail) detailDAO.
						findByTransactionAndFieldID(transaction.getId(), dateField.getId());
				data = new SeriesData(); 
				if(dateDetail != null){
					isDateValid = data.setDate(dateDetail.getFieldValue()); 
					Log.d(TAG, "date value : " + dateDetail.getFieldValue());
				}else{
					isDateValid = data.setDate(transaction.getCreatedAt());
				}
				if(isDateValid == true){
					data.setValue(detail.getFieldValue());
					resultData.add(data);
				}
				
			}else{
				data = new SeriesData();
				isDateValid = data.setDate(detail.getCreatedAt());
				Log.d(TAG, "date value created at : " + detail.getCreatedAt());
				if(isDateValid == true){
					data.setValue(detail.getFieldValue());
					resultData.add(data);
				}
			}
			
		}
		
		detailDAO.close();
		
		TimeSeries timeSeries = new TimeSeries("sampel");
		GregorianCalendar calendar = null;
		for (SeriesData seriesData : resultData) {
			calendar = new GregorianCalendar(seriesData.getYear(), 
						seriesData.getMonth(), 
						seriesData.getDay());
			
			timeSeries.add(calendar.getTime(), seriesData.getIntValue());
			Log.d(TAG, "year = "+ seriesData.getYear() +", month : "+ seriesData.getMonth() + ", day : " + seriesData.getDay());
		}
		
		return timeSeries;
	}

	@Override
	protected void setNumerikNDateField() {
		FormFieldDAO fieldDAO = new FormFieldDAO(context); 
		ArrayList<FormField> fields =  fieldDAO.findFieldByFormId(formId);
		fieldDAO.close(); 
		
		numerikField = null; 
		dateField = null;
		for (FormField formField : fields) {
			boolean isNumerik 	= 	formField.getDataType() == MyConstant.TYPE_NUMBER || 
									formField.getType() == MyConstant.INPUT_NUMBER;
			
			boolean isDate 	 	= 	formField.getDataType() == MyConstant.TYPE_DATE ||
							   		formField.getType() == MyConstant.INPUT_DATE;
			if(isNumerik && numerikField == null){
				numerikField = formField;
			}
			
			if(isDate && dateField == null){
				dateField = formField;
			}
		}
		
	}

	@Override
	protected OnClickListener actionOptions() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ReportActivity.class);
				intent.putExtra("formId", formId);
				context.startActivity(intent);
			}
		};
	}

	@Override
	protected void showOptionDialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContainer(LinearLayout container) {
		this.container = container;
		
	}

	@Override
	public LinearLayout getContainer() {
		return container;

	}

	@Override
	public void created() {
		container.addView(result);
		
	}

}
