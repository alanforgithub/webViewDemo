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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.bean.Registerbean;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.LoginEngine;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.ylyq.R;

/**
 * @author wk
 *个人中心 登陆界面
 */
public class LoginMycenterAct extends BasicActivity{
	
	/** 后退按钮 */
	private ImageView login_back;
	/** 用户名,密码 */
	private EditText login_username,login_password;
	/** 登陆 */
	private Button login_btn_login;
	/** 忘记密码 去注册 */
	private TextView login_forgotpsw,login_goregister;
	private String mobile,psw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mycenter);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initSetlistener();
		
		
		
		
	}
	
	
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back://返回按钮
			
			finish();
			
			break;
		case R.id.login_btn_login://登陆按钮
			
			toLogin();
			
			break;
		case R.id.login_forgotpsw://忘记密码
			Intent forIntent = new Intent(LoginMycenterAct.this,ForgotPsw.class);
			startActivity(forIntent);
			finish();	
			
			break;
		case R.id.login_goregister://去注册
			Intent intent = new Intent(LoginMycenterAct.this,Register.class);
			startActivity(intent);
			finish();
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		login_back = (ImageView) findViewById(R.id.login_back);
		login_username = (EditText) findViewById(R.id.login_username);
		login_password = (EditText) findViewById(R.id.login_password);
		login_btn_login = (Button) findViewById(R.id.login_btn_login);
		login_forgotpsw = (TextView) findViewById(R.id.login_forgotpsw);
		login_goregister = (TextView) findViewById(R.id.login_goregister);
		
		
		
		
		
	}

	@Override
	public void initSetlistener() {
		login_back.setOnClickListener(this);
		login_btn_login.setOnClickListener(this);
		login_forgotpsw.setOnClickListener(this);
		login_goregister.setOnClickListener(this);
		
	}
	
	
	/**
	 * 登陆请求
	 */
	private void toLogin(){
		mobile = login_username.getText().toString().trim();
		 psw = login_password.getText().toString().trim();
		 Constant.phonenu = mobile;
		 Constant.psw = psw;
		 
		 if (TextUtils.isEmpty(mobile)) {
			CommonUtil.custoast(LoginMycenterAct.this, "请输入手机号码");
			return;
		}
		 
		 if (TextUtils.isEmpty(psw)) {
			CommonUtil.custoast(getApplicationContext(), "请输入密码");
			return;
		}
		 
		 DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在登录...");
			// 构造访问网络的URL
			JSONObject params = new JSONObject();
			JSONObject data = new JSONObject();
			try {
				params.put("phone", mobile);
				params.put("password", psw);
				params.put("userToken", Constant.phoneID);

				data.put("method", "userLogin");
				data.put("userId", "");
				data.put("token", "");
				data.putOpt("params", params);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String d = data.toString();
			
			
			//测试打印
			System.out.println("------------"+d);
			
//			String url = Constant.LOGIN;
//			String url = "http://192.168.0.112:8087/appUser!request.action?data=";
//			String url = Constant.KE_BASICURL+"appUser!request.action";
			String url = "http://125.208.12.71/appUser!request.action";
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
						
						Constant.userId = userInfo.getId();
						Constant.token = userInfo.getToken();
						Constant.islogin = true;
						Constant.username = userInfo.getName();
						//Toast.makeText(getApplicationContext(), status.getMsg(),
							//	Toast.LENGTH_SHORT).show();
						// 普通注册 ，注册成功》》》将注册信息存到本地
	//
//						SharedPreferences spf = getSharedPreferences("user",
//								Context.MODE_PRIVATE);
//						Editor editor = spf.edit();
//						editor.putString("moblie", mobile);
//						editor.putString("password", psw);
//
////						editor.putString("token", userInfo.getToken());
////						editor.putString("userid", userInfo.getUserid());
////						editor.putString("usertype", userInfo.getUsertype());
////						editor.putString("ct", userInfo.getCt());
////						editor.putString("organization", "");
//
//						editor.commit();
//						toLogin(moblie, password,which);
//						tologinAct();
						
						Intent intent = new Intent(LoginMycenterAct.this,
								LogingedCenter.class);
						startActivity(intent);
						finish();


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
