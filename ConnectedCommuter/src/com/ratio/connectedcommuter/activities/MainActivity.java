package com.ratio.connectedcommuter.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.footmarks.footmarkssdk.FootmarksBase;
import com.footmarks.footmarkssdk.FootmarksBase.InitCallback;
import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.common.interfaces.ActivityInterface;
import com.ratio.common.utils.Logger;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.fragments.AutoFragment;
import com.ratio.connectedcommuter.fragments.BikeFragment;
import com.ratio.connectedcommuter.fragments.BusFragment;
import com.ratio.connectedcommuter.fragments.MapFragment;
import com.ratio.connectedcommuter.fragments.ProgressFragment;
import com.ratio.connectedcommuter.fragments.RewardFragment;
import com.ratio.connectedcommuter.fragments.SponsoredContentFragment;

public class MainActivity extends Activity implements ActivityInterface {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String APP_KEY = "PWU3056355OAT";
	private static final String APP_SECRET = "wYIBKLibX3qJbSBk5TtelAJGtDJpp0wU";
	private static final String DEVICE_ID = null;
	private static final String USER_ID = null;
	
	private BaseRatioFragment mCurrentFragment;
	private AutoFragment mAutoFragment;
	private BikeFragment mBikeFragment;
	private BusFragment mBusFragment;
	private ProgressFragment mProgressFragment;
	private MapFragment mMapFragment;
	private RewardFragment mRewardFragment;
	private SponsoredContentFragment mSponsoredContentFragment;
	
	private AppMode mAppMode;
	
	public enum AppMode {
		AUTO,
		BIKE,
		BUS,
		PROGRESS,
		MAP,
		SPONSORED_CONTENT,
		REWARD
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Init fragments
		mAutoFragment = new AutoFragment();
		mBikeFragment = new BikeFragment();
		mBusFragment = new BusFragment();
		mProgressFragment = new ProgressFragment();
		mMapFragment = new MapFragment();
		mRewardFragment = new RewardFragment();
		mSponsoredContentFragment = new SponsoredContentFragment();
		
		// Setup ActionBar
		initActionBar();
				
		// Init footmarks SDK - a 4.2+ device is required for this!
		initFootmarks();
	}
	
	private void switchAppMode(AppMode appMode) {
		mAppMode = appMode;
		
		if (mCurrentFragment != null) {
			hideFragment(mCurrentFragment);
		}
		
		switch(appMode) {
			case AUTO:
				showFragment(mAutoFragment);
				break;
			case BIKE:
				showFragment(mBikeFragment);
				break;
			case BUS:
				showFragment(mBusFragment);
				break;
			case PROGRESS:
				showFragment(mProgressFragment);
				break;
			case MAP:
				showFragment(mMapFragment);
				break;
			case REWARD:
				showFragment(mRewardFragment);
				break;
			case SPONSORED_CONTENT:
				showFragment(mSponsoredContentFragment);
				break;
		}
	}
	
	private void initActionBar() {
		
		final ActionBar actionBar = getActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // show the given tab
	        	Logger.Logd(TAG, "onTabSelected: " + tab.getText());
	        	
	        	switch (tab.getPosition()) {
					case 0:
						switchAppMode(AppMode.AUTO);
						break;
	
					case 1:
						switchAppMode(AppMode.SPONSORED_CONTENT);
						break;
						
					case 2:
						switchAppMode(AppMode.REWARD);
						break;
				}
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        	Logger.Logd(TAG, "onTabUnselected: " + tab.getText());
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        }
	    };

	    // Add 3 tabs, specifying the tab's text and TabListener
	    actionBar.addTab(actionBar.newTab().setText("AUTO").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("BIKE").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("BUS").setTabListener(tabListener));
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
	
    private void showFragment(final BaseRatioFragment fragment) {
    	mCurrentFragment = fragment;
        if (!fragment.visible()) {
            if (fragment.added()) {
                fragment.onShow(this);
            } else {
                fragment.onAdd(this, fragment.getFragmentTag());
            }
        }
    }

    private void hideFragment(final BaseRatioFragment fragment) {
        if (fragment.visible()) {
            fragment.onHide(this);
        }
    }

	@Override
	public int getContainerViewId(String fragmentTag) {
		return R.id.fragment_container;
	}

	@Override
	public boolean isLandscape() {
		return false;
	}
}
