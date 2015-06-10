/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import static cn.smssdk.framework.utils.R.getStringRes;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.klgz.shakefun.ui.Register.TimeCount;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.ylyq.R;

import android.content.Intent;
import android.graphics.Color;
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

/**
 * @author wk
 *忘记密码界面
 */
public class ForgotPsw extends BasicActivity{
	
	/** 返回按钮*/
	private ImageView forpsw_back;
	/** 用户名 */
	private EditText forpsw_username;
	/**验证码 */
	private EditText forpsw_yanzhengma;
	/** 获取验证码按钮*/
	private Button forpsw_btn_yanzhengma;
	/** 确定 */
	private Button forpsw_btn_forpsw;
	private TimeCount time;
	// 填写从短信SDK应用后台注册得到的APPKEY 
   private static String APPKEY = "6a050013eb20";
		// 填写从短信SDK应用后台注册得到的APPSECRET
   private static String APPSECRET = "b50f1a2dff216f1787191017c2a6d8e1";
	private boolean flag2 = false;
	private String phString;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_forpsw);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initSetlistener();
		
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
					Intent intent = new Intent(ForgotPsw.this,ChangPsw.class);
					intent.putExtra("phone", forpsw_username.getText().toString());
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
					Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
					
				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(ForgotPsw.this, "smssdk_network_error");
				Toast.makeText(ForgotPsw.this, "验证码错误", Toast.LENGTH_SHORT).show();
				if (resId > 0) {
					Toast.makeText(ForgotPsw.this, resId, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		
	};
	
	
	
	
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
        	forpsw_btn_yanzhengma.setClickable(false);
        	forpsw_btn_yanzhengma.setText(millisUntilFinished / 1000 + "秒后重新发送");
        }
 
        @Override
        public void onFinish() {
        	forpsw_btn_yanzhengma.setText("重新获取验证码");
        	forpsw_btn_yanzhengma.setClickable(true);
//        	forpsw_btn_yanzhengma.setBackgroundColor(Color.parseColor("#4EB84A"));
 
        }
    }
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.forpsw_back://退出
			finish();
			break;
		case R.id.forpsw_btn_yanzhengma://获取验证码
			
			if (!TextUtils.isEmpty(forpsw_username.getText().toString())) {
				time.start();
				
			}
			
			if(!TextUtils.isEmpty(forpsw_username.getText().toString())){
				SMSSDK.getVerificationCode("86",forpsw_username.getText().toString());
				phString=forpsw_username.getText().toString();
			}else {
				Toast.makeText(this, "电话不能为空", 1).show();
			}
			
			break;
		
		
		case R.id.forpsw_btn_forpsw://确定按钮
			toChangPswAct();
//			startActivity(new Intent(ForgotPsw.this,ChangPsw.class));
			break;

		default:
			break;
		}
		
	}
	
	private void toChangPswAct(){
		
		if(!TextUtils.isEmpty(forpsw_yanzhengma.getText().toString())){
			SMSSDK.submitVerificationCode("86", phString, forpsw_yanzhengma.getText().toString());
		}else {
			Toast.makeText(this, "验证码不能为空", 1).show();
			return;
		}
	 
	 
	
	 
	}
	

	@Override
	public void initView() {
		
		forpsw_back = (ImageView) findViewById(R.id.forpsw_back);
		forpsw_btn_yanzhengma = (Button) findViewById(R.id.forpsw_btn_yanzhengma);
		forpsw_username = (EditText) findViewById(R.id.forpsw_username);
		forpsw_btn_forpsw = (Button) findViewById(R.id.forpsw_btn_forpsw);
		forpsw_yanzhengma = (EditText) findViewById(R.id.forpsw_yanzhengma);
		
	}

	@Override
	public void initSetlistener() {

		forpsw_back.setOnClickListener(this);
		forpsw_btn_yanzhengma.setOnClickListener(this);
		
		forpsw_btn_forpsw.setOnClickListener(this);
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
	

}
