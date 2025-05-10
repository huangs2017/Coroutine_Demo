package com.test.ThreadManager

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Future

// 2.线程池绑定生命周期
class LifecycleExecutor(lifecycleOwner: LifecycleOwner) : DefaultLifecycleObserver {

    private val tasks = mutableListOf<Future<*>>()

    fun submit(task: Runnable): Future<*> {
        val future = AppExecutor.ioThreadPool.submit(task)
        tasks.add(future)
        return future
    }

    override fun onDestroy(owner: LifecycleOwner) {
        tasks.forEach {
            AppExecutor.cancel(it)
        }
        tasks.clear()
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }
}