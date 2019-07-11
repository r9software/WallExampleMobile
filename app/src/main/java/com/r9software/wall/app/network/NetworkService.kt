package com.r9software.wall.app.network

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("http://127.0.0.1:8000/api/wall")
    fun downloadWall(@Query("page") page: Int): Single<Response>

    companion object {
        fun getService(): NetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }
}