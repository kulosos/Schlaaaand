package de.kularts.android_schlaaand;

import de.kularts.android_schlaaand.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.Button;
import android.hardware.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */


public class FullscreenActivity extends Activity implements SensorEventListener {

	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	public MediaPlayer mp = null;
	public String hello = "Hello!";
	public String goodbye = "GoodBye!";
	public String ohha 	= "Ohha";
	public String nachhause = "nachhause";

	private SensorManager sensorManager;
	
	// DEBUG
	private static final String TAG = FullscreenActivity.class.getName();
	private static final boolean D = true;
	
//	 private SensorManager mSensorManager;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity

	
	// ------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);		

		
		this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;

		
		
		// ------------------------------------------------------------

		final Button buttonHello = (Button) findViewById(R.id.idHello);
		buttonHello.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerOfSound(hello);
			} // END onClick()
		}); // END buttonHello

		
		final Button buttonGoodBye = (Button) findViewById(R.id.idWhat);
		buttonGoodBye.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerOfSound(ohha);
			} // END onClick()
		}); // END buttonGoodBye
		
		final Button buttonGoodBye2 = (Button) findViewById(R.id.idGoodBye);
		buttonGoodBye2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerOfSound(goodbye);
			} // END onClick()
		}); // END buttonGoodBye

		// ------------------------------------------------------------
		
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

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
//		findViewById(R.id.dummy_button).setOnTouchListener(
//				mDelayHideTouchListener);
	}

	// ------------------------------------------------------------------------
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	// ------------------------------------------------------------------------
	
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
			mSystemUiHider.hide();
		}
	};

	// ------------------------------------------------------------------------
	
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	// ------------------------------------------------------------------------
	
    protected void managerOfSound(String theText) {
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        if (theText == hello)
            mp = MediaPlayer.create(this, R.raw.ohha);
        else if (theText == ohha)
            mp = MediaPlayer.create(this, R.raw.uuuuh);
        else if (theText == goodbye)
            mp = MediaPlayer.create(this, R.raw.goalshout);
        else if (theText == nachhause)
            mp = MediaPlayer.create(this, R.raw.wernerrussen);
        mp.start();
    }
    
    // ------------------------------------------------------------------------
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (countSensor != null) {
            this.sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } 

    }
    
    // ------------------------------------------------------------------------
    
    @Override
    protected void onPause() {
    	sensorManager.unregisterListener(mSensorListener);
    	super.onPause();
    }

    // ------------------------------------------------------------------------
    
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		if(D) Log.i(TAG, "mAccel Value: " + mAccel);
		
		if(mAccel > 20){
			
			managerOfSound(nachhause);
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	// ------------------------------------------------------------------------
	
	 private final SensorEventListener mSensorListener = new SensorEventListener() {

		    public void onSensorChanged(SensorEvent se) {
		      float x = se.values[0];
		      float y = se.values[1];
		      float z = se.values[2];
		      mAccelLast = mAccelCurrent;
		      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
		      float delta = mAccelCurrent - mAccelLast;
		      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
		      if(D) Log.i(TAG, "mAccel Value: " + mAccel);
		    }

		    public void onAccuracyChanged(Sensor sensor, int accuracy) {
		    }
		  };
	

}
