package com.aaaliua.user;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aaaliua.adapter.Item_List_adapter;
import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.ItemEntity;
import com.aaaliua.event.Event.Delete;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.itemwork.R;
import com.aaaliua.ui.ItemInfoActivity;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.baoyz.pg.PG;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class MyList extends BaseActionBarActivity implements OnItemClickListener{

	@InjectView(R.id.contentList)
	PullToRefreshGridView mListView;
	@InjectView(R.id.empty)
	View empty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_list);
		ButterKnife.inject(this);
		mListView.setShowIndicator(false);
		mListView.setEmptyView(empty);
		//获取数据
		 // Register
        EventBus.getDefault().register(this);
		getdata();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}
	
	   /** 在主线程接收ItemListEvent事件，必须是public void */
    public void onEventMainThread(Delete event) {
    	if(event != null){
    		getdata();
    	}
    }
	
	
	private void getdata(){
		RequestParams params = new RequestParams();
		params.put("pageNow","1");   
		params.put("uid",LoginActivity.getTokenID());
		
		AppApplication.getHttpClient().post(RequestUrl.MY_LIST,params,	new AsyncHttpResponseHandler() {

			final ProgressDialog progressDialog = new ProgressDialog(
					MyList.this);
			@Override
			public void onStart() {
				// 网络请求开始
				progressDialog.setMessage("正在拉取数据");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			@Deprecated
			public void onSuccess(String content) {
				System.out.println(content);
				parsejson(content);
			}

			@Override
			@Deprecated
			public void onFailure(Throwable error) {
				// 设置list的Emptyview
			}

			@Override
			public void onFinish() {
				if (progressDialog != null
						&& progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}

		});
	}
	
	private List<ItemEntity> items;
	
	private void parsejson(String json){
		items = ParseJson.getItemEntityList(json);
		if(items != null && items.size() > 0)
			mListView.setAdapter(new Item_List_adapter(this, items));
		mListView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ItemEntity en = (ItemEntity)parent.getItemAtPosition(position);
		Intent it = new Intent(this,ItemInfoActivity.class);
		it.putExtra("item", PG.convertParcelable(en));
		startActivity(it);
	}
}
