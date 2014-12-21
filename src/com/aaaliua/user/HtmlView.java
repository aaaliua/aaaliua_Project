package com.aaaliua.user;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.itemwork.R;
import com.dazhongcun.baseactivity.BaseActionBarActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class HtmlView extends BaseActionBarActivity{

	@InjectView(R.id.back) View back;
	@InjectView(R.id.htmlHint) WebView tipsWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xieyi);
		ButterKnife.inject(this);
		
		WebSettings webSettings = tipsWebView.getSettings();
        
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        
        tipsWebView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果 
        tipsWebView.loadUrl("file:///android_asset/html/tips.htm");
		
	}
	
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
}
