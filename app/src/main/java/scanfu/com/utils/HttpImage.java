package scanfu.com.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import scanfu.com.bean.MyApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpImage {

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url, MyApplication app) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			
			// -------取cookid-------------
			String cookieval = conn.getHeaderField("set-cookie");
			String sessionid = null;
			if (cookieval != null) {
				sessionid = cookieval.substring(0, cookieval.indexOf(";"));
			}
			// -------取cookid结束-------------
			app.setSessionId(sessionid);

			// 这句可有可无，没有影响
			conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	/**
	 * 向服务器发送手机号
	 * 
	 * @param url
	 * @return
	 */
	public static void getHttpPhome(String url) {
		URL myFileURL;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
