/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.tools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.klgz.shakefun.bean.Registerbean;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.enginemp.LoginEngine;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * @author wk
 *获取网络请求数据
 */
public class GetJsonUtils {
	
	
	
	public interface IJsonCallback{
		void getJsonObject(String result);
	}
	
	
	/**
	 * 获取视频相关问题接口
	 * @param context
	 */
	
	public static void getDataquestJson(final Context context,String id,final IJsonCallback iCallback){
		
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		
//		DialogUtils.showProgressDialog(context, Constant.Dialog_message_Jiazai);
//		DialogUtils.dialog.setMessage("正在加载...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
//			params.put("phone", "");
			params.put("programId", id);

			data.put("method", "getQuestByProgramId");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		
//		String url = Constant.LOGIN;
//		String url = "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL+"programQuest!request.action";
//		String url = "http://192.168.0.112:8080/programQuest!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(context);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					iCallback.getJsonObject(userInfo);
					System.out.println("-------question-------"+userInfo);

//					DialogUtils.closeProgressDialog();
					
					


				} else {
//					Toast.makeText(context, status.getMsg(),
//							Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
//				Toast.makeText(context, "网络出错",
//						Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);
		
	}
	
	/**
	 * 图片下载
	 */
	
	@SuppressLint("SdCardPath")
	@SuppressWarnings("rawtypes")
	public static void downImage(final Context context,String url,String name){
		HttpUtils http = new HttpUtils();
		@SuppressWarnings("unused")
		HttpHandler handler = http.download(url, 
				"/mnt/sdcard/shakefun/"+name, true, true, 
				new RequestCallBack<File>() {
			
			
			@Override
			public void onStart() {
						super.onStart();
					}
			
			@Override
			public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				
			}

			
			
		});

		
	}
	
	/**
	 * 获取视频打开量接口
	 */
	public interface IJsonVideoback{
		void getJsonVideo(String result);
	}
	
	public static void getDataJsonVideo(final Context context,final IJsonVideoback videoback,
							String id,String token,String userid){
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		
//		DialogUtils.showProgressDialog(context, Constant.Dialog_message_Jiazai);
//		DialogUtils.dialog.setMessage("正在加载video...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
//			params.put("phone", "");
			params.put("programId", id);
			params.put("userId", userid);
			params.put("videoMark", "");
			params.put("userToken", token);

			data.put("method", "openVideoTimes");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		System.out.println("--------video------"+d);
		//测试打印
		
//		String url = Constant.LOGIN;
//		String url = "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL+"video!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(context);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					videoback.getJsonVideo(userInfo);
					System.out.println("-------videoback-------"+userInfo);

					DialogUtils.closeProgressDialog();
					
					


				} else {
//					Toast.makeText(context, status.getMsg(),
//							Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
				Toast.makeText(context, "网络出错",
						Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);
	}
	
	
	/**
	 * 用户点赞接口
	 */
	
	public interface IJsongetPraise{
		void getJsonPraise(String result);
	}
	
	public static void sendJsonisgood(final Context context,final IJsongetPraise praiseback,
							String id,String userid,String token,String praise,String down){
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		
//		DialogUtils.showProgressDialog(context, Constant.Dialog_message_Jiazai);
//		DialogUtils.dialog.setMessage("正在加载praise...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
//			params.put("phone", "");
			params.put("programId", id);
			params.put("userId", userid);
			params.put("videoMark", "");
			params.put("userToken", token);
			params.put("praise", praise);
			params.put("good", down);

			data.put("method", "addVideoRecord");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		System.out.println("--------praise------"+d);
		
//		String url = Constant.LOGIN;
//		String url = "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL+"videoRecord!request.action";
//		String url = "http://192.168.0.112:8080/videoRecord!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(context);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					praiseback.getJsonPraise(userInfo);
					System.out.println("-------jsonpraise-------"+userInfo);

					DialogUtils.closeProgressDialog();
					
					


				} else {
					Toast.makeText(context, status.getMsg(),
							Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
//				Toast.makeText(context, "网络出错",
//						Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);
	}
	
	
	/**
	 * 用户获取是否点赞信息，用以改变图标
	 */
	
	public interface IJsonPraiseInfo{
		void getPraiseinfo(String result);
	}
	
	
	public static void getPraiseInfo(final Context context,final IJsonPraiseInfo praiseInfo,
			String id,String token,String userid){
		
		
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		// 构造访问网络的URL
				JSONObject params = new JSONObject();
				JSONObject data = new JSONObject();
				try {
//					params.put("phone", "");
					params.put("programId", id);
					params.put("userId", userid);
					params.put("videoMark", "");
					params.put("userToken", token);
					

					data.put("method", "getVideoRecord");
					data.put("userId", "");
					data.put("token", "");
					data.putOpt("params", params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String d = data.toString();
				
				
				//测试打印
				System.out.println("--------praise------"+d);
				
//				String url = Constant.LOGIN;
//				String url = "http://192.168.0.112:8087/appUser!request.action?data=";
				String url = Constant.KE_BASICURL+"videoRecord!request.action";
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("data", d);

				GetMsg engine = new GetMsg(context);
				engine.setPostResult(new PostResult<String>() {

					@Override
					public void getResult(Status status, List<String> datas) {
						if (status.getCode() == 200) {
							String userInfo = datas.get(0);
							praiseInfo.getPraiseinfo(userInfo);
							System.out.println("-------praiseINfo-------"+userInfo);

							DialogUtils.closeProgressDialog();
							
							


						} else {
//							Toast.makeText(context, "暂无奖品信息",
//									Toast.LENGTH_SHORT).show();
							DialogUtils.closeProgressDialog();
						}

					}

					@Override
					public void onError(VolleyError error) {
						DialogUtils.closeProgressDialog();
//						Toast.makeText(context, "网络出错",
//								Toast.LENGTH_SHORT).show();
					}
				});
				engine.getData(url, maps, Method.POST);
	}
	
	
	/**
	 * 摇奖信息接口
	 */
	
	
	public interface IJsonPrize{
		void getDataPrize(String result);
	}
	
	
	public static void getPrizeInfo(final Context context,final IJsonPrize prizeInfo,
			String phone,String username,String adress,String answer,
			String id,String token,String userid){
		
		
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		// 构造访问网络的URL
				JSONObject params = new JSONObject();
				JSONObject data = new JSONObject();
				try {
//					params.put("phone", "");
					params.put("programId", id);
					params.put("userId", userid);
					params.put("phone", phone);
					params.put("userName", username);
					params.put("adress", adress);
					params.put("answer", answer);
					params.put("userToken", token);
					

					data.put("method", "winningPrizeInfo");
					data.put("userId", "");
					data.put("token", "");
					data.putOpt("params", params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String d = data.toString();
				
				
				//测试打印
				System.out.println("--------praise------"+d);
				
//				String url = Constant.LOGIN;
//				String url = "http://192.168.0.112:8087/appUser!request.action?data=";
				String url = Constant.KE_BASICURL+"rules!request.action";
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("data", d);

				GetMsg engine = new GetMsg(context);
				engine.setPostResult(new PostResult<String>() {

					@Override
					public void getResult(Status status, List<String> datas) {
						if (status.getCode() == 200) {
							String userInfo = datas.get(0);
							prizeInfo.getDataPrize(userInfo);
							System.out.println("-------prizeINfo-------"+userInfo);

							DialogUtils.closeProgressDialog();
							
							


						} else {
//							Toast.makeText(context, status.getMsg(),
//									Toast.LENGTH_SHORT).show();
							DialogUtils.closeProgressDialog();
						}

					}

					@Override
					public void onError(VolleyError error) {
						DialogUtils.closeProgressDialog();
//						Toast.makeText(context, "网络出错",
//								Toast.LENGTH_SHORT).show();
					}
				});
				engine.getData(url, maps, Method.POST);
	}
	
	
	/**
	 * 版本更新接口
	 */
	public interface IJsonVerson{
		void getDataVerson(String result);
	}
	
	public static void getVersonInfo(final Context context,final IJsonVerson jsonVerson
			){
		
		
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		// 构造访问网络的URL
				JSONObject params = new JSONObject();
				JSONObject data = new JSONObject();
				try {
//					params.put("phone", "");
//					params.put("programId", id);
//					params.put("userId", userid);
//					params.put("phone", phone);
//					params.put("userName", username);
//					params.put("adress", adress);
//					params.put("answer", answer);
//					params.put("userToken", token);
					

					data.put("method", "getNewVersion");
					data.put("userId", "");
					data.put("token", "");
					data.putOpt("params", params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String d = data.toString();
				
				
				//测试打印
				System.out.println("--------verson------"+d);
				
//				String url = Constant.LOGIN;
//				String url = "http://192.168.0.112:8087/appUser!request.action?data=";
				String url = Constant.KE_BASICURL+"appUser!request.action";
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("data", d);

				GetMsg engine = new GetMsg(context);
				engine.setPostResult(new PostResult<String>() {

					@Override
					public void getResult(Status status, List<String> datas) {
						if (status.getCode() == 200) {
							String userInfo = datas.get(0);
							jsonVerson.getDataVerson(userInfo);
							System.out.println("-------versonjson-------"+userInfo);

							DialogUtils.closeProgressDialog();
							
							


						} else {
//							Toast.makeText(context, status.getMsg(),
//									Toast.LENGTH_SHORT).show();
							DialogUtils.closeProgressDialog();
						}

					}

					@Override
					public void onError(VolleyError error) {
						DialogUtils.closeProgressDialog();
//						Toast.makeText(context, "网络出错",
//								Toast.LENGTH_SHORT).show();
					}
				});
				engine.getData(url, maps, Method.POST);
	}
	
	
	
	
	
	/**
	 * 中奖公告接口
	 */
	public interface IJsonBoard{
		void getDataBoard(String result);
	}
	
	public static void getBoardInfo(final Context context,final IJsonBoard jsonBoard
			){
		
		
		if (!NetworkDetector.isConn(context)) {
			NetworkDetector.setNetworkMethot(context);
		}
		
		// 构造访问网络的URL
				JSONObject params = new JSONObject();
				JSONObject data = new JSONObject();
				try {
//					params.put("phone", "");
//					params.put("programId", id);
//					params.put("userId", userid);
//					params.put("phone", phone);
//					params.put("userName", username);
//					params.put("adress", adress);
//					params.put("answer", answer);
//					params.put("userToken", token);
					

					data.put("method", "appWinPrizeList");
					data.put("userId", "");
					data.put("token", "");
					data.putOpt("params", params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String d = data.toString();
				
				
				//测试打印
				System.out.println("--------verson------"+d);
				
//				String url = Constant.LOGIN;
//				String url = "http://192.168.0.112:8087/appUser!request.action?data=";
				String url = Constant.KE_BASICURL+"prize!request.action";
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("data", d);

				GetMsg engine = new GetMsg(context);
				engine.setPostResult(new PostResult<String>() {

					@Override
					public void getResult(Status status, List<String> datas) {
						if (status.getCode() == 200) {
							String userInfo = datas.get(0);
							jsonBoard.getDataBoard(userInfo);
//							CommonUtil.custoast(context, "中奖公告");
//							System.out.println("-------versonjson-------"+userInfo);

							DialogUtils.closeProgressDialog();
							
							


						} else {
//							Toast.makeText(context, status.getMsg(),
//									Toast.LENGTH_SHORT).show();
							DialogUtils.closeProgressDialog();
						}

					}

					@Override
					public void onError(VolleyError error) {
						DialogUtils.closeProgressDialog();
//						Toast.makeText(context, "网络出错",
//								Toast.LENGTH_SHORT).show();
					}
				});
				engine.getData(url, maps, Method.POST);
	}
	
	
	

}