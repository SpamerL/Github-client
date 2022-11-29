package com.kryak.data

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.kryak.data.converters.asExternalModel
import com.kryak.data.di.IoDispatcher
import com.kryak.database.DownloadDAO
import com.kryak.database.model.Download
import com.kryak.model.DownloadModel
import com.kryak.model.RepoModel
import com.kryak.model.UserModel
import com.kryak.network.api.GitApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: GitApi,
    private val downloadDAO: DownloadDAO,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : Repository {
    override fun searchUsers(q: String): Flow<List<UserModel>> = flow { emit(api.searchUsers(q)) }
        .map {
            it.users.map { user ->
                user.asExternalModel()
            }
        }
        .flowOn(ioDispatcher)

    override fun getUserRepos(userName: String): Flow<List<RepoModel>> = flow { emit(api.getUserRepos(userName)) }
        .map { repoList ->
            repoList.map { repoItem ->
                repoItem.asExternalModel()
            }
        }
        .flowOn(ioDispatcher)

    override fun getDownloadList(): Flow<List<DownloadModel>> {
        return downloadDAO.getAll()
            .map { List ->
                List.map {
                    it.asExternalModel()
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun insertDownloadRecord(repoModel: RepoModel) {
        val url: String =
            "https://api.github.com/repos/${repoModel.owner}/${repoModel.name}/zipball/master"
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription(repoModel.name)
        request.setTitle(repoModel.name)
        request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${repoModel.name}.zip")
        manager.enqueue(request)

        withContext(ioDispatcher) {
            launch {
                downloadDAO.insertDownloadRecord(
                    Download(
                        id = 0,
                        repositoryName = repoModel.name,
                        owner = repoModel.owner
                    )
                )
            }
        }
    }
}
