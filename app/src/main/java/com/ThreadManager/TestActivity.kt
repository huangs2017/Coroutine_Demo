package com.ThreadManager

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.ThreadManager.AppExecutor
import com.test.ThreadManager.LifecycleExecutor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

// 设计一个线程池，供Android项目各处使用
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 执行CPU密集型任务
        AppExecutor.cpuThreadPool.execute {
//            val result = processImage()
        }


        // 结合Lifecycle使用
        val executor = LifecycleExecutor(this)
        val future =  executor.submit {
//            loadData()
        }


        // 3.将线程池包装成 CoroutineDispatcher，配合协程使用：
        val ioDispatcher = AppExecutor.ioThreadPool.asCoroutineDispatcher()
//      val ioDispatcher = newFixedThreadPoolContext(4, "ThreadPoolName")
        // 在协程中使用
        GlobalScope.launch(ioDispatcher) {
            // IO 任务
        }


        // 4.在Application中注册内存警告回调
        registerComponentCallbacks(object : ComponentCallbacks2 {
            override fun onTrimMemory(level: Int) {
                if (level >= TRIM_MEMORY_UI_HIDDEN) {
                    AppExecutor.ioThreadPool.apply {
                        queue.clear()
                        purge()
                    }
                }
            }

            override fun onConfigurationChanged(newConfig: Configuration) { }
            override fun onLowMemory() { }
        })
    }

}