package com.project.sysumobilelibrary.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * ������������
 * ��ҪȨ�ޣ�
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 *
*/
public class MyNetworkUtil {
	private static final String TAG = "NetworkUtil";
	
	private static final int CONNECT_TIMEOUT = 10000;
	private static final int READ_TIMEOUT = 15000;
	
	private static boolean isGPRSConnected;
	private static boolean isWIFIConnected;
	private static NetworkStateReceiver networkStateReceiver = null;
	
	
	/**��ʼ��������������
	 * @param context ������
	 */
	public void initNetworkEnvironment(Context context) {
		if (networkStateReceiver == null){
			initNetStateReceiver(context, networkStateReceiver);
		}
		
	}
	
	/**�ͷŸ�����������
	 * @param context ������
	 */
	public void releaseNetworkEnvironment(Context context) {
		if (networkStateReceiver != null) {
			releaseNetStateReceiver(context, networkStateReceiver);
		}
	}
	
	/**
	 * @param img_url
	 * @return
	 */
	public static Bitmap download_image(String img_url) {
		HttpURLConnection httpURLConnection = null;
		Bitmap bitmap = null;
		try {
			URL url = new URL(URLEncode(img_url));
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
			httpURLConnection.setReadTimeout(READ_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(false);//Ĭ����false�ģ�����Ϊtrue�ܿ���Get������post
			if (httpURLConnection.getResponseCode() == 200){
				InputStream inputStream = httpURLConnection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);//���룬ImageView����setImageBitmapչʾ
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return bitmap;
	}
	
	
	/** ��ͨ��Get����
	 * @param get_url url������
	 * @return ת��Ϊgbk��String
	 */
	public static String doGet(String get_url) {
		HttpURLConnection httpURLConnection = null;
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL(URLEncode(get_url));
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
			httpURLConnection.setReadTimeout(READ_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(false);//Ĭ����false�ģ�����Ϊtrue�ܿ���Get������post
			if (httpURLConnection.getResponseCode() == 200){
				InputStream inputStream = httpURLConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));//�����룬ȥ��"UTF-8"����
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		String result = unicode2gbk(response.toString());
		return result;
	}
	

	/** ��ͨ��POST����
	 * @param post_url url
	 * @param datas post����
	 * @return ת��Ϊgbk��String
	 */ 
	public static String doPost(String post_url, String datas) {
		HttpURLConnection httpURLConnection = null;
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL(URLEncode(post_url));
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
			httpURLConnection.setReadTimeout(READ_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
			out.writeBytes(URLEncode(datas));//�����˱���
			if (httpURLConnection.getResponseCode() == 200) {
				InputStream inputStream = httpURLConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));//�����룬ȥ��"UTF-8"����
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		String result = unicode2gbk(response.toString());
		return result;
	}
	
	/**��url���б��룬֧������url����
	 * @param urlString �������url
	 * @return
	 */
	public static String URLEncode(String urlString){
		String url = null;
		try {
			/*
			url = new URL(urlString);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			url = uri.toURL();
			*/
			//url = new URL(URLEncoder.encode(urlString, "UTF-8"));
			boolean hasAppend = false;
			if (!urlString.startsWith("http://")){
				urlString = "http://127.0.0.1/?" + urlString;
				hasAppend = true;
			}
			final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
			url = Uri.encode(urlString, ALLOWED_URI_CHARS);
			if (hasAppend) {
				url = url.replace("http://127.0.0.1/?", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**��Unicode����תΪGBK����(����)
	 * @param unicodeString Unicode�����ַ���
	 * @return String GBK�����ַ���
	 */
	public static String unicode2gbk(String unicodeString){
		StringBuilder sbGBK = new StringBuilder();
		int i = -1;
		int pos = 0;
		while((i=unicodeString.indexOf("\\u", pos)) != -1){
			sbGBK.append(unicodeString.substring(pos, i));
			if(i+5 < unicodeString.length()){
				pos = i+6;
				sbGBK.append((char)Integer.parseInt(unicodeString.substring(i+2, i+6), 16));
			}
		}
		sbGBK.append(unicodeString.substring(pos));
		return sbGBK.toString();
	}
	
	
	
	
	
	
	/**ע������״̬�㲥������
	 * @param context ������
	 * @param receiver ������
	 */
	private void initNetStateReceiver(Context context, NetworkStateReceiver receiver) {
		receiver = new NetworkStateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(receiver, filter);
		receiver.onReceive(context, null);//�״μ��һ��
	}
	/**��ע������״̬�㲥������
	 * @param context ������
	 * @param receiver ������
	 */
	private void releaseNetStateReceiver(Context context, NetworkStateReceiver receiver){
		context.unregisterReceiver(receiver);
	}

	public static boolean isNetworkContected() {
		return isGPRSConnected || isWIFIConnected;
	}
	
	
	
	public static boolean isGPRSConnected() {
		return isGPRSConnected;
	}
	public static void setGPRSConnected(boolean isGPRSConnected) {
		MyNetworkUtil.isGPRSConnected = isGPRSConnected;
	}
	public static boolean isWIFIConnected() {
		return isWIFIConnected;
	}
	public static void setWIFIConnected(boolean isWIFIConnected) {
		MyNetworkUtil.isWIFIConnected = isWIFIConnected;
	}
}


/**
 *��������״̬�仯�Ĺ㲥��
 *��ҪȨ�ޣ�<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * 		 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
class NetworkStateReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		MyNetworkUtil.setGPRSConnected(gprs.isConnected());
		MyNetworkUtil.setWIFIConnected(wifi.isConnected());
	}
	
}