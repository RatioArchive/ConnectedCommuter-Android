package com.ratio.connectedcommuter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.activities.MainActivity;
import com.ratio.connectedcommuter.activities.SponsoredDataVideoActivity;

public class SponsoredContentFragment extends BaseRatioFragment {

	MainActivity rootActivity;
	
	public SponsoredContentFragment() {
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
		final View rootView = inflater.inflate(R.layout.fragment_sponsored_content, container, false);
		rootActivity = (MainActivity) getActivity();
		
		ImageView iv = (ImageView)rootView.findViewById(R.id.ivImage);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(rootActivity, SponsoredDataVideoActivity.class);
				rootActivity.startActivity(intent);
				
			}
		});
		
		return rootView;
	}

}
