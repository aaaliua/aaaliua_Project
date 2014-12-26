package com.aaaliua.adapter;

import java.util.List;

import com.aaaliua.entity.ItemType;
import com.aaaliua.itemwork.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SimpleSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

	private Context context;
	private LayoutInflater mInflator;
	private List<ItemType> items;
	private TextView text;
	boolean selected = false;
	
	
	public SimpleSpinnerAdapter(Context context,List<ItemType> items) {
		this.context = context;
		mInflator = LayoutInflater.from(context);
		this.items = items;
	}
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.simple_spinner, null);
		}
		text = (TextView) convertView.findViewById(android.R.id.text1);
		if (!selected) {
			text.setText("请选择");
		} else {
			text.setText(items.get(position).getName());
		}
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflator.inflate(
					android.R.layout.simple_spinner_dropdown_item, null);
		}
		text = (TextView) convertView.findViewById(android.R.id.text1);
		text.setText(items.get(position).getName());
		return convertView;
	}
	
	

}
