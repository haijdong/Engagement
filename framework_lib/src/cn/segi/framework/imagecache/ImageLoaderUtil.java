package cn.segi.framework.imagecache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import cn.segi.framework.util.ImageUtil;

/**
 * 图片加载工具类
 *
 * @author liangzx
 */
public class ImageLoaderUtil {

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void load(Context context, ImageView iv, String imgUrl, int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .placeholder(defaultImgResource)
                .crossFade()
                .into(iv);
    }

    /**
     * 加载资源文件
     * @param context
     * @param iv
     * @param resId
     * @param defaultImgResource
     */
    public static void load(Context context, ImageView iv, int resId, int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context)
                .load(resId)
                .centerCrop()
                .placeholder(defaultImgResource)
                .crossFade()
                .into(iv);
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param target
     */
    public static void load(Context context, ImageView iv, String imgUrl, GlideDrawableImageViewTarget target) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl)
                .centerCrop().crossFade()
                .into(target);
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadWithFitCenter(Context context, ImageView iv, String imgUrl,
                                         int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl)
                .fitCenter().placeholder(defaultImgResource).crossFade()
                .into(iv);
    }

    /**
     *
     * 为了解决部分机型首页底部tab图片被回收的问题
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadMainTab(Context context,final ImageView iv, String imgUrl,
                                   int defaultImgResource, int errorImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl).asBitmap().placeholder(defaultImgResource).error(errorImgResource).into(iv);
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void load(Context context,final ImageView iv, String imgUrl,
                            int defaultImgResource, int errorImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl)
                .centerCrop().placeholder(defaultImgResource)
                .error(errorImgResource).crossFade().into(iv);
    }

    /**
     * @param context
     * @param target
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void load(Context context, BitmapImageViewTarget target,
                            String imgUrl, int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl).asBitmap()
                .centerCrop().placeholder(defaultImgResource).into(target);
    }

    /**
     * @param context
     * @param target
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadImg(Context context, BitmapImageViewTarget target,
                               String imgUrl, int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl).asBitmap()
                .placeholder(defaultImgResource).into(target);
    }

    /**
     * @param context
     * @param target
     * @param imgUrl
     * @param defaultImgResource
     * @param errorImgResource
     */
    public static void load(Context context, BitmapImageViewTarget target,
                            String imgUrl, int defaultImgResource, int errorImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl).asBitmap()
                .centerCrop().placeholder(defaultImgResource)
                .error(errorImgResource).into(target);
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadCircleImg(final Context context, final ImageView iv,
                                     String imgUrl, int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        ImageLoaderUtil.load(context, new BitmapImageViewTarget(iv) {
            @Override
            protected void setResource(Bitmap resource) {
                Bitmap dst;
                if (resource.getWidth() >= resource.getHeight()) {
                    dst = Bitmap.createBitmap(resource, resource.getWidth()
                                    / 2 - resource.getHeight() / 2, 0,
                            resource.getHeight(), resource.getHeight());
                } else {
                    dst = Bitmap.createBitmap(resource, 0,
                            resource.getHeight() / 2 - resource.getWidth()
                                    / 2, resource.getWidth(),
                            resource.getWidth());
                }
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                        .create(context.getResources(), dst);
                circularBitmapDrawable.setCornerRadius(50);
                circularBitmapDrawable.setAntiAlias(true);
                iv.setImageDrawable(circularBitmapDrawable);
                dst.recycle();
                dst = null;
            }
        }, imgUrl, defaultImgResource);
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadCircleImg(final Context context, final ImageView iv,
                                     String imgUrl, int defaultImgResource, final int cornerRadius) {
        if (isActivityDestroyed(context)) {
            return;
        }
        ImageLoaderUtil.load(context,
                new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                                .create(context.getResources(), resource);
                        circularBitmapDrawable
                                .setCornerRadius(cornerRadius);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                }, imgUrl, defaultImgResource);
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadRoundImg(final Context context, final ImageView iv,
                                    String imgUrl, int defaultImgResource) {
        if (isActivityDestroyed(context)) {
            return;
        }
        ImageLoaderUtil.load(context,
                new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        iv.setImageBitmap(ImageUtil.getRoundedCornerBitmap(resource));
                    }
                }, imgUrl, defaultImgResource);
    }

    /**
     * @param context
     * @param imgUrl
     * @return
     */
    public static Bitmap get(Context context, String imgUrl) {
        if (isActivityDestroyed(context)) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(imgUrl).asBitmap().into(100, 100).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 带回调的，例如启动页广告需要加载完成在展示
     *
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     * @param mListener
     * @see ImageLoaderFinishListener
     */
    public static void loadFinishImgCallBack(final Context context,
                                             final ImageView iv, String imgUrl, int defaultImgResource,
                                             final ImageLoaderFinishListener mListener) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).load(imgUrl).asBitmap()
                .centerCrop().placeholder(defaultImgResource)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        // TODO Auto-generated method stub
                        iv.setImageBitmap(resource);
                        if (null != mListener) {
                            mListener.onFinish();
                        }
                    }
                });
    }

    /**
     * 停止加载图片
     *
     * @param context
     */
    public static void stopLoadByContext(Context context) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).pauseRequests();
    }

    /**
     * 停止加载图片
     *
     * @param activity
     */
    public static void stopLoadByActivity(Activity activity) {
        if (isActivityDestroyed(activity)) {
            return;
        }
        Glide.with(activity).pauseRequests();
    }

    /**
     * 恢复加载图片
     *
     * @param activity
     */
    public static void resumeLoadByActivity(Activity activity) {
        if (isActivityDestroyed(activity)) {
            return;
        }
        Glide.with(activity).resumeRequests();
    }

    /**
     * 恢复加载图片
     *
     * @param context
     */
    public static void resumeLoadByContext(Context context) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.with(context).resumeRequests();
    }

    /**
     * 清除图片缓存
     *
     * @param context
     */
    public static void clearMemoryByContext(Context context) {
        if (isActivityDestroyed(context)) {
            return;
        }
        Glide.get(context).clearMemory();
    }

    /**
     * 清除图片缓存
     *
     * @param activity
     */
    public static void clearMemoryByActivity(Activity activity) {
        if (isActivityDestroyed(activity)) {
            return;
        }
        Glide.get(activity).clearMemory();
    }

    /**
     * 转化bitmap
     *
     * @param context
     * @param imgUrl
     * @return
     */
    public static Bitmap getBitmap(Context context, String imgUrl) {
        if (isActivityDestroyed(context)) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(imgUrl).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 判断上下文所属页面是否已销毁
     *
     * @param context
     * @return
     */
    public static boolean isActivityDestroyed(Context context) {
        if (null == context) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 17 && context instanceof FragmentActivity && ((FragmentActivity) context).isDestroyed()) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 17 && context instanceof Activity && ((Activity) context).isDestroyed()) {
            return true;
        }
        return false;
    }
}
