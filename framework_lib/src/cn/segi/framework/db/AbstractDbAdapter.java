/*
 * 文件名：DbAdapterUtil.java
 * 创建人：王玉丰
 * 创建时间：2013-7-18
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package cn.segi.framework.db;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.segi.framework.log.Logger;

/**
 * 数据库操作工具类<BR>
 * @author liangzx
 * @version [lib, 2016-8-12]
 * @param <T>
 */
public abstract class AbstractDbAdapter<T> {
	
	protected static final String TAG = "AbstractDbAdapter";
	
    /**
     * 返回表名
     * @return String
     */
    protected abstract String getTableName();
    
    /**
     * 将model转换成ContentValues
     * @param info
     * @return ContentValues
     */
    protected abstract ContentValues setValues(T info);
    
    
    /**
     * 根据游标解析数据
     * @param cursor 游标对象
     * @return T
     */
    protected abstract T parseToModel(Cursor cursor);
    
    /**
     * 获取数据库操作对象
     * @return SQLiteDatabase
     */
    protected abstract SQLiteDatabase getSqLiteDatabase();
	
	/**
     * 插入数据
     * @param info
     * @return long
     */
    public long insert(T info) {
        if (null == info) {
            return -1;
        }
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.insert(getTableName(), null, setValues(info));
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
			return -1;
		}
    }
    
    /**
     * 插入数据
     * @param values
     * @return long
     */
    public long insert(ContentValues values) {
        if (null == values) {
            return -1;
        }
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.insert(getTableName(), null, values);
    	} catch (Exception e) {
    		Logger.d(TAG, e.getMessage());
    		return -1;
    	}
    }
    
    /**
     * 插入数据
     * @param values
     * @param nullColumnHack
     * @return long
     */
    public long insert(ContentValues values, String nullColumnHack) {
        if (null == values) {
            return -1;
        }
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.insert(getTableName(), nullColumnHack, values);
    	} catch (Exception e) {
    		Logger.d(TAG, e.getMessage());
    		return -1;
    	}
    }
    
    /**
     * 批量插入数据
     * @param list 数据列表
     * @return 批量处理结果
     */
    public int batchInsert(List<T> list) {
        if (null != list && list.size() > 0) {
            SQLiteDatabase db = null;
            try {
                db = getSqLiteDatabase();
                db.beginTransaction();
                for (T info : list) {
                    db.insert(getTableName(), null, setValues(info));
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Logger.d(TAG, e.getMessage());
                return -1;
            } finally {
                try {
                    db.endTransaction();
                } catch (Exception e2) {
                    Logger.d(TAG, e2.getMessage());
                }
            }
            return 1;
        }
        return -1;
    }
    
    /**
     * 批量插入数据
     * @param values 集合列表
     * @return 批量处理结果
     */
    public int batchInsertByCV(List<ContentValues> values) {
        if (null != values && values.size() > 0) {
            SQLiteDatabase db = null;
            try {
                db = getSqLiteDatabase();
                db.beginTransaction();
                for (ContentValues item : values) {
                    db.insert(getTableName(), null, item);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Logger.d(TAG, e.getMessage());
                return -1;
            } finally {
                try {
                    db.endTransaction();
                } catch (Exception e2) {
                    Logger.d(TAG, e2.getMessage());
                }
            }
            return 1;
        }
        return -1;
    }
    
    /**
     * 更新数据
     * @param info 实体
     * @param whereClause 条件
     * @param whereArgs 条件参数
     * @return
     */
    public int update(T info, String whereClause, String[] whereArgs) {
        if (null == info) {
            return -1;
        }
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.update(getTableName()
    				, setValues(info)
    				, whereClause
    				, whereArgs);
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
			return -1;
		}
    }
    
    /**
     * 更新数据
     * @param values 实体
     * @param whereClause 条件
     * @param whereArgs 条件参数
     * @return
     */
    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        if (null == values) {
            return -1;
        }
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.update(getTableName()
    				, values
    				, whereClause
    				, whereArgs);
    	} catch (Exception e) {
    		Logger.d(TAG, e.getMessage());
    		return -1;
    	}
    }
    
    /**
     * 批量更新数据
     * @param list 数据列表
     * @param whereClause 条件
     * @param whereArgs 条件参数
     * @return 批量处理结果
     */
    public int batchUpdate(List<T> list, String whereClause, String[] whereArgs) {
        if (null != list && list.size() > 0) {
            SQLiteDatabase db = null;
            try {
                db = getSqLiteDatabase();
                db.beginTransaction();
                for (T info : list) {
                    db.update(getTableName()
                            , setValues(info)
                            , whereClause
                            , whereArgs);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Logger.d(TAG, e.getMessage());
                return -1;
            } finally {
                try {
                    db.endTransaction();
                } catch (Exception e2) {
                    Logger.d(TAG, e2.getMessage());
                }
            }
            return 1;
        }
        return -1;
    }
    
    /**
     * 批量更新数据
     * @param values 数据列表
     * @param whereClause 条件
     * @param whereArgs 条件参数
     * @return 批量处理结果
     */
    public int batchUpdateByCV(List<ContentValues> values, String whereClause, String[] whereArgs) {
        if (null != values && values.size() > 0) {
            SQLiteDatabase db = null;
            try {
                db = getSqLiteDatabase();
                db.beginTransaction();
                for (ContentValues item : values) {
                    db.update(getTableName()
                            , item
                            , whereClause
                            , whereArgs);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Logger.d(TAG, e.getMessage());
                return -1;
            } finally {
                try {
                    db.endTransaction();
                } catch (Exception e2) {
                    Logger.d(TAG, e2.getMessage());
                }
            }
            return 1;
        }
        return -1;
    }
    
    /**
     * 删除数据
     * @param whereClause 条件
     * @param whereArgs 条件参数
     * @return 执行结果
     */
    public int delete(String whereClause, String[] whereArgs) {
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.delete(getTableName(), whereClause, whereArgs);
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
			return -1;
		}
    }
    
    /**
     * 删除全部数据
     * @return 执行结果
     */
    public int deleteAll() {
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		return db.delete(getTableName(), null, null);
    	} catch (Exception e) {
    		Logger.d(TAG, e.getMessage());
    		return -1;
    	}
    }
    
    /**
     * 查询所有数据
     * @param columns 查出的字段
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @param groupBy 分组
     * @param having 过滤
     * @param orderBy 排序
     * @return T
     */
    public List<T> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
    	List<T> list = null;
    	Cursor cursor = null;
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		cursor = db.query(getTableName()
    				, columns
    				, selection
    				, selectionArgs
    				, groupBy
    				, having
    				, orderBy);
    		if (cursor != null && cursor.moveToFirst()) {
    			list = new ArrayList<T>();
    			T info = null;
    			do {
    				info = parseToModel(cursor);
    				if (info != null) {
    					list.add(info);
    				}
    			} while (cursor.moveToNext());
    		}
    	} catch (Exception e) {
    		Logger.d(TAG, e.getMessage());
    	} finally {
    		closeCursor(cursor);
    	}
    	return list;
    }
    /**
     * 查询所有数据
     * @param columns 查出的字段
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @param orderBy 排序
     * @return T
     */
    public List<T> query(String[] columns,String selection, String[] selectionArgs, String orderBy) {
    	return query(columns, selection, selectionArgs, null, null, orderBy);
    }
    
    /**
     * 查询所有数据
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @param orderBy 排序
     * @return T
     */
    public List<T> query(String selection, String[] selectionArgs, String orderBy) {
    	return query(null, selection, selectionArgs, null, null, orderBy);
    }
    
    /**
     * 查询某个数据
     * @param columns 查出的字段
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @param orderBy 排序
     * @return T
     */
    public T queryOne(String[] columns, String selection, String[] selectionArgs, String orderBy) {
    	T info = null;
    	Cursor cursor = null;
    	SQLiteDatabase db = null;
    	try {
    		db = getSqLiteDatabase();
    		cursor = db.query(getTableName()
    				, columns
    				, selection
    				, selectionArgs
    				, null
    				, null
    				, orderBy);
    		if (cursor != null && cursor.moveToFirst()) {
				info = parseToModel(cursor);
    		}
    	} catch (Exception e) {
    		Logger.d(TAG, e.getMessage());
    	} finally {
    		closeCursor(cursor);
    	}
    	return info;
    }

    /**
     * 查询某个数据
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @param orderBy 排序
     * @return T
     */
    public T queryOne(String selection, String[] selectionArgs, String orderBy) {
    	return  queryOne(null, selection, selectionArgs, orderBy);
    }

    /**
     * 查询数据输出Cursor
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @return
     */
    public Cursor queryOutputCursor(String[] columns, String selection, String[] selectionArgs, String orderBy) {
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = getSqLiteDatabase();
			cursor = db.query(getTableName()
					, columns
					, selection
					, selectionArgs
					, null
					, null
					, orderBy);
			if (cursor != null && cursor.moveToFirst()) {
                return cursor;
			} else{
                return null;
            }
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
		return cursor;
	}
    
    /**
     * 是否存在某条件下的数据
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @return boolean
     */
    public boolean isExist(String selection, String[] selectionArgs) {
    	 boolean isExist = false;
         Cursor cursor = null;
         SQLiteDatabase db = null;
         try {
         	db = getSqLiteDatabase();
         	cursor = db.query(getTableName()
         			, null
	         		, selection
	         		, selectionArgs
	         		, null
	         		, null
	         		, null);
             if (cursor != null && cursor.moveToFirst()) {
                 isExist = true;
             }
         } catch (Exception e) {
        	 Logger.d(TAG, e.getMessage());
         } finally {
             closeCursor(cursor);
         }
         return isExist;
    }
    
    /**
     * 获取条件的数据数量
     * @param selection 条件
     * @param selectionArgs 条件参数值
     * @return int
     */
    public int getCount(String selection, String[] selectionArgs)
    {
    	int count = 0;
    	Cursor cursor = null;
    	SQLiteDatabase db = null;
        try {
        	db = getSqLiteDatabase();
         	cursor = db.query(getTableName()
         			, null
	         		, selection
	         		, selectionArgs
	         		, null
	         		, null
	         		, null);
             if (cursor != null) {
             	count = cursor.getCount();
             }
         } catch (Exception e) {
        	 Logger.d(TAG, e.getMessage());
         } finally {
        	 closeCursor(cursor);
         }
         return count;
    }
    
	
    /**
     * 
     * 根据列名返回列值<BR>
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public String getCursorStringValues(Cursor cursor, String columnName) {
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
    public Long getCursorLongValues(Cursor cursor, String columnName) {
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
     * 
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public double getCursorDoubleValues(Cursor cursor, String columnName) {
    	 double retValue = 0;
         if (!TextUtils.isEmpty(columnName) 
                 && cursor.getColumnIndex(columnName) != -1) {
             retValue = cursor.getDouble(cursor.getColumnIndex(columnName));
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
    public double getCursorFloatValues(Cursor cursor, String columnName) {
    	float retValue = 0;
    	if (!TextUtils.isEmpty(columnName) 
    			&& cursor.getColumnIndex(columnName) != -1) {
    		retValue = cursor.getFloat(cursor.getColumnIndex(columnName));
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
    public int getCursorIntValues(Cursor cursor, String columnName) {
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
     * @param cursor Cursor
     * @param columnName 列名
     * @return 列值
     */
    public int getCursorShortValues(Cursor cursor, String columnName) {
    	short retValue = 0;
    	if (!TextUtils.isEmpty(columnName) 
    			&& cursor.getColumnIndex(columnName) != -1) {
    		retValue = cursor.getShort(cursor.getColumnIndex(columnName));
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
    public byte[] getCursorBytesValues(Cursor cursor, String columnName) {
        byte[] array = null;
        if (!TextUtils.isEmpty(columnName) 
                && cursor.getColumnIndex(columnName) != -1) {
            array = cursor.getBlob(cursor.getColumnIndex(columnName));
        }
        return array;
    }
    
    /**
     * 关闭游标<BR>
     * @param cursor 要关闭的游标对象
     */
    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
    
}
