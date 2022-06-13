package com.example.gittrend.repository

import com.example.gittrend.model.trending.TrendingListItem
import com.example.gittrend.network.GithubService
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubService: GithubService,
) {

    suspend fun getTrendingList(): ArrayList<TrendingListItem> {
        githubService.getTrendingList().let { userResponse ->
            if (userResponse.isSuccessful && userResponse.body() != null) {
                return userResponse.body()!!
            } else {
                throw Exception()
            }
        }


    }

}