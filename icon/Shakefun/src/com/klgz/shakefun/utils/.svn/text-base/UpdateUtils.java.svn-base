/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author wk
 *更新类
 */
public class UpdateUtils {
	
	
	/**
	 * 获取versoncode
	 * @param context
	 * @return
	 */
	public static int getVersonCode(Context context){
		
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		
			return info.versionCode;
		
		
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	
	/**
	 * 获取versonname
	 * @param context
	 * @return
	 */
	public static String getVersonName(Context context){
		
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			
			return info.versionName;
		
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

}
