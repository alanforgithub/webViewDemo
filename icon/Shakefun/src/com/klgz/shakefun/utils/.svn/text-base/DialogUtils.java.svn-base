/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.utils;

import android.app.ProgressDialog;
import android.content.Context;
/**
 * 加载进度条
 * @author wk
 *
 */
/**
 * 对话框工具类
 * @author wk
 * @param imageUrl
 * @return
 */
public class DialogUtils {
	public static ProgressDialog dialog;
	/**
	 * 显示加载的dialog
	 * 
	 * @param context
	 */
	public static void showProgressDialog(Context context,String message) {
		dialog = new ProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(message);
		dialog.show();
	}
	
	/**
	 * 把dialog关闭
	 */
	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
