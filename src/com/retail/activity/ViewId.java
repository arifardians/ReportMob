package com.retail.activity;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewId {
	private static ViewId INSTANCE = new ViewId(); 
	private static final int INIT_VALUE_ID = Integer.MAX_VALUE ;
	private AtomicInteger seq; 
	
	private ViewId(){
		seq = new AtomicInteger(INIT_VALUE_ID);
	}
	
	public int getUniqueId(){
		return seq.decrementAndGet();
	}
	
	public static ViewId getInstance(){
		return INSTANCE;
	}
	
}
