package com.ratio.connectedcommuter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;

public class BusFragment extends BaseRatioFragment {

	public BusFragment() {
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
