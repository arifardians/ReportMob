package com.retail.activity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.arif.httphelper.CustomHttpClient;
import com.example.retail.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class RegisterActivity extends Activity {
	private EditText inputFullName; 
	private EditText inputEmail; 
	private EditText inputPassword; 
	private EditText inputConfirmPassword; 
	
	private Button btnRegister; 
	private RelativeLayout loadingBar; 
	private String messageRespond;
	
	private boolean isSuccess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		inputFullName = (EditText) findViewById(R.id.register_input_full_name);
		inputEmail = (EditText) findViewById(R.id.register_input_email); 
		inputPassword = (EditText) findViewById(R.id.register_input_password); 
		inputConfirmPassword = (EditText) findViewById(R.id.register_input_confirm_password);
		btnRegister	= (Button) findViewById(R.id.register_button_register);
		loadingBar = (RelativeLayout) findViewById(R.id.register_loading_bar);
		loadingBar.setVisibility(View.GONE);
		
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionRegister();
				
			}
		});
		
	}
	
	private void actionRegister(){
		String fullName = inputFullName.getText().toString(); 
		String email 	= inputEmail.getText().toString();
		String password = inputPassword.getText().toString();
		String confirm 	= inputConfirmPassword.getText().toString();
		
		boolean isFullNameValid = fullName.length() > 0; 
		boolean isEmailValid 	= email.length() > 0 ;
		boolean isPasswordValid = password.length() > 0; 
		boolean isConfirmValid	= password.equals(confirm);
		
		SweetAlertDialog alert = null;
		
		if(isFullNameValid && isEmailValid){
			if(isPasswordValid && isConfirmValid){
				Thread thread = new Thread(new RegisterThread(email, password));
				thread.start();
			}else{
				alert = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE); 
				alert.setTitleText("Oops..!!");
				alert.setContentText("Password and confirmation password did not match!"); 
				alert.show();
			}
		}else{
			alert = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE); 
			alert.setTitleText("Oops..!!");
			alert.setContentText("Email and password \ncan not be empty!"); 
			alert.show();
			
		}
	}
	
	private void doRegisterRequest(String email, String password){ 
		messageRespond = "Register failed!";
		
		String url = "http://54.186.254.6:6543/api/v1/signup"; 
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email)); 
		params.add(new BasicNameValuePair("password", password));
		
		isSuccess = false;
		
		try {
			String response = CustomHttpClient.executeHttpPost(url, params);
			if(response != null && isJSONValid(response)){
				Log.d("response", response);
				JSONObject jRespond = new JSONObject(response);
				JSONObject jStatus = jRespond.getJSONObject("response");
				int status = jStatus.getInt("status");
				if(status == 200){
					isSuccess = true;
					messageRespond = jRespond.getString("message"); 
				}else{
					isSuccess = false; 
					messageRespond = jRespond.getString("message");
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					loadingBar.setVisibility(View.GONE);
					SweetAlertDialog alert = null;
					if(isSuccess){
						alert = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE);
						alert.setTitle("Congrats..!"); 
						alert.setContentText(messageRespond);
						alert.setConfirmClickListener(new OnSweetClickListener() {
							
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								finish();
								
							}
						});
						alert.show();
					}else{
						alert = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE); 
						alert.setTitle("Oops..!");
						alert.setContentText(messageRespond);
						alert.show();
					}
					
				}
			});
		}
		
	}
	
	class RegisterThread implements Runnable{
		private String email;
		private String password; 
		
		public RegisterThread(String email, String password) {
			this.email 	= email; 
			this.password = password;
		}
		
		@Override
		public void run() {
			RegisterActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					loadingBar.setVisibility(View.VISIBLE);
				}
			});
			
			doRegisterRequest(email, password);
			
		}
		
	}
	
	private boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        // edited, to include @Arthur's comment
	        // e.g. in case JSONArray is valid as well...
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
}
