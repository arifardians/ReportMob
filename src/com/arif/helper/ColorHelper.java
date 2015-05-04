package com.arif.helper;

import android.content.Context;
import android.graphics.Color;
import android.telephony.TelephonyManager;

public class ColorHelper {
	public static final int RED 	= Color.parseColor("#F44336");
	public static final int PURPLE 	= Color.parseColor("#9C27B0");
	public static final int LIME 	= Color.parseColor("#CDDC39");
	public static final int GREEN 	= Color.parseColor("#4CAF50");
	public static final int BLUE 	= Color.parseColor("#2196F3");
	public static final int PINK 	= Color.parseColor("#E91E63");
	public static final int BROWN 	= Color.parseColor("#795548");
	public static final int TEAL 	= Color.parseColor("#009688");
	public static final int YELLOW 	= Color.parseColor("#FFEB3B");
	public static final int ORANGE 	= Color.parseColor("#FF9800");
	public static final int GREY 	= Color.parseColor("#9E9E9E");
	public static final int DEEP_PURPLE = Color.parseColor("#673AB7");
	public static final int INDIGO 	= Color.parseColor("#3F51B5");
	public static final int CYAN 	= Color.parseColor("#00BCD4");
	public static final int BLUE_GREY = Color.parseColor("#607D8B");
	public static final int BLACK 	= Color.parseColor("#000000");
	public static final int WHITE 	= Color.parseColor("#FFFFFF");
	
	// light fill
	public static final String TRANSPARENTLY = "80";
	public static final int RED_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY + "EF9A9A");
	public static final int PURPLE_LIGHT 	= Color.parseColor("#" + TRANSPARENTLY + "CE93D8");
	public static final int LIME_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY +"E6EE9C"); 
	public static final int GREEN_LIGHT 	= Color.parseColor("#" + TRANSPARENTLY +"A5D6A7");
	public static final int BLUE_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY +"BBDEFB");
	public static final int PINK_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY + "F48FB1");
	public static final int BROWN_LIGHT 	= Color.parseColor("#" + TRANSPARENTLY +"A1887F");
	public static final int TEAL_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY +"B2DFDB");
	public static final int YELLOW_LIGHT 	= Color.parseColor("#" + TRANSPARENTLY +"FFF59D");
	public static final int ORANGE_LIGHT 	= Color.parseColor("#" + TRANSPARENTLY +"FFCC80");
	public static final int GREY_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY +"E0E0E0");
	public static final int DEEP_PURPLE_LIGHT = Color.parseColor("#" + TRANSPARENTLY + "B39DDB"); 
	public static final int INDIGO_LIGHT 	= Color.parseColor("#" + TRANSPARENTLY +"9FA8DA");
	public static final int CYAN_LIGHT 		= Color.parseColor("#" + TRANSPARENTLY +"B2EBF2"); 
	public static final int BLUE_GREY_LIGHT = Color.parseColor("#" + TRANSPARENTLY +"B0BEC5");
	
	private static final int[] STROKE_COLORS ={ BLUE, RED, LIME, GREEN,PURPLE, PINK, BROWN,
												TEAL, YELLOW, ORANGE, GREY, DEEP_PURPLE, INDIGO,
												CYAN, BLUE_GREY};
	private static final int[] SOLID_COLORS ={ 	BLUE_LIGHT, RED_LIGHT, LIME_LIGHT, GREEN_LIGHT,  
												PURPLE_LIGHT, PINK_LIGHT, BROWN_LIGHT, TEAL_LIGHT, 
												YELLOW_LIGHT, ORANGE_LIGHT, GREY_LIGHT, DEEP_PURPLE_LIGHT,
												INDIGO_LIGHT, CYAN_LIGHT, BLUE_GREY_LIGHT};

	public static int getStrokeColor(int i){
		return STROKE_COLORS[i];
	}
	
	public static int getSolidColor(int i){
		return SOLID_COLORS[i];
	}
}