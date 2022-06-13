package com.example.gittrend.model.trending

data class TrendingListItem(
    val builtBy: List<BuiltBy?>?,
    val description: String?,
    val forks: Int?,
    val language: String?,
    val languageColor: String?,
    val rank: Int?,
    val repositoryName: String,
    val since: String?,
    val starsSince: Int?,
    val totalStars: Int?,
    val url: String?,
    val username: String?,
    var isChecked: Boolean = true,


    )