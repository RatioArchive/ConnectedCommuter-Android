package com.ratio.connectedcommuter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.common.views.FontableTextView;
import com.ratio.connectedcommuter.R;

public class RewardFragment extends BaseRatioFragment {

	public RewardFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate root
		final View rootView = inflater.inflate(R.layout.fragment_reward, container, false);
		
		// Do init stuff
		View cardView = (View)rootView.findViewById(R.id.cc_cardview);
		FontableTextView title = (FontableTextView)cardView.findViewById(R.id.title);
		title.setText(getResources().getString(R.string.redeem));
		FontableTextView subtext = (FontableTextView)cardView.findViewById(R.id.subtext1);
		subtext.setText(getResources().getString(R.string.nearest_location));
		FontableTextView subtext2 = (FontableTextView)cardView.findViewById(R.id.subtext2);
		subtext2.setText(" ");
		//FontableTextView title = (FontableTextView)cardView.findViewById(R.id.title);
		
		return rootView;
	}

}
