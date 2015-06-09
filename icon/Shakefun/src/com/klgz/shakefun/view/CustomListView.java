/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

/**
 * 
 * 禁止listview滑动
 * @author wk
 *
 */
public class CustomListView extends ListView{
	
	
	
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	
	/**
	 * 禁止listview滑动
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, 
				MeasureSpec.AT_MOST);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	

}
