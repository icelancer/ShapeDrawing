package com.icelancer.shapedrawing;

import java.util.ArrayList;
import java.util.List;

import com.icelancer.shapefile.shape.Point;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class ShapeView extends View {
	private Paint paint;
	private List<float[]> pointList;
	
	public ShapeView(Context context) {
		super(context);
		this.pointList = new ArrayList<>();
		this.paint = new Paint();
		paint.setColor(Color.RED);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for (float[] list: this.pointList) {
			canvas.drawLines(list, paint);
		}
		
		float[] line = {3,3,100, 100};
		canvas.drawLines(line, paint);
	}
	
	public void drawPolyline (List<List<Point>> partsList, double[] bbox) {
		float[] pointList;
		int idx;
		
		for (List<Point> list: partsList) {
			pointList = new float[list.size() * 2];
			idx = 0;
			
			for (Point point: list) {
				pointList[idx++] = (float) point.getX() - (float)bbox[0];
				pointList[idx++] = (float) point.getY() - (float)bbox[1];
				
				Log.i("SHAPE", point.getX() + " = " + ((float) point.getX() - (float)bbox[0]));
			}
			
			this.pointList.add(pointList);
		}
		
	}
	
	

}
