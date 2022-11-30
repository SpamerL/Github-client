package com.kryak.githubclient.ui.repoList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kryak.githubclient.databinding.FragmentReposBinding
import com.kryak.githubclient.util.CustomClickListener
import com.kryak.model.RepoModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReposFragment : Fragment(), CustomClickListener {

    private lateinit var binding: FragmentReposBinding
    private val viewModel: ReposViewModel by viewModels()

    // private val viewModel: ReposViewModel by hiltNavGraphViewModels(R.id.mobile_navigation)
    private lateinit var adapter: RepoAdapter
    private lateinit var clickListener: CustomClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity.title = viewModel.repositoryOwner
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReposBinding.inflate(
            inflater,
            container,
            false
        )
        clickListener = this
        adapter = RepoAdapter(clickListener)
        setupUI(binding, adapter)
        activity?.title = viewModel.repositoryOwner
        subscribeUI(viewModel)
        return binding.root
    }

    private fun setupUI(binding: FragmentReposBinding, adapter: RepoAdapter) {
        binding.reposRv.adapter = adapter
    }

    private fun subscribeUI(viewModel: ReposViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.reposUIState.collect {
                    when (it) {
                        ReposUIState.Error -> {
                            requireActivity().title = viewModel.repositoryOwner
                        }
                        ReposUIState.Loading -> {
                            requireActivity().title = viewModel.repositoryOwner
                        }
                        is ReposUIState.Success -> {
                            adapter.submitList(it.repos)
                            requireActivity().title = viewModel.repositoryOwner
                        }
                    }
                }
            }
        }
    }

    override fun onItemClickListener(view: View, model: Any) {
        viewModel.loadRepository(model as RepoModel)
    }
}
