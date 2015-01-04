package com.ratio.connectedcommuter.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratio.connectedcommuter.R;

public class ProgressAdapter extends BaseAdapter {

	private List<ProgressItem> mItems;
	
	public class ProgressItem {
		String title;
		String subtext1;
		String subtext2;
		int imageId;
		public boolean enabled;
		
		private ProgressItem(String title, String subtext1, String subtext2, int imageId, boolean enabled) {
			this.title = title;
			this.subtext1 = subtext1;
			this.subtext2 = subtext2;
			this.imageId = imageId;
			this.enabled = enabled;
		}
		
		public boolean isEnabled() {
			return enabled;
		}
	}
	
	public ProgressAdapter() {
		mItems = new ArrayList<ProgressItem>();
		mItems.add(new ProgressItem("Game of Thrones - Season 5", "Achieve 1,000 points for this week to win this Royal Prize!", null, R.drawable.got_hero_30, false));
		mItems.add(new ProgressItem("Subway - Free Drink", "Friday", "Upcoming", R.drawable.offer_subway, false));
		mItems.add(new ProgressItem("Starbucks - Free Shot", "Thursday", "Upcoming", R.drawable.offer_sbux, false));
		mItems.add(new ProgressItem("Subway - 50% Off", "Wednesday", "Today's Challenge", R.drawable.offer_subway, true));
		mItems.add(new ProgressItem("McDonald's - $2 Off", "Tuesday", "Completed", R.drawable.offer_mcdonalds, true));
		mItems.add(new ProgressItem("Starbucks - Free Drip", "Monday", "Completed", R.drawable.offer_sbux, true));
	}
	
	public void enableItem(int position) {
		getItem(position).enabled = true;
	}
	
	public void onRewardUnlocked() {
		enableItem(0);
		mItems.get(0).subtext1 = "Royal Prize";
		mItems.get(0).subtext2 = "Stream now!";
		
		mItems.get(1).subtext2 = "Completed";
		mItems.get(1).enabled = true;
		
		mItems.get(2).subtext2 = "Did not complete";
		
		mItems.get(3).subtext2 = "Completed";
		
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public ProgressItem getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		if (getItemViewType(position) == VIEW_TYPE_HERO) {
			convertView = inflater.inflate(R.layout.progress_hero_cardview, parent, false);
		} else {
			convertView = inflater.inflate(R.layout.progress_cardview, parent, false);
		}
		
		ProgressItem item = getItem(position);
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView subtext1 = (TextView) convertView.findViewById(R.id.subtext1);
		TextView subtext2 = (TextView) convertView.findViewById(R.id.subtext2);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
		
		title.setText(item.title);
		subtext1.setText(item.subtext1);
		if (item.subtext2 != null) {
			subtext2.setText(item.subtext2);
		} else {
			subtext2.setText("");
		}
		imageView.setImageResource(item.imageId);
		
		// Handle alpha
		if (item.enabled) {
			convertView.setAlpha(1f);
		} else {
			convertView.setAlpha(.5f);
		}
		
		return convertView;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return VIEW_TYPE_HERO;
		} else {
			return VIEW_TYPE_NORMAL;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	private int VIEW_TYPE_HERO = 0;
	private int VIEW_TYPE_NORMAL = 1;

}
