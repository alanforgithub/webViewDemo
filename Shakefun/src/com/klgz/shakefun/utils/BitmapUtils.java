/** 
 * 本程序代码版权归北京快乐格子信息技术有限公司或本项目委托的客户方所有。
 * Copyright www.kuailegezi.com  
 * legal@kuailegezi.com
 */
package com.klgz.shakefun.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;



/**
 * @author wk
 *图片工具类
 */
public class BitmapUtils {
	
	
	public static boolean act_bool=true;
	public static List<Bitmap> bmp=new ArrayList<Bitmap>();
	public static List<String> drr=new ArrayList<String>();
	
	//图片sd地址，上传图片时调用下面方法压缩，保存到临时文件夹，图片压缩后小于100kb,失真不明显
	public static Bitmap revitionImageSize(String path) throws IOException{
		BufferedInputStream in=new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		
		BitmapFactory.decodeStream(in,null,options);
		in.close();
		int i=0;
		Bitmap bitmap=null;
		while(true){
			if((options.outWidth>>i<=800)&&(options.outHeight>>i<=800)){
				in=new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize=(int) Math.pow(2.0D, i);
				options.inJustDecodeBounds=false;
				bitmap=BitmapFactory.decodeStream(in,null,options);
				break;
			}
			i+=1;
		}
		
		return bitmap;
	}
	
	
	/**
	 * 获取本地图片
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url){
		try{
			FileInputStream fis=new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param x
	 *            图像的宽度
	 * @param y
	 *            图像的高度
	 * @param image
	 *            源图片
	 * @param outerRadiusRat
	 *            圆角的大小
	 * @return 圆角图片
	 */
	public static Bitmap createFramePhoto(int x,int y,Bitmap image,float outerRadiusRat ){
		//根据一个源文件新建一个darwable对象
		Drawable imageDrawable=new BitmapDrawable(image);
		
		//新建一个输出的图片
		Bitmap output=Bitmap.createBitmap(x,y,Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		
		//新建一个矩形
		RectF outerRect=new RectF(0,0,x,y);
		//产生一个红色的圆角矩形
		Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
		
		//将原图片回执到这个圆角矩形上
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, x, y);
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();
		
		return output;
		
	}
	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap){
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		float roundPx;
		float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
		if(width<=height){
			roundPx=width/2;
			left=0;
			top=0;
			right=width;
			bottom=width;
			height=width;
			dst_left=0;
			dst_top=0;
			dst_right=width;
			dst_bottom=width;
		}else{
			roundPx =height/2;
			float clip=(width-height)/2;
			left=clip;
			right=width-clip;
			top=0;
			bottom=height;
			width=height;
			
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		
		Bitmap output=Bitmap.createBitmap(width,height,Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		
		final Paint paint=new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		
		paint.setAntiAlias(true); //设置画笔无锯齿
		
		canvas.drawARGB(0, 0, 0, 0);//填充整个Canvas
		
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src,dst, paint);

		return output;
	}
	
	/**
	 * save bitmap
	 * @param bitName
	 * @param mBitmap
	 */
	public static void saveBitmap(String bitPath,Bitmap mBitmap){
		try{
			File file=new File(bitPath);
			file.createNewFile();
			FileOutputStream fOut=new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			
			fOut.flush();
			fOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
