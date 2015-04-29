package com.arif.reportpanel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arif.helper.ColorHelper;
import com.arif.helper.DateHelper;
import com.arif.helper.DaysRecap;
import com.arif.helper.MyTableRow;
import com.arif.helper.NoDefaultSpinner;
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
import com.test.androidcharts.LineView;

@SuppressLint("InflateParams")
public class PanelReportDetail extends AbstractPanel {
	
	private static final String TAG = "[PanelReport]";
	
	private Context context;
	private long formId;
	private ArrayList<FormField> numerikFields;
	private ArrayList<Long> numerikFieldIds;
	private ArrayList<Long> selectedFields;
	private FormField dateField;
	
	private AlertDialog alertDialog;
	
	private LinearLayout container; 
	private TextView textDimension; 
	private ImageButton btnOptions; 
	private LinearLayout result;
	private LinearLayout chartLayout;
	private LinearLayout tableLayout;
	
	private LinearLayout textReportContainer;
	
	private Spinner axisSpinner;
	private NoDefaultSpinner spinnerDate;
	private Spinner spinnerTableLeft; 
	private NoDefaultSpinner spinnerTableRight;
	
	private LayoutInflater inflater;
	private TextView textDateInterval;
	
	
	private CharSequence[] dimensions;
	
	public PanelReportDetail(Context context) {
		this.context = context;
		
		inflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		result		= (LinearLayout) inflater.inflate(R.layout.panel_default, null);
		
		textDimension 		= (TextView) result.findViewById(R.id.panel_text_dimension); 
		btnOptions	  		= (ImageButton) result.findViewById(R.id.panel_button_option);
		chartLayout	  		= (LinearLayout) result.findViewById(R.id.panel_chart);
		tableLayout  		= (LinearLayout) result.findViewById(R.id.panel_default_content_table);
		textDateInterval 	= (TextView) result.findViewById(R.id.panel_default_text_date_interval);
		
		axisSpinner	  		= (Spinner) result.findViewById(R.id.panel_horizontal_spinner);
		spinnerDate	  		= (NoDefaultSpinner) result.findViewById(R.id.panel_spinner_date_option);
		
		spinnerTableLeft 	= (Spinner) result.findViewById(R.id.panel_default_spinner_table_left); 
		spinnerTableRight	= (NoDefaultSpinner) result.findViewById(R.id.panel_default_spinner_table_right);
		textReportContainer	= (LinearLayout) result.findViewById(R.id.panel_default_text_container);
		
		selectedFields 	= new ArrayList<Long>();
	}
	
	@Override
	public long getFormId() {
		return formId;
		
	}

	@Override
	public void setFormId(long formId) {
		this.formId = formId;
		setNumerikNDateField();
		setSpinnerItem();
		if(dateField != null)
			axisSpinner.setSelection(dimensions.length -1);
		
		btnOptions.setOnClickListener(actionOptions());
		axisSpinner.setOnItemSelectedListener(spinnerAction());
		spinnerDate.setOnItemSelectedListener(spinnerDateAction());
		textDateInterval.addTextChangedListener(onTextDateChangeEvent());
		
		selectedFields.clear();
		if(numerikFields.size() >0){
			selectedFields.add(numerikFieldIds.get(0));
			Log.d(TAG, "numerik field 0 : "+selectedFields.get(0));
			Log.d(TAG, "default field size : " + selectedFields.size());
			if(dateField != null){
				Log.d(TAG, "date field exist!");
				Log.d(TAG, "default field size : " + selectedFields.size());
				updateChart(dateField.getId(), selectedFields);
			}else{
				Log.d(TAG, "date field not exist!");
				updateChart(selectedFields.get(0), selectedFields);
			}
		}
		
		// set the string report
		setStringReport();

	}
	
	private void setStringReport(){
		textReportContainer.removeAllViews();
		FormFieldDAO fieldDAO = new FormFieldDAO(context);
		ArrayList<FormField> fields = fieldDAO.findFieldByFormId(formId);
		fieldDAO.close();
		
		PanelReportString panel = null;
		if(fields.size() > 0 ){
			for (FormField field : fields) {
				if(field.getDataType() == MyConstant.TYPE_STRING){
					panel = new PanelReportString(context, field.getId());
					panel.setContainer(textReportContainer);
					panel.create();
				}
			}
		}
	}
	
	
	private TextWatcher onTextDateChangeEvent(){
		return new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(axisSpinner.getSelectedItem().toString() == dateField.getTitle()){
					updateChart(dateField.getId(), selectedFields);
				}else{
					updateChart(numerikFieldIds.get(axisSpinner.getSelectedItemPosition()), selectedFields);
					
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	@Override
	protected OnClickListener actionOptions() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				showOptionDialog();
			}
		};
	}
	
	private OnItemSelectedListener spinnerAction(){
		return new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position >= numerikFieldIds.size() && dateField != null){
					updateChart(dateField.getId(), selectedFields);
				}else{
					 updateChart(numerikFieldIds.get(position), selectedFields);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	private OnItemSelectedListener spinnerDateAction(){
		return new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position <= DateHelper.THIS_MONTH){
					String strIntervalDate = DateHelper.getStringInterval(position, context);
					textDateInterval.setText(strIntervalDate);
				}else{
					DateHelper.getCustomDateInterval(context, textDateInterval);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	@Override
	protected void showOptionDialog() {
		CharSequence[] items = new CharSequence[numerikFields.size()];
		int i = 0;
		for (FormField field : numerikFields) {
			items[i] = field.getTitle();
			i++;
		}
		
		final ArrayList<Long> oldSelectedFields = new ArrayList<Long>(); 
		
		for (Long fieldId : selectedFields) {
			oldSelectedFields.add(fieldId);
		}
		
		this.selectedFields.clear();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context); 
		builder.setTitle("Select dimension"); 
		builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				long fieldId = numerikFieldIds.get(which);
				if(isChecked){
					 // If the user checked the item, add it to the selected items
					selectedFields.add(fieldId);
				}else{
					// Else, if the item is already in the array, remove it 
					selectedFields.remove(Long.valueOf(fieldId));
				}
				
			}
		});
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(TAG, "number of selected fields : " + selectedFields.size());
				if(selectedFields.size() == 0){
					for (Long long1 : oldSelectedFields) {
						selectedFields.add(long1);
					}
				}
				String fieldsToString = "";
				for (Long fieldId : selectedFields) {
					fieldsToString +=""+fieldId+", ";
				}
				Log.d(TAG, "selected fields : " + fieldsToString);
				if(axisSpinner.getSelectedItem().toString() == dateField.getTitle()){
					updateChart(dateField.getId(), selectedFields);
				}else{
					updateChart(numerikFieldIds.get(axisSpinner.getSelectedItemPosition()), selectedFields);
					
				}
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedFields.clear(); 
				for (Long long1 : oldSelectedFields) {
					selectedFields.add(long1);
				}
				dialog.dismiss();
				
			}
		});
		
		alertDialog = builder.create(); 
		alertDialog.show();
		
	}
	
	private void updateChart(long xField, ArrayList<Long> selectedFields){
		String panelTitle = "";
		for (FormField field : numerikFields) {
			for (Long idSelected : selectedFields) {
				if(idSelected == field.getId()){
					panelTitle += field.getTitle()+", ";
					break;
				}
			}
		}
		
		if(panelTitle.equals("")){
			panelTitle = "Please select column!";
		}else if(panelTitle.length() > 0){
			panelTitle = panelTitle.substring(0, panelTitle.length()-2);
		}
		
		textDimension.setText(panelTitle);
		chartLayout.removeAllViews();
		GraphicalView chart = null;
		this.selectedFields = selectedFields;
		Log.d(TAG, "number of selected fields : " + selectedFields.size());
		try {
			
			if(dateField != null && dateField.getId() == xField){
				Log.d(TAG, "xField == date field == " + xField);
				chart = getChart(formId, selectedFields);
				int position = spinnerTableRight.getSelectedItemPosition();
				spinnerTableRight.setSelection(position);
			}else{
				Log.d(TAG, "xField == numerik field == " + xField);
				chart = getScatterChart(xField, selectedFields, formId);
				Log.d(TAG, "xField == numerik field == " + xField + " done");
			}
		
		chartLayout.addView(chart);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected GraphicalView getChart(long formId, ArrayList<Long> selectedFields){
		FormDataDAO formDAO = new FormDataDAO(context);
		FormData form = (FormData) formDAO.findById(formId);
		formDAO.close();
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); 
		
		TimeSeries series = null; 
		//ArrayList<TimeSeries> allSeries = new ArrayList<TimeSeries>();
		
		for (Long fieldId : selectedFields) {
			try {
				series = getSeries(formId, fieldId);
				dataset.addSeries(series);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<XYSeriesRenderer> renderers = new ArrayList<XYSeriesRenderer>();
		XYSeriesRenderer renderer = null; 
		FillOutsideLine fillLine = null;
		for (int i = 0; i < selectedFields.size(); i++) {
			renderer = new XYSeriesRenderer();
			renderer.setColor(ColorHelper.getStrokeColor(numerikFieldIds.indexOf(selectedFields.get(i))));
			renderer.setPointStyle(PointStyle.CIRCLE);
			renderer.setPointStrokeWidth(10);
			renderer.setFillPoints(true);
			renderer.setLineWidth(1);
			renderer.setDisplayChartValues(true);

			fillLine = new FillOutsideLine(FillOutsideLine.Type.BELOW);
			fillLine.setColor(ColorHelper.getSolidColor(numerikFieldIds.indexOf(selectedFields.get(i))));
			renderer.addFillOutsideLine(fillLine);
			renderers.add(renderer);
		}
	
		
        
     // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setChartTitle("" + form.getName());
        multiRenderer.setXTitle("Days");
        multiRenderer.setYTitle("Count");
        
        for (XYSeriesRenderer mRenderer : renderers) {
        	multiRenderer.addSeriesRenderer(mRenderer);
		}
        
        multiRenderer.setYAxisMin(0);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setLabelsTextSize(12);
        multiRenderer.setZoomButtonsVisible(false); 
        multiRenderer.setZoomEnabled(false);
        multiRenderer.setInScroll(true);
        
        GraphicalView chart = (GraphicalView) ChartFactory
        		.getTimeChartView(context,
        				dataset, 
        				multiRenderer,
        				"dd-MMM-yyyy");
        
        return chart;
	}
	

	@Override
	protected TimeSeries getSeries(long formId, long fieldId) throws ParseException {
		
		FormFieldDAO fieldDAO = new FormFieldDAO(context);
		FormField field = (FormField) fieldDAO.findById(fieldId);
		fieldDAO.close();
		
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
		
		String[] splitDates = textDateInterval.getText().toString().split(" - ");
		DateFormat df = DateFormat.getDateInstance();
		Date startDate	= df.parse(splitDates[0]);
		Date endDate 	= df.parse(splitDates[1]); 
		Log.d("date 1", splitDates[0]);
		Log.d("date 2", splitDates[1]);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date date = null;
		String fieldValue = "0";
		try {
			for (int i = 0; i < transactions.size(); i++) {
				transaction = (Transaction) transactions.get(i);
				detail = (TransactionDetail) detailDAO.findByTransactionAndFieldID(transaction.getId(), fieldId);
				dateDetail = (TransactionDetail) detailDAO.
						findByTransactionAndFieldID(transaction.getId(), dateField.getId());
				data = new SeriesData(); 
				isDateValid =  data.setDate(dateDetail.getFieldValue()); 
				Log.d(TAG, "date value : " + dateDetail.getFieldValue());
				if(isDateValid == true){
					if(detail == null || detail.getFieldValue() == null){
						fieldValue = "0";
					}else{
						Log.d(TAG, "detail fiel val : " + detail.getFieldValue());
						fieldValue = detail.getFieldValue();
					}
					data.setValue(fieldValue);
					date = sdf.parse(data.getDate());
					if(DateHelper.isDateBetween(date, startDate, endDate)){
						resultData.add(data);
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		detailDAO.close();
		
		TimeSeries timeSeries = new TimeSeries(""+field.getTitle());
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

	private GraphicalView getScatterChart(long xField, ArrayList<Long> selectedFields, long formId){
		FormDataDAO formDAO = new FormDataDAO(context);
		FormData form = (FormData) formDAO.findById(formId);
		formDAO.close();
		
		FormFieldDAO fieldDAO = new FormFieldDAO(context);
		FormField field = (FormField) fieldDAO.findById(xField);
		fieldDAO.close();
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = null;
		for (Long yField : selectedFields) {
			Log.d(TAG, "xField : " + xField + ", yField : " + yField+", form id : "+formId);
			series = getScatterSeries(xField, yField, formId);
			dataset.addSeries(series);
		}
		
		XYMultipleSeriesRenderer renderers = new XYMultipleSeriesRenderer();
		XYSeriesRenderer renderer = null;
		FillOutsideLine fill = null;
		for (int i = 0; i <selectedFields.size() ; i++) {
			renderer = new XYSeriesRenderer();
			renderer.setPointStyle(PointStyle.CIRCLE);
			renderer.setColor(ColorHelper.getStrokeColor(numerikFieldIds.indexOf(selectedFields.get(i))));
			renderer.setPointStrokeWidth(10);
			renderer.setFillPoints(true);
			renderer.setLineWidth(1);
			
			/*fill 	= new FillOutsideLine(FillOutsideLine.Type.BELOW);
			fill.setColor(ColorHelper.getSolidColor(numerikFieldIds.indexOf(selectedFields.get(i))));
			renderer.addFillOutsideLine(fill);
			
			renderer.setDisplayChartValues(true);*/
			
			renderers.addSeriesRenderer(renderer);
		}
		
		renderers.setChartTitle("" + form.getName());
	    renderers.setXTitle(""+field.getTitle());
	    renderers.setYTitle("Count");
		
	    renderers.setYAxisMin(0);
        renderers.setLabelsColor(Color.BLACK);
        renderers.setBackgroundColor(Color.WHITE);
        renderers.setMarginsColor(Color.WHITE);
        renderers.setYLabelsColor(0, Color.BLACK);
        renderers.setXLabelsColor(Color.BLACK);
        renderers.setLabelsTextSize(12);
        renderers.setZoomEnabled(false);
        renderers.setZoomButtonsVisible(false); 
        renderers.setInScroll(true);
        
		GraphicalView chart =  ChartFactory.getLineChartView(context, dataset, renderers);
		
		return chart;
	}
	
	
	private XYSeries getScatterSeries(long xAxis, long yAxis, long formId){
		FormFieldDAO fieldDAO = new FormFieldDAO(context);
		FormField field = (FormField) fieldDAO.findById(yAxis);
		fieldDAO.close();
		
		XYSeries series = new XYSeries(field.getTitle());
		
		TransactionDAO transactionDAO = new TransactionDAO(context);
		ArrayList<Model> transactions = transactionDAO.findByFormId(formId);
		transactionDAO.close();
		
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context);
		Transaction transaction;
		TransactionDetail xData; 
		TransactionDetail yData;
		for (Model model : transactions) {
			transaction = (Transaction) model;
			yData = (TransactionDetail) detailDAO.findByTransactionAndFieldID(transaction.getId(), yAxis);
			xData = (TransactionDetail) detailDAO.findByTransactionAndFieldID(transaction.getId(), xAxis);
			series.add(Double.parseDouble(xData.getFieldValue()), Double.parseDouble(yData.getFieldValue()));
		}
		detailDAO.close();
		
		return series;
	}

	
	
	@Override
	protected void setNumerikNDateField() {
		FormFieldDAO fieldDAO = new FormFieldDAO(context); 
		ArrayList<FormField> fields =  fieldDAO.findFieldByFormId(formId);
		fieldDAO.close(); 
		
		numerikFields = new ArrayList<FormField>();
		numerikFieldIds = new ArrayList<Long>();
		for (FormField formField : fields) {
			boolean isNumerik 	= 	formField.getDataType() == MyConstant.TYPE_NUMBER || 
									formField.getType() == MyConstant.INPUT_NUMBER;
			
			boolean isDate 	 	= 	formField.getDataType() == MyConstant.TYPE_DATE ||
							   		formField.getType() == MyConstant.INPUT_DATE;
			if(isNumerik){
				numerikFields.add(formField);
				numerikFieldIds.add(formField.getId());
			}
			
			if(isDate){
				dateField = formField;
			}
		}
		
	}
	
	private void setSpinnerItem(){
		int size = 0;
		if(dateField != null){
			size = numerikFieldIds.size() + 1;
			Log.d(TAG, "size dimmension = " + size);
		}
		
		
		dimensions = new CharSequence[size];
		CharSequence[] numeriks = new CharSequence[numerikFields.size()];
		
		try {
			for (int i = 0; i < dimensions.length; i++) {
				if(i == dimensions.length -1 && dateField != null){
					dimensions[i] = dateField.getTitle();
					Log.d(TAG, "index " + i + " : " + dateField.getTitle());
				}else{
					dimensions[i] = numerikFields.get(i).getTitle();
					numeriks[i] = numerikFields.get(i).getTitle();
					Log.d(TAG, "index " + i + " : " + numerikFields.get(i).getTitle());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, 
				android.R.layout.simple_spinner_item, dimensions);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		axisSpinner.setAdapter(adapter);
		
		
		CharSequence[] dateOptions = {"Last Week", "This Week", "Last Month", "This Month", "Custom Date.."};
		ArrayAdapter<CharSequence> dateOptionAdapter = generateAdapter(dateOptions);
		spinnerDate.setAdapter(dateOptionAdapter);
		
		CharSequence[] dateGroups = {"Days"};
		ArrayAdapter<CharSequence> dateGroupAdapter = generateAdapter(dateGroups);
		spinnerTableLeft.setAdapter(dateGroupAdapter);
		
		ArrayAdapter<CharSequence> numerikAdapter = generateAdapter(numeriks);
		spinnerTableRight.setAdapter(numerikAdapter);
		
		spinnerTableRight.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				setTableData(numerikFields.get(position).getId());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});

		
		
	}
	
	private ArrayAdapter<CharSequence> generateAdapter(CharSequence[] items){
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, 
										android.R.layout.simple_spinner_item,
										items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
	
	private void setTableData(long fieldId){
		MyTableRow tableRow = null;
		tableLayout.removeAllViews();
		DaysRecap recap = new DaysRecap();
		try {
			recap = getDaysRecaps(fieldId);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int color = 0;
		for (int i = 0; i < recap.TOTAL_VALUES.length; i++) {
			tableRow = new MyTableRow(context);
			tableRow.setStrLeft(DaysRecap.STR_DAYS[i]);
			tableRow.setStrRight(""+recap.TOTAL_VALUES[i]);
			
			if(i % 2 == 0){
				color = Color.WHITE;
			}else{
				color = ColorHelper.GREY_LIGHT;
			}
			
			tableRow.setBackgroundColor(color);
			tableRow.setContainer(tableLayout);
			tableRow.create();
		}
	}
	
	
	private DaysRecap getDaysRecaps(long fieldId) throws ParseException{
		FormFieldDAO fieldDAO = new FormFieldDAO(context); 
		FormField field = (FormField) fieldDAO.findById(fieldId);
		fieldDAO.close(); 
		
		TransactionDAO transactionDAO = new TransactionDAO(context); 
		ArrayList<Model> transactions = transactionDAO.findByFormId(formId);
		transactionDAO.close(); 
		
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context); 
		
		DaysRecap recap = new DaysRecap(); 
		recap.setTitle(field.getTitle());
		
		String[] splitDates = textDateInterval.getText().toString().split(" - ");
		DateFormat df = DateFormat.getDateInstance();
		Date startDate	= df.parse(splitDates[0]);
		Date endDate 	= df.parse(splitDates[1]); 
		Log.d("date 1", splitDates[0]);
		Log.d("date 2", splitDates[1]);
		
		
		Transaction transaction 	= null; 
		TransactionDetail detail 	= null;
		TransactionDetail dateDetail = null;
		boolean isDateValid = false;
		SeriesData data = null;
		Date date = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String fieldValue = "0";
		for (Model model : transactions) {
			transaction = (Transaction) model;
			data = new SeriesData(); 
			detail = (TransactionDetail) detailDAO.findByTransactionAndFieldID(transaction.getId(), fieldId);
			if(dateField != null){
				dateDetail = (TransactionDetail) detailDAO.
						findByTransactionAndFieldID(transaction.getId(), dateField.getId());
				if(dateField != null && dateDetail != null){
					isDateValid =  data.setDate(dateDetail.getFieldValue()); 
					Log.d(TAG, "date value : " + dateDetail.getFieldValue());
				}else{
					isDateValid = data.setDate(transaction.getCreatedAt());
				}
			}else{
				isDateValid = data.setDate(transaction.getCreatedAt());
			}
			
			if(isDateValid == true){
				if(detail == null || detail.getFieldValue() == null){
					fieldValue = "0";
				}else{
					Log.d(TAG, "detail fiel val : " + detail.getFieldValue());
					fieldValue = detail.getFieldValue();
				}
				data.setValue(fieldValue);
				date = sdf.parse(data.getDate());
				if(DateHelper.isDateBetween(date, startDate, endDate)){
					recap.addData(date, data.getIntValue());
				}
			}else{
				continue;
			}
		}
		
		detailDAO.close();
		
		return recap; 
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

	@Override
	protected GraphicalView getChart(long formId, long fieldId) {
		// TODO Auto-generated method stub
		return null;
	}

}
