package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.base.HomeBannerBean
import com.sqsong.wanandroid.util.Constants
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
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        mApiService = retrofit.create(ApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().let {
            it.connectTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .readTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(interceptor)
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build()
        }
    }

    fun getHomeBanner(): Observable<HomeBannerBean> {
        return mApiService.getHomeBanner()
    }

    companion object {

        private var INSTANCE: RetrofitRequestManager? = null

        fun getInstance(): RetrofitRequestManager {
            return INSTANCE ?: RetrofitRequestManager().apply { INSTANCE = this }
        }
    }

}