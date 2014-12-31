package com.aaaliua.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.ItemEntity;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.ImageOptions;
import com.dazhongcun.utils.ViewHolder;

/**
 * 
 * 
 * @blog 团购适配器
 * 
 * @author lt
 *
 */
public class Item_List_adapter extends BaseAdapter{

	private boolean flag = true;
	
	private List<ItemEntity> lists;
	private LayoutInflater mInflater; 
	private Context mContext;
	public Item_List_adapter(Context context,List<ItemEntity> lists) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemEntity item = lists.get(position);
		if(convertView == null){  
			convertView  = mInflater.inflate(R.layout.item_list_info, null);
		}
		ImageView image = ViewHolder.GetChars(convertView, R.id.imageview_item);
		TextView validate = ViewHolder.GetChars(convertView, R.id.validate);
		TextView title = ViewHolder.GetChars(convertView, R.id.title);
		TextView bcount = ViewHolder.GetChars(convertView, R.id.bcount);
		TextView pcount = ViewHolder.GetChars(convertView, R.id.pcount);
		title.setText(item.getTitle());
		pcount.setText(item.getPraise());
		if ("1".equals(item.getPublish_type())) {
			//借 送 卖
			validate.setText("借");
		}else if("2".equals(item.getPublish_type())){
			validate.setText("送");
		}else if("3".equals(item.getPublish_type())){
			validate.setText("卖");
		}else{
			validate.setVisibility(View.GONE);
		}
		
		
		
		if(TextUtils.isEmpty(item.getImage())){
			AppApplication.getImageLoader().displayImage("", image,ImageOptions.defaultOptions);
		}else{
			String[] images = item.getImage().split("\\|");
			if(images.length > 0 ){
				//此处应做处理 目前为1  发布正式后请改为0
				AppApplication.getImageLoader().displayImage(images[1], image,ImageOptions.defaultOptions);
			}else{
				AppApplication.getImageLoader().displayImage("", image,ImageOptions.defaultOptions);
			}
		}
		return convertView;
	}


}
