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
import com.klgz.shakefun.enginemp.GetMsg;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wk
 *编辑个人信息界面
 */
public class EditMessage extends BasicActivity{
	
	
	/** 后退按钮 */
	private ImageView edit_back;
	/** 保存 */
	private TextView edit_savemessage;
	/** 用户名 */
	private EditText edit_username;
	/** 男，女 */
	private LinearLayout edit_lnman,edit_lnwoman;
	/** 男女选中图片 */
	private ImageView edit_sexchoice,edit_sexchoice2;
	/** 手机号*/
	private EditText edit_phonenum;
	/** 邮箱*/
	private EditText edit_emailadd;
	/** 联系地址 */
	private EditText edit_address;
	private boolean flag1 = true,flag2 = true;//男女选择
	
	private String username,sex="0",phone,emailstr,addressstr;
	private String tag = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_editmessage);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initSetlistener();
		edit_phonenum.setText(Constant.phonenu);
		edit_phonenum.setFocusable(false);
		
		tag = getIntent().getStringExtra("tag");
		if ("register".equals(tag)) {
			
		}else if("login".equals(tag)){
			//加载数据
//			CommonUtil.custoast(getApplicationContext(), "login");
			getData();
			
			
		}
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_back:
			finish();
			break;
		case R.id.edit_savemessage://保存按钮
			username = edit_username.getText().toString().trim();
			if (username.length()>20) {
				CommonUtil.custoast(getApplicationContext(), "你输入的字符过长");
				return;
			}
			
			
			phone = edit_phonenum.getText().toString().trim();
			emailstr = edit_emailadd.getText().toString().trim();
			addressstr = edit_address.getText().toString().trim();
			
			if (!TextUtils.isEmpty(username)) {
				Constant.username = username;
			}
			
			if (!TextUtils.isEmpty(username)||!TextUtils.isEmpty(phone)||!TextUtils.isEmpty(emailstr)||!TextUtils.isEmpty(addressstr)) {
				saveMessage();
			}else {
				startActivity(new Intent(EditMessage.this,LogingedCenter.class));
				finish();
				
				
			}
			
			
			
			break;
		case R.id.edit_lnman:
				flag1 = true;
				edit_sexchoice.setImageResource(R.drawable.sexchoicey);
				edit_sexchoice2.setImageResource(R.drawable.sexchoicen);
				
			
			
			break;
		case R.id.edit_lnwoman:
				flag1 = false;
				edit_sexchoice.setImageResource(R.drawable.sexchoicen);
				edit_sexchoice2.setImageResource(R.drawable.sexchoicey);
			
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		edit_back = (ImageView) findViewById(R.id.edit_back);
		edit_savemessage = (TextView) findViewById(R.id.edit_savemessage);
		
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_lnman = (LinearLayout) findViewById(R.id.edit_lnman);
		edit_lnwoman = (LinearLayout) findViewById(R.id.edit_lnwoman);
		edit_sexchoice = (ImageView) findViewById(R.id.edit_sexchoice);
		edit_sexchoice2 = (ImageView) findViewById(R.id.edit_sexchoice2);
		edit_phonenum = (EditText) findViewById(R.id.edit_phonenum);
		edit_emailadd = (EditText) findViewById(R.id.edit_emailadd);
		edit_address = (EditText) findViewById(R.id.edit_address);
		
		
		
	}

	@Override
	public void initSetlistener() {
		edit_back.setOnClickListener(this);
		edit_savemessage.setOnClickListener(this);
		edit_lnman.setOnClickListener(this);
		edit_lnwoman.setOnClickListener(this);
		
		
	}
	
	
	private void saveMessage(){
		
		if (flag1) {
			sex = "0";
		}else {
			sex = "1";
		}
		
		
		
		
		
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在提交...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("phone", phone);
			params.put("name", username);
			params.put("sex", sex);
			params.put("email", emailstr);
			params.put("address", addressstr);
			params.put("userToken", Constant.phoneID);
			

			data.put("method", "modifyUserInfo");
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
					
					startActivity(new Intent(EditMessage.this,
							LogingedCenter.class));
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
	
	private void getData(){
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在加载...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("password", Constant.psw);
			params.put("phone", Constant.phonenu);
			

			data.put("method", "getuserInfo");
			data.put("userId", Constant.userId);
			data.put("token", Constant.token);
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

		GetMsg engine = new GetMsg(getApplicationContext());
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);
					System.out.println("-----------datas-----"+userInfo);

					DialogUtils.closeProgressDialog();
					
					setUI(userInfo);
					
//					edit_username.setText(userInfo.getName());
//					if (userInfo.getSex().equals("0")) {
//						edit_sexchoice.setImageResource(R.drawable.sexchoicey);
//						edit_sexchoice2.setImageResource(R.drawable.sexchoicen);
//					}else {
//						edit_sexchoice.setImageResource(R.drawable.sexchoicen);
//						edit_sexchoice2.setImageResource(R.drawable.sexchoicey);
//					}
////					edit_phonenum.setText(userInfo.getPhone());
//					edit_emailadd.setText(userInfo.getEmail());
//					edit_address.setText(userInfo.getAddress());
					
					
					


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
	
	private void setUI(String result){
		
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject object = jsonObject.getJSONObject("userInfo");
			
			System.out.println("------object----"+object.toString());
			
			edit_username.setText(object.get("name").toString());
			if (object.get("sex").toString().equals("0")) {
				edit_sexchoice.setImageResource(R.drawable.sexchoicey);
				edit_sexchoice2.setImageResource(R.drawable.sexchoicen);
			}else {
				edit_sexchoice.setImageResource(R.drawable.sexchoicen);
				edit_sexchoice2.setImageResource(R.drawable.sexchoicey);
			}
			edit_emailadd.setText(object.getString("email"));
			edit_address.setText(object.getString("address"));
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
