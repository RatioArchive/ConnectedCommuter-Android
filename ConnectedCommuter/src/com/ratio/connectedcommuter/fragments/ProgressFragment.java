package com.ratio.connectedcommuter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.activities.MainActivity;
import com.ratio.connectedcommuter.activities.MainActivity.AppMode;
import com.ratio.connectedcommuter.adapters.ProgressAdapter;
import com.ratio.connectedcommuter.adapters.ProgressAdapter.ProgressItem;

public class ProgressFragment extends BaseRatioFragment {

	private ListView mListView;
	private ProgressAdapter mAdapter;
	private View mToGoLayout;
	private TextView mPointsBar;
	
	public ProgressFragment() {
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
		final View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
		mListView = (ListView) rootView.findViewById(R.id.listview);
		mToGoLayout = rootView.findViewById(R.id.to_go_layout);
		mPointsBar = (TextView) rootView.findViewById(R.id.points);
		mAdapter = new ProgressAdapter();
		if (mIsEnabled) {
			unlockPointsBar();
			mAdapter.onRewardUnlocked();
		}
		
		// Do init stuff
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position == 0) {
					ProgressItem item = mAdapter.getItem(position);
					if (item.isEnabled()) {
						mActivity.switchAppMode(AppMode.SPONSORED_CONTENT);
					}
				}
			}
		});
		mListView.setAdapter(mAdapter);
		
		return rootView;
	}
	
	private boolean mIsEnabled;
	public void onRewardsUnlocked() {
		mIsEnabled = true;
		// Enable the hero
		if (mAdapter != null) {
			unlockPointsBar();
			mAdapter.onRewardUnlocked();
			mAdapter.notifyDataSetChanged();
		}
	}
	
	private void unlockPointsBar() {
		mToGoLayout.setVisibility(View.GONE);
		
		RelativeLayout.LayoutParams params = (LayoutParams) mPointsBar.getLayoutParams();
		params.topMargin = 0;
		mPointsBar.setText("1000");
	}

}
