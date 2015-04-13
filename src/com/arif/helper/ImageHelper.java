package com.arif.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class ImageHelper {
	
	
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
	    Bitmap result = null;
	    try {
	        result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(result);

	        int color = 0xff424242;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, 200, 200);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawCircle(50, 50, 50, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	    } catch (NullPointerException e) {
	    } catch (OutOfMemoryError o) {
	    }
	    return result;
	}
	
	public static int dpToPx(int dp, Context context){
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics(); 
		int px = Math.round(dp * (displayMetrics.xdpi/DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}
	
	public static int pxToDp(int px, Context context){
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int dp = Math.round(px/(displayMetrics.xdpi/DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}
	
}
