package cn.segi.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 打印某种数据结构的数据
 * @author liangzx
 * @version 1.0
 */
public class PrintObjectData {

	/**
	 * 打印hashmap数据
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String printRequestData(Object data)
	{
		if (null == data) {
			return "";
		}
		try {
			if (data instanceof Map) {
				HashMap<String, String> hmData = (HashMap<String, String>) data;
				StringBuilder sb = new StringBuilder();
				Iterator<String> it = hmData.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = hmData.get(key);
					sb.append(key).append("=").append(null != value ? URLEncoder.encode(value, "utf-8") : "");
					if (it.hasNext()) {
						sb.append("&");
					}
				}
				return sb.toString();
			}else if (data instanceof JSONObject){
				return URLEncoder.encode(data.toString(), "utf-8");
			}else if (data instanceof JSONArray) {
				return URLEncoder.encode(data.toString(), "utf-8");
			}else if (data instanceof String || data instanceof Integer || data instanceof Float || data instanceof Double || data instanceof Character || data instanceof Long) {
				return URLEncoder.encode(String.valueOf(data), "utf-8");
			}else {
				return "";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 打印hashmap数据
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String printRequestDataForLog(Object data)
	{
		if (null == data) {
			return "";
		}
		if (data instanceof Map) {
			HashMap<String, String> hmData = (HashMap<String, String>) data;
			StringBuilder sb = new StringBuilder();
			Iterator<String> it = hmData.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = hmData.get(key);
				sb.append(key).append("=").append(null != value ? value : "");
				if (it.hasNext()) {
					sb.append("&");
				}
			}
			return sb.toString();
		}else if (data instanceof JSONObject){
			return data.toString();
		}else if (data instanceof JSONArray) {
			return data.toString();
		}else if (data instanceof String || data instanceof Integer || data instanceof Float || data instanceof Double || data instanceof Character || data instanceof Long) {
			return String.valueOf(data);
		}else {
			return "";
		}
	
	}
	
}
