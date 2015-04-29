package com.example.dbhelper;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FormFieldDAO extends PatternDAO {
	
	public static final String TAG = "[FormFieldDAO]";
	
	// Database fields
	private SQLiteDatabase database; 
	private DBHelper dbHelper; 
	private static final String TABLE_NAME = DBHelper.TABLE_FIELD;
	private static final String[] TABLE_COLUMNS = DBHelper.TABLE_FIELD_COLUMNS; 
	
	private Context context;
	
	public FormFieldDAO(Context context) {
		dbHelper 	 = new DBHelper(context);
		this.context = context;
		
		try {
			open();
		} catch (SQLException e) {
			Log.d(TAG, "SQLException on opening database : " + e.getMessage()); 
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
		FormField field = (FormField) model;
		ContentValues values = new ContentValues(); 
		
		int required = 0; 
		if(field.isRequired()){
			required = 1;
		}
		
		values.put(DBHelper.KEY_FORM_ID, field.getFormId()); 
		values.put(DBHelper.KEY_FIELD_TITLE, field.getTitle()); 
		values.put(DBHelper.KEY_FIELD_NAME, field.getName()); 
		values.put(DBHelper.KEY_PLACEHOLDER, field.getPlaceholder());
		values.put(DBHelper.KEY_FIELD_TYPE, field.getType()); 
		values.put(DBHelper.KEY_FIELD_SIZE, field.getSize()); 
		values.put(DBHelper.KEY_COLNAME, field.getColName());
		values.put(DBHelper.KEY_DATA_TYPE, field.getDataType()); 
		values.put(DBHelper.KEY_LIST_ORDER, field.getListOrder()); 
		values.put(DBHelper.KEY_IS_REQUIRED, required);
		if(field.isFieldSystem()){
			values.put(DBHelper.KEY_IS_FIELD_SYS, 1);
		}else {
			values.put(DBHelper.KEY_IS_FIELD_SYS, 0);
		}
		
		long insertId = database.insert(TABLE_NAME, null, values);
		
		Log.d(TAG, "[inserting data], with id : "+insertId+", data : " + field.toString());
		
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
		
		FormField field = (FormField) model;
		ContentValues values = new ContentValues(); 
		
		int required = 0; 
		if(field.isRequired()){
			required = 1;
		}
		
		values.put(DBHelper.KEY_FORM_ID, field.getFormId()); 
		values.put(DBHelper.KEY_FIELD_TITLE, field.getTitle()); 
		values.put(DBHelper.KEY_FIELD_NAME, field.getName()); 
		values.put(DBHelper.KEY_PLACEHOLDER, field.getPlaceholder());
		values.put(DBHelper.KEY_FIELD_TYPE, field.getType()); 
		values.put(DBHelper.KEY_FIELD_SIZE, field.getSize()); 
		values.put(DBHelper.KEY_COLNAME, field.getColName());
		values.put(DBHelper.KEY_DATA_TYPE, field.getDataType()); 
		values.put(DBHelper.KEY_LIST_ORDER, field.getListOrder()); 
		values.put(DBHelper.KEY_IS_REQUIRED, required);
		
		int affectedRows = database.update(TABLE_NAME, values, 
				DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(field.getId())});
		
		return affectedRows;
	}

	@Override
	public int delete(long id) {
		// delete all options
		FieldOptionDAO optionDao = new FieldOptionDAO(context); 
		optionDao.deleteByFieldID(id);
		optionDao.close();
		
		TransactionDetailDAO detailDAO = new TransactionDetailDAO(context);
		detailDAO.deleteByFieldId(id);
		detailDAO.close();
		
		int affectedRows = database.delete(TABLE_NAME, DBHelper.KEY_ID + " = ? ", 
				new String[] {String.valueOf(id)});
		
		return affectedRows;
	}
	
	public ArrayList<FormField> findFieldByFormId(long formId){
		
		ArrayList<FormField> fields = new ArrayList<FormField>(); 
		FormField field = null; 
		
		Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, 
				DBHelper.KEY_FORM_ID + " = ? ", 
				new String[] {String.valueOf(formId)}, 
				null, null, DBHelper.KEY_LIST_ORDER +" ASC ");
		if(cursor != null && cursor.moveToFirst()){
			do {
				field = (FormField) cursorToModel(cursor);
				fields.add(field);
			} while (cursor.moveToNext());
		}
		
		return fields;
	}
	
	public int deleteFieldByFormId(long formId){
		ArrayList<FormField> fieldDatas = findFieldByFormId(formId);
		int affectedRows = 0;
		for (FormField field : fieldDatas) {
		 	affectedRows += delete(field.getId());
		}
		Log.d(TAG, "delete fields by form id : " + formId + ", affected rows : "+ affectedRows);
		
		return affectedRows;
	}
	

	@Override
	protected Model cursorToModel(Cursor cursor) {
			
		FormField field = new FormField(); 
		field.setId(cursor.getLong(0)); 
		field.setFormId(cursor.getLong(1)); 
		field.setTitle(cursor.getString(2)); 
		field.setName(cursor.getString(3)); 
		field.setPlaceholder(cursor.getString(4));
		field.setType(cursor.getInt(5)); 
		field.setSize(cursor.getInt(6)); 
		field.setColName(cursor.getString(7)); 
		field.setDataType(cursor.getInt(8)); 
		field.setListOrder(cursor.getInt(9)); 
		if(cursor.getInt(10) == 1){
			field.setFieldSystem(true);
		}else{
			field.setFieldSystem(false); 
		}
		
		if(cursor.getInt(11) == 1){
			field.setRequired(true);
		}else{
			field.setRequired(false);
		}
		
		return field;
	}

}
