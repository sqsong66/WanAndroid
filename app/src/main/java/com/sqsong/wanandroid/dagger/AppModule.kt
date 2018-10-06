package com.sqsong.wanandroid.dagger

import android.content.Context
import com.sqsong.wanandroid.base.BaseApplication
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.util.Constants
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@dagger.Module
class AppModule {

    @Provides
    fun provideContext(application: BaseApplication): Context {
        return application.applicationContext
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Singleton
    @Provides
    fun provideActivityLifecycleCallbacks(): ActivityLifecycleCallbacksImpl {
        return ActivityLifecycleCallbacksImpl()
    }

    @Singleton
    @Provides
    fun provideAipService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().let {
            it.client(client)
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
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

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

}