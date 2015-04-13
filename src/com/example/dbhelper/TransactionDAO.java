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

public class TransactionDAO extends PatternDAO{
	
	public static final String TAG = "[TransactionDAO]";
	
	// Database fields
	private SQLiteDatabase database; 
	private DBHelper dbHelper; 
	private static final String TABLE_NAME = DBHelper.TABLE_TRANSACTION;
	private static final String[] TABLE_COLUMNS = DBHelper.TABLE_TRANSACTION_COLUMNS; 
	private Context context;
	
	public TransactionDAO(Context context) {
		dbHelper 	 = new DBHelper(context);
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
		
		Transaction transaction = (Transaction) model;
		ContentValues values = new ContentValues(); 
		
		values.put(DBHelper.KEY_FORM_ID, transaction.getFormId());
		values.put(DBHelper.KEY_RECORD_ORDER, transaction.getRecordOrder()); 
		values.put(DBHelper.KEY_CREATED_AT, strDate); 
		values.put(DBHelper.KEY_UPDATED_AT, strDate);
		values.put(DBHelper.KEY_CREATED_BY, transaction.getCreatedBy());
		values.put(DBHelper.KEY_UPDATED_BY, transaction.getUpdatedBy());
		
		long insertId = database.insert(TABLE_NAME, null, values);
		
		return insertId;
	}

	@Override
	public Model findById(long id) {
		Cursor cursor = database.query(TABLE_NAME, 
				TABLE_COLUMNS, DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)}, null, null, null);
		
		Model model = null;
		if(cursor !=null && cursor.moveToFirst()){
			model = cursorToModel(cursor);
		}
		return model;
	}
	
	public ArrayList<Model> findByFormId(long formId){
		ArrayList<Model> allData = new ArrayList<Model>();
		Model model = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, DBHelper.KEY_FORM_ID + " = ? ", 
				new String[] {String.valueOf(formId)}, null, null, null); 
		
		if(cursor != null && cursor.moveToFirst()){
			do {
				model = cursorToModel(cursor); 
				allData.add(model);
			} while (cursor.moveToNext());
		}
		
		return allData;
	}
	
	@Override
	public ArrayList<Model> getAll() {
		ArrayList<Model> allData = new ArrayList<Model>();
		Model model = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, 
				null, null, null, null, null);
		
		if(cursor !=null && cursor.moveToFirst()){
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
		
		Transaction transaction = (Transaction) model;
		ContentValues values = new ContentValues(); 
		
		values.put(DBHelper.KEY_FORM_ID, transaction.getFormId());
		values.put(DBHelper.KEY_RECORD_ORDER, transaction.getRecordOrder()); 
		values.put(DBHelper.KEY_UPDATED_AT, strDate);
		values.put(DBHelper.KEY_CREATED_BY, transaction.getCreatedBy());
		values.put(DBHelper.KEY_UPDATED_BY, transaction.getUpdatedBy());
		
		int affectedRows = database.update(TABLE_NAME, values, DBHelper.KEY_ID + " = ? ", 
				new String[]{String.valueOf(transaction.getId())}); 
		
		return affectedRows;
	}

	@Override
	public int delete(long id){
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context);
		detailDAO.deleteByTransactionId(id); 
		detailDAO.close();
		
		int affectedRows = database.delete(TABLE_NAME, DBHelper.KEY_ID + " = ? ", 
				new String[]{String.valueOf(id)});
		return affectedRows;
	}
	
	public int deleteByFormId(long formId){
		ArrayList<Model> formDatas = findByFormId(formId); 
		int affectedRows = 0;
		for (Model model : formDatas) {
			affectedRows += delete(model.getId());
		}
		return affectedRows;
	}
	
	@Override
	protected Model cursorToModel(Cursor cursor) {
		Transaction transaction = new Transaction();
		transaction.setId(cursor.getLong(0)); 
		transaction.setFormId(cursor.getLong(1)); 
		transaction.setRecordOrder(cursor.getInt(2));
		transaction.setCreatedAt(cursor.getString(3)); 
		transaction.setUpdatedAt(cursor.getString(4)); 
		transaction.setCreatedBy(cursor.getLong(5)); 
		transaction.setUpdatedBy(cursor.getLong(6));
		
		return transaction;
	}

}
