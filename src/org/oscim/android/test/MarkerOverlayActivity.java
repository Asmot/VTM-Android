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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import org.oscim.android.MapActivity;
import org.oscim.android.MapView;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.backend.canvas.Color;
import org.oscim.core.GeoPoint;
import org.oscim.layers.PathLayer;
import org.oscim.layers.TileGridLayer;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.ItemizedLayer.OnItemGestureListener;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerItem.HotspotPlace;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.renderer.MapRenderer;

import java.util.ArrayList;
import java.util.List;

import static org.oscim.android.canvas.AndroidGraphics.drawableToBitmap;

public class MarkerOverlayActivity extends MapActivity
        implements OnItemGestureListener<MarkerItem> {

    PathLayer pathLayer;

	private static final boolean BILLBOARDS = true;
	private MarkerSymbol mFocusMarker;

    MapView mMapView;

	public MarkerOverlayActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = (MapView) findViewById(R.id.mapView);
        registerMapView(mMapView);

        MapRenderer.setBackgroundColor(0xff777777);


		/* directly load bitmap from resources */
		Bitmap bitmap = drawableToBitmap(getResources(), R.drawable.marker_default);

		MarkerSymbol symbol;
		if (BILLBOARDS)
			symbol = new MarkerSymbol(bitmap, HotspotPlace.CENTER);
		else
			symbol = new MarkerSymbol(bitmap, 0.5f, 0.5f, false);

		/* another option: use some bitmap drawable */
		Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
		if (BILLBOARDS)
			mFocusMarker = new MarkerSymbol(drawableToBitmap(d),
			                                HotspotPlace.BOTTOM_CENTER);
		else
			mFocusMarker = new MarkerSymbol(drawableToBitmap(d),
			                                0.5f, 0.5f, false);

		ItemizedLayer<MarkerItem> markerLayer =
		        new ItemizedLayer<MarkerItem>(mMap, new ArrayList<MarkerItem>(),
		                                      symbol, this);

		mMap.layers().add(markerLayer);

		List<MarkerItem> pts = new ArrayList<MarkerItem>();

		/*for (double lat = -90; lat <= 90; lat += 5) {
			for (double lon = -180; lon <= 180; lon += 5)
				pts.add(new MarkerItem(lat + "/" + lon, "",
				                       new GeoPoint(lat, lon)));
		}*/
		pts.add(new MarkerItem(45 + "/" + 45, "",
                new GeoPoint(45, 45)));

		markerLayer.addItems(pts);

		mMap.layers().add(new TileGridLayer(mMap));




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

        mMap.setMapPosition(0, 0, 1);
	}

	@Override
	public boolean onItemSingleTapUp(int index, MarkerItem item) {
		if (item.getMarker() == null)
			item.setMarker(mFocusMarker);
		else
			item.setMarker(null);

		Toast toast = Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT);
		toast.show();
		return true;
	}

	@Override
	public boolean onItemLongPress(int index, MarkerItem item) {
		return false;
	}
}
