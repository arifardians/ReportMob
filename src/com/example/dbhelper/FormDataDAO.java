package com.example.dbhelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FormDataDAO extends PatternDAO {
	
	public static final String TAG = "[FormDataDAO]";
	
	// Database fields
	private SQLiteDatabase database; 
	private DBHelper dbHelper; 
	private static final String TABLE_NAME = DBHelper.TABLE_FORM;
	private static final String[] TABLE_COLUMNS = DBHelper.TABLE_FORM_COLUMNS; 
	private Context context;
	
	public FormDataDAO(Context context) {
		 
		dbHelper = new DBHelper(context);
		this.context = context;
		// open the database 
		try {
			open(); 
		} catch (SQLException e) {
			Log.e(TAG, "SQLException on opening database : " + e.getMessage()); 
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase(); 
	}
	@Override
	public void close(){
		dbHelper.close();
	}
	
	
	/**
	 * Insert Model instance of FormData  
	 * as paremeter to save data in database. 
	 * 
	 * @param Model
	 * @return inserted id
	 */
	@Override
	public long insert(Model model) {
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String strDate = sdf.format(calendar.getTime());
		
		FormData form = (FormData) model;
		ContentValues values = new ContentValues(); 
		values.put(DBHelper.KEY_FORM_OWNER_ID, form.getOwnerId()); 
		values.put(DBHelper.KEY_FORM_OWNER_TYPE, form.getOwnerType()); 
		values.put(DBHelper.KEY_FORM_NAME, form.getName()); 
		values.put(DBHelper.KEY_DESCRIPTION, form.getDescription());
		values.put(DBHelper.KEY_FORM_ROW, form.getNumRows()); 
		values.put(DBHelper.KEY_FORM_SLUG, form.getSlug()); 
		values.put(DBHelper.KEY_IS_ACTIVATED, form.isActivated()); 
		values.put(DBHelper.KEY_CREATED_AT, strDate);
		values.put(DBHelper.KEY_UPDATED_AT, strDate);
		values.put(DBHelper.KEY_CREATED_BY, form.getCreatedBy()); 
		values.put(DBHelper.KEY_UPDATED_BY, form.getUpdatedBy());
		
		long insertId = database.insert(TABLE_NAME, null, values); 
		Log.d(TAG, "Insert new form with id : " + insertId);
		Log.d(TAG, "inserting form with data : " + form.toString());
		return insertId;
	}
	
	
	/**
	 * Return Model instance of FormData that 
	 * has same id as paremeter. 
	 * 
	 * @param id 
	 * @return Model
	 */
	@Override
	public Model findById(long id) {
		Cursor cursor = database.query(TABLE_NAME, 
				TABLE_COLUMNS, 
				DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)}, null, null, null);
		
		if(cursor != null){
			cursor.moveToFirst();
		}
		Model model = cursorToModel(cursor);
		
		return model;
	}

	/**
	 * Return all Model instance of FormData 
	 * in table form database
	 * 
	 * @return ArrayList of model from table form
	 */ 
	@Override
	public ArrayList<Model> getAll() {
		ArrayList<Model> allData = new ArrayList<Model>();
		Model model = null;
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, null, 
				null, null, null, null);
		
		if(cursor != null && cursor.moveToFirst()){
			do{
				model = cursorToModel(cursor);
				allData.add(model);
				
			}while (cursor.moveToNext());
		}
		
		return allData;
	}
	
	
	/**
	 * Return number of affected rows of updated data
	 * 
	 * @param Model  
	 * @return number of affected rows
	 */
	@Override
	public int update(Model model) {
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String strDate = sdf.format(calendar.getTime());
		
		FormData form = (FormData) model; 
		ContentValues values = new ContentValues(); 
		values.put(DBHelper.KEY_FORM_OWNER_ID, form.getOwnerId()); 
		values.put(DBHelper.KEY_FORM_OWNER_TYPE, form.getOwnerType()); 
		values.put(DBHelper.KEY_FORM_NAME, form.getName()); 
		values.put(DBHelper.KEY_DESCRIPTION, form.getDescription());
		values.put(DBHelper.KEY_FORM_ROW, form.getNumRows()); 
		values.put(DBHelper.KEY_FORM_SLUG, form.getSlug()); 
		values.put(DBHelper.KEY_IS_ACTIVATED, form.isActivated()); 
		values.put(DBHelper.KEY_UPDATED_AT, strDate);
		values.put(DBHelper.KEY_UPDATED_BY, form.getUpdatedBy());
		
		int affectedRows = database.update(TABLE_NAME, values, 
				DBHelper.KEY_ID + " = ? ",
				new String[] {String.valueOf(model.getId())});
		Log.d(TAG, "Updated data : " + form.toString());
		return affectedRows;
	}

	@Override
	public int delete(long id) {
		FormFieldDAO fieldDAO = new FormFieldDAO(context); 
		fieldDAO.deleteFieldByFormId(id); 
		fieldDAO.close();
		
		TransactionDAO transactionDAO = new TransactionDAO(context); 
		transactionDAO.deleteByFormId(id); 
		transactionDAO.close();
		
		int affectedRows = database.delete(TABLE_NAME, 
				DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)});
		
		return affectedRows;
	}

	@Override
	protected Model cursorToModel(Cursor cursor) {
		FormData form = new FormData();
		form.setId(cursor.getLong(0)); 
		form.setOwnerId(cursor.getLong(1)); 
		form.setOwnerType(cursor.getInt(2)); 
		form.setName(cursor.getString(3));
		form.setDescription(cursor.getString(4));
		form.setNumRows(cursor.getInt(5)); 
		form.setSlug(cursor.getString(6));
		if(cursor.getInt(7) == 1){
			form.setActivated(true);
		}else{
			form.setActivated(false);
		}
		form.setCreatedAt(cursor.getString(8)); 
		form.setUpdatedAt(cursor.getString(9)); 
		form.setCreatedBy(cursor.getLong(10));
		form.setUpdatedBy(cursor.getLong(11));
		
		return form;
	}
	

	
}
