/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.engine;

import java.util.Map;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.klgz.shakefun.enginemp.Try_StringRequest;
import com.klgz.shakefun.utils.DialogUtils;


/**
 * 网络请求的基类
 * 
 */
import java.util.Map;

import android.content.Context;
import android.widget.Toast;


/**
 * 网络请求的基类
 * 
 */
public abstract class BaseEngine {
	public Context context;

	public BaseEngine(Context context) {
		this.context = context;
	}

	public abstract void myOnResponse(String str);

	public void getData(String url, final Map<String, String> params, int method) {
		
		//修改编码集
		RequestQueue queue = Volley.newRequestQueue(context);
		queue.add(new Try_StringRequest(method, url, new Listener<String>() {
		@Override
		public void onResponse(String arg0) {

			myOnResponse(arg0);
		}
	}, new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			DialogUtils.closeProgressDialog();
			
			Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
		}
	}) {

		@Override
		protected Map<String, String> getParams() throws AuthFailureError {
			return params;
		}
	});
	queue.start();
	}
}
