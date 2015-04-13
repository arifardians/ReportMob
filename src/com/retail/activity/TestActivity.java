package com.retail.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.arif.gridhelper.ImageAdapter;
import com.arif.helper.ImageHelper;
import com.arif.httphelper.CustomHttpClient;
import com.example.dbhelper.DBHelper;
import com.example.dbhelper.SessionApp;
import com.example.retail.R;
import com.simpd.my_list_view.CustomAdapter;
import com.simpd.my_list_view.RowItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TestActivity extends Activity{
	private ImageView image;
	private ListView list; 
	private GridView gridView;
	
	private ArrayList<RowItem> rowItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown_gelap)));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_new);
		
		image = (ImageView) findViewById(R.id.main_new_image); 
		list  = (ListView) findViewById(R.id.main_new_list);
		gridView = (GridView) findViewById(R.id.main_new_gridview);
		
		final String[] titles = {"Forms", "Groups", "Reports"};
		/*int[] icons = {R.drawable.ic_action_copy, R.drawable.ic_action_copy,
				  R.drawable.ic_action_copy};*/
		
		rowItems = new ArrayList<RowItem>(); 
		RowItem item = null; 
		for (int i = 0; i < titles.length; i++) {
			item = new RowItem(); 
			item.setTitle(titles[i]); 
			item.setIconId(R.drawable.ic_action_copy); 
			item.setSubtitle("");
			rowItems.add(item);
		}
		
		CustomAdapter adapter = new CustomAdapter(getApplicationContext(), rowItems);
		list.setAdapter(adapter);
		list.setVisibility(View.VISIBLE);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null; 
				switch (position) {
				case 0:
					intent = new Intent(TestActivity.this, NewFormActivity.class);
					startActivity(intent);
					break;
				case 1: 
					/*intent = new Intent(TestActivity.this, MaintenanceActivity.class);
					intent.putExtra("title", titles[position]);
					startActivity(intent); */
					/*Thread thread = new Thread(new TestURL());
					thread.start();*/
					// byte encode 
					/*String str = "B3T6ucEM5nnhSfpbCr6JcfrF-MQ~kcRN";
					byte[]   bytesEncoded = Base64.encode(str.getBytes(), Base64.DEFAULT);
					Toast.makeText(getApplicationContext(), new String(bytesEncoded), Toast.LENGTH_LONG).show();
					Log.d("encoded", new String(bytesEncoded));*/
					Toast.makeText(getApplicationContext(), "Sorry, service is unavailable..!", Toast.LENGTH_SHORT).show();
					break;
				
				case 2: 
					intent = new Intent(TestActivity.this, ReportListActivity.class);
					startActivity(intent);
					break;
				default:
					intent = new Intent(TestActivity.this, MaintenanceActivity.class);
					intent.putExtra("title", titles[position]);
					startActivity(intent);
					break;
				}
				
			}
		});
		
		ArrayList<RowItem> gridItems = new ArrayList<RowItem>();
		String[] gridTitles = {"Last Sync", "Logout"};
		int[] gridIcons = {R.drawable.ic_action_refresh, R.drawable.ic_action_settings};
		
		for (int i = 0; i < gridTitles.length; i++) {
			item = new RowItem();
			item.setTitle(gridTitles[i]);
			item.setIconId(gridIcons[i]);
			item.setSubtitle("");
			item.setValue("");
			gridItems.add(item);
		}
		
		CustomAdapter gridAdapter = new CustomAdapter(getApplicationContext(), gridItems);
		gridView.setAdapter(gridAdapter);
		gridView.setVisibility(View.VISIBLE);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Toast.makeText(getApplicationContext(), "Sorry, service unavailable..!", Toast.LENGTH_SHORT).show();
					break;
				case 1: 
					actionButtonKeluar();
					break;
				default:
					break;
				}
			}
		});
		// Set bitmap 
		int size = 100;
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img4_small);
		Bitmap resized = Bitmap.createScaledBitmap(bm, size, size, true);
		Bitmap conv_bm = ImageHelper.getRoundedRectBitmap(resized, size);
		//image.setImageBitmap(conv_bm);
		
		
	}
	
	private void actionButtonKeluar(){
		SweetAlertDialog alert = new SweetAlertDialog(TestActivity.this, 
				SweetAlertDialog.WARNING_TYPE);
		alert.setTitleText("Warning..!"); 
		alert.setContentText("Are you sure to logout?");
		alert.setConfirmText("Yes, Logout"); 
		alert.setCancelText("Cancel"); 
		alert.showCancelButton(true); 
		alert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.cancel();
			}
		});
		
		alert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				DBHelper db = new DBHelper(getApplicationContext());
				SessionApp session = db.getSession(); 
				session.setEmail("");
				session.setSecret("");
				session.setKey("");
				session.setToken("");
				db.updateSession(session);
				db.close();
				Intent intent = new Intent(TestActivity.this, LoginActivity.class);
				startActivity(intent); 
				finish();
			}
		});
		alert.show();
	}
	
	private void testDate() throws ParseException{
		String strDate1 = "2015-04-08";
		String strDate2 = "Apr 5, 2015";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		DateFormat df = DateFormat.getDateInstance();
		Date date1 = sdf.parse(strDate1);
		Date date2 = df.parse(strDate2);
		
		boolean isSame = date1.equals(date2);
		boolean isBefore = date1.before(date2);
		boolean isAfter  = date1.after(date2);
		
		Toast.makeText(getApplicationContext(), "is same : " + isSame + 
				", is date 1 first : " + isBefore + ", is date1 last : "+ 
				isAfter, Toast.LENGTH_LONG).show();
		
	}
	
	private void testUrlUsingThreads(String url){
		
		NameValuePair header = new BasicNameValuePair("Authorization", "bearer 3a3ce3648234319ebe8c56493a34340ed7ff73f8");
		try {
			String response = CustomHttpClient.executeHttpGet(url, header);
			Log.d("response", response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class TestURL implements Runnable{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			testUrlUsingThreads("http://192.168.0.217:6543/ping/header");
		}
		
	}
}
