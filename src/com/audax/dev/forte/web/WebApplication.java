package com.audax.dev.forte.web;

import android.webkit.WebView;

public abstract class WebApplication {
	private String title;

	public String getTitle() {
		return title;
	}

	public WebApplication setTitle(String title) {
		this.title = title;
		return this;
	}
	
	private Object argument;
	
	public int getTitleResource() {
		return -1;
	}
	
	public Object getArgument() {
		return argument;
	}

	public WebApplication setArgument(Object argument) {
		this.argument = argument;
		return this;
	}

	public abstract WebApplication initializeWebView(WebView view);
}
