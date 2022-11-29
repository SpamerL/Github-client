package com.kryak.githubclient.ui.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kryak.data.Repository
import com.kryak.model.DownloadModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _downloads: MutableStateFlow<List<DownloadModel>> = MutableStateFlow(listOf())
    val downloads: StateFlow<List<DownloadModel>> get() = _downloads

    init {
        getAllDownloads()
    }

    private fun getAllDownloads() {
        repository.getDownloadList()
            .onEach { _downloads.value = it }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}
