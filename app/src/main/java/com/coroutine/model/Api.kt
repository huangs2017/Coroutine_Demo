package com.coroutine.model

import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("users/{user}/repos")
    suspend fun listReposKt(@Path("user") user: String): List<Repo>

    @GET("users/{user}/repos")
    fun listReposRx(@Path("user") user: String): Single<List<Repo>>

    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}