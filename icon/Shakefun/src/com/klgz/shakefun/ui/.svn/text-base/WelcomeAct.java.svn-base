/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.adapter.ViewPagerAdapter;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.tools.GetFirstJsonUtil;
import com.klgz.shakefun.tools.GetFirstJsonUtil.IJsonPictureback;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.ylyq.R;
import com.umeng.analytics.MobclickAgent;


/**
 * @author wk
 *进入程序的欢迎页
 */
public class WelcomeAct extends Activity implements OnPageChangeListener,IJsonPictureback{
	
	private ImageView welcome_img;
	private MediaPlayer mp3player;
	private RelativeLayout re;//欢迎页布局
	//定义ViewPager对象
		private ViewPager viewPager;
		
		//定义ViewPager适配器
		private ViewPagerAdapter vpAdapter;
		
		//定义一个ArrayList来存放View
		private ArrayList<View> views;

		//引导图片资源
	    private static final int[] pics = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
	    
	    //底部小点的图片
	    private ImageView[] points;
	    
	    //记录当前选中位置
	    private int currentIndex;
	private ImageView wel_kaishitiyan;
	
	
	Handler handler = new Handler();
	
	
	
	//延迟两秒跳转  
	Runnable timelast = new Runnable() {  
			      
			    @Override  
			    public void run() {  
			    	
			        Intent  intent=new Intent(WelcomeAct.this, MainActivity.class);  
			        startActivity(intent);    
			        finish();
			    }  
			};
	//延时播放声音
		Runnable player = new Runnable() {
			
			@Override
			public void run() {
				mp3player.start();
				
			}
		};
		
		
		private Animation rotate;//动画
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		initview();
		
		Constant.phoneID = CommonUtil.getPhoneIME(WelcomeAct.this);
		
		GetFirstJsonUtil.getDatapictureJson(WelcomeAct.this, WelcomeAct.this);
		
		
		
		
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;// 密度值
        float xdpi = metrics.xdpi;
//        float ydpi = metrics.ydpi;
//        double zdpi = Math.sqrt(Math.pow(xdpi, 2) + Math.pow(ydpi, 2));
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        double z = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        System.out.println("--------width---"+width+"----"+height+"---"+density+"---"+xdpi);
        // 根据实践，我个人觉得xdpi这个值也可以这么理解，真正的dpi / xdpi = density ,
        // 所以要获取真正的dpi就成了，xdpi*density,所以最后，根据勾股定理算对角线像素，除以dpi，就算出屏幕尺寸了
        double f = (z / (xdpi * density));
		upjsondata();
		
		
		SharedPreferences sp =  getSharedPreferences("isfirstApp", Context.MODE_PRIVATE);
		boolean flag = sp.getBoolean("isfirst", true);//true表示不是第一次打开
		
		if (flag) {//第一次打开程序
			re.setVisibility(View.VISIBLE);
			initfirstView();
			initData();
			Editor editor = sp.edit();  
			editor.putBoolean("isfirst", false);  
			editor.commit();  

			
		}else{//
			re.setVisibility(View.GONE);
			welcome_img.setVisibility(View.VISIBLE);
			mp3player=new MediaPlayer();  
			mp3player = MediaPlayer.create(getApplicationContext(), R.raw.wel);
			handler.postDelayed(player, 800);
			handler.postDelayed(timelast, 2500);
			
			 rotate = AnimationUtils.loadAnimation(this, R.anim.firstact);
			welcome_img.startAnimation(rotate);
			
			rotate.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					welcome_img.clearAnimation();
					
				}
			});
		}
		
		
	}
	
	/**
	 * 第一次进入是加载欢迎页
	 */
	private void initfirstView(){
		views = new ArrayList<View>();
		
		//实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.wel_viewpager);
		
		//实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);
	}
	
	
	private void initview(){
		re = (RelativeLayout) findViewById(R.id.wel_re);
		welcome_img = (ImageView) findViewById(R.id.welcom_img);
		wel_kaishitiyan = (ImageView) findViewById(R.id.wel_kaishitiyan);
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(WelcomeAct.this);
		MobclickAgent.onResume(this);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(WelcomeAct.this);
		MobclickAgent.onPause(this);
	}
	
	
	
	private void upjsondata(){
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在初始化...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
//			params.put("email", "");
//			params.put("phone", "");
//			params.put("name", "");
//			params.put("delegate", "");
//			params.put("license_image", "");
//			params.put("industry", "");
			params.put("appToken", CommonUtil.getPhoneIME(WelcomeAct.this));

			data.put("method", "openApp");
			data.put("userId", "");
			data.put("token", JPushInterface.getRegistrationID(getApplicationContext()));
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();
		
		
		//测试打印
		System.out.println("------------"+d+"-----"+JPushInterface.getRegistrationID(getApplicationContext()));
		
		String url = Constant.KE_BASICURL+"app!request.action";
//		String url = "http://218.241.7.206:8087/app!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(WelcomeAct.this);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String infos = datas.get(0);
//					Toast.makeText(getApplicationContext(), "是不是好了", 1).show();
//					System.out.println("--------welcominfos------"+infos);
					DialogUtils.closeProgressDialog();
					JPushInterface.setAliasAndTags(getApplicationContext(), 
							JPushInterface.getRegistrationID(getApplicationContext()), 
							null,new TagAliasCallback() {
						
						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							
							
						}
					});
					
					
				}else {
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(timelast);
		handler.removeCallbacks(player);
		if (mp3player!=null) {
			mp3player.stop();
			mp3player.release();
			mp3player = null;
		}
	}

	
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		//定义一个布局并设置参数
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                														  LinearLayout.LayoutParams.FILL_PARENT);
       
        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        } 
        
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wel_ll);       
		
        points = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
        	//得到一个LinearLayout下面的每一个子元素
        	points[i] = (ImageView) linearLayout.getChildAt(i);
        	//默认都设为灰色
        	points[i].setEnabled(true);
        	//给每个小点设置监听
//        	points[i].setOnClickListener(this);
        	//设置位置tag，方便取出与当前位置对应
        	points[i].setTag(i);
        }
        
        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
	}
	
	

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		//设置底部小点选中状态
        setCurDot(position);
        if (position==2) {
        	wel_kaishitiyan.setVisibility(View.VISIBLE);
        	wel_kaishitiyan.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(WelcomeAct.this,MainActivity.class));
					finish();
					
					
				}
			});
		}else {
			wel_kaishitiyan.setVisibility(View.GONE);
		}
		
	}
	
	
	 /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon){
         if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
             return;
         }
         points[positon].setEnabled(false);
         points[currentIndex].setEnabled(true);

         currentIndex = positon;
     }
    
    

	@Override
	public void getJsonpicObject(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			String url = jsonObject.getString("imageUrl");
			
			
//			 //httpGet连接对象  
//			 HttpGet httpRequest = new HttpGet("https://www.baidu.com/img/bd_logo1.png");  
//			 //取得HttpClient 对象  
//			HttpClient httpclient = new DefaultHttpClient(); 
//			//请求httpClient ，取得HttpRestponse  
//			HttpResponse httpResponse = httpclient.execute(httpRequest);  
//
//			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
//				//取得相关信息 取得HttpEntiy  
//				 HttpEntity httpEntity = httpResponse.getEntity();  
//				 //获得一个输入流  
//				 InputStream is = httpEntity.getContent();  
////				 System.out.println(is.available());  
////				 System.out.println("Get, Yes!");  
//				 Bitmap bitmap = BitmapFactory.decodeStream(is);  
//				  is.close();  
////				  welcome_img.setImageBitmap(bitmap);
////				  welcome_img.startAnimation(rotate);
//				      }  

			
			
			
//			if (!TextUtils.isEmpty(url)) {//url不为空
//				BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
////				bitmapUtils.display(welcome_img, url);
//				bitmapUtils.display(welcome_img, "https://www.baidu.com/img/bd_logo1.png");
//				
//			}else {
//				
//			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	}
	
	}
}
