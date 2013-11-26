package com.audax.dev.forte;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.audax.dev.forte.util.SystemUiHider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.audax.dev.forte.maps.MapsClient;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class AssetLocator extends FragmentActivity implements MapsClient.ClientListener, View.OnClickListener {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private MapsClient mapsClient;
	
	//A background task to simulate 'busy'
//	private static class DummyProgressTask extends AsyncTask<Integer, Integer, Void> {
//		private Activity context;
//		private ProgressBar progressBar;
//		private Runnable onComplete;
//		
//		public DummyProgressTask(Activity context, Runnable onComplete) {
//			super();
//			this.context = context;
//			this.onComplete = onComplete;
//			this.progressBar = (ProgressBar) context.findViewById(R.id.progressBar1);
//		}
//		
//		@Override
//		protected Void doInBackground(Integer... params) {
//			int cu = 0;
//			while (cu < params[0]) {
//				this.publishProgress(cu);
//				cu += (Math.floor(Math.random() * 5));
//				try {
//					Thread.currentThread().sleep((int)(Math.floor(Math.random() * 100) % 10) + 5);
//				} catch (InterruptedException e) {
//					break;
//				}
//			}
//			this.publishProgress(params[0]);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			this.context.findViewById(R.id.btn_home).setVisibility(View.VISIBLE);
//			this.onComplete.run();	
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			// TODO Auto-generated method stub
//			super.onProgressUpdate(values);
//			for (Integer i : values) {
//				progressBar.setProgress(i);
//			}
//			
//		}
//	}
//	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.activity_forte_mobile);
		setContentView(R.layout.main);
//		WebView webV = (WebView)this.findViewById(R.id.web_view);
//		webV.loadUrl("file:///android_asset/landing.html");
//		
		//Simulate Progress
		final ProgressBar pb = (ProgressBar)this.findViewById(R.id.progressBar1);
		
		final View homeButton = findViewById(R.id.btn_home);
		
		
		
		homeButton.setOnClickListener(this);
		
		//Simulate 'loading'
		
		ValueAnimator anim = ValueAnimator.ofInt(0, pb.getMax());
		
		anim.setDuration(2000);
		
		anim.setInterpolator(new AnticipateInterpolator());
		
		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				homeButton.setVisibility(View.VISIBLE);
				showNearestCenter();
			}
		});
		
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				pb.setProgress((Integer)animation.getAnimatedValue());
			}});
		
		anim.start();
		
		//mapsClient = new MapsClient(this);
		
		//mapsClient.setClientListener(this);

		/*final View controlsView = findViewById(R.id.parent);
		final View contentView = findViewById(R.id.container);
		// final View controlsView =
		// findViewById(R.id.fullscreen_content_controls);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
*/
		// Set up the user interaction to manually show or hide the system UI.
		/*contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
*/
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		/*
		 * findViewById(R.id.dummy_button).setOnTouchListener(
		 * mDelayHideTouchListener);
		 */
	}

	protected void showNearestCenter() {
		View v = this.findViewById(R.id.lay_nearest);
		v.setVisibility(View.VISIBLE);
	}

	protected void switchToMenuActivity() {
		Intent itt = new Intent(this, MainMenuActivity.class);
		this.startActivity(itt);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		//delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			// mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		/*int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (code == ConnectionResult.SUCCESS) {
			Log.i("Google Play", "Service is available");
			
			this.mapsClient.start();
		} else {
			 GooglePlayServicesUtil.getErrorDialog(code, this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);
			 Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
	                    code,
	                    this,
	                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

	            // If Google Play services can provide an error dialog
	            if (errorDialog != null) {
	                // Create a new DialogFragment for the error dialog
	                ErrorDialogFragment errorFragment =
	                        new ErrorDialogFragment();
	                // Set the dialog in the DialogFragment
	                errorFragment.setDialog(errorDialog);
	                // Show the error dialog in the DialogFragment
	                
	                errorFragment.show(getSupportFragmentManager(),
	                        "Location Updates");
	            }
		}*/
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			Intent itt = new Intent(this, MainMenuActivity.class);
			this.startActivity(itt);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	public void showMaps(View v) {
		Intent mapIntent = new Intent(this, MapActivity.class);
		this.startActivity(mapIntent);
	}
	
	private Menu _menu;
	
	private void showMenu(boolean show) {
		if (_menu != null) {
			int count = _menu.size();
			for (int k = 0; k < count; k++) {
				_menu.getItem(k).setVisible(show);
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;
		//Initially hide. Show when progress is complete
		showMenu(false);
		return true;
	}


	private boolean firstTime = true;
	@Override
	public void onLocationChanged(MapsClient client) {
		//Location location = client.getCurrentLocation();
		if (firstTime) {
			firstTime = false;
			this.findViewById(R.id.btn_goto_map).setVisibility(View.VISIBLE);
			this.findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}
		TextView status = (TextView)this.findViewById(R.id.next_station_label);
		status.setText(String.format(this.getString(R.string.next_station), this.getString(R.string.demo_loc), "124Km"));
		client.stop();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mapsClient != null) {
			mapsClient.stop();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_home:
			switchToMenuActivity();
			break;
		}
	}


	
	
}
