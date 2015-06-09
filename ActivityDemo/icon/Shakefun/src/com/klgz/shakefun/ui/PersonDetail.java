/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.ui.PersonDetail.AsyncImageLoader.ImageCallback;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.ylyq.R;

/**
 * @author wk
 *嘉宾信息界面
 */
public class PersonDetail extends BasicActivity{
	
	/** 后退按钮*/
	private ImageView persondetail_back;
	/** 标题信息*/
	private TextView person_title;
	/**
	 * 姓名，职业，地区，出生日期，星座，血型，身高,简介
	 */
	private TextView persondetail_tvname,person_zhiye,persondetail_name1,person_date,
						person_xingzuo,person_xuexing,persondetail_tall
						,persondetaile_jianjie2;
	/** 图片*/
	private ImageView persondetail_iv1;
	
	
	
	//图片滑动
	private String[] imgurls;
	private ViewPager view_pager;  
    private LayoutInflater inflater;
    private ImageView image;  
    private View item ;  
    private ImageView[] indicator_imgs;//存放引到图片数组  
    List<View> listpager = new ArrayList<View>();  
    private MyAdapter adapter ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_persondetail);
		initView();
		initSetlistener();
		
		view_pager = (ViewPager) findViewById(R.id.persondetail_viewPager);  
		
		inflater = LayoutInflater.from(this); 
		
//		getPersonData("1");
		getPersonData(getIntent().getStringExtra("id"));
		
	}
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.persondetail_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void initView() {
		persondetail_back = (ImageView) findViewById(R.id.persondetail_back);
		person_title = (TextView) findViewById(R.id.person_title);
		
		
		persondetail_tvname = (TextView) findViewById(R.id.persondetail_tvname);//演员姓名
		person_zhiye = (TextView) findViewById(R.id.person_zhiye);//职业
		persondetail_name1 = (TextView) findViewById(R.id.persondetail_name1);//地区
		person_date = (TextView) findViewById(R.id.person_date);//出生日期
		person_xingzuo = (TextView) findViewById(R.id.person_xingzuo);//星座
		person_xuexing = (TextView) findViewById(R.id.person_xuexing);//血型
		persondetail_tall = (TextView) findViewById(R.id.persondetail_tall);//身高
		persondetaile_jianjie2 = (TextView) findViewById(R.id.persondetaile_jianjie2);//简介
		
		persondetail_iv1 = (ImageView) findViewById(R.id.persondetail_iv1);
		
		
		
	}

	@Override
	public void initSetlistener() {
		persondetail_back.setOnClickListener(this);
		
	}
	
	
	private void getPersonData(String id){
		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在加载...");
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
			params.put("id", id);

			data.put("method", "getGuestInfoByGuestId");
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
		String url = Constant.KE_BASICURL+"programGuest!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(PersonDetail.this);
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String infos = datas.get(0);
					System.out.println("--------infos------"+infos);
					DialogUtils.closeProgressDialog();
					
					setUI(infos);
					
					
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
	
	
	private void setUI(String result){
		
		JSONObject jsonObject;
		
			try {
				jsonObject = new JSONObject(result);
				String region = jsonObject.getString("region");
				String birthday = jsonObject.getString("birthday");
				String constellation = jsonObject.getString("constellation");
				String adminName = jsonObject.getString("adminName");
				String profession = jsonObject.getString("profession");
				String approval = jsonObject.getString("approval");
				String city = jsonObject.getString("city");
				String id = jsonObject.getString("id");
				String bloodType = jsonObject.getString("bloodType");
				String guestImageUrl = jsonObject.getString("guestImageUrl");
				String programId = jsonObject.getString("programId");
				String height = jsonObject.getString("height");
				String name = jsonObject.getString("name");
				String introduction = jsonObject.getString("introduction");
				
//				BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
//				bitmapUtils.display(persondetail_iv1, guestImageUrl);
				
				System.out.println("---------region--------"+region);
				person_title.setText(name);
				persondetail_tvname.setText(name);
				person_zhiye.setText(profession);
				persondetail_name1.setText(city);
				person_date.setText(birthday);
				person_xingzuo.setText(constellation);
				person_xuexing.setText(bloodType);
				persondetail_tall.setText(height);
				persondetaile_jianjie2.setText(introduction);
				
				
				imgurls = guestImageUrl.split("&&");
				for (int i = 0; i <imgurls.length; i++) {  
		            item = inflater.inflate(R.layout.viewpager_item, null);  
		            listpager.add(item);  
		        }  
			 indicator_imgs = new ImageView[imgurls.length];
			 adapter = new MyAdapter(listpager);  
		        view_pager.setAdapter(adapter);  
		  
		        //绑定动作监听器：如翻页的动画  
		        view_pager.setOnPageChangeListener(new MyListener());  
		          
		        initIndicator(); 
//				persondetail_tvname,person_zhiye,persondetail_name1,person_date,
//				person_xingzuo,person_xuexing,persondetail_tall
//				,persondetaile_jianjie2
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	
	
	 /** 
     * 初始化引导图标 
     * 动态创建多个小圆点，然后组装到线性布局里 
     */  
    private void initIndicator(){  
          
        ImageView imgView;  
        View v = findViewById(R.id.per_viewGroup);// 线性水平布局，负责动态调整导航图标  
          
        for (int i = 0; i < imgurls.length; i++) {  
            imgView = new ImageView(this);  
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(10,10);  
            params_linear.setMargins(7, 10, 7, 10);  
            imgView.setLayoutParams(params_linear);  
            indicator_imgs[i] = imgView;  
              
            if (i == 0) { // 初始化第一个为选中状态  
                  
                indicator_imgs[i].setBackgroundResource(R.drawable.page_indicator_focused);  
            } else {  
                indicator_imgs[i].setBackgroundResource(R.drawable.page_indicator_unfocused);  
            }  
            
            ((ViewGroup)v).addView(indicator_imgs[i]);  
        }  
          
    }  
    
    
    
    /** 
     * 适配器，负责装配 、销毁  数据  和  组件 。 
     */  
    private class MyAdapter extends PagerAdapter {  
  
        private List<View> mList;  
        private AsyncImageLoader asyncImageLoader;  
  
          
          
        public MyAdapter(List<View> list) {  
            mList = list;  
            asyncImageLoader = new AsyncImageLoader();    
        }  
  
          
          
        /** 
         * Return the number of views available. 
         */  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return mList.size();  
        }  
  
          
        /** 
         * Remove a page for the given position. 
         * 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！ 
         * instantiateItem(View container, int position) 
         * This method was deprecated in API level . Use instantiateItem(ViewGroup, int) 
         */  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object) {  
            // TODO Auto-generated method stub  
            container.removeView(mList.get(position));  
              
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            // TODO Auto-generated method stub  
            return arg0==arg1;  
        }  
  
          
        /** 
         * Create the page for the given position. 
         */  
        @SuppressLint("NewApi")
		@Override  
        public Object instantiateItem(final ViewGroup container, final int position) {  
              
  
            Drawable cachedImage = asyncImageLoader.loadDrawable(  
                    imgurls[position], new ImageCallback() {  
  
                        @SuppressLint("NewApi")
						public void imageLoaded(Drawable imageDrawable,  
                                String imageUrl) {  
  
                            View view = mList.get(position);  
                            image = ((ImageView) view.findViewById(R.id.viewpageritem_image));  
                            image.setBackgroundDrawable(imageDrawable);  
                            container.removeView(mList.get(position));  
                            container.addView(mList.get(position));  
                            // adapter.notifyDataSetChanged();  
  
                        }  
                    });  
  
            View view = mList.get(position);  
            image = ((ImageView) view.findViewById(R.id.viewpageritem_image));  
            image.setBackgroundDrawable(cachedImage);  
  
            container.removeView(mList.get(position));  
            container.addView(mList.get(position));  
            // adapter.notifyDataSetChanged();  
                  
  
            return mList.get(position);  
  
        }  
          
      
    }  
    
    
    
    /** 
     * 动作监听器，可异步加载图片 
     * 
     */  
    private class MyListener implements OnPageChangeListener{  
  
        @Override  
        public void onPageScrollStateChanged(int state) {  
            // TODO Auto-generated method stub  
            if (state == 0) {  
                //new MyAdapter(null).notifyDataSetChanged();  
            }  
        }  
  
          
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
              
        }  
  
        @Override  
        public void onPageSelected(int position) {  
              
            // 改变所有导航的背景图片为：未选中  
            for (int i = 0; i < indicator_imgs.length; i++) {  
                  
                indicator_imgs[i].setBackgroundResource(R.drawable.page_indicator_unfocused);  
                  
            }  
              
            // 改变当前背景图片为：选中  
            indicator_imgs[position].setBackgroundResource(R.drawable.page_indicator_focused);  
        }  
          
          
    }  
      
      
  
    /** 
     * 异步加载图片 
     */  
    static class AsyncImageLoader {  
  
        // 软引用，使用内存做临时缓存 （程序退出，或内存不够则清除软引用）  
        private HashMap<String, SoftReference<Drawable>> imageCache;  
  
        public AsyncImageLoader() {  
            imageCache = new HashMap<String, SoftReference<Drawable>>();  
        }  
  
        /** 
         * 定义回调接口 
         */  
        public interface ImageCallback {  
            public void imageLoaded(Drawable imageDrawable, String imageUrl);  
        }  
  
          
        /** 
         * 创建子线程加载图片 
         * 子线程加载完图片交给handler处理（子线程不能更新ui，而handler处在主线程，可以更新ui） 
         * handler又交给imageCallback，imageCallback须要自己来实现，在这里可以对回调参数进行处理 
         * 
         * @param imageUrl ：须要加载的图片url 
         * @param imageCallback： 
         * @return 
         */  
        public Drawable loadDrawable(final String imageUrl,  
                final ImageCallback imageCallback) {  
              
            //如果缓存中存在图片  ，则首先使用缓存  
            if (imageCache.containsKey(imageUrl)) {  
                SoftReference<Drawable> softReference = imageCache.get(imageUrl);  
                Drawable drawable = softReference.get();  
                if (drawable != null) {  
                    imageCallback.imageLoaded(drawable, imageUrl);//执行回调  
                    return drawable;  
                }  
            }  
  
            /** 
             * 在主线程里执行回调，更新视图 
             */  
            final Handler handler = new Handler() {  
                public void handleMessage(Message message) {  
                    imageCallback.imageLoaded((Drawable) message.obj, imageUrl);  
                }  
            };  
  
              
            /** 
             * 创建子线程访问网络并加载图片 ，把结果交给handler处理 
             */  
            new Thread() {  
                @Override  
                public void run() {  
                    Drawable drawable = loadImageFromUrl(imageUrl);  
                    // 下载完的图片放到缓存里  
                    imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));  
                    Message message = handler.obtainMessage(0, drawable);  
                    handler.sendMessage(message);  
                }  
            }.start();  
              
            return null;  
        }  
  
          
        /** 
         * 下载图片  （注意HttpClient 和httpUrlConnection的区别） 
         */  
        public Drawable loadImageFromUrl(String url) {  
  
            try {  
                HttpClient client = new DefaultHttpClient();  
                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000*15);  
                HttpGet get = new HttpGet(url);  
                HttpResponse response;  
  
                response = client.execute(get);  
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                    HttpEntity entity = response.getEntity();  
  
                    Drawable d = Drawable.createFromStream(entity.getContent(),  
                            "src");  
  
                    return d;  
                } else {  
                    return null;  
                }  
            } catch (ClientProtocolException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
  
            return null;  
        }  
  
        //清除缓存  
        public void clearCache() {  
  
            if (this.imageCache.size() > 0) {  
  
                this.imageCache.clear();  
            }  
  
        }  
  
    }  
	
	

}