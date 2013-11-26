package com.audax.dev.forte.maps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapsClient implements LocationSource {
	private String locationProvider = LocationManager.NETWORK_PROVIDER;
	private Context activityContext;
	private LocationManager locationManager;

	
	
	// 3.951941,9.422836
	// Define a listener that responds to location updates
	final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.
			makeUseOfNewLocation(location);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	public static interface ClientListener {
		void onLocationChanged(MapsClient client);
	}

	private ClientListener clientListener;

	public ClientListener getClientListener() {
		return clientListener;
	}

	public void setClientListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}

	public MapsClient(Context activityContext) {
		super();
		this.activityContext = activityContext;
	}

	protected void makeUseOfNewLocation(Location location) {
		this.currentLocation = location;
		if (this.clientListener != null) {
			this.clientListener.onLocationChanged(this);
		}
	}

	private Location currentLocation;

	public Location getCurrentLocation() {
		return currentLocation;
	}

	private boolean listening = false;

	public void start() {
		if (locationManager == null) {
			locationManager = (LocationManager) this.activityContext
					.getSystemService(Context.LOCATION_SERVICE);
		}
		if (!listening) {
			this.listening = true;
			locationManager.requestLocationUpdates(this.locationProvider, 0, 0,
					locationListener);
		}
		this.determineBestLastKnowLocation();
	}

	private void determineBestLastKnowLocation() {
		Location location = null;
		if (this.locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			location = this.locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else {
			location = this.locationManager
					.getLastKnownLocation(locationProvider);
		}
		if (location != null) {
			if (location.hasAccuracy()) {
				float acc = location.getAccuracy();
				if (acc <= 68) {
					this.makeUseOfNewLocation(location);
				}
			}else {
				this.makeUseOfNewLocation(location);
			}
			
		}
	}

	public void stop() {
		if (this.listening) {
			this.listening = false;
			this.locationManager.removeUpdates(locationListener);
		}
	}

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	@Override
	public void activate(final OnLocationChangedListener arg0) {
		this.clientListener = new ClientListener() {

			@Override
			public void onLocationChanged(MapsClient client) {
				arg0.onLocationChanged(client.getCurrentLocation());
			}
		};
		this.start();
	}

	@Override
	public void deactivate() {
		this.stop();
	}
}
