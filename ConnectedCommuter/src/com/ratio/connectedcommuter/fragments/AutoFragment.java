package com.ratio.connectedcommuter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.ratio.common.fragments.BaseRatioFragment;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.activities.MainActivity;
import com.ratio.connectedcommuter.activities.MainActivity.AppMode;
import com.ratio.connectedcommuter.adapters.RiderAdapter;

public class AutoFragment extends BaseRatioFragment implements OnClickListener {

	private HorizontalListView mRiderView;
	private RiderAdapter mRiderAdapter;
	
	private TextView mNumRidersCardViewTv;
	private TextView mNumRidersMyPointsTv;
	private ImageView mMyPointsMeterImage;
	private TextView mMyPointsMultTv;
	private View mTodaysChallengeBtn;
	
	public AutoFragment() {
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
		
		// Inflate root and find Views
		final View rootView = inflater.inflate(R.layout.fragment_auto, container, false);
		View cardView = rootView.findViewById(R.id.cc_cardview);
		mRiderView = (HorizontalListView) rootView.findViewById(R.id.rider_view);
		mNumRidersCardViewTv = (TextView) rootView.findViewById(R.id.subtext1);
		mNumRidersMyPointsTv = (TextView) rootView.findViewById(R.id.num_riders_multiplier);;
		mMyPointsMeterImage =(ImageView) rootView.findViewById(R.id.my_points_meter);
		mMyPointsMultTv = (TextView) rootView.findViewById(R.id.my_points_multiplier);;
		mTodaysChallengeBtn = rootView.findViewById(R.id.todays_challenge_btn);
		
		// Set Rider adapter
		mRiderAdapter = new RiderAdapter();
		mRiderView.setAdapter(mRiderAdapter);
		
		// Do init stuff
		cardView.setOnClickListener(this);
		mTodaysChallengeBtn.setOnClickListener(this);
		
		
		
		return rootView;
	}
	
	public void onRiderAdded() {
		mRiderAdapter.add();
		mRiderAdapter.notifyDataSetChanged();
		
		mNumRidersCardViewTv.setText(mRiderAdapter.getCount() + " riders");
		mNumRidersMyPointsTv.setText(mRiderAdapter.getCount() + " riders");
		mMyPointsMultTv.setText("3x");
	}
	
	public void onRiderRemoved() {
		mRiderAdapter.remove();
		mRiderAdapter.notifyDataSetChanged();
		
		mNumRidersCardViewTv.setText(mRiderAdapter.getCount() + " riders");
		mNumRidersMyPointsTv.setText(mRiderAdapter.getCount() + " riders");
	}
	
	public void incrementPoints() {
		mMyPointsMeterImage.setImageResource(R.drawable.dial_18);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cc_cardview:
			if (mRiderAdapter.getCount() == 3) {
				onRiderAdded();
			} else {
				onRiderRemoved();
			}
			break;
		case R.id.todays_challenge_btn:
			mActivity.switchAppMode(AppMode.REWARD);
		default:
			break;
		}
	}

}
