package com.icelancer.shapedrawing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.icelancer.shapefile.shape.Point;

public class ShapeView extends View {
	private Paint paint;
	private List<float[]> pointList;
	private Matrix matrix;
	private double[] bbox;
	
	public ShapeView(Context context) {
		super(context);
		this.pointList = new ArrayList<float[]>();
		this.paint = new Paint();
		paint.setColor(Color.RED);
		
		this.matrix = new Matrix();
		
//		matrix.setRotate(180, this.getWidth()/2, this.getHeight()/2);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		double mapWidth = bbox[2] - bbox[0];
		double mapHeight = bbox[3] - bbox[1];
		int screenWidth = canvas.getWidth();
		int screenHeight = canvas.getHeight();
		
		matrix.setScale((float)(screenWidth/mapWidth), (float)(screenHeight/mapHeight));
		
		matrix.setRotate(180);
		
		canvas.setMatrix(matrix);
		
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
		
		this.bbox = bbox;
		
	}
	
	

}
