/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.enginemp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.BaseEngine;
import com.klgz.shakefun.engine.PostResult;
/**
 * 获取url的信息
 * 返回result字符串
 * @author F.K
 *
 */
public class GetMsg extends BaseEngine{
	private PostResult<String> postResult;
	public void setPostResult(PostResult<String> postResult) {
		this.postResult = postResult;
	}

	public GetMsg(Context context) {
		super(context);
	}

	@Override
	public void myOnResponse(String str) {
		Log.d("GetMsg", str);
		List<String> infos = new ArrayList<String>();
		
		System.out.println("---------str-------"+str);
		
		try {
			JSONObject jsonObject = new JSONObject(str);
			String statusStr = jsonObject.getString("status");
			Gson gson = new Gson();
			Status status = gson.fromJson(statusStr, Status.class);
			String result;
			if (200 == status.getCode()) {
				result = jsonObject.getString("result");
				
//				System.out.println("-------result------"+result.toString());
				
				Log.d("taobaole", "result>" + result);
			} else {
				result = "";
			}
			infos.add(result);
			if (postResult != null) {
				postResult.getResult(status, infos);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
