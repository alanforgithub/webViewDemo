/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.utils;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * @author wk
 *共享文件类
 */
public class SharedPreferencesUtils {
	
	public static final String SP_NAME = "config";
	private static SharedPreferences sp;

	public static void saveboolean(Context ct, String key, boolean value) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, 0);
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getboolean(Context ct, String key, boolean defValue) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, 0);
		return sp.getBoolean(key, defValue);
	}
	
	
	public static void saveString(Context ct, String key, String value) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context ct, String key, String defValue) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, 0);
		return sp.getString(key, defValue);
	}

	
	/**
	 * 保存用户，密码
	 * @param ct
	 * @param username
	 * @param password
	 */
	public static void saveUserPass(Context ct,String username,String password){
		SharedPreferencesUtils.saveString(ct, "username", username);
		SharedPreferencesUtils.saveString(ct, "password", password);
	}

}
