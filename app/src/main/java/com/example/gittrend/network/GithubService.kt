package com.example.gittrend.network

import com.example.gittrend.model.trending.TrendingList
import retrofit2.Response
import retrofit2.http.GET


interface GithubService {


    @GET("repositories")
    suspend fun getTrendingList(): Response<TrendingList>
}