package me.ikvarxt.halo.ui.posts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentPostsListBinding
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.ui.posts.post.PostFragment

private const val TAG = "PostsListsFragment"

@AndroidEntryPoint
class PostsListFragment : Fragment(), PostsListPagingAdapter.Listener {

    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostsListPagingAdapter
    private val viewModel by viewModels<PostsListViewModel>()

    private val operationInProgressLoading by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.view_content_loading)
            .create().apply {
                setCanceledOnTouchOutside(false)
            }
    }

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

        setHasOptionsMenu(true)

        adapter = PostsListPagingAdapter(this)
        binding.recyclerView.adapter = adapter

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.pagingPostsData.collectLatest {
                    adapter.submitData(it)
                }
            }

            launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    binding.swipeRefreshLayout.isRefreshing =
                        loadStates.mediator?.refresh is LoadState.Loading
                }
            }

            launch {
                viewModel.operationInProgress.collectLatest {
                    if (it) operationInProgressLoading.show()
                    else operationInProgressLoading.dismiss()
                }
            }

            launch {
                viewModel.refreshAdapter.collectLatest { adapter.refresh() }
            }
        }

        viewModel.msg.observe(viewLifecycleOwner) { showToast(it) }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        binding.addFab.setOnClickListener {
            val action = PostsListFragmentDirections.gotoPostsDetailsAction(
                0,
                PostFragment.NO_HIGHLIGHT_ID,
                true
            )
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_list_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.checkTags -> {
                findNavController().navigate(R.id.gotoTagsFragment)
            }
            R.id.checkCategories -> {
                findNavController().navigate(R.id.gotoCategoriesFragment)
            }
            R.id.checkRecyclePost -> {
                // TODO: complate this
            }
        }
        return true
    }

    override fun deletePostPermanently(item: PostItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you want to permanently delete this post?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deletePostPermanently(item.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}