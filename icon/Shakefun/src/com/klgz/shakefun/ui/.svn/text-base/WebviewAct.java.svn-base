/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.klgz.shakefun.tools.GetFirstJsonUtil;
import com.klgz.shakefun.tools.GetFirstJsonUtil.IJsonZhiboback;
import com.klgz.ylyq.R;

/**
 * @author wk
 *播放网页视频
 */
public class WebviewAct extends BasicActivity implements IJsonZhiboback{
	
	private FrameLayout frameLayout = null;
	private WebView webView = null;
	private WebChromeClient chromeClient = null;
	private View myView = null;
	private WebChromeClient.CustomViewCallback myCallBack = null;
	
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.act_webview);
		GetFirstJsonUtil.getDataZhiboJson(WebviewAct.this, WebviewAct.this);
		
		frameLayout = (FrameLayout)findViewById(R.id.framelayout);
		webView = (WebView)findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new MyWebviewCient());
		
		chromeClient = new MyChromeClient();
		
		webView.setWebChromeClient(chromeClient);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);	
		
		final String USER_AGENT_STRING = webView.getSettings().getUserAgentString() + " Rong/2.0";
		webView.getSettings().setUserAgentString( USER_AGENT_STRING );
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setPluginState(WebSettings.PluginState.ON);
		webView.getSettings().setLoadWithOverviewMode(true);
		
//		webView.loadUrl("file:///android_asset/index.html");
//		webView.loadUrl("http://view.l.bokecc.com/api/view/index?viewid=27A2B785FA5F5D4A");
//		webView.loadUrl("http://www.yaolaiyaoqu.net/play/LiveL2.html");
//		webView.loadUrl("rtmp://125.208.28.38:1935/live");
//		webView.loadUrl("http://210.242.196.119/5tv/cctv99_400k_t.stream/playlist.m3u8");
		//可用
//		webView.loadUrl("http://view.l.bokecc.com/api/view/index?roomid=A381EDF45C4F09009C33DC5901307461&userid=CBB6D9ABFB512EC1");
		if(savedInstanceState != null){
			webView.restoreState(savedInstanceState);
		}
		
		
		
		
	}
	
	
	@Override
	public void onBackPressed() {
		if(myView == null){
			super.onBackPressed();
		}
		else{
			chromeClient.onHideCustomView();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		webView.saveState(outState);
	}
	
	public void addJavaScriptMap(Object obj, String objName){
		webView.addJavascriptInterface(obj, objName);
	}
	
	public class MyWebviewCient extends WebViewClient{
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			WebResourceResponse response = null;
			response = super.shouldInterceptRequest(view, url);
			return response;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			Log.d("dream", "***on page finished");
			webView.loadUrl("javascript:myFunction()"); 
		}
		
	}
	
	public class MyChromeClient extends WebChromeClient{
		
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if(myView != null){
				callback.onCustomViewHidden();
				return;
			}
			frameLayout.removeView(webView);
			frameLayout.addView(view);
			myView = view;
			myCallBack = callback;
		}
		
		@Override
		public void onHideCustomView() {
			if(myView == null){
				return;
			}
			frameLayout.removeView(myView);
			myView = null;
			frameLayout.addView(webView);
			myCallBack.onCustomViewHidden();
		}
		
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			// TODO Auto-generated method stub
			Log.d("ZR", consoleMessage.message()+" at "+consoleMessage.sourceId()+":"+consoleMessage.lineNumber());
			return super.onConsoleMessage(consoleMessage);
		}
	}
	
	

	@Override
	public void onClick(View arg0) {
		
		
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void initSetlistener() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.destroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			finish();

		}
		
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void getJsonObject(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			String url = jsonObject.getString("imageUrl");
//			CommonUtil.custoast(getApplicationContext(), url);
			System.out.println("----image---"+url);
			webView.loadUrl(url);
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
