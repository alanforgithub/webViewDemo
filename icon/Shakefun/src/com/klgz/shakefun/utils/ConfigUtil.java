/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.utils;

public class ConfigUtil {

	// 配置API KEY
	//public final static String API_KEY = "5vY4duYzUMdxH0aWppUp3apSNpAo95lU";
	// 修改于2015/5/18 feng
	public final static String API_KEY = "EB0AtiJ4qVnhkHKh0K4311fRUy0YGLKg";

	// 配置帐户ID
	//public final static String USERID = "CBB6D9ABFB512EC1";
	// 修改于2015/5/18 feng
	public final static String USERID = "8D67C00D1C0A293F";

	// 配置下载文件路径
	public final static String DOWNLOAD_DIR = "CCDownload";
	
	// 配置视频回调地址
	public final static String NOTIFY_URL = "http://www.example.com";
	
	/** Fragment */
	
	public final static int MAIN_FRAGMENT_MAX_TAB_SIZE = 3;
	public final static int PLAY_TAB = 0;
	public final static int UPLOAD_TAB = 1;
	public final static int DOWNLOAD_TAB = 2;
	
	public final static int DOWNLOAD_FRAGMENT_MAX_TAB_SIZE = 2;
	public final static int DOWNLOADED = 0;
	public final static int DOWNLOADING = 1;
	
	/** Service Action */
	
	public final static String ACTION_UPLOAD = "video.upload";
	public final static String ACTION_DOWNLOADED = "demo.service.downloaded";
	public final static String ACTION_DOWNLOADING = "demo.service.downloading";
	
	public final static int UPLOAD_REQUEST = 100;
	
	/** Input Info ID */
	
	public final static int INPUT_INFO_TITLE_ID = 10000000;
	public final static int INPUT_EDIT_TITLE_ID = 10000010;
	
	public final static int INPUT_INFO_TAGS_ID = 10000001;
	public final static int INPUT_EDIT_TAGS_ID = 10000011;
	
	public final static int INPUT_INFO_DESC_ID = 10000002;
	public final static int INPUT_EDIT_DESC_ID = 10000012;
	
	/** Download Group ID */
	public final static int DOWNLOADING_MENU_GROUP_ID = 20000000;
	public final static int DOWNLOADED_MENU_GROUP_ID = 20000001;

}
