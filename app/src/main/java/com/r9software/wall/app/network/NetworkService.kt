package com.r9software.wall.app.network

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



interface NetworkService {

    @GET("http://10.0.2.2:8000/api/wall")
    fun downloadWall(@Query("page") page: Int): Single<WallResponse>

    @FormUrlEncoded
    @POST("http://10.0.2.2:8000/api/login")
    fun login(@Field("email") email: String,@Field("password")password:String): Single<LoginResponse>

    @FormUrlEncoded
    @POST("http://10.0.2.2:8000/api/wall")
    fun postToWall(@Field("wall_content") content: String,@Field("user_id")userId:String): Single<SimpleResponse>

    @FormUrlEncoded
    @POST("http://10.0.2.2:8000/api/register")
    fun register(@Field("email") email: String,@Field("password")password:String,@Field("confirmation_password")confirmationPassword:String,@Field("name")name:String): Single<LoginResponse>



    companion object {
        fun getService(): NetworkService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }
}