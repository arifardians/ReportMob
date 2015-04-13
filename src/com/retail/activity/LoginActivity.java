package com.retail.activity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.arif.httphelper.CustomHttpClient;
import com.example.dbhelper.DBHelper;
import com.example.dbhelper.SessionApp;
import com.example.retail.R;
import com.simpd.my_list_view.CustomAdapter;

public class LoginActivity extends Activity {
	
	private EditText inputEmail; 
	private EditText inputPassword; 
		
	private Button btnLogin; 
	private TextView textSignUp;
	private RelativeLayout loadingBar;
	private String  statusMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		
		DBHelper db = new DBHelper(this);
		
		if(db.getWritableDatabase().getVersion() < DBHelper.VERSION){
			db.onUpgrade(db.getWritableDatabase(), db.getWritableDatabase().getVersion(), DBHelper.VERSION);
			Log.d("upgrade database", "upgrade database from version : "+db.getWritableDatabase().getVersion()+"" +
					" to version : "+DBHelper.VERSION);
		}
		
		SessionApp session = db.getSession();
		//Log.d("session", session.toString());
		if(session != null && session.isLogin()){
			//Log.d("[session-exist]", "token is empty? "+(session.getToken().equalsIgnoreCase("logout")));
			
			Intent intent = new Intent(LoginActivity.this, TestActivity.class); 
			startActivity(intent);
			finish();
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		inputEmail 		= (EditText) findViewById(R.id.login_input_email); 
		inputPassword	= (EditText) findViewById(R.id.login_input_password); 
		btnLogin		= (Button) findViewById(R.id.login_button_login); 
		textSignUp		= (TextView) findViewById(R.id.login_text_sign_up); 
		loadingBar		= (RelativeLayout) findViewById(R.id.login_rellayout_loadingbar);
		
		loadingBar.setVisibility(View.GONE);
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = inputEmail.getText().toString(); 
				String password = inputPassword.getText().toString(); 
				
				SweetAlertDialog alert;
				
				if(email.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
					alert = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE); 
					alert.setTitleText("Oops..!!");
					alert.setContentText("Email and password \ncan not be empty!"); 
					alert.show();
				}else{
					Thread thread = new Thread(new ProcessLoginThread(email, password));
					thread.start();
				}
				
				
			}
		});
		
		textSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				
			}
		});
		
	}
	
	
	private void doLoginRequest(String email, String password){
		boolean isValidInput = email.length() > 0 && password.length() > 0;
		statusMessage = "Login failed!"; 
		boolean isLoginSuccess = false;
		
	
		String url = "http://54.186.254.6:6543/api/v1/login";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password)); 
		
		try {
			String response =  CustomHttpClient.executeHttpPost(url, params);
			if(response != null && isJSONValid(response)){
				Log.d("respond login", response);
				JSONObject jRespond = new JSONObject(response);
				JSONObject jStatus 	= jRespond.getJSONObject("response");
				int status = jStatus.getInt("status");
				if(status == 200){
					JSONObject jData = jRespond.getJSONObject("data"); 
					DBHelper db = new DBHelper(LoginActivity.this);
					SessionApp session = db.getSession();
					if(session == null){
						session = new SessionApp();
					}
					session.setEmail(email);
					session.setSecret(jData.getString("secret"));
					session.setKey(jData.getString("key"));
					if(session.getId() == 0){
						db.insertSession(session);
					}else{
						db.updateSession(session);
					}
					db.close();
					isLoginSuccess = true;
				}else{
					statusMessage = jRespond.getString("message");
					isLoginSuccess = false;
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
					
				}
			});
			
			if(isLoginSuccess){
				Intent intent = new Intent(LoginActivity.this, TestActivity.class);
				startActivity(intent);
			}else{
				this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						SweetAlertDialog alert = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE); 
						alert.setTitleText("Oops..!!"); 
						alert.setContentText(statusMessage);
						alert.show();
						
					}
				});
			}
		}
			
		
	}
	
	class ProcessLoginThread implements Runnable{
		private String email; 
		private String password; 
		
		public ProcessLoginThread(String email, String password) {
			this.email = email; 
			this.password = password;
		}
		
		@Override
		public void run() {
			LoginActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					loadingBar.setVisibility(View.VISIBLE);
					
				}
			});
			
			doLoginRequest(email, password);
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
