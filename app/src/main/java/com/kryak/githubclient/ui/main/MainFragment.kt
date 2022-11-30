package com.kryak.githubclient.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kryak.githubclient.R
import com.kryak.githubclient.databinding.FragmentMainBinding
import com.kryak.model.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.mobile_navigation)
    private lateinit var adapter: UserAdapter
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Github client"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )
        adapter = UserAdapter { userModel ->
            onUserListItemClickListener(userModel)
        }
        setupUI(binding, adapter)
        subscribeUI(viewModel)
        return binding.root
    }

    private fun setupUI(binding: FragmentMainBinding, adapter: UserAdapter) {
        binding.userRv.adapter = adapter
        // user search setup
        binding.userSearchContainer.setEndIconOnClickListener {
            if (!binding.userSearchContainer.editText?.text.isNullOrEmpty()) {
                viewModel.searchUser(binding.userSearchContainer.editText?.text.toString())
            } else {
                binding.userSearchContainer.error = "empty line"
            }
        }
    }

    private fun subscribeUI(viewModel: MainViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.userListState.collect {
                    when (it) {
                        UserUiState.Error -> {}
                        UserUiState.Loading -> {}
                        is UserUiState.Success -> {
                            adapter.submitList(it.users)
                        }
                    }
                }
            }
        }
    }

    fun onUserListItemClickListener(userModel: UserModel) {
        val direction = MainFragmentDirections.actionMainFragmentToReposFragment(userModel.name)
        findNavController().navigate(direction)
    }
}
