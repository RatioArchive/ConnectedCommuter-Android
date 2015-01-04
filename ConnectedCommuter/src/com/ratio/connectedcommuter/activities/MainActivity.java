package com.ratio.connectedcommuter.activities;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.couchbase.lite.Document;
import com.footmarks.footmarkssdk.FMBeacon;
import com.footmarks.footmarkssdk.FootmarksBase;
import com.footmarks.footmarkssdk.FootmarksBase.InitCallback;
import com.footmarks.footmarkssdk.FootmarksSdkBroadcastReceiver;
import com.footmarks.footmarkssdk.model.Experience;
import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.common.interfaces.ActivityInterface;
import com.ratio.common.utils.Logger;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.application.CCApp;
import com.ratio.connectedcommuter.application.Constants;
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
	private static String DB_NAME = "connected_car";

	private Context mContext;

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
		mContext = this;
		// Init footmarks SDK - a 4.2+ device is required for this!
		initFootmarks();
		testDB();
	}
	
	private Integer getTotalPoints() {
		
		Document person = CCApp.getInstance().selectDoc(CCApp.getInstance().getPersonId());
		return (Integer) person.getProperty(Constants.TOTAL_PTS);
		
	}
	
	private List<Integer> getRiders() {
		
		Document pool = CCApp.getInstance().selectDoc(CCApp.getInstance().getPoolId());
		return (List<Integer>) pool.getProperty(Constants.RIDERS);
		
	}
	
	@SuppressLint("ShowToast")
	private void testDB() {
		
		String pts = getTotalPoints().toString();
		Toast.makeText(mContext, pts, Toast.LENGTH_SHORT).show();
		
		List<Integer> riders = getRiders();
		String size = Integer.toString(riders.size());
		Toast.makeText(mContext, size, Toast.LENGTH_SHORT).show();
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
	
	public static class CCFootmarksSdkBroadcastReceiver extends
		FootmarksSdkBroadcastReceiver {
		
		private static final String RECEIVER_TAG = CCFootmarksSdkBroadcastReceiver.class.getSimpleName();
		
		public CCFootmarksSdkBroadcastReceiver() {
		}
		
		@Override
		public void didCompleteExperiences(FMBeacon beacon, List<Experience> experiences) {
			Logger.Logd(RECEIVER_TAG, "didCompleteExperiences: " + beacon);
			Logger.Logd(RECEIVER_TAG, "experiences: " + experiences);
		
		}
		
		@Override
		public void didEnterRegion(FMBeacon beacon) {
			Logger.Logd(RECEIVER_TAG, "didEnterRegion: " + beacon);
			Toast.makeText(CCApp.getInstance(), "didEnterRegion", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void didExitRegion(List<FMBeacon> beacons) {
			Logger.Logd(RECEIVER_TAG, "didExitRegion: " + beacons);
			Toast.makeText(CCApp.getInstance(), "didExitRegion", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void didRangeBeacons(List<FMBeacon> beacons) {
			Logger.Logd(RECEIVER_TAG, "didRangeBeacons: " + beacons);
		}
		
		@Override
		public void onBeaconDiscovered(FMBeacon beacon) {
			Logger.Logd(RECEIVER_TAG, "onBeaconDiscovered: " + beacon);
		}
		
	}
}
