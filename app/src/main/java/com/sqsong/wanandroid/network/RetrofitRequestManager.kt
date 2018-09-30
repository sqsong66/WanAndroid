package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.base.HomeBannerBean
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitRequestManager {

    private var mApiService: ApiService

    constructor() {
        val retrofit = Retrofit.Builder().let {
            it.client(createOkHttpClient())
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        mApiService = retrofit.create(ApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder().let {
            it.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(interceptor)
                    .build()
        }
    }

    fun getHomeBanner(): Observable<HomeBannerBean> {
        return mApiService.getHomeBanner()
    }

    companion object {

        private const val DEFAULT_TIME_OUT: Long = 10000
        private const val BASE_URL: String = "http://www.wanandroid.com/"

        private var INSTANCE: RetrofitRequestManager? = null

        fun getInstance(): RetrofitRequestManager {
            return INSTANCE ?: RetrofitRequestManager().apply { INSTANCE = this }
        }
    }

}