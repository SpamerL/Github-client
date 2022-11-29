package com.kryak.network.model.repos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserReposList(
    val list: Map<Int, UserReposListItem>
)
