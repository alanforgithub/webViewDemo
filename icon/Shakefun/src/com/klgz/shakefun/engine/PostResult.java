/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.engine;

import java.util.List;

import com.android.volley.VolleyError;
import com.klgz.shakefun.bean.Status;

/**
 * 请求成功后返回的回调
 * 
 * @author Administrator
 * 
 */
public interface PostResult<T> {
	/**
	 * 请求成功返回的内容
	 * 
	 * @param status
	 * @param datas
	 */
	void getResult(Status status,List<T> datas);

	/**
	 * 请求失败返回的错误信息
	 * 
	 * @param error
	 */
	void onError(VolleyError error);
}
