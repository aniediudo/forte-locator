package com.audax.dev.forte;

import com.audax.dev.forte.web.ApplicationRegistry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainMenuActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_menu);
		// Show the Up button in the action bar.
		
		setupActionBar();
		
		this.findViewById(R.id.btn_fuel_locator).setOnClickListener(this);
		
		this.findViewById(R.id.btn_vehicle_maintenance).setOnClickListener(this);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		//getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.item_list:
			Intent itt = new Intent(this, CenterListActivity.class);
			this.startActivity(itt);
			return true;
		case R.id.item_news:
			itt = new Intent(this, WebViewActivity.class);
			itt.putExtra("application", ApplicationRegistry.APP_NEWS);
			this.startActivity(itt);
			return true;
		case R.id.item_products:
			itt = new Intent(this, ProductListActivity.class);
			this.startActivity(itt);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		
	}

}
