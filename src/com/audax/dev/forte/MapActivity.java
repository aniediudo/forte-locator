package com.audax.dev.forte;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.audax.dev.forte.data.Center;
import com.audax.dev.forte.data.ListUtils;
import com.audax.dev.forte.data.Repository;
import com.audax.dev.forte.maps.MapsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity{

	private SupportMapFragment mapFragment;
	private GoogleMap googleMap;
	private MapsClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.map_layout);
		
		client = new MapsClient(this);
		
		initLocations();
	}
	
	

	private Repository store;
	
	private static class CenterMarkerMap {
		public Marker marker;
		public UUID centerId;
		public CenterMarkerMap() {
		}
		public CenterMarkerMap(Marker marker, UUID centerId) {
			this.marker = marker;
			this.centerId = centerId;
		}
		
	}
	
	private ArrayList<CenterMarkerMap> centerMarkers = new ArrayList<MapActivity.CenterMarkerMap>();
	
	private class LocationsLoadTask extends AsyncTask<String, Center, Void> {

		@Override
		protected Void doInBackground(String... providers) {
			for (Center l : store.getAvailableCenters()) {
				this.publishProgress(l);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Center... values) {
			for (Center loc : values) {
				MarkerOptions mo = new MarkerOptions();
				
				mo.position(loc.getPosition());
				
				mo.title(loc.getName());
				
				mo.icon(BitmapDescriptorFactory.fromResource(loc.resolveIconResource()));
				
				mo.snippet(loc.getLocation());
				
				Marker marker = googleMap.addMarker(mo);
				
				centerMarkers.add(new CenterMarkerMap(marker, loc.getId()));
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			captureSentCenter();
		}
		
		
	}
	
	
	private void initLocations() {	
		if (store == null) {
			store = new Repository();
		}
		if (googleMap == null) {
			
		    if (mapFragment == null) {
		    	mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment);
		    }
		    /*if (mapFragment != null) {
				Log.i("Fragment", "Google Maps fragment found");
			}else {
				Log.e("Fragment", "Google Maps fragment not found");
			}
			*/
		    
			googleMap = mapFragment.getMap();
			
		    if (googleMap != null) {
		    	
		    	googleMap.setLocationSource(client);
		    	
		    	googleMap.setMyLocationEnabled(true);
		    	
		    	googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
					
					@Override
					public void onCameraChange(CameraPosition arg0) {
						//Location l = client.getCurrentLocation();
						//Log.w("Location", "Could not find current");
						
						googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Repository.PT_LAGOS, 10));
						
						googleMap.setOnCameraChangeListener(null);
					}
				});
		    }
		}
		if (googleMap != null) {
			
			//Mark current location
			LocationsLoadTask task = new LocationsLoadTask();
			task.execute(LocationManager.NETWORK_PROVIDER);
		}
	}


	//Look for center ids that was passed to activity via intent
	public void captureSentCenter() {
		Intent itt = this.getIntent();
		if (itt != null && itt.getExtras().containsKey("centerId")) {
			
			final UUID id = UUID.fromString(itt.getExtras().getString("centerId"));
			
			CenterMarkerMap map = ListUtils.getFirst(centerMarkers, new ListUtils.Matcher<CenterMarkerMap>() {

				@Override
				public boolean matches(CenterMarkerMap p0) {
					int co = p0.centerId.compareTo(id);
					
					return co == 0;
				}
			});
			
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(map.marker.getPosition(), 9));
			
			map.marker.showInfoWindow();
			
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.client.stop();
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.client.stop();
	}
	
	

}
