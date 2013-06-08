package com.example.arduinodrawingmachine;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

public class Layer
{
	
	 List<Point> points = new ArrayList<Point>();

	Bitmap bitmap = Bitmap.createBitmap(1270, 720, Bitmap.Config.ARGB_8888);
	Canvas bufferCanvas = new Canvas(bitmap);
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public Layer() {
		// Exists only to defeat instantiation.
		
		paint.setColor(0xffff0000);
		paint.setStrokeCap(Paint.Cap.ROUND);
	}
	public void redraw()
	{
		Paint paintp = new Paint();
		paintp.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		bufferCanvas.drawPaint(paintp);
		if(points.size()>1)
		{
			
			for(int i=1;i<points.size();i++)
			{
				if(!points.get(i).isUP)
				{
				paint.setStrokeWidth(points.get(i).size);
				bufferCanvas.drawLine(points.get(i).x,points.get(i).y,points.get(i - 1).x, points.get(i - 1).y, paint);
				}
			}
			
		}
		
	}
}
