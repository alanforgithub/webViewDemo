/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.bokecc.sdk.mobile.exception.ErrorCode;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.klgz.shakefun.tools.Constant;
import com.klgz.shakefun.tools.GetJsonUtils;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonCallback;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonPraiseInfo;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonPrize;
import com.klgz.shakefun.tools.GetJsonUtils.IJsonVideoback;
import com.klgz.shakefun.tools.GetJsonUtils.IJsongetPraise;
import com.klgz.shakefun.utils.CommonUtil;
import com.klgz.shakefun.utils.ConfigUtil;
import com.klgz.shakefun.utils.ParamsUtil;
import com.klgz.shakefun.view.BulletinView;
import com.klgz.shakefun.view.PopMenu;
import com.klgz.shakefun.view.PopMenu.OnItemClickListener;
import com.klgz.shakefun.view.VerticalSeekBar;
import com.klgz.ylyq.R;
import com.mob.tools.utils.UIHandler;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * 视频播放界面
 * 
 * @author CC视频
 * 
 */
public class MediaPlayActivity extends Activity implements
		PlatformActionListener, Callback,
		DWMediaPlayer.OnBufferingUpdateListener,
		DWMediaPlayer.OnPreparedListener, DWMediaPlayer.OnErrorListener,
		IJsonPrize, SensorEventListener, SurfaceHolder.Callback, IJsonCallback,
		IJsonVideoback, IJsongetPraise, IJsonPraiseInfo {

	private DWMediaPlayer player;// media对象
	// private Subtitle subtitle;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private ProgressBar bufferProgressBar;// 进度条
	private SeekBar skbProgress;// 播放进度条
	private ImageView playOp, backPlayList;// 返回
	private TextView videoIdText, playDuration, videoDuration;// 播放当前进度，播放时长
	// private Button screenSizeBtn, subtitleBtn;
	private TextView definitionBtn;
	private PopMenu screenSizeMenu, definitionMenu;
	// , subtitleMenu;
	private LinearLayout playerTopLayout, volumeLayout;
	private RelativeLayout playerBottomLayout;// 底部控件
	private AudioManager audioManager;
	private VerticalSeekBar volumeSeekBar;// 音量进度条
	private int currentVolume;
	private int maxVolume;
	// private TextView subtitleText;

	private boolean isLocalPlay;
	private boolean isPrepared;
	private Map<String, Integer> definitionMap;

	private Handler playerHandler;
	private Timer timer = new Timer();// 定时器
	private TimerTask timerTask;// 定时任务

	private int currentScreenSizeFlag = 1;
	// private int currrentSubtitleSwitchFlag = 0;
	private int currentDefinition = 0;

	String path;

	private Boolean isPlaying;
	// 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
	private boolean isFreeze = false;
	private boolean isSurfaceDestroy = false;

	int currentPosition;
	private Dialog dialog;

	private String[] definitionArray;
	private final String[] screenSizeArray = new String[] { "满屏", "100%",
			"75%", "50%" };
	// private final String[] subtitleSwitchArray = new String[] { "开启", "关闭" };
	private final String subtitleExampleURL = "http://dev.bokecc.com/static/font/example.utf8.srt";

	// 添加
	private DrawerLayout drawerLayout;// 侧滑栏
	private LinearLayout right_layoutLayout;
	/**
	 * 点赞图标，踩图标，微信分享，微博分享
	 */
	private LinearLayout rightdra_up, rightdra_down2, rightdra_weichat,
			rightdra_sinaweibo, rightdra_facebook;

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private boolean isSina = true;

	// 点赞处理
	private boolean ispraise = true;
	private boolean isgood = true;
	private ImageView dwplayer_praise, dwplayer_good;

	private boolean flagPraise = true;// 点赞之后不可再次点击
	private boolean flagGood = true;// 点踩之后不可再次点击
	private IWXAPI api;// 微信接口

	private LinearLayout media_relatoptitle;
	private LinearLayout media_relabottomtitle;

	private BulletinView media_changwords;
	private TextView media_count;// 点赞数量

	// 摇一摇
	private boolean isShake = false;
	private SensorManager sensorManager = null;
	private Vibrator vibrator = null;

	// 创建MediaPlayer对象
	private MediaPlayer mp3player;

	int jiange = 10000;
	int durationlength;

	int count = 0;
	boolean run = false;
	private int countlistques = 0;

	// 动画容器
	private LinearLayout media_lncenter;
	private ImageView media_gifview;

	/**
	 * 摇奖开始时间，时间间隔
	 */
	private TextView media_bottomtitle;
	private int startMinute = -1, frequency = -1;
	int startlength;

	private final Handler handler = new Handler();

	/**
	 * 延时导入动画
	 */
	Runnable gifstart = new Runnable() {

		@Override
		public void run() {
			media_gifview.setBackgroundResource(R.anim.third);
			media_lncenter.addView(media_gifview);
			handler.postDelayed(task, 1000);

		}
	};

	// private Runnable task;

	/**
	 * 倒计时动画
	 */
	private final Runnable task = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (run && durationlength - (count * jiange) > jiange) {

				// handler.postDelayed(task, jiange);
				count++;

			}

			AnimationDrawable anim = null;
			Object ob = media_gifview.getBackground();
			anim = (AnimationDrawable) ob;
			anim.stop();
			anim.start();
			isShake = true;
		}
	};

	/**
	 * 移除动画
	 */
	Runnable gifleft = new Runnable() {

		@Override
		public void run() {
			media_lncenter.removeAllViews();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.act_dwmediaplayer);

		media_lncenter = (LinearLayout) findViewById(R.id.media_lncenter);// 动画容器
		media_gifview = new ImageView(this);

		LayoutParams params = new LayoutParams(800, 800);

		media_gifview.setLayoutParams(params);

		GetJsonUtils.getDataquestJson(MediaPlayActivity.this, getIntent()
				.getStringExtra("questid"), MediaPlayActivity.this);// 问题集合
		// 视频打开量
		GetJsonUtils.getDataJsonVideo(MediaPlayActivity.this,
				MediaPlayActivity.this, getIntent().getStringExtra("questid"),
				Constant.phoneID, "");
		// GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
		// MediaPlayActivity.this, "2", "2", "", "1", "");
		// 获奖信息
		if (!TextUtils.isEmpty(Constant.userId)) {
			GetJsonUtils.getPraiseInfo(MediaPlayActivity.this,
					MediaPlayActivity.this,
					getIntent().getStringExtra("questid"), Constant.phoneID,
					Constant.userId);

		} else {
			GetJsonUtils
					.getPraiseInfo(MediaPlayActivity.this,
							MediaPlayActivity.this,
							getIntent().getStringExtra("questid"),
							Constant.phoneID, "");
		}
		// 下载图片用以分享
		GetJsonUtils.downImage(MediaPlayActivity.this, getIntent()
				.getStringExtra("imageurl"),
				getIntent().getStringExtra("imageurl") + ".jpg");

		// System.out.println("-----iamgeurl---"+getIntent().getStringExtra("imageurl"));
		// Constant.cachefile = getIntent().getStringExtra("imageurl");
		initView();
		// initdata();
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 关闭手势菜单
		initPlayHander();

		initPlayInfo();

		initScreenSizeMenu();

		super.onCreate(savedInstanceState);
	}

	private void initdata() {// 初始化数据
		String pariseno = getIntent().getStringExtra("praisenumber");// 原点赞数量
		String isparisenum = getIntent().getStringExtra("isparise");// 该用户是否点赞
		if (!TextUtils.isEmpty(pariseno)) {// 如果有数据设置，否则默认为0
			media_count.setText(pariseno);
		}
		if (isparisenum.equals("1")) {// 该用户点过赞
			dwplayer_praise.setImageResource(R.drawable.movies_up2);
			ispraise = true;
			flagPraise = false;
		} else {
			dwplayer_praise.setImageResource(R.drawable.movies_up);
			flagPraise = true;
		}
	}

	private void initView() {
		surfaceView = (SurfaceView) findViewById(R.id.playerSurfaceView);
		playOp = (ImageView) findViewById(R.id.btnPlay);
		backPlayList = (ImageView) findViewById(R.id.backPlayList);
		bufferProgressBar = (ProgressBar) findViewById(R.id.bufferProgressBar);

		videoIdText = (TextView) findViewById(R.id.videoIdText);
		playDuration = (TextView) findViewById(R.id.playDuration);
		videoDuration = (TextView) findViewById(R.id.videoDuration);
		playDuration.setText(ParamsUtil.millsecondsToStr(0));
		videoDuration.setText(ParamsUtil.millsecondsToStr(0));

		// screenSizeBtn = (Button) findViewById(R.id.playScreenSizeBtn);
		definitionBtn = (TextView) findViewById(R.id.definitionBtn);
		// subtitleBtn = (Button) findViewById(R.id.subtitleBtn);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		volumeSeekBar = (VerticalSeekBar) findViewById(R.id.volumeSeekBar);
		volumeSeekBar.setThumbOffset(2);

		volumeSeekBar.setMax(maxVolume);
		volumeSeekBar.setProgress(currentVolume);
		volumeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

		skbProgress = (SeekBar) findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);

		playerTopLayout = (LinearLayout) findViewById(R.id.playerTopLayout);
		volumeLayout = (LinearLayout) findViewById(R.id.volumeLayout);
		playerBottomLayout = (RelativeLayout) findViewById(R.id.playerBottomLayout);

		playOp.setOnClickListener(onClickListener);
		backPlayList.setOnClickListener(onClickListener);
		// screenSizeBtn.setOnClickListener(onClickListener);
		definitionBtn.setOnClickListener(onClickListener);
		// subtitleBtn.setOnClickListener(onClickListener);

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceView.setOnTouchListener(touchListener);

		// subtitleText = (TextView) findViewById(R.id.subtitleText);

		// 添加
		rightdra_up = (LinearLayout) findViewById(R.id.rightdra_up);
		rightdra_up.setOnClickListener(onClickListener);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		right_layoutLayout = (LinearLayout) findViewById(R.id.right_drawer);
		// rightdra_down2 = (LinearLayout) findViewById(R.id.rightdra_down2);
		rightdra_weichat = (LinearLayout) findViewById(R.id.rightdra_weichat);
		rightdra_sinaweibo = (LinearLayout) findViewById(R.id.rightdra_sinaweibo);
		rightdra_facebook = (LinearLayout) findViewById(R.id.rightdra_facebook);

		// rightdra_down2.setOnClickListener(onClickListener);
		rightdra_weichat.setOnClickListener(onClickListener);
		rightdra_sinaweibo.setOnClickListener(onClickListener);
		rightdra_facebook.setOnClickListener(onClickListener);

		dwplayer_praise = (ImageView) findViewById(R.id.dwplayer_praise);
		// dwplayer_good = (ImageView) findViewById(R.id.dwplayer_good);

		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WEIXIN, false);
		media_relabottomtitle = (LinearLayout) findViewById(R.id.media_relabottomtitle);
		media_relatoptitle = (LinearLayout) findViewById(R.id.media_relatoptitle);

		media_changwords = (BulletinView) findViewById(R.id.media_changwords);
		// media_gifview = (ImageView) findViewById(R.id.media_gifview);

		media_count = (TextView) findViewById(R.id.media_count);// 点赞数量

		// 摇一摇
		mp3player = new MediaPlayer();
		mp3player = MediaPlayer.create(this, R.raw.shakeing);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		media_bottomtitle = (TextView) findViewById(R.id.media_bottomtitle);

		if (!TextUtils.isEmpty(getIntent().getStringExtra("startMinute"))
				&& !TextUtils.isEmpty(getIntent().getStringExtra("frequency"))) {
			startMinute = Integer.parseInt(getIntent().getStringExtra(
					"startMinute"));// 开始时间
			frequency = Integer.parseInt(getIntent()
					.getStringExtra("frequency"));// 时间间隔
			startlength = startMinute * 60 * 1000;
			// System.out.println("-----startminute-----"+startMinute+"----"+frequency);

		}

	}

	private boolean isstartlength = true;

	private void isshowwhich() {
		if (isstartlength) {
			run = true;
			isShake = true;
			handler.post(gifstart);
			isstartlength = false;
		} else {
			handler.post(task);

		}
	}

	private void initPlayHander() {
		playerHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (player == null) {
					return;
				}

				// 刷新字幕
				// subtitleText.setText(subtitle.getSubtitleByTime(player.getCurrentPosition()));

				// 更新播放进度
				int position = player.getCurrentPosition();
				final int duration = player.getDuration();

				if (duration > 0) {

					// System.out.println("-----current----"+player.getCurrentPosition());
					// if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000)))
					// {
					// // CommonUtil.custoast(getApplicationContext(),
					// "position");
					// isshowwhich();
					//
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+1*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+2*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+3*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+4*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+5*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+6*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+7*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+8*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+9*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }else if
					// (ParamsUtil.millsecondsToStr(player.getCurrentPosition()).equals(ParamsUtil.millsecondsToStr(startMinute*60*1000+10*frequency*60*1000)))
					// {
					//
					// isshowwhich();
					// }

					long pos = skbProgress.getMax() * position / duration;
					playDuration.setText(ParamsUtil.millsecondsToStr(player
							.getCurrentPosition()));
					// task = new Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// if
					// (run&&durationlength-(count*frequency*60*1000)-startMinute*60*1000>frequency*60*1000)
					// {
					//
					// // handler.postDelayed(task, frequency*60*1000);
					// count++;
					// startlength+=frequency*60*1000;
					//
					// }
					//
					// AnimationDrawable anim = null;
					// Object ob = media_gifview.getBackground();
					// anim = (AnimationDrawable) ob;
					// anim.stop();
					// anim.start();
					// isShake = true;
					// }
					// };

					skbProgress.setProgress((int) pos);

				}
			};
		};

		// 通过定时器和Handler来更新进度
		timerTask = new TimerTask() {
			@Override
			public void run() {

				if (!isPrepared) {
					return;
				}

				playerHandler.sendEmptyMessage(0);
			}
		};

	}

	private void initPlayInfo() {
		timer.schedule(timerTask, 0, 1000);
		isPrepared = false;
		player = new DWMediaPlayer();
		player.reset();
		player.setOnErrorListener(this);

		String videoId = getIntent().getStringExtra("videoId");
		// System.out.println("-----videoId-----"+videoId);

		// String videoId = "A069054401AEC03D9C33DC5901307461";
		videoIdText.setText(getIntent().getStringExtra("videoname"));
		isLocalPlay = getIntent().getBooleanExtra("isLocalPlay", false);
		try {

			if (!isLocalPlay) {// 播放线上视频

				player.setVideoPlayInfo(videoId, ConfigUtil.USERID,
						ConfigUtil.API_KEY, this);

			} else {// 播放本地已下载视频

				if (android.os.Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					path = Environment.getExternalStorageDirectory()
							+ "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/")
									.concat(videoId).concat(".mp4");
					if (!new File(path).exists()) {
						return;
					}

					player.setDataSource(path);
				}
			}

			player.prepareAsync();

		} catch (IllegalArgumentException e) {
			Log.e("player error", e.getMessage());
		} catch (SecurityException e) {
			Log.e("player error", e.getMessage());
		} catch (IllegalStateException e) {
			Log.e("player error", e + "");
		} catch (IOException e) {
			Log.e("player error", e.getMessage());
		}

		// 设置视频字幕
		// subtitle = new Subtitle(new OnSubtitleInitedListener() {
		//
		// @Override
		// public void onInited(Subtitle subtitle) {
		// //初始化字幕控制菜单
		// initSubtitleSwitchpMenu(subtitle);
		// }
		// });
		// subtitle.initSubtitleResource(subtitleExampleURL);

	}

	private void initScreenSizeMenu() {
		screenSizeMenu = new PopMenu(this, R.drawable.popdown,
				currentScreenSizeFlag);
		screenSizeMenu.addItems(screenSizeArray);
		screenSizeMenu
				.setOnItemClickListener(new PopMenu.OnItemClickListener() {

					@Override
					public void onItemClick(int position) {
						// 提示已选择的屏幕尺寸
						Toast.makeText(getApplicationContext(),
								screenSizeArray[position], Toast.LENGTH_SHORT)
								.show();

						LayoutParams params = getScreenSizeParams(position);
						params.addRule(RelativeLayout.CENTER_IN_PARENT);
						surfaceView.setLayoutParams(params);
					}
				});

	}

	@SuppressWarnings("deprecation")
	private LayoutParams getScreenSizeParams(int position) {
		currentScreenSizeFlag = position;
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		int vWidth = player.getVideoWidth();
		int vHeight = player.getVideoHeight();

		if (vWidth > width || vHeight > height) {
			float wRatio = (float) vWidth / (float) width;
			float hRatio = (float) vHeight / (float) height;
			float ratio = Math.max(wRatio, hRatio);

			width = (int) Math.ceil((float) vWidth / ratio);
			height = (int) Math.ceil((float) vHeight / ratio);
		} else {
			float wRatio = (float) width / (float) vWidth;
			float hRatio = (float) height / (float) vHeight;
			float ratio = Math.min(wRatio, hRatio);

			width = (int) Math.ceil((float) vWidth * ratio);
			height = (int) Math.ceil((float) vHeight * ratio);
		}

		String screenSizeStr = screenSizeArray[position];
		if (screenSizeStr.indexOf("%") > 0) {// 按比例缩放
			int screenSize = ParamsUtil.getInt(screenSizeStr.substring(0,
					screenSizeStr.indexOf("%")));
			width = (width * screenSize) / 100;
			height = (height * screenSize) / 100;

		} else { // 拉伸至全屏
			width = wm.getDefaultDisplay().getWidth();
			height = wm.getDefaultDisplay().getHeight();
		}

		LayoutParams params = new LayoutParams(width, height);
		return params;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			player.setDisplay(surfaceHolder);
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setOnBufferingUpdateListener(this);
			player.setOnPreparedListener(this);

			if (isSurfaceDestroy) {
				if (isLocalPlay) {
					player.setDataSource(path);
				}
				player.prepareAsync();
			}
		} catch (Exception e) {
			Log.e("videoPlayer", "error", e);
		}
		Log.i("videoPlayer", "surface created");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		holder.setFixedSize(width, height);
		player.setDisplay(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (player == null) {
			return;
		}
		if (isPrepared) {
			currentPosition = player.getCurrentPosition();
		}

		isPrepared = false;
		isSurfaceDestroy = true;

		player.stop();
		player.reset();

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		isPrepared = true;

		if (!isFreeze) {
			player.start();
		}

		if (isPlaying != null && !isPlaying.booleanValue()) {
			player.pause();
		}

		if (currentPosition > 0) {
			player.seekTo(currentPosition);
		}

		definitionMap = player.getDefinitions();
		if (!isLocalPlay) {
			initDefinitionPopMenu();
		}

		bufferProgressBar.setVisibility(View.GONE);

		durationlength = player.getDuration();

		if (isPrepared) {
			run = true;
			isShake = false;
			handler.postDelayed(gifstart, startMinute * 60 * 1000);

		}

		LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		surfaceView.setLayoutParams(params);
		videoDuration
				.setText(ParamsUtil.millsecondsToStr(player.getDuration()));
	}

	private void initDefinitionPopMenu() {
		definitionBtn.setVisibility(View.VISIBLE);
		definitionMenu = new PopMenu(this, R.drawable.popup, currentDefinition);
		// 设置清晰度列表
		definitionArray = new String[] {};
		definitionArray = definitionMap.keySet().toArray(definitionArray);

		definitionMenu.addItems(definitionArray);
		definitionMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				try {
					currentDefinition = position;
					int definitionCode = definitionMap
							.get(definitionArray[position]);

					if (isPrepared) {
						currentPosition = player.getCurrentPosition();
						if (player.isPlaying()) {
							isPlaying = true;
						} else {
							isPlaying = false;
						}
					}

					player.reset();
					player.setDefinition(getApplicationContext(),
							definitionCode);

				} catch (IOException e) {
					Log.e("player error", e.getMessage());
				}

			}
		});
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		skbProgress.setSecondaryProgress(percent);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnPlay:
				if (!isPrepared) {
					return;
				}

				if (isLocalPlay && !player.isPlaying()) {
					try {
						player.prepare();

					} catch (IllegalArgumentException e) {
						Log.e("player error", e.getMessage());
					} catch (SecurityException e) {
						Log.e("player error", e.getMessage());
					} catch (IllegalStateException e) {
						Log.e("player error", e + "");
					} catch (IOException e) {
						Log.e("player error", e + "");
					}
				}

				if (player.isPlaying()) {
					player.pause();
					playOp.setImageResource(R.drawable.btn_play);

				} else {
					player.start();
					playOp.setImageResource(R.drawable.btn_pause);
				}

				break;

			case R.id.backPlayList:
				finish();
				break;
			// case R.id.playScreenSizeBtn:
			// screenSizeMenu.showAsDropDown(v);
			// break;
			// case R.id.subtitleBtn:
			// subtitleMenu.showAsDropDown(v);
			// break;
			case R.id.definitionBtn:
				// definitionMenu.showAsDropDown(v);
				drawerLayout.openDrawer(Gravity.RIGHT);
				player.pause();
				playOp.setImageResource(R.drawable.btn_play);
				break;

			case R.id.rightdra_up:
				ispraise = false;
				if (flagPraise) {
					if (!TextUtils.isEmpty(Constant.userId)) {

						GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
								MediaPlayActivity.this, getIntent()
										.getStringExtra("questid"),
								Constant.userId, Constant.phoneID, "1", "");
					} else {
						GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
								MediaPlayActivity.this, getIntent()
										.getStringExtra("questid"), "",
								Constant.phoneID, "1", "");

					}

				} else {
					if (!TextUtils.isEmpty(Constant.userId)) {
						GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
								MediaPlayActivity.this, getIntent()
										.getStringExtra("questid"),
								Constant.userId, Constant.phoneID, "0", "");

					} else {
						GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
								MediaPlayActivity.this, getIntent()
										.getStringExtra("questid"), "",
								Constant.phoneID, "0", "");

					}
				}
				break;

			// case R.id.rightdra_down2:
			// isgood = false;
			// if (flagGood) {
			// if (!TextUtils.isEmpty(Constant.userId)) {
			//
			// GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
			// MediaPlayActivity.this, getIntent().getStringExtra("questid"),
			// Constant.userId, "", "", "1");
			// }else {
			// GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
			// MediaPlayActivity.this, getIntent().getStringExtra("questid"),
			// "", Constant.phoneID, "", "1");
			//
			// }
			//
			// }else {
			// if (!TextUtils.isEmpty(Constant.userId)) {
			// GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
			// MediaPlayActivity.this, getIntent().getStringExtra("questid"),
			// Constant.userId, "", "", "0");
			//
			// }else {
			// GetJsonUtils.sendJsonisgood(MediaPlayActivity.this,
			// MediaPlayActivity.this, getIntent().getStringExtra("questid"),
			// "", Constant.phoneID, "", "0");
			//
			// }
			// }
			//
			// break;
			case R.id.rightdra_weichat:
				if (api.isWXAppInstalled()) {
					sharetoWechat();
					drawerLayout.closeDrawers();

				} else {
					CommonUtil.custoast(getApplicationContext(), "请先安装微信");
					drawerLayout.closeDrawers();
				}
				break;
			case R.id.rightdra_sinaweibo:
				// player.pause();
				// if (isSina) {
				CommonUtil.custoast(getApplicationContext(), "正在分享....");
				sharesina();
				drawerLayout.closeDrawers();

				// }
				break;
			case R.id.rightdra_facebook:
				CommonUtil.custoast(getApplicationContext(), "正在分享....");
				sharetoFacebook();
				drawerLayout.closeDrawers();
				break;

			case R.id.answer1:// 答案1
				if ("A".equalsIgnoreCase(ansright)) {
					if (!TextUtils.isEmpty(Constant.userId)) {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "1", getIntent().getStringExtra("questid"),
								"", Constant.userId);

					} else {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "1", getIntent().getStringExtra("questid"),
								Constant.phoneID, "");
					}

				} else {

					if (!TextUtils.isEmpty(Constant.userId)) {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "0", getIntent().getStringExtra("questid"),
								"", Constant.userId);

					} else {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "0", getIntent().getStringExtra("questid"),
								Constant.phoneID, "");
					}

				}

				popupWindow.dismiss();

				break;
			case R.id.answer2:// 答案2

				if ("B".equalsIgnoreCase(ansright)) {
					if (!TextUtils.isEmpty(Constant.userId)) {

						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "1", getIntent().getStringExtra("questid"),
								"", Constant.userId);
					} else {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "1", getIntent().getStringExtra("questid"),
								Constant.phoneID, "");
					}

				} else {

					if (!TextUtils.isEmpty(Constant.userId)) {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "0", getIntent().getStringExtra("questid"),
								"", Constant.userId);

					} else {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "0", getIntent().getStringExtra("questid"),
								Constant.phoneID, "");
					}

				}

				popupWindow.dismiss();

				break;
			case R.id.answer3:// 答案3
				if ("C".equalsIgnoreCase(ansright)) {
					if (!TextUtils.isEmpty(Constant.userId)) {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "1", getIntent().getStringExtra("questid"),
								"", Constant.userId);

					} else {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "1", getIntent().getStringExtra("questid"),
								Constant.phoneID, "");
					}

				} else {

					if (!TextUtils.isEmpty(Constant.userId)) {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "0", getIntent().getStringExtra("questid"),
								"", Constant.userId);

					} else {
						GetJsonUtils.getPrizeInfo(getApplicationContext(),
								MediaPlayActivity.this, Constant.phonenu, "",
								"", "0", getIntent().getStringExtra("questid"),
								Constant.phoneID, "");
					}

				}

				popupWindow.dismiss();

				break;
			case R.id.popquest_login:// 去登陆
				startActivity(new Intent(MediaPlayActivity.this,
						LoginMycenterAct.class));
				finish();

				break;
			case R.id.popuest_haslogin:// 已登录用户去领奖
				startActivity(new Intent(MediaPlayActivity.this,
						LogingedCenter.class));
				finish();
				break;

			}
		}
	};

	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
		int progress = 0;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			player.seekTo(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			this.progress = progress * player.getDuration() / seekBar.getMax();
			// 播放时间进度
			// int progress2 = progress * player.getDuration() /
			// seekBar.getMax();
			// System.out.println("------progress2----"+progress2+"--"+progress+"--"+
			// player.getDuration()+"---"+seekBar.getMax());
		}
	};

	VerticalSeekBar.OnSeekBarChangeListener seekBarChangeListener = new VerticalSeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(VerticalSeekBar verticalseekbar) {

		}

		@Override
		public void onStartTrackingTouch(VerticalSeekBar verticalseekbar) {

		}

		@Override
		public void onProgressChanged(VerticalSeekBar verticalseekbar, int i,
				boolean flag) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
			currentVolume = i;
			volumeSeekBar.setProgress(i);
		}
	};

	// 控制播放器面板显示
	private boolean isDisplay = false;
	private OnTouchListener touchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			if (!isPrepared) {
				return false;
			}

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (isDisplay) {
					setLayoutVisibility(View.GONE, false);
				} else {
					setLayoutVisibility(View.VISIBLE, true);
				}
			}
			return false;
		}
	};

	// private void initSubtitleSwitchpMenu(Subtitle subtitle) {
	// this.subtitle = subtitle;
	// subtitleBtn.setVisibility(View.VISIBLE);
	// subtitleMenu = new PopMenu(this, R.drawable.popup,
	// currrentSubtitleSwitchFlag);
	// subtitleMenu.addItems(subtitleSwitchArray);
	// subtitleMenu.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(int position) {
	// switch (position) {
	// case 0:// 开启字幕
	// currentScreenSizeFlag = 0;
	// subtitleText.setVisibility(View.VISIBLE);
	// break;
	// case 1:// 关闭字幕
	// currentScreenSizeFlag = 1;
	// subtitleText.setVisibility(View.GONE);
	// break;
	// }
	// }
	// });
	// }

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// 监测音量变化
		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
				|| event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {

			int volume = audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (currentVolume != volume) {
				currentVolume = volume;
				volumeSeekBar.setProgress(currentVolume);
			}

			if (isPrepared) {
				setLayoutVisibility(View.VISIBLE, true);
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private void setLayoutVisibility(int visibility, boolean isDisplay) {
		if (player == null) {
			return;
		}

		if (player.getDuration() <= 0) {
			return;
		}

		this.isDisplay = isDisplay;
		playerTopLayout.setVisibility(visibility);
		playerBottomLayout.setVisibility(visibility);
		volumeLayout.setVisibility(visibility);
	}

	private Handler alertHandler = new Handler() {

		AlertDialog.Builder builder;
		AlertDialog.OnClickListener onClickListener = new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		};

		@Override
		public void handleMessage(Message msg) {

			String message = "";
			boolean isSystemError = false;
			if (ErrorCode.INVALID_REQUEST.Value() == msg.what) {
				message = "无法播放此视频，请检查视频状态";
			} else if (ErrorCode.NETWORK_ERROR.Value() == msg.what) {
				message = "无法播放此视频，请检查网络状态";
			} else if (ErrorCode.PROCESS_FAIL.Value() == msg.what) {
				message = "无法播放此视频，请检查帐户信息";
			} else {
				isSystemError = true;
			}

			if (!isSystemError) {
				builder = new AlertDialog.Builder(MediaPlayActivity.this);
				dialog = builder.setTitle("提示").setMessage(message)
						.setPositiveButton("OK", onClickListener)
						.setCancelable(false).show();
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Message msg = new Message();
		msg.what = what;
		if (alertHandler != null) {
			alertHandler.sendMessage(msg);
		}
		return false;
	}

	@Override
	public void onResume() {
		if (isFreeze) {
			isFreeze = false;
			if (isPrepared) {
				player.start();
			}
		} else {
			if (isPlaying != null && isPlaying.booleanValue() && isPrepared) {
				player.start();
			}
		}
		super.onResume();
		// MobclickAgent.onResume(this);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		if (isPrepared) {
			// 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
			if (player.isPlaying()) {
				isPlaying = true;
			} else {
				isPlaying = false;
			}
			player.pause();
		} else {
			// 如果播放器没有prepare完成，则设置isFreeze为true
			isFreeze = true;
		}
		media_lncenter.removeAllViews();
		super.onPause();
		// MobclickAgent.onPause(this);
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onDestroy() {
		if (null != media_changwords) {
			media_changwords.stopScreenBroadcastReceiver();
		}
		timerTask.cancel();
		alertHandler.removeCallbacksAndMessages(null);
		handler.removeCallbacks(gifstart);
		handler.removeCallbacks(gifleft);
		handler.removeCallbacks(task);
		media_lncenter.removeAllViews();
		alertHandler = null;
		// handler.removeCallbacks(task);

		if (player != null && mp3player != null) {
			mp3player.release();
			player.release();
			player = null;
			mp3player = null;
		}

		if (dialog != null) {
			dialog.dismiss();
		}

		super.onDestroy();
	}

	@SuppressLint("SdCardPath")
	// oks.disableSSOWhenAuthorize();//关闭sso授权
	private void sharesina() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.setPlatform(SinaWeibo.NAME);
		// System.out.println("----url---"+getIntent().getStringExtra("videourl"));
		// if (!TextUtils.isEmpty(prizename)) {
		// oks.setText("@摇来摇趣"+"  摇到了"+prizename+"，希望我的幸运能传递给每位朋友  "+getIntent().getStringExtra("videourl"));
		//
		// }else {
		// oks.setText("@摇来摇趣"+"  摇来摇趣奖品多多，小伙伴们快来一起摇吧!!!  "+getIntent().getStringExtra("videourl"));
		//
		// }

		oks.setText("@摇来摇趣" + "  摇来摇趣奖品多多，小伙伴们快来一起摇吧!!! ");
		// oks.setImagePath(TEST_IMAGE);
		// oks.setImagePath("/sdcard/temp.jpg");
		oks.setImagePath("/mnt/sdcard/shakefun/"
				+ getIntent().getStringExtra("imageurl") + ".jpg");
		// oks.setImagePath("/sdcard/shakefun/"+"wk.jpg");
		// 分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
		// 权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
		// 注意：本地图片和网络图片，同时设置时，只分享本地图片
		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		oks.setSilent(false); // 显示编辑页面
		oks.show(MediaPlayActivity.this);

		// SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		// //设置分享内容
		// sp.text ="@摇来摇趣"+" 摇到意外惊喜  "+"http://www.yaolaiyaoqu.net";
		// //分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
		// //权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
		// //注意：本地图片和网络图片，同时设置时，只分享本地图片
		// // sp.imageUrl =
		// "http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg";
		// sp.imagePath="/sdcard/temp.jpg";
		// //初始化新浪分享平台
		// Platform pf = ShareSDK.getPlatform(MediaPlayActivity.this,
		// SinaWeibo.NAME);
		// //设置分享监听
		// pf.setPlatformActionListener(MediaPlayActivity.this);
		// //执行分享
		// pf.share(sp);

	}

	@SuppressLint("SdCardPath")
	private void sharetoWechat() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.setPlatform(WechatMoments.NAME);
		// if (!TextUtils.isEmpty(getIntent().getStringExtra("videourl"))) {
		// oks.setUrl(getIntent().getStringExtra("videourl"));
		//
		// }
		if (!TextUtils.isEmpty(prizename)) {
			oks.setText("我在摇来摇趣" + "  摇到了" + prizename + "，希望我的幸运能传递给每位朋友  ");

		} else {
			oks.setText("我在摇来摇趣" + "  摇来摇趣奖品多多，小伙伴们快来一起摇吧!!!  ");

		}
		oks.setImagePath("/mnt/sdcard/shakefun/"
				+ getIntent().getStringExtra("imageurl") + ".jpg");
		oks.setSilent(false);// 显示编辑页面
		oks.show(MediaPlayActivity.this);
	}

	@SuppressLint("SdCardPath")
	private void sharetoFacebook() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.setPlatform(Facebook.NAME);
		if (!TextUtils.isEmpty(prizename)) {
			oks.setText("我在摇来摇趣" + "  摇到了" + prizename + "，希望我的幸运能传递给每位朋友  ");

		} else {
			oks.setText("我在摇来摇趣" + "  摇来摇趣奖品多多，小伙伴们快来一起摇吧!!!  ");

		}
		oks.setImagePath("/mnt/sdcard/shakefun/"
				+ getIntent().getStringExtra("imageurl") + ".jpg");
		oks.setSilent(false);// 显示编辑页面
		oks.show(MediaPlayActivity.this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(MediaPlayActivity.this, text, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
			case 1: { // 成功, successful notification
				showNotification(5000, getString(R.string.share_completed));
			}
				break;
			case 2: { // 失败, fail notification
				String expName = msg.obj.getClass().getSimpleName();
				if ("WechatClientNotExistException".equals(expName)
						|| "WechatTimelineNotSupportedException"
								.equals(expName)) {
					showNotification(2000,
							getString(R.string.wechat_client_inavailable));
				} else if ("GooglePlusClientNotExistException".equals(expName)) {
					showNotification(2000,
							getString(R.string.google_plus_client_inavailable));
				} else if ("QQClientNotExistException".equals(expName)) {
					showNotification(2000,
							getString(R.string.qq_client_inavailable));
				} else {
					showNotification(2000, getString(R.string.share_failed));
				}
			}
				break;
			case 3: { // 取消, cancel notification
				showNotification(2000, getString(R.string.share_canceled));
			}
				break;
			}
		}
			break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
			break;
		}
		return false;
	}

	@Override
	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);

	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		// isSina = false;

	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);

	}

	// 在状态栏提示分享操作,the notification on the status bar
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.shake_logo,
					text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(),
					0);
			notification.setLatestEventInfo(app, "sharesdk test", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * json数据解析 问题集合
	 */
	private List<Map<String, Object>> listquestion = new ArrayList<Map<String, Object>>();

	@Override
	public void getJsonObject(String result) {
		// System.out.println("---------jsonresult------"+result);

		Map<String, Object> map = null;

		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("list");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				map = new HashMap<String, Object>();
				map.put("createtime", object.getString("createtime"));
				map.put("content", object.getString("content"));
				map.put("id", object.getString("id"));
				map.put("programId", object.getString("programId"));
				map.put("updatetime", object.getString("updatetime"));
				map.put("status", object.getString("status"));
				map.put("answer", object.getString("answer"));
				map.put("ask3", object.getString("ask3"));
				map.put("ask2", object.getString("ask2"));
				map.put("ask1", object.getString("ask1"));

				listquestion.add(map);

			}

			System.out.println("---listquestion---" + listquestion.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 点赞或踩之后操作
	 */
	@Override
	public void getJsonPraise(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			String praisecount = jsonObject.getString("praise");
			String goodcount = jsonObject.getString("good");

			// System.out.println("--------goodcount------"+goodcount);
			if (!TextUtils.isEmpty(praisecount) && !ispraise && flagPraise) {
				dwplayer_praise.setImageResource(R.drawable.movies_up2);
				// dwplayer_good.setImageResource(R.drawable.movies_down);
				media_count.setText(praisecount);
				ispraise = true;
				flagPraise = false;
				flagGood = true;
				drawerLayout.closeDrawers();
			}

			if (!TextUtils.isEmpty(praisecount) && !ispraise && !flagPraise) {// 点赞取消
				dwplayer_praise.setImageResource(R.drawable.movies_up);
				media_count.setText(praisecount);
				flagPraise = true;
				drawerLayout.closeDrawers();

			}

			if (!TextUtils.isEmpty(goodcount) && !isgood && flagGood) {
				// dwplayer_good.setImageResource(R.drawable.movies_down2);
				dwplayer_praise.setImageResource(R.drawable.movies_up);
				isgood = true;
				flagPraise = true;
				flagGood = false;
			}

			if (!TextUtils.isEmpty(goodcount) && !isgood && !flagGood) {// 踩取消
				// dwplayer_good.setImageResource(R.drawable.movies_down);
				flagGood = true;

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 视频打开量接口
	 */
	@Override
	public void getJsonVideo(String result) {
		// TODO Auto-generated method stub

	}

	/**
	 * 用户点赞信息
	 */
	@Override
	public void getPraiseinfo(String result) {
		try {

			JSONObject jsoObject = new JSONObject(result);
			String praisenumber = jsoObject.getString("praisenumber");
			String praise = jsoObject.getString("praise");
			if (!TextUtils.isEmpty(praisenumber)) {// 如果有数据设置，否则默认为0
				media_count.setText(praisenumber);
			} else {
				media_count.setText("0");
			}
			if (praise.equals("1")) {// 该用户点过赞
				dwplayer_praise.setImageResource(R.drawable.movies_up2);
				ispraise = true;
				flagPraise = false;
			} else {
				dwplayer_praise.setImageResource(R.drawable.movies_up);
				flagPraise = true;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * popuwindow
	 */

	private PopupWindow popupWindow;
	private LinearLayout popquest_ln1, popwindowque_ln2;
	/**
	 * 问题 答案1，2，3 获得奖品名称 去登陆
	 */
	private TextView quest_title, answer1, answer2, answer3, popquest_prize,
			popquest_login;

	private String ansright;

	private void initPopuWindow(int count) {
		View popView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.popwindow_question, null);
		popupWindow = new PopupWindow(popView, 650, 360, true);

		popquest_ln1 = (LinearLayout) popView.findViewById(R.id.popquest_ln1);

		quest_title = (TextView) popView.findViewById(R.id.quest_title);
		answer1 = (TextView) popView.findViewById(R.id.answer1);
		answer2 = (TextView) popView.findViewById(R.id.answer2);
		answer3 = (TextView) popView.findViewById(R.id.answer3);

		if (listquestion.size() > 0) {
			quest_title.setText(listquestion.get(count).get("content")
					.toString()
					+ "?");
			answer1.setText("A "
					+ listquestion.get(count).get("ask1").toString());
			answer2.setText("B "
					+ listquestion.get(count).get("ask2").toString());
			answer3.setText("C "
					+ listquestion.get(count).get("ask3").toString());
			ansright = listquestion.get(count).get("answer").toString();

		}

		answer1.setOnClickListener(onClickListener);
		answer2.setOnClickListener(onClickListener);
		answer3.setOnClickListener(onClickListener);

	}

	/**
	 * 获取popuwindow实例
	 */
	public void getPopuWindow(int count) {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuWindow(count);
		}

	}

	/**
	 * 获奖信息
	 */
	private String prizename;// 获奖信息名称

	@Override
	public void getDataPrize(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject object = jsonObject.getJSONObject("prizeInfo");
			String userwinPrize = jsonObject.get("userWinPrizeInfo").toString();
			media_relabottomtitle.setVisibility(View.VISIBLE);// 底部获奖信息
			if (frequency != -1) {
				media_bottomtitle
						.setText("距离下次摇奖还有" + frequency + "分钟。本次中奖信息:");

			}
			// // media_relatoptitle.setVisibility(View.VISIBLE);
			List<String> news = new ArrayList<String>();

			if (TextUtils.isEmpty(userwinPrize)) {
				news.add("暂无中奖信息,请继续努力,摇出乐趣");
				media_changwords.setData(news);// 底部文字滚动
			} else {
				news.add(userwinPrize);
				media_changwords.setData(news);
			}

			// 延迟10秒隐藏
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					media_relabottomtitle.setVisibility(View.GONE);
					media_lncenter.removeAllViews();// 移除动画内容
				}
			}, 10000);
			String sponsor = object.get("sponsor").toString();// 赞助商
			String winingTime = object.get("winingTime").toString();
			String image_url = object.get("image_url").toString();
			String winMark = object.get("winMark").toString();
			// CommonUtil.custoast(getApplicationContext(), winMark);
			prizename = object.get("name").toString();
			getPrizePopuWindow();
			isPrizePopupWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0,
					0);
			if (winMark.equals("1")) {// 显示中奖
				// CommonUtil.custoast(getApplicationContext(), "回答正确");
				popisprize_dia.setVisibility(View.GONE);
				if (Constant.islogin) {
					popwindowque_ln2.setVisibility(View.VISIBLE);
					isprize_lnislogin.setVisibility(View.GONE);
					popuest_haslogin.setVisibility(View.VISIBLE);// 用户名去登陆
					popuest_haslogin.setText(Constant.username + "去登陆");
					popquest_login.setVisibility(View.GONE);
				} else {
					popwindowque_ln2.setVisibility(View.VISIBLE);
					isprize_lnislogin.setVisibility(View.VISIBLE);

					popquest_login.setVisibility(View.VISIBLE);
					popuest_haslogin.setVisibility(View.GONE);
				}

			} else {// 提示未中奖
				popisprize_dia.setVisibility(View.VISIBLE);
				popwindowque_ln2.setVisibility(View.GONE);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (isShake) {

			int sensorType = event.sensor.getType();
			// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
			float[] values = event.values;
			if (sensorType == Sensor.TYPE_ACCELEROMETER) {
				if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math
						.abs(values[2]) > 15)) {
					Log.d("sensor x ", "============ values[0] = " + values[0]);
					Log.d("sensor y ", "============ values[1] = " + values[1]);
					Log.d("sensor z ", "============ values[2] = " + values[2]);
					// 摇动手机后，再伴随震动提示~~
					mp3player.start();
					vibrator.vibrate(500);
					if (listquestion.size() > 0
							&& countlistques < listquestion.size()) {
						getPopuWindow(countlistques);
						popupWindow.showAtLocation(drawerLayout,
								Gravity.CENTER, 0, 0);
						countlistques++;
						if (countlistques >= listquestion.size()) {
							countlistques = 0;
						}
					} else {
						CommonUtil.custoast(MediaPlayActivity.this, "暂无相关问题");
					}
					isShake = false;
				}
			}
		}

	}

	/**
	 * 显示是否中奖的窗口
	 */
	private PopupWindow isPrizePopupWindow;
	private ImageView popisprize_dia;
	private LinearLayout isprize_lnislogin;// 未登录时显示
	private TextView popuest_haslogin;

	private void initIsPrizeWindow() {
		View popView2 = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.popuwindow_isprize, null);
		// isPrizePopupWindow = new PopupWindow(popView2,650,450,true);
		isPrizePopupWindow = new PopupWindow(popView2,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		isPrizePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		isPrizePopupWindow.setOutsideTouchable(true);

		popwindowque_ln2 = (LinearLayout) popView2
				.findViewById(R.id.popwindowque_ln2);
		popquest_prize = (TextView) popView2.findViewById(R.id.popquest_prize);
		popquest_login = (TextView) popView2.findViewById(R.id.popquest_login);
		popisprize_dia = (ImageView) popView2.findViewById(R.id.popisprize_dia);
		isprize_lnislogin = (LinearLayout) popView2
				.findViewById(R.id.isprize_lnislogin);
		popuest_haslogin = (TextView) popView2
				.findViewById(R.id.popuest_haslogin);
		if (!TextUtils.isEmpty(prizename)) {
			popquest_prize.setText(prizename);

		}
		popquest_login.setOnClickListener(onClickListener);
		popuest_haslogin.setOnClickListener(onClickListener);

	}

	/**
	 * 获取popuwindow实例
	 */
	public void getPrizePopuWindow() {
		if (null != isPrizePopupWindow) {
			isPrizePopupWindow.dismiss();
			return;
		} else {
			initIsPrizeWindow();
		}

	}

}
