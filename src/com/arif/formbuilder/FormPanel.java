package com.arif.formbuilder;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.example.dbhelper.FormDataDAO;
import com.example.dbhelper.FormField;
import com.example.retail.R;
import com.retail.activity.FormActivity;
import com.retail.activity.FormListActivity;
import com.retail.activity.FormTransactionActivity;
import com.retail.activity.ListTransactionActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FormPanel {
	
	private Context context; 
	private TextView textTitle; 
	private TextView textSubtitle; 
	private TextView textNumber; 
	
	private long formId;
	private String title; 
	private String subtitle; 
	private int numberField;
	
	private LayoutInflater inflater;
	private LinearLayout panelLayout;
	private RelativeLayout panelView;
	private LinearLayout container;
	private Button btnEdit; 
	private Button btnRemove;
	public FormPanel(Context context) {
		this.context = context; 
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		panelLayout = (LinearLayout) inflater.inflate(R.layout.form_panel, null);
		panelView		= (RelativeLayout) panelLayout.findViewById(R.id.panel_form);
		textTitle		= (TextView) panelLayout.findViewById(R.id.form_panel_title);
		textSubtitle 	= (TextView) panelLayout.findViewById(R.id.form_panel_subtitle);
		textNumber		= (TextView) panelLayout.findViewById(R.id.form_panel_text_number);
		btnEdit			= (Button) panelLayout.findViewById(R.id.form_panel_button_edit);
		btnRemove 		= (Button) panelLayout.findViewById(R.id.form_panel_button_remove);
	}
	
	
	
	public long getFormId() {
		return formId;
	}



	public void setFormId(long formId) {
		this.formId = formId;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getNumberField() {
		return numberField;
	}

	public void setNumberField(int numberField) {
		this.numberField = numberField;
	}

	public LinearLayout getContainer() {
		return container;
	}

	public void setContainer(LinearLayout container) {
		this.container = container;
	}
	
	
	private void showOptionDialog() {
		final CharSequence[] items = {"Edit", "Delete"};
	
		AlertDialog.Builder builder = new AlertDialog.Builder(context); 
		builder.setTitle("Select dimension"); 
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					dialog.dismiss();
					startEditActivity();
					break;
				case 1: 
					// delete
					dialog.dismiss();
					showWarningRemoveDialog();
				default:
					break;
				}
			}
		});
		
		AlertDialog alertDialog = builder.create(); 
		alertDialog.show();
		
	}
	
	private View.OnClickListener actionButtonEdit(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startEditActivity();
				
			}
		};
	}
	
	private void startEditActivity(){
		Intent intent = new Intent(context, FormActivity.class);
		intent.putExtra("formId", formId);
		context.startActivity(intent);
	}
	
	
	private View.OnClickListener actionButtonRemove(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showWarningRemoveDialog();
				
			}
		};
	}
	
	private void showWarningRemoveDialog(){
		SweetAlertDialog alert = new SweetAlertDialog(context, 
				SweetAlertDialog.WARNING_TYPE); 
		
		alert.setTitleText("Warning..!!"); 
		alert.setContentText("Are you sure to delete this "+ title + " form ? "); 
		alert.setConfirmText("Yes, Delete"); 
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
				
				FormDataDAO formDao = new FormDataDAO(context);
				formDao.delete(formId);
				formDao.close();
				sweetAlertDialog.dismiss();
			}
		});
		alert.show();
	}
	
	private View.OnLongClickListener actionLongClickPanel(){
		return new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				showOptionDialog();
				return true;
			}
		};
	}
	
	public void create(){
		textTitle.setText(title);
		textSubtitle.setText(subtitle);
		
		if(numberField < 10){
			textNumber.setText(" "+numberField+" ");
		}else{
			textNumber.setText(""+numberField);
		}
		
		panelView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ListTransactionActivity.class);
				intent.putExtra("formId", formId);
				context.startActivity(intent);
			}
		});
		panelView.setOnLongClickListener(actionLongClickPanel());
		
		btnEdit.setOnClickListener(actionButtonEdit());
		btnRemove.setOnClickListener(actionButtonRemove());
		
		container.addView(panelLayout);
	}
	
	
}
