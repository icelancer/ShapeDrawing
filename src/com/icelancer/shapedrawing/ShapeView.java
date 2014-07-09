package com.icelancer.shapedrawing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.icelancer.shapefile.shape.Point;

public class ShapeView extends View {
	private Paint paint;
	private List<float[]> pointList;
	private Matrix matrix;
	private RectF drawableRect;
	private RectF viewRect;
	private ScaleGestureDetector detector;
	private float oldScale = 1;
	
	
	public ShapeView(Context context) {
		super(context);
		this.pointList = new ArrayList<float[]>();
		this.paint = new Paint();
		paint.setColor(Color.RED);
		
		this.matrix = new Matrix();
		
		this.detector = new ScaleGestureDetector(this.getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){

			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float scale = detector.getScaleFactor();

				scale *= oldScale;

				changeScale(scale, detector.getFocusX(), detector.getFocusY());
				
				return super.onScale(detector);
			}
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				super.onScaleEnd(detector);
				oldScale = oldScale * detector.getScaleFactor();
			}
		});
		
		this.setOnTouchListener(new OnTouchListener() {
			private float initX;
			private float initY;
			
				
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				
				if (detector.isInProgress() == false) {
					int index = event.getActionIndex();
			        int action = event.getActionMasked();
			        int pointerId = event.getPointerId(index);

			        switch(action) {
		            case MotionEvent.ACTION_DOWN:
		            	initX = event.getX(index);
		            	initY = event.getY(index);
		                break;
		            case MotionEvent.ACTION_MOVE:
		               float curX = event.getX(index);
		               float curY = event.getY(index);;
		               move(curX - initX, curY - initY);
		               
		               initX = curX;
		               initY = curY;
		               break;
		            case MotionEvent.ACTION_UP:
		            case MotionEvent.ACTION_CANCEL:
		                
		                break;
			        }
				}

				return true;
			}
			
			private float calcDistance (float x1, float y1, float x2, float y2) {
				float dx = x1 - x2;
	    		float dy = y1 - y2;
	    		
				return FloatMath.sqrt(dx * dx + dy * dy);
			}
		});
	}
	
	private void changeScale (float scale, float x, float y) {
		matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
		matrix.postRotate(180, this.getWidth()/2, this.getHeight()/2);
		matrix.postScale(scale, scale, x, y);
		
		invalidate();
	}
	
	private void move(float dx, float dy) {
		matrix.postTranslate(dx, dy);
		
		invalidate();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		this.viewRect = new RectF(0, 0, this.getWidth(), this.getHeight());
		matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
		matrix.postRotate(180, this.getWidth()/2, this.getHeight()/2);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
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
		
		
		
		invalidate();
	}

}

