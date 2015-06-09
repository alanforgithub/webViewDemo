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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.klgz.shakefun.bean.Status;
import com.klgz.shakefun.engine.PostResult;
import com.klgz.shakefun.enginemp.GetMsg;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonVerson;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.DialogUtils;
import com.klgz.shakefun.utils.FileUtils;
import com.klgz.shakefun.utils.UpdateUtils;
import com.klgz.ylyq.R;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * @author wk 主界面
 */
public class MainActivity extends Activity implements OnClickListener,
		AMapLocationListener, Runnable, IJsonVerson {

	/** 日期，地址定位 */
	private TextView main_date, main_address;
	/** 美食,我要合作 */
	private ImageView main_cate, main_coorper;
	/** 掉馅饼,点播，直播，地方版，摇美女 */
	private ImageView main_diaoxianbing, main_dianbo, main_zhibo, main_place,
			main_girl;
	/** 玩，品牌互动,logo图标 */
	private ImageView main_play, main_brand, main_iv1;
	/**
	 * 点击按钮：美食，掉馅饼，个人中心， 点播，直播 地方版，摇美女 玩，品牌互动
	 */
	private RelativeLayout main_re_cate, main_re_diaoxianbing,
			main_re_mycenter, main_re_dianbo, main_re_zhibo, main_re_place,
			main_re_girl, main_re_play, main_re_brand;

	BitmapFactory.Options opts;
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();
	private Typeface typeface;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	/**
	 * 摇美食，掉馅饼，爱旅行，看直播，聚娱乐，台北，享生活，品牌秀
	 */
	private TextView main_firstname, main_secondname, main_thirdname,
			main_forthname, main_fifthname, main_sixname, main_seventhname,
			main_eightname;
	// 中奖公告
	private TextView main_changwords;
	// 推送标志
	public static boolean isForeground = false;

	private boolean iscoonnet = true;

	private TextView main_board;

	BroadcastReceiver connectionReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				Log.i("tag", "unconnect");
				// unconnect network
				Toast.makeText(getApplicationContext(), "暂无网络连接", 1).show();
			} else {
				// Toast.makeText(getApplicationContext(), "you网络", 1).show();
				// connect network
				if (iscoonnet && list.size() == 0) {
					getData();
					// iscoonnet = false;
					// Toast.makeText(getApplicationContext(), "you网络",
					// 1).show();

				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);// U盟自动更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);// 非wifi状态下提醒
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_main);
		initView();
		JPushInterface.init(getApplicationContext());
		registerMessageReceiver();
		// 版本检测接口
		// GetJsonUtils.getVersonInfo(getApplicationContext(),
		// MainActivity.this);
		// toroundcorner();
		initSetListener();
		getdateadd();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionReceiver, intentFilter);
		aMapLocManager = LocationManagerProxy.getInstance(this);
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
		// handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位

	}

	private void getdateadd() {
		Time time = new Time("GMT+8");
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		// main_date.setTypeface(typeface);
		main_date.setText(year + "/" + (month + 1) + "/" + day);

	}

	private void initView() {

		main_date = (TextView) findViewById(R.id.main_date);
		main_address = (TextView) findViewById(R.id.main_address);
		main_cate = (ImageView) findViewById(R.id.main_cate);

		main_coorper = (ImageView) findViewById(R.id.main_coorper);// 我要合作
		main_diaoxianbing = (ImageView) findViewById(R.id.main_diaoxianbing);
		main_dianbo = (ImageView) findViewById(R.id.main_dianbo);
		main_zhibo = (ImageView) findViewById(R.id.main_zhibo);
		main_place = (ImageView) findViewById(R.id.main_place);
		main_girl = (ImageView) findViewById(R.id.main_girl);
		main_play = (ImageView) findViewById(R.id.main_play);
		main_brand = (ImageView) findViewById(R.id.main_brand);

		main_re_cate = (RelativeLayout) findViewById(R.id.main_re_cate);
		main_re_diaoxianbing = (RelativeLayout) findViewById(R.id.main_re_diaoxianbing);
		main_re_mycenter = (RelativeLayout) findViewById(R.id.main_re_mycenter);
		main_re_dianbo = (RelativeLayout) findViewById(R.id.main_re_dianbo);
		main_re_zhibo = (RelativeLayout) findViewById(R.id.main_re_zhibo);
		main_re_place = (RelativeLayout) findViewById(R.id.main_re_place);
		main_re_girl = (RelativeLayout) findViewById(R.id.main_re_girl);
		main_re_play = (RelativeLayout) findViewById(R.id.main_re_play);
		main_re_brand = (RelativeLayout) findViewById(R.id.main_re_brand);

		// main_firstname,main_secondname,main_thirdname,main_forthname,
		// main_fifthname,main_sixname,main_seventhname,main_eightname;
		main_firstname = (TextView) findViewById(R.id.main_firstname);
		main_secondname = (TextView) findViewById(R.id.main_secondname);
		main_thirdname = (TextView) findViewById(R.id.main_thirdname);
		main_forthname = (TextView) findViewById(R.id.main_forthname);
		main_fifthname = (TextView) findViewById(R.id.main_fifthname);

		main_sixname = (TextView) findViewById(R.id.main_sixname);
		main_seventhname = (TextView) findViewById(R.id.main_seventhname);
		main_eightname = (TextView) findViewById(R.id.main_eightname);

		main_changwords = (TextView) findViewById(R.id.main_changwords);
		main_iv1 = (ImageView) findViewById(R.id.main_iv1);

		main_board = (TextView) findViewById(R.id.main_board);// 中奖公告

		// typeface = Typeface.createFromAsset(getAssets(),
		// "fonts/songti.TTF");//导入字体

		opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		// opts.inJustDecodeBounds = true;

		// 圆角图片
		// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.cate,opts);
		// Bitmap cateBitmap = CommonUtil.toRoundCorner(bitmap, 40);
		// main_cate.setImageBitmap(cateBitmap);

	}

	private void initSetListener() {
		main_board.setOnClickListener(this);// 中奖公告

		main_coorper.setOnClickListener(this);

		main_re_cate.setOnClickListener(this);
		main_re_mycenter.setOnClickListener(this);
		main_re_diaoxianbing.setOnClickListener(this);

		main_re_dianbo.setOnClickListener(this);
		main_re_zhibo.setOnClickListener(this);
		main_re_place.setOnClickListener(this);
		main_re_girl.setOnClickListener(this);

		main_re_play.setOnClickListener(this);
		main_re_brand.setOnClickListener(this);

		main_iv1.setOnClickListener(this);

	}

	// /**
	// * 圆角图片
	// */
	// private void toroundcorner(){
	// //掉馅饼
	// Bitmap diaoxianBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.diaoxianbing_img,opts);
	// main_diaoxianbing.setImageBitmap(CommonUtil.toRoundCorner(diaoxianBitmap,
	// 40));
	// diaoxianBitmap.recycle();
	// diaoxianBitmap = null;
	//
	// //点播
	// Bitmap dianbobBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.dianbo_img,opts);
	// main_dianbo.setImageBitmap(CommonUtil.toRoundCorner(dianbobBitmap, 40));
	// dianbobBitmap.recycle();
	//
	// //直播
	// Bitmap zhibBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.zhibo_img,opts);
	// main_zhibo.setImageBitmap(CommonUtil.toRoundCorner(zhibBitmap, 40));
	// zhibBitmap.recycle();
	//
	// //地方版
	// Bitmap placeBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.place_img,opts);
	// main_place.setImageBitmap(CommonUtil.toRoundCorner(placeBitmap, 20));
	// placeBitmap.recycle();
	//
	// //摇美女
	// Bitmap girBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.gir_img,opts);
	// main_girl.setImageBitmap(CommonUtil.toRoundCorner(girBitmap, 20));
	// girBitmap.recycle();
	//
	// //玩
	// Bitmap playBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.play_img,opts);
	// main_play.setImageBitmap(CommonUtil.toRoundCorner(playBitmap, 30));
	// playBitmap.recycle();
	//
	// //品牌互动
	// Bitmap brandBitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.brand_img,opts);
	// main_brand.setImageBitmap(CommonUtil.toRoundCorner(brandBitmap, 30));
	// brandBitmap.recycle();
	//
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.main_coorper:// 我要合作
			Intent merchantiIntent = new Intent(MainActivity.this,
					MerchantAct.class);
			startActivity(merchantiIntent);

			break;

		case R.id.main_re_cate:// 摇美食
			if (list.size() > 0) {
				Intent cateIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				cateIntent.putExtra("id", list.get(0).get("id").toString());
				startActivity(cateIntent);

			} else {
				CommonUtil.custoast(getApplicationContext(), "获取频道信息失败");
			}

			break;

		case R.id.main_re_mycenter:// 个人中心
			if (Constant.islogin) {
				startActivity(new Intent(MainActivity.this,
						LogingedCenter.class));
			} else {
				Intent loginIntent = new Intent(MainActivity.this,
						LoginMycenterAct.class);
				startActivity(loginIntent);

			}

			break;
		case R.id.main_re_diaoxianbing:// 掉馅饼
			if (list.size() > 0) {
				Intent dianoIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				dianoIntent.putExtra("id", list.get(1).get("id").toString());
				startActivity(dianoIntent);

			} else {
				CommonUtil.custoast(getApplicationContext(), "获取频道信息失败");
			}

			break;
		case R.id.main_re_dianbo:// 台北
			if (list.size() > 0) {
				Intent dianboIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				dianboIntent.putExtra("id", list.get(2).get("id").toString());
				startActivity(dianboIntent);

			}

			break;
		case R.id.main_re_zhibo:// 直播

			Intent zhiboIntent = new Intent(MainActivity.this, WebviewAct.class);
			// zhiboIntent.putExtra("id", list.get(3).get("id").toString());
			startActivity(zhiboIntent);
			break;
		case R.id.main_re_place:// 爱旅行
			if (list.size() > 0) {
				Intent plaIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				plaIntent.putExtra("id", list.get(5).get("id").toString());
				startActivity(plaIntent);

			}
			break;
		case R.id.main_re_girl:// 享生活
			if (list.size() > 0) {

				Intent girlIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				girlIntent.putExtra("id", list.get(6).get("id").toString());
				startActivity(girlIntent);
			}

			break;
		case R.id.main_re_play:// 聚娱乐
			if (list.size() > 0) {

				Intent plaIntent2 = new Intent(MainActivity.this,
						HorizonListAct.class);
				plaIntent2.putExtra("id", list.get(4).get("id").toString());
				startActivity(plaIntent2);
			}
			break;
		case R.id.main_re_brand:// 品牌秀
			if (list.size() > 0) {

				Intent brandiIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				brandiIntent.putExtra("id", list.get(7).get("id").toString());
				;
				startActivity(brandiIntent);
			}

			break;
		case R.id.main_iv1:// “关于”跳转
			startActivity(new Intent(MainActivity.this, AboutUs.class));
			break;

		case R.id.main_board:// 中奖公告
			startActivity(new Intent(MainActivity.this, AboutBoard.class));
			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
		MobclickAgent.onPause(this);
		stopLocation();// 停止定位
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
		stopLocation();
		if (connectionReceiver != null) {
			unregisterReceiver(connectionReceiver);
		}
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		if (aMapLocation == null) {
			// ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
			// main_address.setText("北京");
			// stopLocation();// 销毁掉定位
		}

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			// String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
			// + "\n精    度    :" + location.getAccuracy() + "米"
			// + "\n定位方式:" + location.getProvider() + "\n定位时间:"
			// + AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
			// + cityCode + "\n位置描述:" + desc + "\n省:"
			// + location.getProvince() + "\n市:" + location.getCity()
			// + "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
			// .getAdCode());
			String str = location.getCity();
			String str2 = str.replace("市", "");
			// main_address.setTypeface(typeface);
			main_address.setText(str2);
			if (!TextUtils.isEmpty(str)) {
				main_sixname.setText(str2);
				stopLocation();
			} else {
				main_sixname.setText("北京");
			}

		}

	}

	private void getData() {

		DialogUtils.showProgressDialog(this, Constant.Dialog_message_Jiazai);
		DialogUtils.dialog.setMessage("正在加载...");
		// 构造访问网络的URL
		JSONObject params = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			params.put("email", "");
			params.put("phone", "");
			params.put("name", "");
			params.put("delegate", "");
			params.put("license_image", "");
			params.put("industry", "");
			params.put("address", "");

			data.put("method", "getChannelInfo");
			data.put("userId", "");
			data.put("token", "");
			data.putOpt("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String d = data.toString();

		// 测试打印
		// System.out.println("------------"+d);

		// String url = Constant.LOGIN;
		// String url =
		// "http://192.168.0.112:8087/appUser!request.action?data=";
		String url = Constant.KE_BASICURL + "channel!request.action";
		// String url = "http://218.241.7.206:8087/channel!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(getApplicationContext());
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String infos = datas.get(0);
					// System.out.println("--------infos------"+infos);
					// Toast.makeText(getApplicationContext(), "getchanleinfo",
					// 1).show();
					DialogUtils.closeProgressDialog();

					setUI(infos);

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

	private void setUI(String result) {

		JSONObject jsonObject;
		Map<String, Object> map = null;
		try {
			jsonObject = new JSONObject(result);

			main_changwords.setText(jsonObject.get("userWinPrizeInfo")
					.toString());

			// jsonObject = jsonObject.getJSONObject("result");
			JSONArray jsonArray = jsonObject.getJSONArray("list");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.get("id"));
				map.put("adminId", object.get("adminId"));
				map.put("adminName", object.get("adminName"));
				map.put("status", object.get("status"));
				map.put("reason", object.get("reason"));
				map.put("imageUrl", object.get("imageUrl"));
				map.put("name", object.get("name"));
				map.put("appSeat", object.get("appSeat"));
				map.put("approval", object.get("approval"));
				list.add(map);

			}
			// System.out.println("-----------list----"+list.toString());
			BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());

			// System.out.println("---------firstname-----"+list.get(2).get("name").toString());
			main_firstname.setText(list.get(0).get("name").toString());
			main_secondname.setText(list.get(1).get("name").toString());
			main_thirdname.setText(list.get(2).get("name").toString());
			main_forthname.setText(list.get(3).get("name").toString());

			main_fifthname.setText(list.get(4).get("name").toString());
			// main_sixname.setText(list.get(5).get("name").toString());
			main_seventhname.setText(list.get(6).get("name").toString());
			// 注释 2015/5/18 feng 防止数组越界异常
			main_eightname.setText(list.get(7).get("name").toString());

			// System.out.println(" ----------imagurl--------"+list.get(0).get("imageUrl").toString());
			//

			bitmapUtils.display(main_cate, list.get(0).get("imageUrl")
					.toString());
			bitmapUtils.display(main_diaoxianbing, list.get(1).get("imageUrl")
					.toString());
			bitmapUtils.display(main_dianbo, list.get(2).get("imageUrl")
					.toString());
			bitmapUtils.display(main_zhibo, list.get(3).get("imageUrl")
					.toString());
			bitmapUtils.display(main_play, list.get(4).get("imageUrl")
					.toString());
			bitmapUtils.display(main_place, list.get(5).get("imageUrl")
					.toString());
			bitmapUtils.display(main_girl, list.get(6).get("imageUrl")
					.toString());
			// 注释 2015/5/18 feng 防止数组越界异常
			bitmapUtils.display(main_brand, list.get(7).get("imageUrl")
					.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long firstTime = 0;

	@SuppressLint("SdCardPath")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else { // 两次按键小于2秒时，退出应用
			// System.exit(0);
				FileUtils
						.deleteDir("/sdcard/shakefun/http://125.208.12.71:8080/upload/");
				finish();
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 极光推送
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!TextUtils.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				// setCostomMsg(showMsg.toString());
			}
		}
	}

	/**
	 * 版本检测
	 */
	@Override
	public void getDataVerson(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject object = jsonObject.getJSONObject("DownloadUrl");
			int versoncode = Integer.parseInt(object.getString("extra2"));
			String msg = object.getString("extra1");// 更新内容
			String url = object.getString("imageUrl");// 下载链接

			System.out.println("-----versoncode---" + versoncode);
			if (versoncode != UpdateUtils.getVersonCode(MainActivity.this)) {
				CommonUtil.custoast(MainActivity.this, "版本不一致");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// private void setCostomMsg(String msg){
	// if (null != msgText) {
	// msgText.setText(msg);
	// msgText.setVisibility(android.view.View.VISIBLE);
	// }
	// }

}
