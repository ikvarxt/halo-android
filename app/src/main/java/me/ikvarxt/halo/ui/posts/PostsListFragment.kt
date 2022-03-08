package me.ikvarxt.halo.ui.posts

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.FragmentPostsListBinding
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.network.infra.Status

private const val TAG = "PostsListsFragment"

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostsListAdapter
    private val viewModel by viewModels<PostsListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPostsListBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNonNull()

        adapter = PostsListAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.postsList.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it.status == Status.LOADING) {
                View.VISIBLE
            } else View.GONE

            when (it.status) {
                Status.SUCCESS -> {
                    adapter.submitList(it?.data)
                }
                Status.ERROR -> {
                    binding.errorText.text = it.message
                    Log.e(TAG, "onViewCreated: ${it.message}")
                }
                else -> {}
            }
        }
    }

    private fun checkNonNull(): Boolean {
        return if (TextUtils.isEmpty(Constants.domain) && TextUtils.isEmpty(Constants.accessKey)) {
            Toast.makeText(context, "Constants is null", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

}