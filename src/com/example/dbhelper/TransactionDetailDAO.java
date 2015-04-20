package com.example.dbhelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.arif.reportpanel.StringDataRecord;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TransactionDetailDAO extends PatternDAO{
	
	public static final String TAG = "[TransactionDAO]";
	
	// Database fields
	private SQLiteDatabase database; 
	private DBHelper dbHelper; 
	private static final String TABLE_NAME = DBHelper.TABLE_TRANSACTION_DETAIL;
	private static final String[] TABLE_COLUMNS = DBHelper.TABLE_TRANSACTION_DETAIL_COLUMNS; 
	
	public TransactionDetailDAO(Context context) {
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
		
		TransactionDetail detail = (TransactionDetail) model;
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.KEY_TRANSACTION_ID, detail.getTransactionId()); 
		values.put(DBHelper.KEY_FIELD_ID, detail.getFieldId()); 
		values.put(DBHelper.KEY_FIELD_VALUE, detail.getFieldValue()); 
		values.put(DBHelper.KEY_CREATED_AT, strDate); 
		values.put(DBHelper.KEY_UPDATED_AT, strDate); 
		
		long insertId = database.insert(TABLE_NAME, null, values); 
		return insertId;
	}

	@Override
	public Model findById(long id) {
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)}, null, null, null);
		Model model = null;
		if(cursor != null && cursor.moveToFirst()){
			model = cursorToModel(cursor);
		}
		
		return model;
	}
	
	public ArrayList<Model> findByTransactionId(long transactionId){
		ArrayList<Model> allData = new ArrayList<Model>(); 
		Model model = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, DBHelper.KEY_TRANSACTION_ID + " = ? ",
				new String[] {String.valueOf(transactionId)}, null, null, null); 
		
		if (cursor != null && cursor.moveToFirst()){
			do {
				model = cursorToModel(cursor);
				allData.add(model); 
			} while (cursor.moveToNext());
		}
		
		return allData; 
	}
	
	public Model findByTransactionAndFieldID(long transactionId, long fieldId){
		Model model = null;
		
		String selection = DBHelper.KEY_TRANSACTION_ID + " = ? AND " + DBHelper.KEY_FIELD_ID + " = ? ";
		
		Cursor cursor = database.query(TABLE_NAME, 
				TABLE_COLUMNS, 
				selection, 
				new String[] {String.valueOf(transactionId), String.valueOf(fieldId)}, 
				null, null, null);
		
		if(cursor != null && cursor.moveToFirst()){
				model = cursorToModel(cursor);
		}
		
		return model; 
	}
	
	public ArrayList<StringDataRecord> getCountStringData(long fieldId){
		ArrayList<StringDataRecord> records = new ArrayList<StringDataRecord>();
		StringDataRecord record = null; 
		
		String sql = "SELECT DISTINCT " + DBHelper.KEY_FIELD_VALUE + 
					 " , COUNT(" + DBHelper.KEY_ID + ") FROM " + TABLE_NAME + 
					 " WHERE " + DBHelper.KEY_FIELD_ID + " = ? " +
					 " GROUP BY " + DBHelper.KEY_FIELD_VALUE;
		
		Cursor cursor = database.rawQuery(sql, new String[] {String.valueOf(fieldId)});
		
		if(cursor != null && cursor.moveToFirst()){
			do {
				record = new StringDataRecord();
				record.setValue(cursor.getString(0));
				record.setTotal(cursor.getInt(1));
				records.add(record);
			} while (cursor.moveToNext());
		}
		
		return records;
	}

	@Override
	public ArrayList<Model> getAll() {
		ArrayList<Model> allData = new ArrayList<Model>(); 
		Model model = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, 
				null, null, null, null, null);
		
		if(cursor != null && cursor.moveToFirst()){
			do {
				model = cursorToModel(cursor); 
				allData.add(model);
			} while (cursor.moveToNext());
		}
		
		return allData;
	}

	@Override
	public int update(Model model) {
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String strDate = sdf.format(calendar.getTime());
		
		TransactionDetail detail = (TransactionDetail) model; 
		ContentValues values = new ContentValues(); 
		
		values.put(DBHelper.KEY_TRANSACTION_ID, detail.getTransactionId()); 
		values.put(DBHelper.KEY_FIELD_ID, detail.getFieldId()); 
		values.put(DBHelper.KEY_FIELD_VALUE, detail.getFieldValue()); 
		values.put(DBHelper.KEY_UPDATED_AT, strDate);
		
		int affectedRows = database.update(TABLE_NAME, values, 
				DBHelper.KEY_ID + " = ? ", 
				new String[]{String.valueOf(detail.getId())});
		
		return affectedRows;
	}

	@Override
	public int delete(long id) {
		int affectedRows = database.delete(TABLE_NAME, 
				DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)});
		
		return affectedRows;
	}
	
	public int deleteByTransactionId(long transactionId){
		int affectedRows = database.delete(TABLE_NAME, 
				DBHelper.KEY_TRANSACTION_ID + " = ? ", 
				new String[] {String.valueOf(transactionId)});
		
		return affectedRows;
	}
	
	public int deleteByFieldId(long fieldId){
		int affectedRows = database.delete(TABLE_NAME,
				DBHelper.KEY_FIELD_ID + " = ?", 
				new String[] {String.valueOf(fieldId)});
		return affectedRows;
	}

	@Override
	protected Model cursorToModel(Cursor cursor) {
		TransactionDetail detail = new TransactionDetail(); 
		detail.setId(cursor.getLong(0)); 
		detail.setTransactionId(cursor.getLong(1));
		detail.setFieldId(cursor.getLong(2));
		detail.setFieldValue(cursor.getString(3)); 
		detail.setCreatedAt(cursor.getString(4)); 
		detail.setUpdatedAt(cursor.getString(5));
		
		return detail;
	}
	
}
