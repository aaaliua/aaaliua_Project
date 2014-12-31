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
import android.widget.TextView;

public class HtmlView extends BaseActionBarActivity{

	private int code;
	@InjectView(R.id.back) View back;
	@InjectView(R.id.htmlHint) WebView tipsWebView;
	@InjectView(R.id.title) TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xieyi);
		ButterKnife.inject(this);
		code = getIntent().getIntExtra("code", 1);
		WebSettings webSettings = tipsWebView.getSettings();
        
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultFontSize(28);
        tipsWebView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果 
        
        if(code == 1){
        	title.setText("协议");
        	tipsWebView.loadUrl("file:///android_asset/html/xieyi.htm");
        }else{
        	title.setText("关于我们");
        	tipsWebView.loadUrl("file:///android_asset/html/about.htm"); 
        }
		
	}
	
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
}
