package com.klgz.shakefun.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

/**
 * @author wk
 *图片上传所需方法
 */
public class UpImageUtils {

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public void byteArr2Base64(byte[] byArr){
		String imgBase64 = Base64.encodeToString(byArr, Base64.DEFAULT);
	}
	
	//test
/*	public static String bitmap2base64(Bitmap bm, String imgname){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		bm.compress((".png").equals(imgname)?Bitmap.CompressFormat.PNG:Bitmap.CompressFormat.JPEG, 30, baos);
		return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
	}*/
	
	public static String bitmap2base64(Bitmap bm, String imgname){
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        bm.compress((".png").equals(imgname)?Bitmap.CompressFormat.PNG:Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	        int options = 100;  
	        while (options>=10 && baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
	            baos.reset();//重置baos即清空baos  
	            bm.compress((".png").equals(imgname)?Bitmap.CompressFormat.PNG:Bitmap.CompressFormat.JPEG, options, baos);
	            options -= 10;//每次都减少10  
	        }  
		return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
	}
	
	//压缩图片大小  
    public static Bitmap compressImage(Bitmap image) {  
  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while (baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	
	
	 public static Bitmap compressImageFromFile(String srcPath, int width2, int height2) {  
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	        
	        newOpts.inJustDecodeBounds = false;  
	        int w = newOpts.outWidth;  
	        int h = newOpts.outHeight;  
	        float hh = height2;//  
	        float ww = width2;//  
	        int be = 1;  
	        if (w > h && w > ww) {  
	            be = (int) (newOpts.outWidth / ww);  
	        } else if (w < h && h > hh) {  
	            be = (int) (newOpts.outHeight / hh);  
	        }  
	        if (be <= 0)  
	            be = 1;  
	        newOpts.inSampleSize = be;//设置采样率  
	          
	        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
	        newOpts.inPurgeable = true;// 同时设置才会有效  
	        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  http://ptlogin2.qq.com/jump?uin=157350302&skey=@pGlt4oRBR&u1=http%3A%2F%2Fuser.qzone.qq.com%2F157350302%2Finfocenter%3Fqz_referrer%3Dqqtips%26tab%3Dnotification
	       
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	        //return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩   //其实是无效的,大家尽管尝试  
	        
	        //旋转图片
	        Matrix matrix = new Matrix();
	        matrix.postRotate(getExifOrientation(srcPath));
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	        return bitmap;  
	    }  
	 
	 public static Bitmap compressImageFromFile(String srcPath) {  
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	        
	        newOpts.inJustDecodeBounds = false;  
	        int w = newOpts.outWidth;  
	        int h = newOpts.outHeight;  
	        float hh = 800f;//  
	        float ww = 480f;//  
	        int be = 1;  
	        if (w > h && w > ww) {  
	            be = (int) (newOpts.outWidth / ww);  
	        } else if (w < h && h > hh) {  
	            be = (int) (newOpts.outHeight / hh);  
	        }  
	        if (be <= 0)  
	            be = 1;  
	        newOpts.inSampleSize = be;//设置采样率  
	          
	        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
	        newOpts.inPurgeable = true;// 同时设置才会有效  
	        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  
	       
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	        //return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩   //其实是无效的,大家尽管尝试  
	        
	        //选择图片
	        Matrix matrix = new Matrix();
	        matrix.postRotate(getExifOrientation(srcPath));
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	        
	        
	        return bitmap;  
	    } 
	 
	 /**
	  * 获取图片，正向，需选择的角度
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath) {
	       int degree = 0;
	       ExifInterface exif = null;

	       try {
	           exif = new ExifInterface(filepath);
	       } catch (IOException ex) {
	          // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
	       }

	       if (exif != null) {
	           int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	           if (orientation != -1) {
	               // We only recognize a subset of orientation tag values.
	               switch (orientation) {
	               case ExifInterface.ORIENTATION_ROTATE_90:
	                   degree = 90;
	                   break;

	               case ExifInterface.ORIENTATION_ROTATE_180:
	                   degree = 180;
	                   break;

	               case ExifInterface.ORIENTATION_ROTATE_270:
	                   degree = 270;
	                   break;
	               default:
	                   break;
	               }
	           }
	       }

	       return degree;
	   }

	
//	 public static void or(){
//  
//
//		 
//
//         int degree = NmsCommonUtils.getExifOrientation(path);
//         if(degree == 90 || degree == 180 || degree == 270){
//             //Roate preview icon according to exif orientation
//             Matrix matrix = new Matrix();
//             matrix.postRotate(degree);
//             b = Bitmap.createBitmap( BitmapFactory.decodeStream(input),0, 0, outWidth, outHeight, matrix, true);
//             
//         }else{
//             //do not need roate the icon,default
//             b = BitmapFactory.decodeStream(input, null, options);
//         }
//	 }
}