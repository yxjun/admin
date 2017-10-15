package per.zc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UrlUtil {

	/**
	 * 获得url的基础地址 
	 */
	public static String formatBaseUrl(String url) {
		url = removeUrlParam(url).replaceAll("//+", "/").replaceAll("/$", "");
		if(url.split("/").length >= 2) {
			url = url.replaceAll("/\\w*$", "");
		}
		return url;
	}
	
	
	/**
	 *  替换url 中 连续多 /(2个以上) 为一个斜杠，去掉末尾的斜杠
	 * @param url
	 * @return
	 */
	public static String formatUrl(String url) {
		// 多替为一，去掉末尾
		url = url.replaceAll("//+", "/").replaceAll("/$", "");
		return url;
	}
	
	/**
	 * 去掉url参数
	 * @param url
	 * @return
	 */
	public static String removeUrlParam(String url) {
		return url.replaceAll("[?#].*", "");
	}
	
	
	/**
	 * 读取url资源 为 文本
	 * @param url
	 * @return
	 */
	public static String getAsText(String url) {
		String urlString = "";
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.setConnectTimeout(1000 * 30);	//30秒超时
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String current;
			while ((current = in.readLine()) != null) {
				urlString += current;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlString;
	}
	
	public static void main(String[] args) {
		String url = "http://www.baidu.com/son?xxx=dfddfd&kkk=sdfdfd";
		System.out.println(formatUrl(url));
		System.out.println(removeUrlParam(url));
	}
}
