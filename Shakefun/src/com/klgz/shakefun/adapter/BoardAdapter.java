/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.adapter;

import java.util.List;
import java.util.Map;

import com.klgz.shakefun.adapter.LoginedCenterAdapter.ViewHolder;
import com.klgz.ylyq.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author wk
 *中奖公告适配器
 */
public class BoardAdapter extends BaseAdapter{
	
	private Context context;
	private List<Map<String, Object>> list;
	
	
	public BoardAdapter(Context context,List<Map<String, Object>> list){
		this.context = context;
		this.list = list;
	}
	

	@Override
	public int getCount() {
		if (list.size()>=30) {
			return 30;
		}else {
			return list.size();
		}
			
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
			convertView = LayoutInflater.from(context).inflate(R.layout.board_listitem, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.boardlistitem_name);
			holder.prizename = (TextView) convertView.findViewById(R.id.boardlistitem_prizename);
			holder.phone = (TextView) convertView.findViewById(R.id.boardlistitem_phone);
			
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText("昵称:"+list.get(position).get("username").toString());
		holder.phone.setText("手机:"+list.get(position).get("phone").toString());
		holder.prizename.setText("奖品:"+list.get(position).get("prizeName").toString());
		
		return convertView;
	}
	
	
	class ViewHolder{
		//用户名，奖品名称，电话号码
		private TextView name,prizename,phone;
	}

}
