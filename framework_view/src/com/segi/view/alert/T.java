package com.segi.view.alert;

import android.content.Context;
import android.widget.Toast;

/**
 * 显示提示或弹窗
 * @author liangzx
 * @version [uhome, 2015-1-6]
 */
public class T {
    /**
     * 显示toast
     * @param desc
     */
    public static void show(Context context, String desc)
    {
    	Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 显示toast
     * @param id
     */
    public static void show(Context context, int id)
    {
    	Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }
    
   
}
