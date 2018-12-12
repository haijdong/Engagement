package com.synjones.huixinexiao.common;

/**
 * 界面跳转相关参数定义<BR>
 * 
 * @author liangzx
 * @version 1.0
 */
public interface FusionIntent {

	String ACTION = "com.vcloud.mijia.";
	/**
	 * 主页
	 */
	public String MAIN_ACTION = ACTION + "MAIN";
	/**
	 * 
	 */
	public String LOGIN_ACTION = ACTION + "LoginActivity";
	public String STOP_SERVICE = ACTION + "TcpService";

	/**
	 *  
	 */
	String EXTRA_DATA1 = "extra_data1";
	/**
	 * 
	 */
	String EXTRA_DATA2 = "extra_data2";
	/**
	 * 
	 */
	String EXTRA_DATA3 = "extra_data3";
	/**
	 * 
	 */
	String EXTRA_DATA4 = "extra_data4";
	/**
	 * 
	 */
	String EXTRA_DATA5 = "extra_data5";
	/**
	 * 
	 */
	String EXTRA_DATA6 = "extra_data6";
	/**
	 * 
	 */
	String EXTRA_DATA7 = "extra_data7";
	/**
	 * 
	 */
	String EXTRA_FROM = "extra_from";
}
