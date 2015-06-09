/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * viewpager  adapter
 *	
 */
public class ViewPagerAdapter extends PagerAdapter {
	
	//�����б�
    private ArrayList<View> views;
    
    public ViewPagerAdapter (ArrayList<View> views){
        this.views = views;
    }
       
	/**
	 * ��õ�ǰ������
	 */
	@Override
	public int getCount() {
		 if (views != null) {
             return views.size();
         }      
         return 0;
	}

	/**
	 * ��ʼ��positionλ�õĽ���
	 */
    @Override
    public Object instantiateItem(View view, int position) {
       
        ((ViewPager) view).addView(views.get(position), 0);
       
        return views.get(position);
    }
    
    /**
	 * �ж��Ƿ��ɶ�����ɽ���
	 */
	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return (view == arg1);
	}

	/**
	 * ���positionλ�õĽ���
	 */
    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(views.get(position));       
    }
}
