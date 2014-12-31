package com.aaaliua.fragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

import com.aaaliua.adapter.Item_List_adapter;
import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.ItemEntity;
import com.aaaliua.itemwork.R;
import com.aaaliua.ui.ItemInfoActivity;
import com.aaaliua.user.LoginActivity;
import com.aaaliua.user.SelectSchoolActivity;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.baoyz.pg.PG;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ContentFragment extends Fragment implements OnItemClickListener,OnRefreshListener2 {

	@InjectView(R.id.contentList)
	PullToRefreshGridView mListView;
	@InjectView(R.id.empty)
	View empty;
	private int position;
	public static Fragment newInstance(int index){
		ContentFragment fm = new ContentFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("index", index);
		fm.setArguments(bundle);
		return fm;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		position = getArguments().getInt("index",0);
		View view = inflater.inflate(R.layout.fragment_content, container, false);
		ButterKnife.inject(this,view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mListView.setShowIndicator(false);
		mListView.setEmptyView(empty);
		mListView.setOnRefreshListener(this);
		//获取数据
		getdata();
	}
	
	private void getdata(){
		RequestParams params = new RequestParams();
		params.put("pageNow","1");   
		params.put("type",position+"");
		if("".equals(LoginActivity.getTokenID())){
			//用户没有登录接口
			params.put("sh_id","0");
		}else{
			//用户登陆的接口
			params.put("sh_id","".equals(LoginActivity.getUser(LoginActivity.getTokenID()).getSchoo())?"0":LoginActivity.getUser(LoginActivity.getTokenID()).getSchoo());
		}
		 
		
		AppApplication.getHttpClient().post(RequestUrl.GET_DATA,params,	new AsyncHttpResponseHandler() {

			final ProgressDialog progressDialog = new ProgressDialog(
					getActivity());
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
			mListView.setAdapter(new Item_List_adapter(getActivity(), items));
		mListView.setOnItemClickListener(this);
		
	}
	
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ItemEntity en = (ItemEntity)parent.getItemAtPosition(position);
		Intent it = new Intent(getActivity(),ItemInfoActivity.class);
		it.putExtra("item", PG.convertParcelable(en));
		getActivity().startActivity(it);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		getdata();
		refreshView.onRefreshComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		
	}
	
}
