package com.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.coroutine.model.MyViewModel
import com.coroutine.databinding.ActivityMainBinding
import com.coroutine.model.Repo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.Main) {
            ioCode1()
            uiCode1()
        }


        val loginViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val api = loginViewModel.api

        // 协程方式
        GlobalScope.launch(Dispatchers.Main) {
          try {
            val repos = api.listReposKt("rengwuxian") // 后台
            binding.textView.text = repos[0].name // 前台
          } catch (e: Exception) {
            binding.textView.text = e.message // 前台
          }
        }


        // RxJava方式
//        api.listReposRx("rengwuxian")
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : SingleObserver<List<Repo>> {
//                override fun onSuccess(repos: List<Repo>) {
//                    binding.textView.text = repos[0].name
//                }
//
//                override fun onSubscribe(d: Disposable) { }
//
//                override fun onError(e: Throwable) {
//                    binding.textView.text = e.message
//                }
//            })


        // 原始方式
//        api.listRepos("rengwuxian")
//          .enqueue(object : Callback<List<Repo>?> {
//            override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
//              val nameRengwuxian = response.body()?.get(0)?.name
//              api.listRepos("google")
//                .enqueue(object : Callback<List<Repo>?> {
//                  override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
//                    binding.textView.text = "${nameRengwuxian}-${response.body()?.get(0)?.name}"
//                  }
//
//                  override fun onFailure(call: Call<List<Repo>?>, t: Throwable) { }
//                })
//            }
//
//            override fun onFailure(call: Call<List<Repo>?>, t: Throwable) { }
//          })



        // 2个请求并行
//        GlobalScope.launch(Dispatchers.Main) {
//          val one = async { api.listReposKt("rengwuxian") } // 同时执行
//          val two = async { api.listReposKt("google") } // 同时执行
//          binding.textView.text = "${one.await()[0].name} -> ${two.await()[0].name}"
//        }

        // 2个请求并行
//        Single.zip(
//            api.listReposRx("rengwuxian"),
//            api.listReposRx("google"),
//            { list1, list2 -> "${list1[0].name} - ${list2[0].name}" }
//        ).observeOn(AndroidSchedulers.mainThread())
//            .subscribe { combined -> binding.textView.text = combined }
    }

    suspend fun ioCode1() {
        withContext(Dispatchers.IO) {
            println("Coroutines Camp io1 ${Thread.currentThread().name}")
        }
    }

    fun uiCode1() {
        println("Coroutines Camp ui1 ${Thread.currentThread().name}")
    }

}