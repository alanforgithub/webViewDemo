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

	private TextView main_address;
	private ImageView main_coorper;
	private ImageView v_show_img, suprise_img;
	private ImageView main_iv1, travel_img;
	private ImageView mine_img, main_re_brand, main_re_zhibo, place_img;

	BitmapFactory.Options opts;
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();
	private Typeface typeface;
	private List<Map<String, Object>> requestList = new ArrayList<Map<String, Object>>();
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
				Toast.makeText(getApplicationContext(), "暂无网络连接", 1).show();
			} else {
				// Toast.makeText(getApplicationContext(), "you网络", 1).show();
				// connect network
				if (iscoonnet && requestList.size() == 0) {
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

	private void initView() {

		main_address = (TextView) findViewById(R.id.main_address);

		main_coorper = (ImageView) findViewById(R.id.main_coorper);// 我要合作

		suprise_img = (ImageView) findViewById(R.id.suprise_img); // 摇惊喜
		v_show_img = (ImageView) findViewById(R.id.v_show_img); // v视秀
		mine_img = (ImageView) findViewById(R.id.mine_img); // 我的
		place_img = (ImageView) findViewById(R.id.place_img); // 地方台
		main_re_zhibo = (ImageView) findViewById(R.id.direct_img); // 直播
		travel_img = (ImageView) findViewById(R.id.travel_img); // 旅行
		main_re_brand = (ImageView) findViewById(R.id.main_re_brand); // 品牌秀

		main_changwords = (TextView) findViewById(R.id.main_changwords);
		main_iv1 = (ImageView) findViewById(R.id.main_iv1);

		main_board = (TextView) findViewById(R.id.main_board);// 中奖公告

		opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;

	}

	private void initSetListener() {
		main_board.setOnClickListener(this);// 中奖公告

		main_coorper.setOnClickListener(this);

		suprise_img.setOnClickListener(this);
		mine_img.setOnClickListener(this);
		v_show_img.setOnClickListener(this);

		place_img.setOnClickListener(this);
		main_re_zhibo.setOnClickListener(this);
		travel_img.setOnClickListener(this);

		main_re_brand.setOnClickListener(this);

		main_iv1.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.main_coorper:// 我要合作
			Intent merchantiIntent = new Intent(MainActivity.this,
					MerchantAct.class);
			startActivity(merchantiIntent);

			break;

		case R.id.suprise_img:// 摇美食
			if (requestList.size() > 0) {
				Intent cateIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				cateIntent.putExtra("id", requestList.get(0).get("id").toString());
				startActivity(cateIntent);

			} else {
				CommonUtil.custoast(getApplicationContext(), "获取频道信息失败");
			}

			break;

		case R.id.mine_img:// 个人中心
			if (Constant.islogin) {
				startActivity(new Intent(MainActivity.this,
						LogingedCenter.class));
			} else {
				Intent loginIntent = new Intent(MainActivity.this,
						LoginMycenterAct.class);
				startActivity(loginIntent);

			}

			break;
		case R.id.v_show_img:// 掉馅饼
			if (requestList.size() > 0) {
				Intent dianoIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				dianoIntent.putExtra("id", requestList.get(1).get("id").toString());
				startActivity(dianoIntent);

			} else {
				CommonUtil.custoast(getApplicationContext(), "获取频道信息失败");
			}

			break;
		case R.id.place_img:// 地方台
			if (requestList.size() > 0) {
				Intent dianboIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				dianboIntent.putExtra("id", requestList.get(2).get("id").toString());
				startActivity(dianboIntent);

			}

			break;
		case R.id.direct_img:// 直播

			Intent zhiboIntent = new Intent(MainActivity.this, WebviewAct.class);
			// zhiboIntent.putExtra("id", list.get(3).get("id").toString());
			startActivity(zhiboIntent);
			break;
		case R.id.travel_img:// 爱旅行
			if (requestList.size() > 0) {
				Intent plaIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				plaIntent.putExtra("id", requestList.get(5).get("id").toString());
				startActivity(plaIntent);

			}
			break;
		// case R.id.main_re_play:// 聚娱乐
		// if (list.size() > 0) {
		//
		// Intent plaIntent2 = new Intent(MainActivity.this,
		// HorizonListAct.class);
		// plaIntent2.putExtra("id", list.get(4).get("id").toString());
		// startActivity(plaIntent2);
		// }
		// break;
		case R.id.main_re_brand:// 品牌秀
			if (requestList.size() > 0) {

				Intent brandiIntent = new Intent(MainActivity.this,
						HorizonListAct.class);
				brandiIntent.putExtra("id", requestList.get(7).get("id").toString());
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
			main_address.setText(str2);
			// if (!TextUtils.isEmpty(str)) {
			// main_sixname.setText(str2);
			// stopLocation();
			// } else {
			// main_sixname.setText("北京");
			// }

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

		String url = Constant.KE_BASICURL + "channel!request.action";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("data", d);

		GetMsg engine = new GetMsg(getApplicationContext());
		engine.setPostResult(new PostResult<String>() {

			@Override
			public void getResult(Status status, List<String> datas) {
				if (status.getCode() == 200) {
					String infos = datas.get(0);
					DialogUtils.closeProgressDialog();

					// setUI(infos);

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

	/**
	 * 老版本，在服务器上获取UI信息，目前已不用
	 * 
	 * @param result
	 */
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
				requestList.add(map);

			}
			// System.out.println("-----------list----"+list.toString());
			// BitmapUtils bitmapUtils = new
			// BitmapUtils(getApplicationContext());

			// //
			// System.out.println("---------firstname-----"+list.get(2).get("name").toString());
			// main_firstname.setText(list.get(0).get("name").toString());
			// main_secondname.setText(list.get(1).get("name").toString());
			// main_thirdname.setText(list.get(2).get("name").toString());
			// main_forthname.setText(list.get(3).get("name").toString());
			//
			// main_fifthname.setText(list.get(4).get("name").toString());
			// // main_sixname.setText(list.get(5).get("name").toString());
			// main_seventhname.setText(list.get(6).get("name").toString());
			// // 注释 2015/5/18 feng 防止数组越界异常
			// main_eightname.setText(list.get(7).get("name").toString());
			//
			// Log.i(" ----------imagurl--------"+list.get(0).get("imageUrl").toString());
			// //
			// bitmapUtils.display(main_cate, list.get(0).get("imageUrl")
			// .toString());

		} catch (JSONException e) {
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
