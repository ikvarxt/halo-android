package me.ikvarxt.halo.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentPostsListBinding

private const val TAG = "PostsListsFragment"

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostsListPagingAdapter
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

//        adapter = PostsListAdapter()
        adapter = PostsListPagingAdapter()
        binding.recyclerView.adapter = adapter

//        viewModel.postsList.observe(viewLifecycleOwner) {
//            binding.loading.visibility = if (it.status == Status.LOADING) {
//                // 通过网络重新获取数据的时候会预先返回加载状态的数据库返回数据，我们可以使用这个数据预填充
//                if (it.data != null) {
//                    adapter.submitList(it.data)
//                }
//                View.VISIBLE
//                return@observe
//            } else View.GONE
//
//            binding.swipeRefreshLayout.isRefreshing = false
//
//            when (it.status) {
//                Status.SUCCESS -> {
//                    adapter.submitList(it?.data)
//                }
//                Status.ERROR -> {
//                    binding.errorText.text = it.message
//                    Log.e(TAG, "onViewCreated: ${it.message}")
//                }
//                else -> {}
//            }
//        }
        lifecycleScope.launch {
            viewModel.pagingPostsData.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefreshLayout.isRefreshing =
                    loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.addPostFragment)
        }
    }
}