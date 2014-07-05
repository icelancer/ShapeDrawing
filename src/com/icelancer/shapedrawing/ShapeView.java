package com.icelancer.shapedrawing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.icelancer.shapefile.shape.Point;

public class ShapeView extends View {
	private Paint paint;
	private List<float[]> pointList;
	private Matrix matrix;
	private RectF drawableRect;
	private RectF viewRect;
	
	public ShapeView(Context context) {
		super(context);
		this.pointList = new ArrayList<float[]>();
		this.paint = new Paint();
		paint.setColor(Color.RED);
		
		this.matrix = new Matrix();
		
		Log.i(VIEW_LOG_TAG, this.getWidth()+"");
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.viewRect = new RectF(0, 0, w, h);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
		
		matrix.postRotate(180, this.getWidth()/2, this.getHeight()/2);
		canvas.concat(matrix);
		
		for (float[] list: this.pointList) {
			canvas.drawLines(list, paint);
		}
		
	}
	
	public void drawPolyline (List<List<Point>> partsList, double[] bbox) {
		float[] pointList;
		int idx;
		
		for (List<Point> list: partsList) {
			pointList = new float[list.size() * 2];
			idx = 0;
			
			for (Point point: list) {
				pointList[idx++] = ((float) point.getX() - (float)bbox[0]);
				pointList[idx++] = ((float) point.getY() - (float)bbox[1]);
			}
			
			this.pointList.add(pointList);
		}
		
		float mapWidth = (float) (bbox[2] - bbox[0]);
		float mapHeight = (float) (bbox[3] - bbox[1]);
		
		
		this.drawableRect = new RectF(0, 0, mapWidth, mapHeight);
	}
	
}
