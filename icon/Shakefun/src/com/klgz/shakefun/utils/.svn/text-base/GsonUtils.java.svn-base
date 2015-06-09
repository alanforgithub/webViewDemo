/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.utils;

import com.google.gson.Gson;

/**
 * @author wk
 * bean 和 json 的转换
 *
 */
public class GsonUtils {
	
	
	
	public static <T> T json2bean(String result ,Class<T> clazz){
		Gson gson = new Gson();
		T t = gson.fromJson(result, clazz);
		return t;
	}
	
	public static String bean2json(Object obj){
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

}
