/**
 * 
 */
package de.mrunde.bachelorthesis.basics;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.MapView;

/**
 * @author Marius Runde
 */
public class MyDefaultItemizedOverlay extends DefaultItemizedOverlay {

	public MyDefaultItemizedOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    return super.draw(canvas, mapView, false, when);
	}
}
