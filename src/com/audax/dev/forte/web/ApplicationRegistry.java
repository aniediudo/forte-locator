package com.audax.dev.forte.web;

import java.util.HashMap;
import java.util.Map;

public class ApplicationRegistry {
	private Map<Integer, Class<?>> applications;

	public static final Integer APP_NEWS = 100;
	
	
	private static ApplicationRegistry app;
	private ApplicationRegistry() {
		this.applications = new HashMap<Integer, Class<?>>(4);
		this.applications.put(APP_NEWS, NewsApplication.class);
	}
	
	public static ApplicationRegistry getInstance() {
		if (app == null) {
			app = new ApplicationRegistry();
		}
		return app;
	}
	
	public WebApplication get(Integer code) throws InstantiationException, IllegalAccessException {
		Class<?> obj = this.applications.get(code);
		return (WebApplication) obj.newInstance();
	}
	
}
