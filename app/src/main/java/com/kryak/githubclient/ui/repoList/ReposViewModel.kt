package com.kryak.githubclient.ui.repoList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kryak.data.Repository
import com.kryak.data.di.IoDispatcher
import com.kryak.githubclient.util.Result
import com.kryak.githubclient.util.asResult
import com.kryak.model.RepoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _reposUIState: MutableStateFlow<ReposUIState> = MutableStateFlow(ReposUIState.Loading)
    val reposUIState: StateFlow<ReposUIState> get() = _reposUIState

    lateinit var repositoryOwner: String

    init {
        val userName = savedStateHandle.get<String>("userName")
        repositoryOwner = userName!!
        getUserRepositories(userName)
    }

    private fun getUserRepositories(userName: String) {
        getUserRepositoriesStream(userName, repository, ioDispatcher)
            .onEach { _reposUIState.value = it }
            .flowOn(ioDispatcher)
            .launchIn(viewModelScope)
    }

    fun loadRepository(repo: RepoModel) {
        viewModelScope.launch {
            repository.insertDownloadRecord(repo)
        }
    }
}

private fun getUserRepositoriesStream(userName: String, repository: Repository, ioDispatcher: CoroutineDispatcher): Flow<ReposUIState> {
    val repositoriesStream: Flow<List<RepoModel>> =
        repository.getUserRepos(userName)
            .flowOn(ioDispatcher)

    return repositoriesStream
        .asResult()
        .map { toResult ->
            when (toResult) {
                is Result.Error -> {
                    ReposUIState.Error
                }
                is Result.Loading -> {
                    ReposUIState.Loading
                }
                is Result.Success -> {
                    ReposUIState.Success(toResult.data)
                }
            }
        }
}

sealed interface ReposUIState {
    data class Success(val repos: List<RepoModel>) : ReposUIState
    object Error : ReposUIState
    object Loading : ReposUIState
}
