/*
 * 文件名：FusionConfig.java
 * 创建人：liangzx
 * 创建时间：2015-11-6
 */
package com.tommy.base.config;

/**
 * 公共配置信息<BR>
 * TODO 暂只放置了服务器URL常量, 有需要按实际使用可实现成单例(当包含一些方法时)
 *
 * @author liangzx
 * @version [uHomeBk, 2015-11-6]
 */
public class FusionConfig {

    public static String textUrl = "http://dev-air-grid.xinxuntech.com/api";

    public static String serverUrl = "http://dev-air-grid-o.xinxuntech.com/api";

    /**
     * B端接口地址
     */
    public static String SERVER_URL = serverUrl;
    //	public static String SERVER_URL = "http://dev-air-grid.xinxuntech.com/api";
//	public static String SERVER_URL = "http://192.168.0.144:8085/api";
    public static String IMAGE_URL = "";
    public static String THUMB_IMAGE_URL = "";


    public static String WX_ID = "wxd930ea5d5a258f4f";
}
