package com.kryak.githubclient.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kryak.data.RepositoryImpl
import com.kryak.githubclient.util.Result
import com.kryak.githubclient.util.asResult
import com.kryak.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {
    private val _userUiState: MutableStateFlow<UserUiState> = MutableStateFlow(UserUiState.Loading)
    val userListState: StateFlow<UserUiState> get() = _userUiState

    fun searchUser(searchString: String) {
        usersUiStream(searchString, repository)
            .onEach { _userUiState.value = it }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}

private fun usersUiStream(
    searchString: String,
    repository: RepositoryImpl
): Flow<UserUiState> {
    val usersStream: Flow<List<UserModel>> =
        repository.searchUsers(searchString)
            .flowOn(Dispatchers.IO)

    return usersStream
        .asResult()
        .map { toUserResult ->
            when (toUserResult) {
                is Result.Error -> {
                    UserUiState.Error
                }
                is Result.Loading -> {
                    UserUiState.Loading
                }
                is Result.Success -> {
                    UserUiState.Success(toUserResult.data)
                }
            }
        }
}

sealed interface UserUiState {
    data class Success(val users: List<UserModel>) : UserUiState
    object Error : UserUiState
    object Loading : UserUiState
}
