package com.example.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;

public class DBHelper extends SQLiteOpenHelper {
	// Logcat tag 
	public static final String LOG = "[databaseHelper]"; 
	

	/*
	 * Database version 
	 */
	public static final int VERSION  =  6;
	
	/**
	 * Database name
	 */
	public static final String DATABASE_NAME = "db_report_mob";
	
	/**
	 * List of table name
	 */
	public static final String TABLE_SESSION	 = "session";
	public static final String TABLE_FORM		 = "tb_form"; 
	public static final String TABLE_FIELD		 = "tb_form_field";
	
	public static final String TABLE_FIELD_OPTIONS 	= "tb_field_options";
	public static final String TABLE_TRANSACTION   	= "tr_form"; 
	
	public static final String TABLE_TRANSACTION_DETAIL = "tr_form_dtl";		
	
	public static final String TABLE_GROUP 			= "tb_group";
	public static final String TABLE_GRP_USR_ACL	= "grp_usr_acl";
	public static final String TABLE_FORM_ASSIGN 	= "form_assign"; 
	public static final String TABLE_ACL 			= "tb_acl"; 
	public static final String TABLE_ACL_DETAIL 	= "dtl_acl";
	public static final String TABLE_GROUP_FORM		= "grp_form";
	
	/**
	 * Common column names
	 */
	public static final String KEY_ID 			= "_id";
	
	public static final String KEY_FORM_ID 		= "form_id";
	public static final String KEY_FIELD_ID		= "field_id";
	
	public static final String KEY_TRANSACTION_ID = "tr_form_id"; 
	
	public static final String KEY_CREATED_AT 	= "created_at"; 
	public static final String KEY_UPDATED_AT 	= "updated_at"; 
	public static final String KEY_CREATED_BY 	= "created_by"; 
	public static final String KEY_UPDATED_BY 	= "updated_by";
	
	/**
	 * Session Table - column names 
	 */
	public static final String KEY_EMAIL 		= "email";
	public static final String KEY_SECRET		= "secret";
	public static final String KEY_KEY_SESSION	= "key";
	public static final String KEY_TOKEN		= "token";
	
	/**
	 * field table form
	 */
	public static final String KEY_FORM_OWNER_ID		= "owner_id";
	public static final String KEY_FORM_OWNER_TYPE		= "owner_type";
	
	public static final String KEY_FORM_NAME 	= "form_name"; 
	public static final String KEY_DESCRIPTION 	= "description";
	public static final String KEY_FORM_ROW		= "form_row"; 
	public static final String KEY_FORM_SLUG 	= "slug";
	public static final String KEY_IS_ACTIVATED = "is_activated";
	
	
	/**
	 * Field table tb_form_field
	 */
	public static final String KEY_FIELD_TITLE	= "field_title"; 
	public static final String KEY_FIELD_NAME	= "field_name";
	public static final String KEY_PLACEHOLDER	= "placeholder";
	public static final String KEY_FIELD_TYPE	= "field_type"; 
	public static final String KEY_FIELD_SIZE 	= "field_size"; 
	public static final String KEY_COLNAME 		= "col_name"; 
	public static final String KEY_DATA_TYPE 	= "data_type"; 
	public static final String KEY_LIST_ORDER 	= "list_order";
	public static final String KEY_FIELD_OPTIONS = "field_options";
	public static final String KEY_IS_FIELD_SYS	= "is_field_sys";
	public static final String KEY_IS_REQUIRED  = "is_required";
	/**
	 * Field table tb_field_options
	 */
	public static final String KEY_OPTION_NAME 	= "option_name"; 
	public static final String KEY_OPTION_VALUE	= "option_value"; 
	public static final String KEY_OPTION_ORDER	= "option_order"; 
	
	/**
	 * Columns of table transaction
	 */
	public static final String KEY_RECORD_ORDER = "record_order";
	
	/**
	 * Columns of table transaction detail
	 */
	public static final String KEY_FIELD_VALUE = "field_value";
	
	
	/**
	 * Field table group
	 */
	public static final String KEY_GROUP_NAME 	= "group_name"; 
	public static final String KEY_GROUP_SLUG 	= "group_slug";
	public static final String KEY_GROUP_EMAILS = "group_emails";
	
	/**
	 * group user acl 
	 */
	public static final String KEY_GROUP_ID = "grp_id"; 
	public static final String KEY_USER_ID 	= "usr_id"; 
	public static final String KEY_ACL_ID 	= "acl_id";
	
	
	/**
	 * Form Assign 
	 */
	public static final String KEY_ASSIGN_TO = "assign_to"; 
	
	
	/**
	 * Table ACL
	 */
	public static final String KEY_ACL_PRINCIPAL 		= "acl_principal"; 
	public static final String KEY_ACL_PRINCIPAL_SLUG 	= "acl_principal_slug"; 
	
	
	/**
	 * Detail ACL
	 */
	public static final String KEY_ACL_ACTION 		= "acl_action"; 
	public static final String KEY_ACL_PERMISSION 	= "acl_permission";
	
	/**
	 * Columns of session table
	 */
	public static final String[] TABLE_SESSION_COLUMNS	= { KEY_ID, 
															KEY_EMAIL,
															KEY_SECRET, 
															KEY_KEY_SESSION,
															KEY_TOKEN, 
															KEY_USER_ID};
	
	/**
	 * Columns of form table
	 */
	public static final String[] TABLE_FORM_COLUMNS 	= { KEY_ID,
															KEY_FORM_OWNER_ID, 
															KEY_FORM_OWNER_TYPE, 
															KEY_FORM_NAME,
															KEY_DESCRIPTION,
															KEY_FORM_ROW, 
															KEY_FORM_SLUG, 
															KEY_IS_ACTIVATED, 
															KEY_CREATED_AT, 
															KEY_UPDATED_AT, 
															KEY_CREATED_BY, 
															KEY_UPDATED_BY};
	
	/**
	 * Columns of form field table 
	 */
	public static final String[] TABLE_FIELD_COLUMNS	= { KEY_ID, 
															KEY_FORM_ID, 
															KEY_FIELD_TITLE, 
															KEY_FIELD_NAME,
															KEY_PLACEHOLDER,
															KEY_FIELD_TYPE, 
															KEY_FIELD_SIZE,
															KEY_COLNAME, 
															KEY_DATA_TYPE, 
															KEY_LIST_ORDER, 
															KEY_IS_FIELD_SYS,
															KEY_IS_REQUIRED,
															KEY_CREATED_AT, 
															KEY_UPDATED_AT};
	
	/**
	 * Columns of option fields table
	 */
	public static final String[] TABLE_FIELD_OPTION_COLUMNS	= { KEY_ID, 
																KEY_OPTION_NAME, 
																KEY_OPTION_VALUE, 
																KEY_OPTION_ORDER, 
																KEY_FIELD_ID, 
																KEY_CREATED_AT, 
																KEY_UPDATED_AT};
	
	/**
	 * Columns of transaction table
	 */
	public static final String[] TABLE_TRANSACTION_COLUMNS 	= {	KEY_ID,
																KEY_FORM_ID, 
																KEY_RECORD_ORDER,
																KEY_USER_ID,
																KEY_CREATED_AT, 
																KEY_UPDATED_AT, 
																KEY_CREATED_BY,
																KEY_UPDATED_BY};
	
	public static final String[] TABLE_TRANSACTION_DETAIL_COLUMNS = { KEY_ID, 
																	  KEY_TRANSACTION_ID,
																	  KEY_FIELD_ID, 
																	  KEY_FIELD_VALUE, 
																	  KEY_CREATED_AT, 
																	  KEY_UPDATED_AT};
	
	/**
	 * Columns of group table
	 */
	public static final String[] TABLE_GROUP_COLUMNS 	= {	KEY_ID, 
															KEY_GROUP_NAME, 
															KEY_GROUP_SLUG, 
															KEY_CREATED_AT, 
															KEY_UPDATED_AT, 
															KEY_CREATED_BY, 
															KEY_UPDATED_BY};
	
	/**
	 * Columns of tb_acl (access control list)
	 */
	public static final String[] TABLE_ACL_COLUMNS = { KEY_ID,
													   KEY_ACL_PRINCIPAL,
													   KEY_ACL_PRINCIPAL_SLUG,
													   KEY_CREATED_AT,
													   KEY_UPDATED_AT, 
													   KEY_CREATED_BY, 
													   KEY_UPDATED_BY};
	
	/**
	 * Columns of tb_acl_dtl (detail acl)
	 */
	public static final String[] TABLE_ACL_DETAIL_COLUMNS = { KEY_ID,  
														   KEY_ACL_ACTION, 
														   KEY_ACL_PERMISSION, 
														   KEY_CREATED_AT,
														   KEY_UPDATED_AT,
														   KEY_ACL_ID};
	
	/**
	 * Columns of table grp_usr_acl
	 */
	public static final String[] TABLE_GRP_USR_ACL_COLUMNS = { KEY_ID, 
															 KEY_GROUP_ID, 
															 KEY_USER_ID, 
															 KEY_ACL_ID, 
															 KEY_CREATED_AT, 
															 KEY_UPDATED_AT};
	/**
	 * Columns of table form_assign 
	 */
	public static final String[] TABLE_FORM_ASSIGN_COLUMNS = { KEY_ID, 
															KEY_FORM_ID, 
															KEY_ASSIGN_TO, 
															KEY_CREATED_AT, 
															KEY_UPDATED_AT, 
															KEY_CREATED_BY, 
															KEY_UPDATED_BY};
	/**
	 * Columns of table group_form
	 */
	public static final String[] TABLE_GROUP_FORM_COLUMNS = {KEY_ID, 
															KEY_GROUP_ID, 
															KEY_FORM_ID, 
															KEY_CREATED_AT, 
															KEY_UPDATED_AT};
	/**
	 * CREATE table statements sqlite
	 */
	
	public static final String CREATE_TABLE_SESSION 	= "CREATE TABLE "+ TABLE_SESSION +
			  " (" + KEY_ID + " INTEGER PRIMARY KEY, "
				   + KEY_EMAIL + " TEXT, "
				   + KEY_SECRET + " TEXT, "
				   + KEY_KEY_SESSION + " TEXT, "
			  	   + KEY_TOKEN + " TEXT, "
			  	   + KEY_USER_ID + " INTEGER)";
	
	public static final String CREATED_UPDATED_AT_SQL = " " + KEY_CREATED_AT + " DATETIME, "
															+ KEY_UPDATED_AT + " DATETIME ";
	public static final String CREATED_UPDATED_BY_SQL = " " + KEY_CREATED_BY + " INTEGER, "
															+ KEY_UPDATED_BY + " INTEGER ";
	
	public static final String CREATE_TABLE_FORM = "CREATE TABLE " + TABLE_FORM + " (" 
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_FORM_OWNER_ID + " INTEGER, " 
								+ KEY_FORM_OWNER_TYPE + " INTEGER, "
								+ KEY_FORM_NAME + " TEXT, "
								+ KEY_DESCRIPTION + " TEXT, "
								+ KEY_FORM_ROW + " INTEGER, "
								+ KEY_FORM_SLUG + " TEXT, "
								+ KEY_IS_ACTIVATED + " INTEGER, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ CREATED_UPDATED_BY_SQL + ")";
	

	public static final String CREATE_TABLE_FIELD = "CREATE TABLE " + TABLE_FIELD + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_FORM_ID + " INTEGER, "
								+ KEY_FIELD_TITLE + " TEXT, "
								+ KEY_FIELD_NAME + " TEXT, "
								+ KEY_PLACEHOLDER + " TEXT, "
								+ KEY_FIELD_TYPE + " INTEGER, "
								+ KEY_FIELD_SIZE + " TEXT, "
								+ KEY_COLNAME + " TEXT, "
								+ KEY_DATA_TYPE + " TEXT, "
								+ KEY_LIST_ORDER + " INTEGER, "
								+ KEY_IS_FIELD_SYS + " INTEGER," 
								+ KEY_IS_REQUIRED + " INTEGER," 
								+ CREATED_UPDATED_AT_SQL + ", "
								+ "FOREIGN KEY (" + KEY_FORM_ID + ") " 
								+ "REFERENCES " + TABLE_FORM + "(" + KEY_ID + "))";
	
	public static final String CREATE_TABLE_FIELD_OPTIONS = "CREATE TABLE " + TABLE_FIELD_OPTIONS + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_OPTION_NAME + " TEXT, "
								+ KEY_OPTION_VALUE + " TEXT, "
								+ KEY_OPTION_ORDER + " INTEGER, "
								+ KEY_FIELD_ID + " INTEGER, " 
								+ CREATED_UPDATED_AT_SQL + ", "
								+ "FOREIGN KEY (" + KEY_FIELD_ID + ") " 
								+ "REFERENCES " + TABLE_FIELD + "(" + KEY_ID + "))";
	
	public static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY,  "
								+ KEY_FORM_ID +" INTEGER, "
								+ KEY_USER_ID + " INTEGER, "
								+ KEY_RECORD_ORDER + " INTEGER, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ CREATED_UPDATED_BY_SQL + ", " 
								+ "FOREIGN KEY (" + KEY_FORM_ID + ") "
								+ "REFERENCES " + TABLE_FORM + "(" + KEY_ID + "))";
	
	
	public static final String CREATE_TABLE_TRANSACTION_DETAIL = "CREATE TABLE " + TABLE_TRANSACTION_DETAIL + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_TRANSACTION_ID + " INTEGER, "
								+ KEY_FIELD_ID + " INTEGER, "
								+ KEY_FIELD_VALUE + " TEXT, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ "FOREIGN KEY (" + KEY_TRANSACTION_ID + ") "
								+ "REFERENCES " + TABLE_TRANSACTION + "(" + KEY_ID + "), "
								+ "FOREIGN KEY (" + KEY_FIELD_ID + ") "
								+ "REFERENCES " + TABLE_FIELD + "( " + KEY_ID + "))";
	
	public static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_GROUP +  " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_GROUP_NAME + " TEXT, "
								+ KEY_GROUP_SLUG + " TEXT, "
								+ KEY_GROUP_EMAILS + " TEXT, "
								+ CREATED_UPDATED_AT_SQL + " ,"
								+ CREATED_UPDATED_BY_SQL + " )";
	
	
	public static final String CREATE_TABLE_ACL = "CREATE TABLE " + TABLE_ACL + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_ACL_PRINCIPAL + " TEXT, "
								+ KEY_ACL_PRINCIPAL_SLUG + " TEXT, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ CREATED_UPDATED_BY_SQL + ")";
	
	public static final String CREATE_TABLE_ACL_DTL = "CREATE TABLE " + TABLE_ACL_DETAIL + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, " 
								+ KEY_ACL_ACTION + " TEXT, "
								+ KEY_ACL_PERMISSION + " TEXT, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ KEY_ACL_ID + " INTEGER , "
								+ "FOREIGN KEY (" + KEY_ACL_ID + ") "
								+ "REFERENCES " + TABLE_ACL + "(" + KEY_ID + "))";
	
	public static final String CREATE_TABLE_GRP_USR_ACL = "CREATE TABLE " + TABLE_GRP_USR_ACL + " ( "
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_GROUP_ID + " INTEGER, "
								+ KEY_USER_ID + " INTEGER, "
								+ KEY_ACL_ID + " INTEGER, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ "FOREIGN KEY (" + KEY_GROUP_ID + ") REFERENCES " + TABLE_GROUP + "(" + KEY_ID + "), "
								+ "FOREIGN KEY (" + KEY_ACL_ID +") REFERENCES " + TABLE_ACL +"(" + KEY_ID + "))";
	
	public static final String CREATE_TABLE_FORM_ASSIGN = "CREATE TABLE " + TABLE_FORM_ASSIGN + "("
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_FORM_ID + " INTEGER, "
								+ KEY_ASSIGN_TO + " INTEGER, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ CREATED_UPDATED_BY_SQL + ", "
								+ "FOREIGN KEY (" + KEY_FORM_ID +") REFERENCES " + TABLE_FORM +"(" + KEY_ID +"))";
	
	public static final String CREATE_TABLE_GROUP_FORM = "CREATE TABLE " + TABLE_GROUP_FORM + "("
								+ KEY_ID + " INTEGER PRIMARY KEY, "
								+ KEY_GROUP_ID + " INTEGER, "
								+ KEY_FORM_ID + " INTEGER, "
								+ CREATED_UPDATED_AT_SQL + ", "
								+ "FOREIGN KEY (" + KEY_GROUP_ID + ") REFERENCES " + TABLE_GROUP +"(" + KEY_ID + "), "
								+ "FOREIGN KEY (" + KEY_FORM_ID +") REFERENCES " + TABLE_FORM +"(" + KEY_ID + "))" ;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}
	
		
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SESSION);
		db.execSQL(CREATE_TABLE_FORM);
		db.execSQL(CREATE_TABLE_FIELD);
		db.execSQL(CREATE_TABLE_FIELD_OPTIONS);
		db.execSQL(CREATE_TABLE_TRANSACTION);
		db.execSQL(CREATE_TABLE_TRANSACTION_DETAIL);
		db.execSQL(CREATE_TABLE_GROUP);
		db.execSQL(CREATE_TABLE_FORM_ASSIGN);
		db.execSQL(CREATE_TABLE_GROUP_FORM); 
		db.execSQL(CREATE_TABLE_ACL); 
		db.execSQL(CREATE_TABLE_ACL_DTL); 
		db.execSQL(CREATE_TABLE_GRP_USR_ACL); 
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_OPTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM_ASSIGN); 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_FORM); 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACL); 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACL_DETAIL); 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRP_USR_ACL);
		// Create new table 
		
		onCreate(db);
	}
	
	/**=========== TABLE SESSION =============**/
	
	public long insertSession(SessionApp session){
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put(KEY_EMAIL, session.getEmail());
		values.put(KEY_SECRET, session.getSecret()); 
		values.put(KEY_KEY_SESSION, session.getKey());
		values.put(KEY_TOKEN, session.getToken());
		
		long insertedId = db.insert(TABLE_SESSION, null, values);
		db.close();
		return insertedId;
	}
	
	public long updateSession(SessionApp session){
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put(KEY_EMAIL, session.getEmail());
		values.put(KEY_SECRET, session.getSecret()); 
		values.put(KEY_KEY_SESSION, session.getKey());
		values.put(KEY_TOKEN, session.getToken());
		
		long affectedRows = db.update(TABLE_SESSION, values, KEY_ID+" = ?", 
									  new String[]{String.valueOf(session.getId())});
		db.close(); 
		return affectedRows;
	}
	
	public SessionApp getSession(){
		SQLiteDatabase db = this.getReadableDatabase(); 
		Cursor cursor = db.query(TABLE_SESSION, TABLE_SESSION_COLUMNS, null, null, null, null, null);
		SessionApp session = null;
		if(cursor.moveToFirst()){
			session = new SessionApp();
			session.setId(cursor.getInt(0));
			session.setEmail(cursor.getString(1)); 
			session.setSecret(cursor.getString(2));
			session.setKey(cursor.getString(3));
			session.setToken(cursor.getString(4));
		}
		db.close();
		return session;
	}
	
	public long resetSession(SessionApp session){
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put(KEY_SECRET, "");
		values.put(KEY_KEY_SESSION, "");
		values.put(KEY_TOKEN, "");
		
		long affectedRows = db.update(TABLE_SESSION, values, KEY_ID+" = ?", 
									  new String[] {String.valueOf(session.getId())});
		db.close();
		return affectedRows;
	}
	
	public long deleteSession(){
		SQLiteDatabase db = this.getWritableDatabase();
		long affectedRows = db.delete(TABLE_SESSION, null, null); 
		db.close();
		return affectedRows;
	}
	
	/**=========== END TABLE SESSION =============**/


}
