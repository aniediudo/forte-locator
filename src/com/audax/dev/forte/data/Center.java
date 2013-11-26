package com.audax.dev.forte.data;

import java.util.UUID;

import com.google.android.gms.maps.model.LatLng;

public class Center implements Cloneable {
	private String name, category, distance, location, availability;
	private UUID id;
	private float distanceInMiles;
	public Center(UUID id, String name, String category, String distance, String location) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.distance = distance;
		this.location = location;
	}
	
	

	@Override
	public Center clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Center)super.clone();
	}



	private int iconResource = -1;
	
	private LatLng position;

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public int getIconResource() {
		return iconResource;
	}
	
	

	public float getDistanceInMiles() {
		return distanceInMiles;
	}

	public void setDistanceInMiles(float distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}



	public void setIconResource(int iconResource) {
		this.iconResource = iconResource;
	}

	public Center(UUID id) {
		this.id = id;
	}	

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public UUID getId() {
		return id;
	}
	
	public int resolveIconResource() {
		if (this.getIconResource() == -1) {
			return this.getDefaultIconResource();
		}
		return this.getIconResource();
	}
	
	public int getDefaultIconResource() {
		//TODO: Use the category to determine icon resource
		return com.audax.dev.forte.R.drawable.service_point;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
