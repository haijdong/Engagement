package cn.segi.framework.imagecache;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import cn.segi.framework.application.BaseApplication;
import cn.segi.framework.util.FrameworkUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.volley.VolleyUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

/**
 * Glide全局配置
 * @author liangzx
 *
 */
public class CustomGlideModule implements GlideModule {

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		// TODO Auto-generated method stub
		//加载的图片质量
		builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
		
		MemorySizeCalculator calculator = new MemorySizeCalculator(context);  
		int defaultMemoryCacheSize = calculator.getMemoryCacheSize();  
		int defaultBitmapPoolSize = calculator.getBitmapPoolSize();  
		//设置内存缓存大小
		builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
		//设置bitmap池的大小
		builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));
		//设置磁盘缓存目录及大小
		//此处应该检测磁盘还剩多少空间
		File mImageFile = new File(FrameworkUtil.getImagePath());
		if (!mImageFile.exists()) {
			mImageFile.mkdir();
		}
		builder.setDiskCache(new DiskLruCacheFactory(FrameworkUtil.getImagePath(), 150*1024*1024));
	}

	@Override
	public void registerComponents(Context context, Glide glide) {
		// TODO Auto-generated method stub
		glide.register(GlideUrl.class, InputStream.class,new VolleyUrlLoader.Factory(BaseApplication.getRequestQueue()));
	}

}
