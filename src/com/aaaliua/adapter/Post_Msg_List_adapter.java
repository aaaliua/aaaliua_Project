package com.aaaliua.adapter;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.PostUser;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.ImageOptions;
import com.dazhongcun.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 
 * 
 * @blog评论列表适配器
 * 
 * @author lt
 * 
 */
public class Post_Msg_List_adapter extends BaseAdapter {

	private boolean flag = true;

	private List<PostUser> lists;
	private LayoutInflater mInflater;
	private Context mContext;

	public Post_Msg_List_adapter(Context context, List<PostUser> lists,boolean flag) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.lists = lists;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		if (lists.size() >= 3 && flag) {
			return 3;
		} else {

			return lists.size();
		}
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
		PostUser user = lists.get(position);
		if (convertView == null) { // convertView =
									// mInflater.inflate(R.layout.order_list_menu_item,
									// null);
			convertView = mInflater.inflate(R.layout.book_post_item, null);
		}
		TextView name = ViewHolder.GetChars(convertView, R.id.name);
		TextView date = ViewHolder.GetChars(convertView, R.id.date);
		TextView content = ViewHolder.GetChars(convertView, R.id.content);
		ImageView icon = ViewHolder.GetChars(convertView, R.id.icon);

		date.setText(user.getAddtime());
		name.setText(user.getNickname());
		content.setText(user.getContent());
		AppApplication.getImageLoader().displayImage(user.getIcon(), icon,ImageOptions.icon_Options);
		return convertView;
	}

	
	//时间格式化
	private String getTime(String user_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		long re_time = 0 ;
		Date d;
		String s = null;
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			s = sm.format(d).toString();
//			String str = String.valueOf(l);
//			String timestr = str.substring(0, 10);
//			re_time = Long.valueOf(timestr);
			re_time = l;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
