/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonCallback;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;

/**
 * @author wk 获取直播地址，动画图片
 * 
 */
public class GetFirstJsonUtil {

	/**
	 * 
	 * 直播地址
	 * 
	 * @author wk
	 * 
	 */

	public interface IJsonZhiboback {
		void getJsonObject(String result);
	}

	public static void getDataZhiboJson(final Context context,
			final IJsonZhiboback iCallback) {

		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}

		// DialogUtils.showProgressDialog(context,
		// Constant.Dialog_message_Jiazai);
		// DialogUtils.dialog.setMessage("正在加载...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			// params.put("phone", "");
			params.put("", "");

			data.put("method", "getZhibUrl");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();

		// 测试打印

		// String url = Constant.LOGIN;
		// String url =
		// "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL + "video!request.action";
		// String url = "http://192.168.0.112:8080/programQuest!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(context);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					iCallback.getJsonObject(userInfo);
					// System.out.println("-------直播地址-------"+userInfo);
					// CommonUtil.custoast(context, "直播地址");
					// DialogUtils.closeProgressDialog();

				} else {
					// Toast.makeText(context, status.getMsg(),
					// Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
				// Toast.makeText(context, "网络出错",
				// Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);

	}

	/**
	 * 开机图片
	 * 
	 * @author wk
	 * 
	 */

	public interface IJsonPictureback {
		void getJsonpicObject(String result);
	}

	public static void getDatapictureJson(final Context context,
			final IJsonPictureback iCallback) {

		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}

		// DialogUtils.showProgressDialog(context,
		// Constant.Dialog_message_Jiazai);
		// DialogUtils.dialog.setMessage("正在加载...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			// params.put("phone", "");
			params.put("", "");

			data.put("method", "getDongtaiUrl");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();

		// 测试打印

		// String url = Constant.LOGIN;
		// String url =
		// "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL + "video!request.action";
		// String url = "http://192.168.0.112:8080/programQuest!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(context);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					iCallback.getJsonpicObject(userInfo);
					// System.out.println("-------图片-------"+userInfo);
					// CommonUtil.custoast(context, "图片地址");
					// DialogUtils.closeProgressDialog();

				} else {
					// Toast.makeText(context, status.getMsg(),
					// Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
				// Toast.makeText(context, "网络出错",
				// Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);

	}

}
