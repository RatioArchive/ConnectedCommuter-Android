package com.ratio.connectedcommuter.receivers;

import java.util.List;

import com.footmarks.footmarkssdk.FMBeacon;
import com.footmarks.footmarkssdk.FootmarksSdkBroadcastReceiver;
import com.footmarks.footmarkssdk.model.Experience;
import com.ratio.common.utils.Logger;

public class CCFootmarksSdkBroadcastReceiver extends
		FootmarksSdkBroadcastReceiver {

	private static final String TAG = CCFootmarksSdkBroadcastReceiver.class.getSimpleName();
	
	public CCFootmarksSdkBroadcastReceiver() {
	}

	@Override
	public void didCompleteExperiences(FMBeacon beacon, List<Experience> experiences) {
		Logger.Logd(TAG, "didCompleteExperiences: " + beacon);
		Logger.Logd(TAG, "experiences: " + experiences);

	}

	@Override
	public void didEnterRegion(FMBeacon beacon) {
		Logger.Logd(TAG, "didEnterRegion: " + beacon);
	}

	@Override
	public void didExitRegion(List<FMBeacon> beacons) {
		Logger.Logd(TAG, "didExitRegion: " + beacons);
	}

	@Override
	public void didRangeBeacons(List<FMBeacon> beacons) {
		Logger.Logd(TAG, "didRangeBeacons: " + beacons);
	}

	@Override
	public void onBeaconDiscovered(FMBeacon beacon) {
		Logger.Logd(TAG, "onBeaconDiscovered: " + beacon);
	}

}
