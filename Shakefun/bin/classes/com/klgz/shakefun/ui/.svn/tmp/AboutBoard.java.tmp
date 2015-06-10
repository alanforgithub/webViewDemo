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
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.klgz.shakefun.adapter.BoardAdapter;
import com.klgz.shakefun.tools.GetJsonUtils;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonBoard;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.ylyq.R;
/**
 * 中奖信息界面
 * @author wk
 *
 */

public class AboutBoard extends BasicActivity implements IJsonBoard{
	
	private ImageView board_back;//后退
	private ListView listview;//中奖信息
	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private BoardAdapter adapter;
	private TextView board_title;//如何摇奖
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.act_board);
		
		initView();
		initSetlistener();
		
		GetJsonUtils.getBoardInfo(AboutBoard.this, AboutBoard.this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.board_back://返回按钮
			finish();
			break;
		case R.id.board_title://如何摇奖
			startActivity(new Intent(AboutBoard.this,BoardRules.class));
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		board_back = (ImageView) findViewById(R.id.board_back);
		listview = (ListView) findViewById(R.id.board_listview);
		
		board_title = (TextView) findViewById(R.id.board_title);
		
	}

	@Override
	public void initSetlistener() {
		board_back.setOnClickListener(this);
		board_title.setOnClickListener(this);
	}

	
	/**
	 * 中奖信息
	 */
	@Override
	public void getDataBoard(String result) {
		JSONObject jsonObject;
		Map<String, Object> map;
		
		try {
			jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				map = new HashMap<String, Object>();
				map.put("username", object.get("name"));
				map.put("phone", object.get("phone"));
				map.put("prizeName", object.get("prizeName"));
				
				
				list.add(map);
				
			}
			if (list.size() == 0) {
				CommonUtil.custoast(getApplicationContext(), "暂无中奖信息");
			}else {
				adapter = new BoardAdapter(getApplicationContext(), list);
				listview.setAdapter(adapter);
				
			}
//			CommonUtil.setListViewHeightBasedOnChildren(listview);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
