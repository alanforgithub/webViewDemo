/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import static cn.smssdk.framework.utils.R.getStringRes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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

public class Register extends BasicActivity{
	
	
	
	/** 返回按钮 */
	private ImageView register_back;
	/** 用户名*/
	private EditText username;
	/** 密码 */
	private EditText password;
	/** 验证码 */
	private EditText yanzhengma;
	/** 获取验证码按钮 */
	private Button btn_yanzhengma;
	/** 注册按钮 */
	private Button btn_register;
	/** 同意条款 */
	private ImageView agreeimg;
	private TimeCount time;
	// 填写从短信SDK应用后台注册得到的APPKEY 
	private static String APPKEY = "6a050013eb20";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "b50f1a2dff216f1787191017c2a6d8e1";
	private String phString;
	private String mobile;
	private String psw;
	private boolean flag = true;//是否同意条款
	private boolean flag2 = false;//验证码是否匹配
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		
		
//		System.loadLibrary("OSbase");
		SMSSDK.initSDK(this,APPKEY,APPSECRET);
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
			
		};
		SMSSDK.registerEventHandler(eh);
		initSetlistener();
		time = new TimeCount(60000, 1000);
		
		
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					//验证码成功
					flag2 = true;
					toregister();
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
					Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
					
				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(Register.this, "smssdk_network_error");
				Toast.makeText(Register.this, "验证码错误", Toast.LENGTH_SHORT).show();
				if (resId > 0) {
					Toast.makeText(Register.this, resId, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		
	};
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_back://退出按钮
			
			finish();
			
			break;
			
		case R.id.register_btn_regist://注册按钮
			
			phString=username.getText().toString();
			
			if(!TextUtils.isEmpty(yanzhengma.getText().toString())){
				SMSSDK.submitVerificationCode("86", phString, yanzhengma.getText().toString());
			}else {
				Toast.makeText(this, "验证码不能为空", 1).show();
				return;
			}
			
			
			break;
			
		case R.id.register_agree://同意条款
			if (flag) {
				agreeimg.setImageResource(R.drawable.noagreement);
				flag = false;
			}else {
				agreeimg.setImageResource(R.drawable.agreement);
				flag = true;
			}
			
		    break;
		
		
		
		case R.id.register_btn_yanzhengma://获取验证码按钮
			
			 mobile = username.getText().toString().trim();
			
			 //判断手机号码是否有效
			 if (!CommonUtil.isMobileNO(mobile)) {
				 Toast.makeText(getApplicationContext(), "请输入11位有效手机号码",
							Toast.LENGTH_SHORT).show();
					return;
			}
			
			 
			 
//			//判断手机号长度是否符合
//				if (mobile.length() < 11) {
//					Toast.makeText(getApplicationContext(), "请输入11位有效手机号码",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
			
			
			if (!TextUtils.isEmpty(username.getText().toString())) {
				time.start();
				
			}
			
			if(!TextUtils.isEmpty(username.getText().toString())){
				SMSSDK.getVerificationCode("86",username.getText().toString());
				phString=username.getText().toString();
			}else {
				Toast.makeText(this, "电话不能为空", 1).show();
			}
			
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		username = (EditText) findViewById(R.id.register_username);
		password = (EditText) findViewById(R.id.register_password);
		yanzhengma = (EditText) findViewById(R.id.register_yanzhengma);
		btn_yanzhengma = (Button) findViewById(R.id.register_btn_yanzhengma);
		btn_register = (Button) findViewById(R.id.register_btn_regist);
		agreeimg = (ImageView) findViewById(R.id.register_agree);
		register_back = (ImageView) findViewById(R.id.register_back);
	}

	@Override
	public void initSetlistener() {
		register_back.setOnClickListener(this);
		btn_yanzhengma.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		agreeimg.setOnClickListener(this);
		
	}
	
	
	/**
	 * 获取验证码倒计时
	 * @author wk
	 *
	 */
	
	class TimeCount extends CountDownTimer {
		 
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
 
        @Override
        public void onTick(long millisUntilFinished) {
//        	btn_yanzhengma.setBackgroundColor(Color.parseColor("#B6B6D8"));
        	btn_yanzhengma.setClickable(false);
        	btn_yanzhengma.setText(millisUntilFinished / 1000 + "秒后重新发送");
        }
 
        @Override
        public void onFinish() {
        	btn_yanzhengma.setText("重新获取验证码");
        	btn_yanzhengma.setClickable(true);
//        	btn_yanzhengma.setBackgroundColor(Color.parseColor("#4EB84A"));
 
        }
    }
	
	
	/**
	 * 注册请求方法
	 */
	private void toregister(){
		 mobile = username.getText().toString().trim();
		 psw = password.getText().toString().trim();
		 
		 
		 //判断手机号码是否有效
//		 if (!CommonUtil.isMobileNO(mobile)) {
//			 Toast.makeText(getApplicationContext(), "请输入11位有效手机号码",
//						Toast.LENGTH_SHORT).show();
//				return;
//		}
		
		 
		 
		//判断手机号长度是否符合
			if (mobile.length() < 11) {
				Toast.makeText(getApplicationContext(), "请输入11位有效手机号码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			
			if (TextUtils.isEmpty(mobile)) {
				Toast.makeText(getApplicationContext(), "请输入您的手机号码",
						Toast.LENGTH_SHORT).show();
				return;
			}
		 
		 
		 if (TextUtils.isEmpty(psw)) {
				Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		 
		 
		 
		 
		 
		 if (flag) {
				//不做处理
			}else {
				Toast.makeText(getApplicationContext(), "您必须同意使用协议",
						Toast.LENGTH_SHORT).show();
				return;
			}
		 
		 
		 DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
			DialogUtils.dialog.setMessage("正在注册...");
			// 构造访问网络的URL
			JSONObject params = new JSONObject();
			JSONObject data = new JSONObject();
			try {
				params.put("phone", mobile);
				params.put("password", psw);

				data.put("method", "register");
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
						
						Constant.phonenu = userInfo.getPhone();
						Constant.userId = userInfo.getId();
						Constant.token = userInfo.getToken();
						Constant.psw = userInfo.getPassword();
						Constant.islogin = true;
						
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
						toEditAct();


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
	
	
	
	private void toEditAct(){
		Intent intent = new Intent(Register.this,EditMessage.class);
		intent.putExtra("tag", "register");
		
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
	
	

}
