package com.kryak.data

import com.kryak.model.DownloadModel
import com.kryak.model.RepoModel
import com.kryak.model.UserModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun searchUsers(q: String): Flow<List<UserModel>>

    fun getUserRepos(userName: String): Flow<List<RepoModel>>

    fun getDownloadList(): Flow<List<DownloadModel>>

    suspend fun insertDownloadRecord(repoModel: RepoModel)
}
