/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.adapter.LoginedCenterAdapter;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.ylyq.R;

/**
 * @author wk
 *登陆后显示显示界面
 */
public class LogingedCenter extends BasicActivity{
	
	/** 退出按钮 */
	private ImageView logined_back;
	/** 退出登录 */
	private TextView logined_off;
	/** 用户名 */
	private TextView logined_username;
	/** 编辑个人信息 */
	private LinearLayout logined_bianjigeren;
	/** listview */
	private ListView listView;
	
	private LoginedCenterAdapter adapter;
	
	
	List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_loginedcenter);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initSetlistener();
		
		getData();
		
//		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//		Map<String, Object> map;
//		for (int i = 0; i < 10; i++) {
//			 map = new HashMap<String, Object>();
//			
//			 map.put("name", "价值200元味多美优惠券1张");
//			 map.put("endtime", "2015-06-01");
//			 list.add(map);
//			
//		}
//		adapter = new LoginedCenterAdapter(LogingedCenter.this, list);
//		listView.setAdapter(adapter);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logined_back://后退按钮
			finish();
			
			break;
		case R.id.logined_off://退出登陆
			
			loginoff();
			
			break;
		case R.id.logined_bianjigeren://编辑个人信息
			Intent bianjiIntent = new Intent(LogingedCenter.this,EditMessage.class);
			bianjiIntent.putExtra("tag", "login");
			startActivity(bianjiIntent);
			finish();
			
			break;
			
		default:
			break;
		}
		
		
	}

	@Override
	public void initView() {
		logined_back = (ImageView) findViewById(R.id.logined_back);
		logined_off = (TextView) findViewById(R.id.logined_off);
		logined_username = (TextView) findViewById(R.id.logined_username);
		logined_bianjigeren = (LinearLayout) findViewById(R.id.logined_bianjigeren);
		listView = (ListView) findViewById(R.id.logined_listview);
		
		if (!TextUtils.isEmpty(Constant.username)) {
			logined_username.setText(Constant.username);
			
		}
		
		
		
	}

	@Override
	public void initSetlistener() {
		logined_back.setOnClickListener(this);
		logined_off.setOnClickListener(this);
		logined_bianjigeren.setOnClickListener(this);
		
	}
	
	
	/**
	 * 退出登录网络请求
	 */
	
	private void loginoff(){
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在退出...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("password", Constant.psw);
			params.put("phone", Constant.phonenu);
			

			data.put("method", "logout");
			data.put("userId", Constant.userId);
			data.put("token", Constant.token);
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		System.out.println("------------"+d);
		
//		String url = Constant.LOGIN;
//		String url = "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL+"appUser!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(getApplicationContext());
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);

					DialogUtils.closeProgressDialog();
					
					Constant.psw ="";
					Constant.userId ="";
					Constant.token ="";
					Constant.islogin =false;
					
					
					Intent intent = new Intent(LogingedCenter.this,MainActivity.class);
					startActivity(intent);
					finish();
					


				} else {
					Toast.makeText(getApplicationContext(), status.getMsg(),
							Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
				Toast.makeText(getApplicationContext(), "网络出错",
						Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);
		
		
	}
		
	/**
	 * 获取列表信息
	 */
		
		private void getData(){
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在加载...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("password", Constant.psw);
			params.put("phone", Constant.phonenu);
			

			data.put("method", "getuserInfo");
			data.put("userId", Constant.userId);
			data.put("token", Constant.token);
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		System.out.println("------------"+d);
		
//		String url = Constant.LOGIN;
//		String url = "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL+"appUser!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(getApplicationContext());
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					System.out.println("-----------datas-----"+userInfo);

					DialogUtils.closeProgressDialog();
					
					setUI(userInfo);
					
//					edit_username.setText(userInfo.getName());
//					if (userInfo.getSex().equals("0")) {
//						edit_sexchoice.setImageResource(R.drawable.sexchoicey);
//						edit_sexchoice2.setImageResource(R.drawable.sexchoicen);
//					}else {
//						edit_sexchoice.setImageResource(R.drawable.sexchoicen);
//						edit_sexchoice2.setImageResource(R.drawable.sexchoicey);
//					}
////					edit_phonenum.setText(userInfo.getPhone());
//					edit_emailadd.setText(userInfo.getEmail());
//					edit_address.setText(userInfo.getAddress());
					
					
					


				} else {
					Toast.makeText(getApplicationContext(), status.getMsg(),
							Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
				Toast.makeText(getApplicationContext(), "网络出错",
						Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);
		
		
	}
	
	private void setUI(String result){
		
		Map<String, Object> map = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("prize");
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.get("id"));
				map.put("image_url", object.get("image_url"));
				map.put("name", object.get("name"));
//				map.put("adminId", object.get("adminId"));
//				map.put("adminName", object.get("adminName"));
//				map.put("status", object.get("status"));
//				map.put("reason", object.get("reason"));
//				map.put("imageUrl", object.get("imageUrl"));
//				map.put("name", object.get("name"));
//				map.put("appSeat", object.get("appSeat"));
//				map.put("approval", object.get("approval"));
				list2.add(map);
				
			}
			System.out.println("-------list2---"+list2.toString());
			adapter = new LoginedCenterAdapter(LogingedCenter.this, list2);
			listView.setAdapter(adapter);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

}
