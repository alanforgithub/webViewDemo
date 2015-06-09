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
import com.klgz.shakefun.bean.Registerbean;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.BaseEngine;
import com.klgz.shakefun.engine.PostResult;

public class LoginEngine extends BaseEngine {

	private PostResult<Registerbean> postResult;

	public LoginEngine(Context context) {
		super(context);
	}

	public void setPostResult(PostResult<Registerbean> postResult) {
		this.postResult = postResult;
	}

	@Override
	public void myOnResponse(String str) {
		JSONObject jsonObject;
		List<Registerbean> infos = new ArrayList<Registerbean>();
		try {
			jsonObject = new JSONObject(str);
			String statusStr = jsonObject.getString("status");
			Gson gson = new Gson();
			Status status = gson.fromJson(statusStr, Status.class);

			Registerbean userInfo;
			if (status.getCode() == 200) {
				String resultStr = jsonObject.getString("result");
				System.out.println("-----------"+resultStr);
				userInfo = gson.fromJson(resultStr, Registerbean.class);
			} else {
				userInfo = new Registerbean();
			}

			infos.add(userInfo);
			if (postResult != null) {
				postResult.getResult(status, infos);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
