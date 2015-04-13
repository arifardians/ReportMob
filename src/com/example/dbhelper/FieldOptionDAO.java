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

public class FieldOptionDAO extends PatternDAO{
	
	public static final String TAG = "[FieldOptionDAO]";
	
	// Database fields
	private SQLiteDatabase database; 
	private DBHelper dbHelper; 
	private static final String TABLE_NAME = DBHelper.TABLE_FIELD_OPTIONS;
	private static final String[] TABLE_COLUMNS = DBHelper.TABLE_FIELD_OPTION_COLUMNS; 
	
	public FieldOptionDAO(Context context) {
		dbHelper = new DBHelper(context);
		
		// open the database 
		try {
			open(); 
		} catch (SQLException e) {
			Log.e(TAG, "SQLException on opening database : " + e.getMessage()); 
			e.printStackTrace();
		}
	}
	
	@Override
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase(); 
		
	}

	@Override
	public void close() {
		dbHelper.close();
		
	}

	@Override
	public long insert(Model model) {
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String strDate = sdf.format(calendar.getTime());
		
		FieldOption option = (FieldOption) model;
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.KEY_OPTION_NAME, option.getName()); 
		values.put(DBHelper.KEY_OPTION_VALUE, option.getValue());
		values.put(DBHelper.KEY_OPTION_ORDER, option.getOrder()); 
		values.put(DBHelper.KEY_FIELD_ID, option.getFieldID());
		
		long insertId = database.insert(TABLE_NAME, null, values);
		
		return insertId;
	}

	@Override
	public Model findById(long id) {
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)}, null, null, null);
		
		if(cursor != null){
			cursor.moveToFirst();
		}
		
		Model model = cursorToModel(cursor);
		
		return model;
	}
	
	public ArrayList<Model> findByFieldID(long fieldID){
		ArrayList<Model> allData = new ArrayList<Model>();
		Model model = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, DBHelper.KEY_FIELD_ID + " = ? " , 
				new String[]{String.valueOf(fieldID)}, 
				null, null, null);
		if(cursor != null && cursor.moveToFirst()){
			do{
				model = cursorToModel(cursor);
				allData.add(model);
			}while (cursor.moveToNext()) ;
		}
		return allData;
	}

	@Override
	public ArrayList<Model> getAll() {
		ArrayList<Model> allData = new ArrayList<Model>();
		Model model = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, null, 
				null, null, null, null); 
		
		if(cursor != null && cursor.moveToFirst()){
			
			do {
				model = cursorToModel(cursor);
				allData.add(model);
			}while (cursor.moveToNext());
		}
		
		return allData;
	}

	@Override
	public int update(Model model) {
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String strDate = sdf.format(calendar.getTime());
		
		FieldOption option = (FieldOption) model;
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.KEY_OPTION_NAME, option.getName()); 
		values.put(DBHelper.KEY_OPTION_VALUE, option.getValue());
		values.put(DBHelper.KEY_OPTION_ORDER, option.getOrder()); 
		values.put(DBHelper.KEY_FIELD_ID, option.getFieldID());
		
		int affectedRows = database.update(TABLE_NAME, values, DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(model.getId())});
		
		return affectedRows;
	}

	@Override
	public int delete(long id) {
		int affectedRows = database.delete(TABLE_NAME, 
				DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)});
		
		return affectedRows;
	}
	
	public int deleteByFieldID(long id){
		int affectedRows = database.delete(TABLE_NAME, 
				DBHelper.KEY_FIELD_ID + " = ? ", new String[] {String.valueOf(id)});
		return affectedRows;
	}
	
	@Override
	protected Model cursorToModel(Cursor cursor) {
		
		FieldOption option = new FieldOption(); 
		option.setId(cursor.getLong(0)); 
		option.setName(cursor.getString(1)); 
		option.setValue(cursor.getString(2)); 
		option.setOrder(cursor.getInt(3)); 
		option.setFieldID(cursor.getLong(4));
		return option;
	}

}
