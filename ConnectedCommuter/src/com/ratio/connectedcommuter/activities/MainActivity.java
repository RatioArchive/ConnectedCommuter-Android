package com.ratio.connectedcommuter.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
	private View mQRModal;
	private View mPointsModal;
	private View mQRDone;
	private View mPointsDone;
	
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

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		ActionBar ab = getActionBar();
		ab.setIcon(getResources().getDrawable(R.drawable.hamburger_icon_31));
		ab.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));
		
		// Init fragments
		mAutoFragment = new AutoFragment();
		mBikeFragment = new BikeFragment();
		mBusFragment = new BusFragment();
		mProgressFragment = new ProgressFragment();
		mMapFragment = new MapFragment();
		mRewardFragment = new RewardFragment();
		mSponsoredContentFragment = new SponsoredContentFragment();
		
		mQRModal = findViewById(R.id.qr_code_modal);
		mPointsModal = findViewById(R.id.points_modal);
		mQRDone = findViewById(R.id.qr_done);
		mPointsDone = findViewById(R.id.points_done);
		
		// Setup ActionBar
		initActionBar();
		mContext = this;
		
		switchAppMode(AppMode.AUTO);
		
		// Init footmarks SDK - a 4.2+ device is required for this!
		initFootmarks();
		
		// Setup db
		testDB();
		
		mQRDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				hideQRModal();
				showPointsModal();
			}

		});
		
		mPointsDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				hidePointsModal();
			}

		});
	}
	
	public void showQRModal(){
		mQRModal.setVisibility(View.VISIBLE);
	}
	
	public void hideQRModal(){
		mQRModal.setVisibility(View.GONE);
	}
	
	public void showPointsModal(){
		mPointsModal.setVisibility(View.VISIBLE);
	}
	
	public void hidePointsModal(){
		mPointsModal.setVisibility(View.GONE);
	}
	
	private Integer getTotalPoints() {
		return 694;
//		Document person = CCApp.getInstance().selectDoc(CCApp.getInstance().getPersonId());
//		return (Integer) person.getProperty(Constants.TOTAL_PTS);
		
	}
	
	@SuppressWarnings("unchecked")
	private List<Integer> getRiders() {
		
		List<Integer> riders = new ArrayList<Integer>();
		riders.add(R.drawable.avatars_14);
		riders.add(R.drawable.avatars_15);
		riders.add(R.drawable.avatars_16);
		return riders;
//		Document pool = CCApp.getInstance().selectDoc(CCApp.getInstance().getPoolId());
//		return (List<Integer>) pool.getProperty(Constants.RIDERS);
		
	}
	
	private void testDB() {
		
//		String pts = getTotalPoints().toString();
//		Toast.makeText(mContext, pts, Toast.LENGTH_SHORT).show();
//		
//		List<Integer> riders = getRiders();
//		String size = Integer.toString(riders.size());
//		Toast.makeText(mContext, size, Toast.LENGTH_SHORT).show();
		
	}
	
	public void onRewardsUnlocked(){
		
	}
	
	public void switchAppMode(AppMode appMode) {
		
		// Ignore if already in this mode
		if (mAppMode == appMode) {
			return;
		}
		
		mAppMode = appMode;
		
		if (mCurrentFragment != null) {
			hideFragment(mCurrentFragment);
		}
		
		switch(appMode) {
			case AUTO:
				showFragment(mAutoFragment);
				showTabs();
				break;
			case BIKE:
				showFragment(mBikeFragment);
				showTabs();
				break;
			case BUS:
				showFragment(mBusFragment);
				showTabs();
				break;
			case PROGRESS:
				showFragment(mProgressFragment);
				hideTabs();
				break;
			case MAP:
				showFragment(mMapFragment);
				hideTabs();
				break;
			case REWARD:
				showFragment(mRewardFragment);
				hideTabs();
				break;
			case SPONSORED_CONTENT:
				showFragment(mSponsoredContentFragment);
				hideTabs();
				break;
		}
	}
	
	@Override
	public void onBackPressed() {
		switch (mAppMode) {
			case AUTO:
				super.onBackPressed();
				break;
			case BIKE:
				switchAppMode(AppMode.AUTO);
				break;
			case BUS:
				switchAppMode(AppMode.AUTO);
				break;
			case PROGRESS:
				switchAppMode(AppMode.AUTO);
				break;
			case MAP:
				switchAppMode(AppMode.AUTO);
				break;
			case REWARD:
				switchAppMode(AppMode.AUTO);
				break;
			case SPONSORED_CONTENT:
				switchAppMode(AppMode.AUTO);
				break;
		}
	}
	
	private void initActionBar() {
		
		final ActionBar actionBar = getActionBar();
		
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
						
						break;
						
					case 2:
						
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
    
    private void showTabs() {
    	getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
    
    private void hideTabs() {
    	getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
