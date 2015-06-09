/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.tools;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断网络连接
 * 
 * 调用方法
 * // 判断网络连接是否可用
 if (!NetworkDetector.isConn(getApplicationContext())) {
 NetworkDetector.setNetworkMethot(User_Login_Activity.this);
 }
 * 
 */
public class NetworkDetector {

	public static boolean isConn(Context context) {

		boolean bisConnFlag = false;
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conMan.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conMan.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}

	public static void setNetworkMethot(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("网络设置提示")
				.setMessage("网络连接不可用，是否进行设置？")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName componet = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(componet);
							intent.setAction("android.intent.action.VIEW");
						}
						context.startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).show();

	}


}