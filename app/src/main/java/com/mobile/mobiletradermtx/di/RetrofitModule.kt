package com.mobile.mobiletradermtx.di

import androidx.viewbinding.BuildConfig
import com.google.gson.GsonBuilder
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.util.SupportInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideNetworkService(): RetrofitService {

        val supportInterceptor = SupportInterceptor()

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(supportInterceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        return Retrofit.Builder()
            .baseUrl("http://mobiletraderapi.com:9100")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClientBuilder.build())
            .build()
            .create(RetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiService(): RetrofitServices {

        val supportInterceptor = SupportInterceptor()

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(supportInterceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        return Retrofit.Builder()
            .baseUrl("http://mtnodejsapi.com:9000")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClientBuilder.build())
            .build()
            .create(RetrofitServices::class.java)
    }

}