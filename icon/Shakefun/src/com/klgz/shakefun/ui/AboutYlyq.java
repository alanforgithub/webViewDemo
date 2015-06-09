/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import com.klgz.ylyq.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wk 关于界面
 */
public class AboutYlyq extends Activity implements OnClickListener{

	private Typeface typeface;
	/**
	 * 标题，内容，版权
	 */
	private TextView aboutact_title, aboutact_content, aboutact_banquan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.act_about);
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;

		default:
			break;
		}

	}

	public void initView() {
		findViewById(R.id.about_back).setOnClickListener(this);
		// aboutact_title = (TextView) findViewById(R.id.aboutact_title);
		// aboutact_content = (TextView) findViewById(R.id.aboutact_content);
		//
		// typeface = Typeface.createFromAsset(getAssets(),
		// "fonts/songti.TTF");//导入字体
		// aboutact_title.setTypeface(typeface);
		// aboutact_content.setTypeface(typeface);
		// aboutact_banquan.setTypeface(typeface);

	}

}
