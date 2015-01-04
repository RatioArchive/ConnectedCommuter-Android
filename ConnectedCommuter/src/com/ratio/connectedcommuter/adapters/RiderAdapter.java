package com.ratio.connectedcommuter.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ratio.connectedcommuter.R;

public class RiderAdapter extends BaseAdapter {

	private List<Integer> mRiderDrawables;
	
	public RiderAdapter() {
		mRiderDrawables = new ArrayList<Integer>();
		mRiderDrawables.add(R.drawable.avatars_14);
		mRiderDrawables.add(R.drawable.avatars_15);
		mRiderDrawables.add(R.drawable.avatars_16);
		mRiderDrawables.add(R.drawable.avatars_17);
	}

	@Override
	public int getCount() {
		return mRiderDrawables.size();
	}

	@Override
	public Integer getItem(int position) {
		return mRiderDrawables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.rider_item, parent, false);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
		imageView.setImageResource(getItem(position));
		return convertView;
	}

}
