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

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.adapter.HorizonAdapter;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.shakefun.view.HorizontalListView;
import com.klgz.ylyq.R;

/**
 * @author wk
 *节目列表界面
 */
public class HorizonListAct extends BasicActivity{
	
	private ImageView jiemudianbo_back;//返回按钮
	private HorizontalListView listView;//加载列表
	private HorizonAdapter adapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private int currentpage = 1;
	private TextView listhor_before;//上一页
	
	private LinearLayout listhor_lnright;//下一页按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_lsithorizon);
		
		initView();
		initSetlistener();
		
		
		System.out.println("----------intentID------"+getIntent().getStringExtra("id"));
		getChanneData(getIntent().getStringExtra("id"),1);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jiemudianbo_back:
			finish();
			
			break;
		case R.id.listhor_before://刷新
//			list.clear();
//			getChanneData(getIntent().getStringExtra("id"), currentpage);
			adapter.notifyDataSetChanged();
			break;
		case R.id.listhor_next:
//			if (currentpage>0) {
//				currentpage++;
//				System.out.println("------page-----"+currentpage);
//				getChanneData(getIntent().getStringExtra("id"), currentpage);
//			}
		
			break;
		case R.id.listhor_lnright:
			if (currentpage>0) {
				currentpage++;
				System.out.println("------page-----"+currentpage);
				getChanneData(getIntent().getStringExtra("id"), currentpage);
			}
		
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		listView = (HorizontalListView) findViewById(R.id.lsithorizon_horizonlistview);
		jiemudianbo_back = (ImageView) findViewById(R.id.jiemudianbo_back);
		listhor_before = (TextView) findViewById(R.id.listhor_before);
//		listhor_next = (TextView) findViewById(R.id.listhor_next);
		listhor_lnright = (LinearLayout) findViewById(R.id.listhor_lnright);
		
		
	}

	@Override
	public void initSetlistener() {
		jiemudianbo_back.setOnClickListener(this);
		listhor_before.setOnClickListener(this);
//		listhor_next.setOnClickListener(this);
		
		listhor_lnright.setOnClickListener(this);
		
	}
	
	
	/**
	 * 请求网络
	 * @param id
	 */
	
	private void getChanneData(String id,int page){
		
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在初始化...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
//			params.put("email", "");
//			params.put("phone", "");
//			params.put("name", "");
//			params.put("delegate", "");
//			params.put("license_image", "");
//			params.put("industry", "");
			params.put("channelId", id);
			params.put("count_per_page", "5");
			params.put("current_page", page);
			params.put("userToken", Constant.phoneID);

			data.put("method", "getProgramInfoByChannelId");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		System.out.println("------------"+d);
		
//		String url = Constant.LOGIN;
		String url = Constant.KE_BASICURL+"program!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(HorizonListAct.this);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String infos = datas.get(0);
					System.out.println("--------infos------"+infos);
					DialogUtils.closeProgressDialog();
					
					setUI(infos);
					
					
				}else {
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
		JSONObject jsonObject;
		Map<String, Object> map = null;
	
			try {
				jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					map = new HashMap<String, Object>();
					map.put("startMinute", object.get("startMinute"));//第几分钟开始摇奖
					map.put("guestName", object.get("guestName"));
					map.put("imageUrl", object.get("imageUrl"));
					map.put("channelId", object.get("channelId"));
					map.put("frequency", object.get("frequency"));//摇奖时间间隔
					map.put("guestid", object.get("guestid"));
					map.put("city", object.get("city"));
					map.put("id", object.get("id"));
//					map.put("status", object.get("status"));
					map.put("scans", object.get("scans"));
					map.put("questCount", object.get("questCount"));
					map.put("vedioId", object.get("vedioId"));
//					map.put("approval", object.get("approval"));
					map.put("name", object.get("name"));
					map.put("programType", object.get("programType"));
					map.put("introduction", object.get("introduction"));
					map.put("videoUrl", object.get("videoUrl"));
					map.put("praisenumber", object.get("praisenumber"));
					map.put("isPrase", object.get("isPrase"));
					
					map.put("videoLength", object.get("videoLength"));
					
					
					list.add(map);
					
				}
				System.out.println("-----------list----"+list.toString());
				if (jsonArray.length()>0) {
					if (currentpage==1) {
						
						adapter = new HorizonAdapter(this,list);
					listView.setAdapter(adapter);
					}else {
						adapter.notifyDataSetChanged();
					}
				}else {
					currentpage--;
					CommonUtil.custoast(getApplicationContext(), "已经是最后一页了");
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (list!=null) {
			list.clear();
		}
		if (adapter!=null) {
			adapter = null;
		}
		
	}
	

}
