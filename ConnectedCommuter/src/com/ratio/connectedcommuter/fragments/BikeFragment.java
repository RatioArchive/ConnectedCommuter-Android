package com.ratio.connectedcommuter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.activities.MainActivity;

public class BikeFragment extends BaseRatioFragment {

	public BikeFragment() {
	}
	
	private MainActivity mActivity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate root
		final View rootView = inflater.inflate(R.layout.fragment_auto, container, false);
		
		// Do init stuff
		
		
		return rootView;
	}

}
