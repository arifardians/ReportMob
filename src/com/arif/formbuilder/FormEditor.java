package com.arif.formbuilder;

import java.util.ArrayList;

import com.example.retail.R;
import com.retail.activity.MyConstant;
import com.retail.activity.MyField;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

public class FormEditor {
	
	public static LinearLayout editorLayout;
	public static ArrayList<MyField> tempFields;
	public static final int NEW_FIELDS = -1;
	
	public static MyField myField;
	
	private Context context;
	
	public FormEditor(Context context) {
		this.context = context;
	}
	
	public static void showPopupForm(int index, Context context){
		Log.d("[form-editor]", "show pop up editor, index : " + index + ", context : "+context);
		
		myField = null; 
		if(index == NEW_FIELDS){
			myField = new MyField("");
		}else{
			myField = tempFields.get(index);
		}
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		View popupView = inflater.inflate(R.layout.popup_field, null); 
		final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true); 
		popupWindow.setAnimationStyle(R.style.PopupAnimaitonBottom);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true); 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		final EditText inputName 		= (EditText) popupView.findViewById(R.id.popup_input_nama); 
		final Spinner  spinnerTipeInput = (Spinner) popupView.findViewById(R.id.popup_spinner_tipe_input);
		final Spinner  spinnerTipeData  = (Spinner) popupView.findViewById(R.id.popup_spinner_tipe_data); 
		
		inputName.setText(myField.getName());
		
		Button   btnSubmit = (Button) popupView.findViewById(R.id.popup_button_submit);
		Button   btnCancel = (Button) popupView.findViewById(R.id.popup_button_cancel);
		
		final TextView labelOption = (TextView) popupView.findViewById(R.id.popup_input_label_opsi);
		final EditText inputOpsi = (EditText) popupView.findViewById(R.id.popup_input_text_opsi);
		
		labelOption.setVisibility(View.GONE);
		inputOpsi.setVisibility(View.GONE);
		
		ArrayAdapter<CharSequence> adapterTipeInput = new ArrayAdapter<CharSequence>(
				  context, 
				  R.layout.spinner_item, 
				  MyConstant.TIPE_INPUT);
		
		spinnerTipeInput.setAdapter(adapterTipeInput);
		
		spinnerTipeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				
				String selectedItem = MyConstant.TIPE_INPUT[position];
				if(selectedItem.equalsIgnoreCase("check box") || 
				   selectedItem.equalsIgnoreCase("combo box") || 
				   selectedItem.equalsIgnoreCase("radio button")){
					
					labelOption.setVisibility(View.VISIBLE);
					inputOpsi.setVisibility(View.VISIBLE);
					
					if(myField.getOptions() != null && myField.getOptions().size() > 1){
						String option2String = listOptions2String(myField.getOptions());
						inputOpsi.setText(option2String);
					}
				}else{
					labelOption.setVisibility(View.GONE); 
					inputOpsi.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinnerTipeInput.setSelection(myField.getInputType());
		
		ArrayAdapter<CharSequence> adapterTipeData = new ArrayAdapter<CharSequence>(
				 context, 
				 R.layout.spinner_item, 
				 MyConstant.TIPE_DATA);
		
		spinnerTipeData.setAdapter(adapterTipeData);
		
		spinnerTipeData.setSelection(myField.getDataType());

		btnCancel.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// create new or edit field
				
			}
		});
		
		
		
		
	}
	
	private static String listOptions2String(ArrayList<String> listOptions){
		String result=""; 
		
		for (String item : listOptions) {
			result += item + "\n";
		}
		
		return result;
	}
	

}
