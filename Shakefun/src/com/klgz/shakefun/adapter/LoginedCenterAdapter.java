/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.klgz.shakefun.ui.PrizeCheck;
import com.klgz.ylyq.R;

/**
 * @author wk
 *个人中心中奖列表adapter
 */
public class LoginedCenterAdapter extends BaseAdapter{
	
	
	private Context context;
	private List<Map<String, Object>> list;
	
	
	
	public LoginedCenterAdapter(Context context,List<Map<String, Object>> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.loginedlist_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.logined_name);
			holder.logined_list1 = (TextView) convertView.findViewById(R.id.logined_list1);
//			holder.endtime = (TextView) convertView.findViewById(R.id.logined_list3);
			
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(list.get(position).get("name").toString());
//		holder.endtime.setText(list.get(position).get("endtime").toString());
		holder.logined_list1.setTag(position);
		
		holder.logined_list1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,PrizeCheck.class);
				intent.putExtra("imageurl", list.get(Integer.parseInt(v.getTag().toString())).get("image_url").toString());
				context.startActivity(intent);
				
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		private TextView name;//店铺名称
		private TextView type;//兑换券类型
		private TextView endtime;//截至日期
		private TextView logined_list1;//查看
		
	}
	

}
