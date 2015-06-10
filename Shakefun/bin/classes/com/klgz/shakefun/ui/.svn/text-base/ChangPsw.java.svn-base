/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */

package com.klgz.shakefun.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.klgz.shakefun.bean.Registerbean;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.LoginEngine;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.ylyq.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author wk
 *修改密码界面
 */
public class ChangPsw extends BasicActivity{
	
	/**后退 */
	private ImageView changepsw_back;
	/** 用户名 ,密码，重复密码*/
	private EditText changepsw_username,changepsw_password,changepsw_password2;
	/** 确定按钮 */
	private Button login_btn_login;
	private String mobile,psw,psw2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_changepsw);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initSetlistener();
		
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changepsw_back:
			
			startActivity(new Intent(ChangPsw.this,LoginMycenterAct.class));
			
			finish();
			break;
		case R.id.login_btn_login://确定按钮
			
			toLoginAct();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		changepsw_back = (ImageView) findViewById(R.id.changepsw_back);
		login_btn_login = (Button) findViewById(R.id.login_btn_login);
		changepsw_username = (EditText) findViewById(R.id.changepsw_username);
		changepsw_password = (EditText) findViewById(R.id.changepsw_password);
		changepsw_password2 = (EditText) findViewById(R.id.changepsw_password2);
		changepsw_username.setText(getIntent().getStringExtra("phone"));
	}

	@Override
	public void initSetlistener() {
		changepsw_back.setOnClickListener(this);
		login_btn_login.setOnClickListener(this);
		
	}
	
	/**
	 * 修改成功后跳转登录界面
	 */
	
	private void toLoginAct(){
		
		mobile = changepsw_username.getText().toString().trim();
		psw = changepsw_password.getText().toString().trim();
		psw2 = changepsw_password2.getText().toString().trim();
		
		
		if (TextUtils.isEmpty(mobile)) {
			CommonUtil.custoast(getApplicationContext(), "请输入手机号码");
			return;
		}
		
		if (TextUtils.isEmpty(psw)) {
			CommonUtil.custoast(getApplicationContext(), "请输入密码");
			return;
		}
		if (TextUtils.isEmpty(psw2)) {
			CommonUtil.custoast(getApplicationContext(), "请再次输入密码");
			return;
		}
		
		if (!psw.equals(psw2)) {
			CommonUtil.custoast(getApplicationContext(), "两次输入的密码不一致，请核对");
			return;
		}
		
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在注册...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("phone", mobile);
			params.put("newPassword", psw);
			params.put("repeatPassword", psw2);

			data.put("method", "resetpwd");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		System.out.println("------------"+d);
		
//		String url = Constant.LOGIN;
//		String url = "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL+"appUser!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		LoginEngine engine = new LoginEngine(getApplicationContext());
		engine.setPostResult(new PostResult<Registerbean>() {

			@Override
			public void getResult(Status status, List<Registerbean> datas) {
				if (status.getCode() == 200) {
					Registerbean userInfo = datas.get(0);
					System.out.println("-----------datas-----"+userInfo.getPhone());

					DialogUtils.closeProgressDialog();
					
					startActivity(new Intent(ChangPsw.this,
							LoginMycenterAct.class));


				} else {
					Toast.makeText(getApplicationContext(), status.getMsg(),
							Toast.LENGTH_SHORT).show();
					DialogUtils.closeProgressDialog();
				}

			}

			@Override
			public void onError(VolleyError error) {
				DialogUtils.closeProgressDialog();
				Toast.makeText(getApplicationContext(), "网络出错",
						Toast.LENGTH_SHORT).show();
			}
		});
		engine.getData(url, maps, Method.POST);
		
		
	}
	

}
