/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.ylyq.R;
import com.lidroid.xutils.BitmapUtils;

public class PrizeCheck extends BasicActivity{
	
	
	private ImageView prizecheck_img;
	private ImageView prizecheck_back;//后退按钮
	private String imgurl;//图片链接
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_prizecheck);
		initView();
		initSetlistener();
		imgurl = getIntent().getStringExtra("imageurl");
		BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
		if (!TextUtils.isEmpty(imgurl)) {
			bitmapUtils.display(prizecheck_img, imgurl);
			
		}else {
			CommonUtil.custoast(getApplicationContext(), "暂无奖品介绍");
		}
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prizecheck_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		prizecheck_img = (ImageView) findViewById(R.id.prizecheck_img);
		prizecheck_back = (ImageView) findViewById(R.id.prizecheck_back);
		
		
		
		
	}

	@Override
	public void initSetlistener() {
		prizecheck_back.setOnClickListener(this);
	}

}
