/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.tools.NetworkDetector;
import com.klgz.shakefun.tools.UploadFile;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.ylyq.R;

/**
 * @author wk
 *商户中心界面
 */
public class MerchantAct extends BasicActivity{
	
	
	/** 后退按钮 */
	private ImageView merchant_back;
	/** 提交审核*/
	private TextView merchant_submit;
	/** 企业名称*/
	private EditText merchant_companyname;
	/** 法人代表，联系电话，邮箱，所属行业，注册地址 */
	private EditText merchant_corporate,merchant_phonenum,merchant_emailadd,merchant_industry
									,merchant_regaddress;
	/** 营业执照 */
	private ImageView merchant_addimg;
	/** 点击添加营业执照 */
	private PopupWindow popupWindow;
	/** popuwindow中的按钮 */
	private Button popmer_takepic,popmer_album,popmer_cancle;
	private LinearLayout merchant_popContainer;
	private UploadFile uploadFile;
	private boolean flag = false;
	
	public static final int NONE = 0;  
    public static final int PHOTOHRAPH = 1;// 拍照   
    public static final int PHOTOZOOM = 2; // 缩放   
    public static final int PHOTORESOULT = 3;// 结果   
  
    public static final String IMAGE_UNSPECIFIED = "image/*";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_merchant);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initSetlistener();
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.merchant_back://退出按钮
			finish();
			
			break;
		case R.id.merchant_submit://提交审核
			
			
			issubmit();
			
			
			
			break;
		case R.id.merchant_addimg://添加营业执照
			getPopuWindow();
			popupWindow.showAtLocation(merchant_popContainer, Gravity.BOTTOM, 0, 0);
			
			
			break;
		case R.id.popmer_takepic://拍照
			Intent intenttakepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			intenttakepic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));  
            startActivityForResult(intenttakepic, PHOTOHRAPH); 
			
			popupWindow.dismiss();
			
			break;
		case R.id.popmer_album://打开图库
			 Intent intentalbum = new Intent(Intent.ACTION_PICK, null);  
			 intentalbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);  
             startActivityForResult(intentalbum, PHOTOZOOM);
			popupWindow.dismiss();
			break;
		case R.id.popmer_cancle://取消
			popupWindow.dismiss();
			break;

		default:
			break;
		}
		
	}
	/**
	 * 提交前判定
	 */
	
	private void issubmit(){
		if (TextUtils.isEmpty(merchant_companyname.getText().toString())) {
			CommonUtil.custoast(getApplicationContext(), "请输入企业名称");
			return;
		}
		
		if (TextUtils.isEmpty(merchant_corporate.getText().toString())) {
			
			CommonUtil.custoast(getApplicationContext(), "请输入法人代表");
			return;
		}
		
		if (TextUtils.isEmpty(merchant_phonenum.getText().toString())) {
			CommonUtil.custoast(getApplicationContext(), "请输入联系电话");
			return;
			
		}
		if (TextUtils.isEmpty(merchant_emailadd.getText().toString())) {
			CommonUtil.custoast(getApplicationContext(), "请输入邮箱");
			return;
		}
		if (TextUtils.isEmpty(merchant_industry.getText().toString())) {
			CommonUtil.custoast(getApplicationContext(), "请输入所属行业");
			return;
		}
		if (TextUtils.isEmpty(merchant_regaddress.getText().toString())) {
			CommonUtil.custoast(getApplicationContext(), "请输入注册地址");
			return;
		}
		
		if (!flag) {
			CommonUtil.custoast(getApplicationContext(), "请添加证件照");
			return;
		}
		
		upimage("temp.jpg");
		
	}
	
	

	@Override
	public void initView() {
		merchant_popContainer = (LinearLayout) findViewById(R.id.merchant_popContainer);
		merchant_back = (ImageView) findViewById(R.id.merchant_back);
		merchant_submit = (TextView) findViewById(R.id.merchant_submit);
		merchant_companyname = (EditText) findViewById(R.id.merchant_companyname);
		
		merchant_corporate = (EditText) findViewById(R.id.merchant_corporate);
		merchant_phonenum = (EditText) findViewById(R.id.merchant_phonenum);
		merchant_emailadd = (EditText) findViewById(R.id.merchant_emailadd);
		merchant_industry = (EditText) findViewById(R.id.merchant_industry);
		merchant_regaddress = (EditText) findViewById(R.id.merchant_regaddress);
		
		merchant_addimg = (ImageView) findViewById(R.id.merchant_addimg);
		
		
	}

	@Override
	public void initSetlistener() {
		merchant_addimg.setOnClickListener(this);
		merchant_back.setOnClickListener(this);
		merchant_submit.setOnClickListener(this);
	}
	
	/**
	 * 初始化popwindow
	 */
	private void initPopuwindow(){
		View popView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_merchant, null);
		popupWindow = new PopupWindow(popView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,true);
//		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.setAnimationStyle(R.style.merchantbottom);
		
		popmer_takepic = (Button) popView.findViewById(R.id.popmer_takepic);
		popmer_album = (Button) popView.findViewById(R.id.popmer_album);
		popmer_cancle = (Button) popView.findViewById(R.id.popmer_cancle);
		
		popmer_album.setOnClickListener(this);
		popmer_cancle.setOnClickListener(this);
		popmer_takepic.setOnClickListener(this);
		
		
		
	}
	
	
	/**
	 * 获取popuwindow实例
	 */
	public void getPopuWindow() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuwindow();
		}

	}
	
	
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == NONE)  
            return;  
        // 拍照   
        if (requestCode == PHOTOHRAPH) {  
            //设置文件保存路径这里放在跟目录下   
            File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");  
            startPhotoZoom(Uri.fromFile(picture));  
        }  
          
        if (data == null)  
            return;  
          
        // 读取相册缩放图片   
        if (requestCode == PHOTOZOOM) {  
            startPhotoZoom(data.getData());  
        }  
        // 处理结果   
        if (requestCode == PHOTORESOULT) {  
            Bundle extras = data.getExtras();  
            if (extras != null) {  
                Bitmap photo = extras.getParcelable("data");  
                ByteArrayOutputStream stream = new ByteArrayOutputStream();  
//                photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);// (0 - 100)压缩文件   
                FileOutputStream foutput = null;  
                try {  
                    foutput = new FileOutputStream(Environment.getExternalStorageDirectory() + "/temp.jpg");  
                    photo.compress(Bitmap.CompressFormat.PNG, 80, foutput);  
                } catch (FileNotFoundException e) {  
                    e.printStackTrace();  
                }finally{  
                    if(null != foutput){  
                        try {  
                            foutput.close();  
                        } catch (IOException e) {  
                            e.printStackTrace();  
                        }  
                    }  
                }  
                
                
                merchant_addimg.setImageBitmap(photo);	
               flag = true;
            }  
  
        }  
  
        super.onActivityResult(requestCode, resultCode, data);  
    }  
  
    public void startPhotoZoom(Uri uri) {  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例   
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        // outputX outputY 是裁剪图片宽高   
        intent.putExtra("outputX", 300);  
        intent.putExtra("outputY", 300);  
        intent.putExtra("return-data", true);  
        startActivityForResult(intent, PHOTORESOULT);  
    }  
    
    private void upimage(final String name) {
    	
    	if (!Constant.islogin) {
			CommonUtil.custoast(getApplicationContext(), "请先登录");
			return;
		}
    	
    	String email = merchant_emailadd.getText().toString().trim();
		String phone = merchant_phonenum.getText().toString().trim();
		
		if (!CommonUtil.isMobileNO(phone)) {
			CommonUtil.custoast(getApplicationContext(), "请核对您的手机号码");
			return;
		}
		if (!CommonUtil.isEmail(email)) {
			CommonUtil.custoast(getApplicationContext(), "请核对您的邮箱地址");
			return;
		}


		new Thread() {
			@Override
			public void run() {
			
					String info = uploadFile.uploadFile("http://125.208.12.71:8080/imageupload",
							"/sdcard/"+ name, "wk.jpg");
					
					Message message = Message.obtain();
					
					message.obj = info;
					message.what = 1;
					handler.sendMessage(message);
				
			}
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				
//				CommonUtil.custoast(getApplicationContext(), "图片上传成功");
				
				System.out.println("--------obj----"+msg.obj);
				
				try {
					JSONObject jsb = new JSONObject(msg.obj.toString());
					String url = jsb.getString("imageurl");
//					System.out.println("------url------"+url+"-------"+Constant.islogin);
					
					
					
					if (Constant.islogin) {
						sendjson(url);
					}else {
						
						CommonUtil.custoast(getApplicationContext(), "请先登录");
					}
						
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	private void sendjson(String urlimg){
		if (!NetworkDetector.isConn(getApplicationContext())) {
			CommonUtil.custoast(getApplicationContext(), "暂无网络");
			return;
		}
		
		
		String name = merchant_companyname.getText().toString().trim();//企业名称
		String delegate = merchant_corporate.getText().toString().trim();//法人代表
		String license_image = urlimg;
		String industry = merchant_industry.getText().toString().trim();
		String address = merchant_regaddress.getText().toString().trim();
		String email = merchant_emailadd.getText().toString().trim();
		String phone = merchant_phonenum.getText().toString().trim();
		
		
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在提交...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("name", name);
			params.put("delegate", delegate);
			params.put("licenseImageUrl", license_image);
			params.put("email", email);
			params.put("industry", industry);
			params.put("address", address);
			
			params.put("phone", phone);
			

			data.put("method", "register");
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
		String url = Constant.KE_BASICURL+"company!request.action";
//		String url = "http://192.168.0.112:8080/company!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(getApplicationContext());
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String userInfo = datas.get(0);

					DialogUtils.closeProgressDialog();
					CommonUtil.custoast(getApplicationContext(), "提交成功");
					
					


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
