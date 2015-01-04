package com.ratio.connectedcommuter.activities;


import android.app.Activity;
import android.os.Bundle;

import com.footmarks.footmarkssdk.FootmarksBase;
import com.footmarks.footmarkssdk.FootmarksBase.InitCallback;
import com.ratio.common.utils.Logger;
import com.ratio.connectedcommuter.R;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String APP_KEY = "PWU3056355OAT";
	private static final String APP_SECRET = "wYIBKLibX3qJbSBk5TtelAJGtDJpp0wU";
	private static final String DEVICE_ID = null;
	private static final String USER_ID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		
		// Init footmarks SDK - a 4.2+ device is required for this!
		initFootmarks();
	}
	
	private void initFootmarks(){

		FootmarksBase.init(getApplicationContext(), APP_KEY, APP_SECRET, DEVICE_ID, USER_ID, new InitCallback() {
			
			@Override
			public void onFinished() {
				Logger.Logd(TAG, "onFinished");
				FootmarksBase.getScanUtility().startBackgroundScan();
			}
			
			@Override
			public void onError(String error) {
				Logger.Logd(TAG, "onError: " + error);
				
			}
		});
	}
}
