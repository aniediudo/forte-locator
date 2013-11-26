package com.audax.dev.forte.maps;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.http.client.utils.CloneUtils;

import android.location.Location;

import com.audax.dev.forte.data.Center;
import com.audax.dev.forte.data.ListUtils;
import com.google.android.gms.maps.model.LatLng;

public class LocationUtils {
	
	private static class LocationMap {
		public UUID centerId;
		public float distance;
		public LocationMap(UUID centerId, float distance) {
			super();
			this.centerId = centerId;
			this.distance = distance;
		}
		
	}
	
	private  static class LComparator implements Comparator<LocationMap> {

		@Override
		public int compare(LocationMap lhs, LocationMap rhs) {
			return Float.compare(lhs.distance, rhs.distance);
		}
	}
	
	public static final float MILES_TO_KILO = 1.60934f;
	
	public float milesToKilometers(float miles) {
		return miles * MILES_TO_KILO;
	}
	
	
	/**
	 * Get the center closest to a certain location/point
	 * @param location
	 * @param centers
	 * @return closest center
	 * @throws CloneNotSupportedException 
	 */
	public static Center getClosest(LatLng location, Collection<Center> centers) throws CloneNotSupportedException {
		int len = centers.size();
		if (len == 0) {
			return null;
		}
		LocationMap[] maps = new LocationMap[len];
		int idx = 0;
		for (Center c : centers) {
			float[] results = {0, 0, 0};
			
			Location.distanceBetween(location.latitude, location.longitude,
						c.getPosition().latitude, c.getPosition().longitude, results);
			maps[idx++] = new LocationMap(c.getId(), results[0]);
		}
		
		//Sort to get the shortest
		java.util.Arrays.sort(maps, new LComparator());
		final UUID id = maps[0].centerId;
		Center cent= ListUtils.getFirst(centers, new ListUtils.Matcher<Center>() {

			@Override
			public boolean matches(Center p0) {
				return p0.getId().equals(id);
			}
			
		}).clone();
		cent.setDistanceInMiles(maps[0].distance);
		return cent;
	}
}
