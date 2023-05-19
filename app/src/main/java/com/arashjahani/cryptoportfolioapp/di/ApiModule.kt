package com.arashjahani.cryptoportfolioapp.di

import android.content.Context
import androidx.viewbinding.BuildConfig
import com.arashjahani.cryptoportfolioapp.data.network.CoinStatsApiService
import com.arashjahani.cryptoportfolioapp.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(context: Context): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()

    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CoinStatsApiService {
        return retrofit.create(CoinStatsApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideApiRetrofit(okHttpClient: OkHttpClient,gsonBuilder:GsonBuilder):Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build()
    }


    @Provides
    @Singleton
    fun provideGsonBuilder():GsonBuilder{
        val gsonBuilder=GsonBuilder()
        return gsonBuilder
    }
}