package com.kryak.githubclient.ui.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kryak.githubclient.databinding.FragmentDownloadsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadsFragment : Fragment() {

    private val viewModel: DownloadsViewModel by viewModels()
    private lateinit var binding: FragmentDownloadsBinding
    private lateinit var adapter: DownloadAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadsBinding.inflate(
            inflater,
            container,
            false
        )
        adapter = DownloadAdapter()
        setupUI(adapter)
        subscribeUI(viewModel)
        return binding.root
    }

    private fun subscribeUI(viewModel: DownloadsViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.downloads.collect {
                    if (it.isNotEmpty()) {
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun setupUI(adapter: DownloadAdapter) {
        binding.downloadRv.adapter = adapter
    }
}
