package com.ratio.connectedcommuter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.activities.MainActivity;
import com.ratio.connectedcommuter.activities.MainActivity.AppMode;
import com.ratio.connectedcommuter.adapters.ProgressAdapter;
import com.ratio.connectedcommuter.adapters.ProgressAdapter.ProgressItem;

public class ProgressFragment extends BaseRatioFragment {

	private ListView mListView;
	private ProgressAdapter mAdapter;
	
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
		mAdapter = new ProgressAdapter();
		
		// Do init stuff
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position == 0) {
					ProgressItem item = mAdapter.getItem(position);
					mAdapter.enableItem(0); // TODO: DON'T DO THIS
					if (item.isEnabled()) {
						mActivity.switchAppMode(AppMode.SPONSORED_CONTENT);
					}
				}
			}
		});
		mListView.setAdapter(mAdapter);
		
		return rootView;
	}
	
	

}
