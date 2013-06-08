package com.example.arduinodrawingmachine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener
{
	private static final String TAG = "DrawView";

	Paint paint = new Paint();
	Model model;
Point previousPoint;
	public DrawView(Context context)
	{
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);

		this.setOnTouchListener(this);
		model = Model.getInstance();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
	}

	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;

	@Override
	public void onDraw(Canvas canvas)
	{

	
		canvas.drawBitmap(model.layer3.bitmap, 0, 0, paint);
		canvas.drawBitmap(model.layer2.bitmap, 0, 0, paint);
		canvas.drawBitmap(model.layer1.bitmap, 0, 0, paint);
	}

	public boolean onTouch(View view, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{

		} else if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
			Point point = new Point();
			point.size =( ((event.getPressure())*(event.getPressure()))) * 10 +1;
			point.x = event.getX();
			point.y = event.getY();
			point.isUP = true;
			model.currentLayer.points.add(point);
			previousPoint  =point;
			return true;

		} else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{

		 float dist =(float)Math.sqrt( Math.pow((previousPoint.x -event.getX()),2)+Math.pow((previousPoint.y -event.getY()),2));
		// return super.onTouchEvent(event);
		if(dist<3)return true;
		Point point = new Point();
		point.size =( ((event.getPressure())*(event.getPressure()))) * 6 +1;
		point.x = event.getX();
		point.y = event.getY();
		model.currentLayer.points.add(point);
		model.currentLayer.paint.setStrokeWidth(point.size);
		int size = model.currentLayer.points.size() - 1;
		previousPoint  =point;

		if (size > 1)
		{
			model.currentLayer.bufferCanvas.drawLine(model.currentLayer.points.get(size).x,model.currentLayer.points.get(size).y,
					model.currentLayer.points.get(size - 1).x, model.currentLayer.points.get(size - 1).y, model.currentLayer.paint);
		}
	}
		invalidate();

		return true;
	}
}
