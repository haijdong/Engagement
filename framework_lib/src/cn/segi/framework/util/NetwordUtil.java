package cn.segi.framework.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.net.NetworkInterface;
import java.util.List;

/**
 * 
 * 网络判断
 * 
 * @author zengxj
 * @version [uHome, 2013-12-24]
 */
public class NetwordUtil {
	
	/**
	 * 判断是否存在网络问题
	 * @param activity
	 * @return
	 */
	public static boolean isConnectInternet(Activity activity) {
		if (null != activity) {
			ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}
		return false;
	}

    /**
     * 判断是否存在网络问题
     * @param mContext
     * @return
     */
    public static boolean isConnectInternetByContext(Context mContext) {
        if (null != mContext) {
            ConnectivityManager conManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
	
	/**
	 * 获取当前有效的网络类型
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context)
	{
		if (context != null) {    
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();    
	         if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {    
	             return mNetworkInfo.getType();    
	         }    
	     }
		return -1;
	}

	/**
	 * 获取已连接的Wifi路由器的Mac地址
	 */
	public static String getConnectedWifiMacAddress(Context context) {
		String connectedWifiMacAddress;
		WifiManager wifiManager;
		if (Build.VERSION.SDK_INT < 24) {
			wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		} else {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (null != connectionInfo) {
			connectedWifiMacAddress = connectionInfo.getBSSID();
		} else {
			return null;
		}
		if (null == connectedWifiMacAddress || connectedWifiMacAddress.equals("00:00:00:00:00:00")) {
			List<ScanResult> wifiList = wifiManager.getScanResults();
			if (null != wifiList && wifiList.size() > 0) {
				for (int i = 0; i < wifiList.size(); i++) {
					ScanResult result = wifiList.get(i);
					if (connectionInfo.getSSID().replace("\"", "").equals(result.SSID)) {
						connectedWifiMacAddress = result.BSSID;
					}
				}
			}
		}
		return connectedWifiMacAddress;
	}

	/**
	 * 获取已连接的Wifi的ip地址
	 */
	public static String getConnectedWifiIpAddress(Context context) {
		String connectedWifiIpAddress = null;
		WifiManager wifiManager;
		if (Build.VERSION.SDK_INT < 24) {
			wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		} else {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (null != connectionInfo) {
            connectedWifiIpAddress = intToIp(connectionInfo.getIpAddress());
		}
		return connectedWifiIpAddress;
	}

	/**
	 * 检测当前连接的网络是否wifi以及是否连接成功
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (null != context) {
			ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
			if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return networkInfo.isConnected();
			}
		}
		return false;
	}

	/**
	 * 将获取的int转为真正的ip地址
	 * @param i
	 * @return
	 */
	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
	}


    /**
     * 获取当前手机wifi网卡的物理地址
     * @param context
     * @return
     */
	public static String getPhoneMacAddress(Context context) {
		String macAddress = null;
		WifiManager wifiManager;
		if (Build.VERSION.SDK_INT < 24) {
			wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		} else {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (null != wifiInfo) {
			macAddress = wifiInfo.getMacAddress();
		}
		if (null == macAddress || macAddress.equals("02:00:00:00:00:00")) {
			try {
				NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
				if (null != networkInterface) {
					byte[] macBytes = networkInterface.getHardwareAddress();
					if (macBytes == null) {
						return null;
					}
					StringBuilder res1 = new StringBuilder();
					for (byte b : macBytes) {
						res1.append(String.format("%02x:", b));
					}
					if (res1.length() > 0) {
						res1.deleteCharAt(res1.length() - 1);
					}
					macAddress = res1.toString();
				}
			} catch (Exception ex) {
			}
		}
		return macAddress;
	}

    /**
     * wifi是否打开
     * @param context
     * @return
     */
	public static boolean isWifiEnable(Context context) {
        WifiManager wifiManager;
        if (Build.VERSION.SDK_INT < 24) {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        } else {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED;
    }

    /**
     * 获取连接的wifi实体（未必已连接成功）
     */
    public static WifiInfo getConnectedWifiInfo(Context context) {
        WifiManager wifiManager;
        if (Build.VERSION.SDK_INT < 24) {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        } else {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        return connectionInfo;
    }

}
