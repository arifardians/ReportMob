package com.retail.activity;

import java.util.ArrayList;

import com.example.retail.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CreateOptionActivity extends Activity {

	LinearLayout container;
	
	//ArrayList<RelativeLayout> list; 

	LayoutInflater inflater;
	
	EditText inputText; 
	Button btnAdd;
	Button btnRemove;
	Button btnSubmit;
	CheckBox checkBox;
	
	private ArrayList<String> listOptions;
	private static final int DEFAULT_TOTAL_OPTIONS = 3; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_blank);
		
		final Intent callerIntent = getIntent(); 
		final Bundle bundle = callerIntent.getExtras();
		
		
		listOptions = new ArrayList<String>();
		container   = (LinearLayout) findViewById(R.id.form_blank_container); 
		inputText 	= (EditText) findViewById(R.id.form_blank_input_text); 
		btnAdd 		= (Button) findViewById(R.id.form_blank_button_add);
		btnSubmit   = (Button) findViewById(R.id.form_blank_submit);
		
		//list = new ArrayList<RelativeLayout>();
		
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		
		String[] options = {"Opsi Pertama", "Opsi Kedua", "Opsi Ketiga"};
		
		for(int i = 0; i < DEFAULT_TOTAL_OPTIONS; i++){
			addCheckBox(options[i]);
		}
		
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String newLabel = inputText.getText().toString();
				if(!newLabel.isEmpty()){
					addCheckBox(newLabel);
					inputText.setText("");
				}else{
					Toast.makeText(getApplicationContext(), "Opsi tidak boleh kosong", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bundle.putStringArrayList("listOptions", listOptions);
				callerIntent.putExtras(bundle);
				setResult(Activity.RESULT_OK, callerIntent);
				finish();
			}
		});
		
	}
	
	private void addCheckBox(final String label){
		final RelativeLayout form = (RelativeLayout) inflater.inflate(R.layout.form_item_checkbox, null); 
		checkBox  	= (CheckBox) form.findViewById(R.id.form_item_checkbox_check);
		btnRemove 	= (Button) form.findViewById(R.id.form_item_checkbox_button_remove);
		
		checkBox.setText(label);
					
		btnRemove.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("[create option]", "remove field");
				container.removeView(form);
				listOptions.remove(label);
			}
		});
		listOptions.add(label);
		container.addView(form);
	}
	
	
}
