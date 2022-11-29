package com.kryak.network.api

import com.kryak.network.model.repos.UserReposList
import com.kryak.network.model.repos.UserReposListItem
import com.kryak.network.model.userSearch.UserSearchNetwork
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApi {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") q: String,
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): UserSearchNetwork

    @Headers("Content-Type: application/json")
    @GET("users/{username}/repos")
    suspend fun getUserRepos(@Path("username") username: String = "google"): List<UserReposListItem>
}
