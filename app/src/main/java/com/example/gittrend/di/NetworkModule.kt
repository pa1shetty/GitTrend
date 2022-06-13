package com.example.gittrend.di

import android.content.Context
import com.example.gittrend.network.GithubService
import com.example.gittrend.network.NetworkConnectionInterceptor
import com.example.gittrend.network.NetworkConstants.CONNECTION_TIME_OUT
import com.example.gittrend.network.NetworkConstants.READ_TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext app: Context,
    ) = NetworkConnectionInterceptor(app)

    @Singleton
    @Provides
    fun provideOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor) =
        OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://gh-trending-api.herokuapp.com/")
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)


}