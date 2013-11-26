package com.audax.dev.forte;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.audax.dev.forte.data.Product;
public class ProductsAdapter extends ArrayAdapter<Product> {

	public ProductsAdapter(Context context, List<Product> objects) {
		super(context, android.R.layout.simple_list_item_activated_2, objects);
	}

	@Override
	public long getItemId(int position) {
		Product p = this.getItem(position);
		return p.getId().getMostSignificantBits();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((Activity)this.getContext())
						.getLayoutInflater().inflate(
								android.R.layout.simple_list_item_activated_2, parent, false);
		}
		Product p = this.getItem(position);
		((TextView) convertView.findViewById(android.R.id.text1)).setText(p.getTitle());
		((TextView) convertView.findViewById(android.R.id.text2)).setText(p.getDescription());
		return convertView;
	}
	
	
	
}
