package com.coroutine

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val executor = Executors.newCachedThreadPool()
        executor.execute {
            println("Executors thread: ${Thread.currentThread().name}")
        }

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            println("Coroutines thread: ${Thread.currentThread().name}")
        }
//        val scope = MainScope()
//        runBlocking {  }


        lifecycleScope.launch {
            println("lifecycleScope------------------------")
            val data = getData()
            val processedData = processData(data)
            findViewById<TextView>(R.id.textView).text = processedData
        }
    }


    // 耗时函数 1
    private suspend fun getData(): String {
        delay(5000)
        return "hen_coder"
    }

    // 耗时函数 2
    private suspend fun processData(data: String): String {
        delay(5000)
//        Thread.sleep(5000)
        return "$data---"
    }

}