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
import android.widget.ImageView;
import android.widget.TextView;

import com.klgz.shakefun.ui.MediaPlayActivity;
import com.klgz.shakefun.ui.TvDetailAct;
import com.klgz.ylyq.R;
import com.lidroid.xutils.BitmapUtils;


/**
 * 节目列表adapter
 */

public class HorizonAdapter extends BaseAdapter{
	
	private Context context;
	private List<Map<String, Object>> list;
//	int[] dra = new int[] {R.drawable.firstpage_juplay,R.drawable.firstpage_lovelife,R.drawable.firstpage_lovetra,
//			R.drawable.firstpage_pingpai,R.drawable.firstpage_place,R.drawable.firstpage_zhibo,
//			};
	
	public HorizonAdapter(Context context,List<Map<String, Object>> list){
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.horizon_listitem, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.horizon_iv1);
			holder.tv = (TextView) convertView.findViewById(R.id.horizon_tv);
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		System.out.println("--------------context------"+list.get(position).get("imageUrl").toString());
		bitmapUtils.display(holder.iv, list.get(position).get("imageUrl").toString());
		holder.tv.setText(list.get(position).get("introduction").toString());
		
		holder.iv.setTag(position);
		holder.tv.setTag(position);
		
		holder.iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context,MediaPlayActivity.class);
				intent.putExtra("videoId", list.get(Integer.parseInt(v.getTag().toString())).get("vedioId").toString());
				intent.putExtra("questid", list.get(Integer.parseInt(v.getTag().toString())).get("id").toString());
				intent.putExtra("startMinute", list.get(Integer.parseInt(v.getTag().toString())).get("startMinute").toString());
				intent.putExtra("frequency", list.get(Integer.parseInt(v.getTag().toString())).get("frequency").toString());
				intent.putExtra("videoname", list.get(Integer.parseInt(v.getTag().toString())).get("name").toString());
				intent.putExtra("videourl", list.get(Integer.parseInt(v.getTag().toString())).get("videoUrl").toString());
				intent.putExtra("praisenumber", list.get(Integer.parseInt(v.getTag().toString())).get("praisenumber").toString());
				intent.putExtra("isparise", list.get(Integer.parseInt(v.getTag().toString())).get("isPrase").toString());
				intent.putExtra("imageurl", list.get(Integer.parseInt(v.getTag().toString())).get("imageUrl").toString());
				context.startActivity(intent);
				
			}
		});
		
		holder.tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(context,TvDetailAct.class);
				intent2.putExtra("id", list.get(Integer.parseInt(v.getTag().toString())).get("id").toString());
				context.startActivity(intent2);
				
			}
		});
		
		
		return convertView;
	}
	
	class ViewHolder{
		private ImageView iv;//节目图片
		private TextView tv;//节目简介
	}
	

}
