/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.klgz.shakefun.utils.AppManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;



/**
 * Activity基类
 * @author wk
 *
 */
public abstract class BasicActivity extends Activity implements OnClickListener{

	/*屏幕宽度*/
	public int screenWidth;
	/*屏幕高度*/
	public int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		
	}
	
	
	/**
	 * 网络请求
	 * @param method
	 * @param url
	 * @param params
	 * @param callBack
	 */
	public void loadData(HttpMethod method,String url,RequestParams params,RequestCallBack<String> callBack){
		HttpUtils httpUtils=new HttpUtils();
		if(params == null){
			params=new RequestParams();
		}
		
		httpUtils.send(method, url, params,callBack);
	}
	
	
			/**
			 * 判断返回是否是json
			 */
	protected boolean checkRequest(String jsonStr){
		try {
			JSONObject jsonObject=new JSONObject(jsonStr);
			String request = jsonObject.getString("response");
			if("error".equals(request)){
				return false;
			}
			else{
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}
	
	
	/**
	 * 初始化数据
	 */
	public abstract void initView();
	
	/**
	 * 设置监听
	 */
	public abstract void initSetlistener();
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	
	
}
