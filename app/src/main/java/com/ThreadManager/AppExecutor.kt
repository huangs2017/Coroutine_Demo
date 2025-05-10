package com.test.ThreadManager

import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

// 1.线程池管理器
object AppExecutor {
    // 根据CPU核心数动态配置
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    // CPU密集型线程池（例如数据处理）
    val cpuThreadPool: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(
            CPU_COUNT, CPU_COUNT * 2, 30L, TimeUnit.SECONDS,
            LinkedBlockingQueue(64),
            ThreadPoolExecutor.CallerRunsPolicy()
        )
    }

    // IO密集型线程池（例如网络请求）
    val ioThreadPool: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(
            CPU_COUNT * 2, CPU_COUNT * 4, 60L, TimeUnit.SECONDS,
            SynchronousQueue(),
            ThreadPoolExecutor.DiscardOldestPolicy()
        )
    }

    fun cancel(task: Future<*>?) {
        task?.cancel(true)
    }
}