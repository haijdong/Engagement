/*
 * 文件名：ExecutorSupport.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 获取Executor的接口
 * 
 * @author liangzx
 * @version [2015-10-16] 
 */
public class ExecutorSupport {
    /**
     * 线程池运行的核心线程数
     */
    private static final int CORE_POOL_SIZE = 5;
    
    /**
     * 线程池最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = 64;
    
    /**
     * 保持运行的线程数
     */
    private static final int KEEP_ALIVE = 3;
    
    /**
     * 采用ThreadFactory管理线程
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        /**
         * 线程index
         */
        private final AtomicInteger mCount = new AtomicInteger(1);

        /**
         * {@inheritDoc}
         */
        @Override
		public Thread newThread(Runnable r) {
            return new Thread(r, "Framework lib Thread #" + mCount.getAndIncrement());
        }
    };

    /**
     * 提交到线程池的Runnable队列
     */
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(10);

    /**
     * 线程池
     */
    private static final ThreadPoolExecutor sExecutor
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                    TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    /**
     * 获取线程池
     * @return 线程池
     */
    public static Executor getExecutor() {
        return sExecutor;
    }
    
    /**
     * 关闭线程池
     */
    public static void shutdown() {
        if (!sExecutor.isShutdown()) {
            sExecutor.shutdown();            
        }
    }
}
