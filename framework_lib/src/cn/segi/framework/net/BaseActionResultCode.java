/*
 * 文件名：BaseActionResultCode.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;

/**
 * 公共的ActionResult码, 项目中使用的ActionResultCode需要继承此类
 * @author liangzx
 * @version [segi, 2015-10-16] 
 */
public interface BaseActionResultCode {
    /**
     * 请求操作成功
     */
    int ACTION_SUCESS = 0;
    
    /**
     * 操作失败
     */
    int ACTION_FAIL = -1;
    
    /**
     * 网络异常
     */
    int NETWORK_ERROR = 4002;

    /**
     * 解析数据出现异常
     */
    int PARSE_ERROR = 4003;
    
    /**
     * 网络连接超时
     */
    int NETWORK_TIMEOUT = 4004;
    
    /**
     * 捕获程序异常
     */
    int CAUGHT_EXCEPTION = 4005;
    
    /**
     * 连接服务器异常
     */
    int HOST_CONNECT_ERROR = 4006;

    /**
     * 未登录
     */
    int TOKEN_ERROR = 1000000;
}
