package com.ratio.connectedcommuter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.adapters.RiderAdapter;

public class AutoFragment extends BaseRatioFragment {

	private HorizontalListView mRiderView;
	private RiderAdapter mRiderAdapter;
	
	public AutoFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate root and find Views
		final View rootView = inflater.inflate(R.layout.fragment_auto, container, false);
		mRiderView = (HorizontalListView) rootView.findViewById(R.id.rider_view);
		
		// Set Rider adapter
		mRiderAdapter = new RiderAdapter();
		mRiderView.setAdapter(mRiderAdapter);
		
		// Do init stuff
		
		
		return rootView;
	}

}
