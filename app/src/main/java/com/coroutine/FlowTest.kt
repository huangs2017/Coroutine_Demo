package com.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlin.random.Random

// 循环调用10次网络请求 获取的结果平方，然后过滤出偶数，最后取前2个
class FlowTest {

    suspend fun useFlow() {
        val flow = flow {
            repeat(10) {
                emit(request())
            }
        }

        flow.map { it * it }
            .filter { it % 2 == 0 }
            .take(2)
            .onEach { println(it) }
            .collect() // flow开始执行


        flow.onEach { println(it) }
            .flowOn(Dispatchers.IO) // flow切换协程-方法1
            .collect()

        val scope = CoroutineScope(Dispatchers.IO)
        flow.onEach { println(it) }
            .launchIn(scope) // flow切换协程-方法2
    }

    suspend fun request(): Int {
        delay(1000)
        return Random.nextInt(100)
    }

}