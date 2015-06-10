/**
 * auto 王广彬
 */
package com.klgz.shakefun.ui;

import com.klgz.shakefun.utils.UpdateUtils;
import com.klgz.ylyq.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 关于界面
 * 
 * @author wk
 * 
 */
public class AboutUs extends BasicActivity {

	private TextView us_versionname;// 版本号
	private TextView us_phone;// 客服电话

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.act_aboutus);

		initView();
		initSetlistener();

		if (!TextUtils.isEmpty(UpdateUtils.getVersonName(AboutUs.this))) {
			us_versionname.setText("Version  "
					+ UpdateUtils.getVersonName(AboutUs.this));
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.us_back:// 后退
			finish();
			break;
		case R.id.us_phone:// 客服电话

			String phoneNumber = us_phone.getText().toString();
			Intent intentPhone = new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:" + phoneNumber));

			startActivity(intentPhone);

			break;
		case R.id.us_about:// 关于
			startActivity(new Intent(AboutUs.this, AboutYlyq.class));
			break;

		default:
			break;
		}

	}

	@Override
	public void initView() {
		us_versionname = (TextView) findViewById(R.id.us_versionname);
		findViewById(R.id.us_phone).setOnClickListener(this);
		findViewById(R.id.us_about).setOnClickListener(this);
		findViewById(R.id.us_back).setOnClickListener(this);
	}

	@Override
	public void initSetlistener() {

	}

}
