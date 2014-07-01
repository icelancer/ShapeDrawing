package com.icelancer.shapedrawing;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.icelancer.NotSupportedFileFormat;
import com.icelancer.shapefile.ShapeReader;
import com.icelancer.shapefile.shape.Point;
import com.icelancer.shapefile.shape.Polyline;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		
		ShapeView view = new ShapeView(this);
		setContentView(view);
		
		InputStream is;
		ShapeReader reader = null;
		
		try {
			is = getAssets().open("Polyline.shp");
			reader = new ShapeReader(is);
			Polyline polyline = null;
			List<List<Point>> partsList;
			
			while(reader.hasNext()) {
				polyline = (Polyline) reader.next();
				double[] bbox = reader.getBBox();
				partsList = polyline.getPartsList();
				view.drawPolyline(partsList, bbox);
			}
			
			view.invalidate();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (NotSupportedFileFormat e) {

			e.printStackTrace();
		}
	}
}
