/*
 * 文件名：DbAdapterUtil.java
 * 创建人：王玉丰
 * 创建时间：2013-7-18
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package cn.segi.framework.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

/**
 * 数据库操作工具类<BR>
 * 
 * @author liangzx
 * @version [segi, 2013-7-18] 
 */
public class DbAdapterUtil {
    /**
     * 
     * 根据列名返回列值<BR>
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public static String getCursorStringValues(Cursor cursor, String columnName) {
        String retValue = null;
        if (!TextUtils.isEmpty(columnName) 
                && cursor.getColumnIndex(columnName) != -1) {
            retValue = cursor.getString(cursor.getColumnIndex(columnName));
        }
        return retValue;
    }
    
    /**
     * 
     * 根据列名返回列值<BR>
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public static Long getCursorLongValues(Cursor cursor, String columnName) {
        Long retValue = 0L;
        if (!TextUtils.isEmpty(columnName) 
                && cursor.getColumnIndex(columnName) != -1) {
            retValue = cursor.getLong(cursor.getColumnIndex(columnName));
        }
        return retValue;
    }
    
    /**
     * 
     * 根据列名返回列值<BR>
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public static int getCursorIntValues(Cursor cursor, String columnName) {
        int retValue = 0;
        if (!TextUtils.isEmpty(columnName) 
                && cursor.getColumnIndex(columnName) != -1) {
            retValue = cursor.getInt(cursor.getColumnIndex(columnName));
        }
        return retValue;
    }

    /**
     * 
     * 根据列名返回列值<BR>
     * 
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public static byte[] getCursorBytesValues(Cursor cursor, String columnName) {
        byte[] array = null;
        if (!TextUtils.isEmpty(columnName) 
                && cursor.getColumnIndex(columnName) != -1) {
            array = cursor.getBlob(cursor.getColumnIndex(columnName));
        }
        
        return array;
    }

    /**
     * 关闭游标<BR>
     *
     * @param cursor 要关闭的游标对象
     */
    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
    
    /**
     * 使用SQLiteStatement进行数据操作时，对数据进行验证
     * @param stat
     * @param index
     * @param value
     */
    public static void bindString(SQLiteStatement stat, int index, String value)
    {
    	if (null != stat) {
    		stat.bindString(index, TextUtils.isEmpty(value) ? "" : value);
		}
    }
    
    /**
     * 使用SQLiteStatement进行数据操作时，对数据进行验证
     * @param stat
     * @param index
     * @param value
     */
    public static void bindLong(SQLiteStatement stat, int index, long value)
    {
    	if (null != stat) {
    		stat.bindLong(index, value);
    	}
    }
}
