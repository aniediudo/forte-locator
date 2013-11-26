package com.audax.dev.forte.web;

import android.webkit.WebView;

public class NewsApplication extends WebApplication {
	
	public NewsApplication() {
		super();
		this.setTitle("News / Highlights");
	}

	@Override
	public WebApplication initializeWebView(WebView view) {
		view.loadUrl("file:///android_asset/news.html");
		return this;
	}

}
