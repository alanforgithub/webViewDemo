/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.tools;



import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 文件上传工具类
 * @author Feng
 * @version 1.0
 * @date 20140915
 *
 */

public class UploadFile {
	
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	
	public static  String GetImageGson;
	
	/**
	 * 上传文件
	 * @author feng
	 * @param actionUrl
	 * @param uploadFile
	 * @param newName
	 * @return
	 */
	public static String uploadFile(String actionUrl, String uploadFile,
			String newName) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(8000);
			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			
			con.setRequestMethod("POST");
			
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			//System.out.println("Content-Disposition: form-data; "+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);

			
			FileInputStream fStream = new FileInputStream(uploadFile);
			
			
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int length = -1;

			while ((length = fStream.read(buffer)) != -1) {
				
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

			
			fStream.close();
			ds.flush();
			
			
			if(con.getResponseCode()==200){
				InputStream inputStream = con.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int b =0;
			while((b=inputStream.read())!=-1){
				bos.write(b);
			}
			
			return bos.toString();
			}
			ds.close();
			
			return "";
		} catch (Exception e) {
			
			e.printStackTrace();
			return "";
		}
	}
	
	
	/**
	 * 根据照片路径获得流
	 * @author feng
	 * @param imagepath
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(String imagepath) throws Exception {
		FileInputStream fs = new FileInputStream(imagepath);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while (-1 != (len = fs.read(buffer))) {
		outStream.write(buffer, 0, len);
		}
		outStream.close();
		fs.close();
		return outStream.toByteArray();
		}
	
	
	/**
	 * 下载图片
	 * @author Feng
	 * @param imageUrl
	 * @return
	 */
	public static Bitmap downloadImage(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
