package cn.segi.framework.db;

import cn.segi.framework.log.Logger;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper's abstract class
 * @author liangzx
 * 
 */
public abstract class AbstractDbHelper extends SQLiteOpenHelper
{

	/**
	 * dbhelper单例对象
	 */
	protected static AbstractDbHelper mSingleInstance = null;

	
	public AbstractDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * 清空单例，关闭数据库
	 */
	public void clearSingleInstance() {
		if (null != mSingleInstance) {
			try {
				mSingleInstance.close();
				mSingleInstance = null;
			} catch (IllegalStateException e) {
				Logger.d("AbstractDbHelper", e.getMessage());
			}
		}
	}
	

}
