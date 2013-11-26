package com.audax.dev.forte.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Repository {
	
	
	public static LatLng toLatLng(String position) {
		String[] parts = position.split("[\\,x]");
		return new LatLng(Double.parseDouble(parts[0].trim()),
				Double.parseDouble(parts[1].trim()));
	}
	
	
	
	
	private static final String[][] CENTERS = {
		{"Forte Oil - Eleko", "Filling Station", "1.4mi", "6:00AM - 10:00PM", "6.483161,3.596596"},
		{"Forte Oil - Alasia", "Service Station", "0.6mi", "6:00AM - 10:00PM", "6.495782,3.573936"},
		{"Forte Oil - Ago Iwoye", "Filling Station", "12mi", "6:00AM - 10:00PM", "6.812751,3.910776"},
		{"Forte Oil - Wharf Road", "Service Station", "11mi", "6:00AM - 10:00PM", "6.51045,3.360229"},
		
	};
	//Use static so that it will be available static-wide
	private static List<Center> demoCenters;
	
	public Collection<Center> getAvailableCenters() {
		if (demoCenters == null) {
			demoCenters = new ArrayList<Center>();
			for (String[] l : CENTERS) {
				Center c = new Center(UUID.randomUUID());
				c.setName(l[0]);
				c.setCategory(l[1]);
				c.setDistance(l[2]);
				c.setAvailability(l[3]);
				c.setPosition(toLatLng(l[4]));
				demoCenters.add(c);	
			}
		}
		return demoCenters;
		//return new ArrayList<Center>(2);
	}

	

	public static final LatLngBounds NIGERIA
		= new LatLngBounds(toLatLng("4.28068,6.654282"), toLatLng("13.902076,7.752914"));
	
	public static final LatLngBounds LAGOS
	= new LatLngBounds(toLatLng("6.380812,3.885727"), toLatLng("6.446318,4.544907"));
	
	public static final LatLng PT_LAGOS = toLatLng("6.47088,3.41104");
	
	private static final ArrayList<Product> PRODUCTS = new ArrayList<Product>();
	
	static {
		PRODUCTS.add(new Product(UUID.randomUUID(), "Aviation Fuel & Lubricants", "file:///android_asset/products/aviation_and_fuel_lubricants.html"));
	}
	
	public List<Product> getProducts() {
		return PRODUCTS;
	}
	
	public Product getProduct(final UUID id) {
		return ListUtils.getFirst(getProducts(), new ListUtils.Matcher<Product>() {

			@Override
			public boolean matches(Product p0) {
				return p0.getId().equals(id);
			}
		});
	}
	
}
