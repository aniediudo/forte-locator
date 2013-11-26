package com.audax.dev.forte.data;

import java.util.UUID;

public class Product {
	private UUID id;
	private String title, description, url;
	private int iconResource;
	public Product(UUID id, String title, String description, String url,
			int iconResource) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.url = url;
		this.iconResource = iconResource;
	}
	public Product(UUID id, String title, String description, String url) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.url = url;
	}
	public Product(UUID id, String title, String url) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIconResource() {
		return iconResource;
	}
	public void setIconResource(int iconResource) {
		this.iconResource = iconResource;
	}
	public UUID getId() {
		return id;
	}
	
}
