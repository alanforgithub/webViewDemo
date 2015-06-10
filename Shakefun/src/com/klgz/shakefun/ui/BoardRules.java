/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;


import com.klgz.ylyq.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
/**
 * 如何摇奖界面
 *
 *
 */
public class BoardRules extends BasicActivity{
	
	private ImageView boardrules_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.act_boardrules);
		initView();
		initSetlistener();
		
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.boardrules_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		boardrules_back = (ImageView) findViewById(R.id.boardrules_back);
		
	}

	@Override
	public void initSetlistener() {
		boardrules_back.setOnClickListener(this);
		
		
	}

}
