package com.example.dbhelper;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;

public abstract class PatternDAO {
	
	abstract public void open() throws SQLException; 
	abstract public void close();
	abstract public long insert(Model model); 
	abstract public Model findById(long id);
	abstract public ArrayList<Model> getAll(); 
	abstract public int update(Model model);
	abstract public int delete(long id);
	abstract protected Model cursorToModel(Cursor cursor);
	
}
