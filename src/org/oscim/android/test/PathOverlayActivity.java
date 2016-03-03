/*
 * Copyright 2014 Hannes Janetzek
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.oscim.android.test;

import static org.oscim.tiling.source.bitmap.DefaultSources.STAMEN_TONER;

import java.util.ArrayList;
import java.util.List;

import org.oscim.backend.canvas.Color;
import org.oscim.core.GeoPoint;
import org.oscim.layers.PathLayer;
import org.oscim.map.Viewport;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;

public class PathOverlayActivity extends BitmapTileMapActivity {
	PathLayer pathLayer;
	public PathOverlayActivity() {
		super(STAMEN_TONER.build());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBitmapLayer.tileRenderer().setBitmapAlpha(0.5f);

//		createLayers(1, true);
		int c = Color.fade(Color.rainbow((float) (39.9 + 90) / 180), 0.5f);
		pathLayer = new PathLayer(mMap, c, 10);
		List<GeoPoint> points = new ArrayList<GeoPoint>();
		points.add(new GeoPoint(29.654, 91.139));
		points.add(new GeoPoint(28.265, 114.188));
		points.add(new GeoPoint(26.265, 115.188));
		points.add(new GeoPoint(23.265, 105.188));
		points.add(new GeoPoint(22.265, 114.188));
//		new LatLng(29.654, 91.139), new LatLng(28.265, 114.188), new LatLng(26.265, 115.188), new LatLng(23.265, 105.188),new LatLng(22.265, 114.188)
		pathLayer.setPoints(points);
		
		mMap.layers().add(pathLayer);
		

		mMap.setMapPosition(0, 0, 1 << 2);
		
//		long t = SystemClock.uptimeMillis();
//		float pos = t % 20000 / 10000f - 1f;
//		createLayers(pos, true);
		

//		looooop();
	}
	
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//	    // TODO Auto-generated method stub
//		android.util.Log.i("zxy", "path  onTouch add point to pathLayer " + event.getAction());
//		if(event.getAction() == MotionEvent.ACTION_DOWN) {
//			android.util.Log.i("zxy", "path  onTouch add point to pathLayer");
//			Viewport mapPosition = mMap.viewport();
//			
//			pathLayer.addPoint(mapPosition.fromScreenPoint(event.getX(), event.getY()));
//		}
//		
//	    return super.onTouchEvent(event);
//	}

	void looooop() {
		mMap.postDelayed(new Runnable() {
			@Override
			public void run() {
				long t = SystemClock.uptimeMillis();
				float pos = t % 20000 / 10000f - 1f;
				createLayers(pos, false);
				//Samples.log.debug("update took" + (SystemClock.uptimeMillis() - t) + " " + pos);
				looooop();
				redraw();
			}
		}, 50);
	}

	void redraw() {
		mMap.render();
	}

	ArrayList<PathLayer> mPathLayers = new ArrayList<PathLayer>();

	void createLayers(float pos, boolean init) {

		int i = 0;

		for (double lat = -90; lat <= 90; lat += 5) {
			List<GeoPoint> pts = new ArrayList<GeoPoint>();

			for (double lon = -180; lon <= 180; lon += 2) {
				//pts.add(new GeoPoint(lat, lon));
				double longitude = lon + (pos * 180);
				if (longitude < -180)
					longitude += 360;
				if (longitude > 180)
					longitude -= 360;

				double latitude = lat + (pos * 90);
				if (latitude < -90)
					latitude += 180;
				if (latitude > 90)
					latitude -= 180;

				latitude += Math.sin((Math.abs(pos) * (lon / Math.PI)));

				pts.add(new GeoPoint(latitude, longitude));
			}
			PathLayer pathLayer;
			if (init) {
				int c = Color.fade(Color.rainbow((float) (lat + 90) / 180), 0.5f);
				pathLayer = new PathLayer(mMap, c, 6);
				mMap.layers().add(pathLayer);
				mPathLayers.add(pathLayer);
			} else {
				pathLayer = mPathLayers.get(i++);
			}

			pathLayer.setPoints(pts);
		}

	}
}
