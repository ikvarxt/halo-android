package me.ikvarxt.halo.ui.posts.post

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentPostBinding
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.ui.MainActivity
import me.ikvarxt.halo.ui.posts.post.comment.PostCommentPanel
import me.ikvarxt.halo.ui.posts.post.edit.AddAssetFragment
import me.ikvarxt.halo.ui.posts.post.edit.PostEditingFragment
import me.ikvarxt.halo.ui.posts.post.preview.PostPreviewFragment
import me.ikvarxt.halo.ui.posts.post.publish.PostPublishSheetFragment
import javax.inject.Inject

private const val PAGE_VIEWING = 0
private const val PAGE_EDITING = 1

@AndroidEntryPoint
class PostFragment : Fragment() {

    @Inject
    lateinit var markwon: Markwon

    private val args by navArgs<PostFragmentArgs>()
    private val viewModel: PostViewModel by viewModels()

    private lateinit var binding: FragmentPostBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PostFragmentStateAdapter

    private val viewingBottomMenuItemClickListener = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.tag -> {}
            R.id.categories -> {}
        }
        true
    }

    private val editingBottomMenuItemClickListener = Toolbar.OnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.insertAsset -> {
                showInsertAssetsPanel()
            }
        }
        true
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val toolbar = (activity as MainActivity).toolbar

            toolbar.title = when (position) {
                PAGE_VIEWING -> {
                    viewModel.title
                }
                PAGE_EDITING -> {
                    resources.getString(R.string.editing)
                }
                else -> viewModel.title
            }
            setupBottomBarAndFab(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.setupArgs(args)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)

        viewPager = binding.viewPager
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PostFragmentStateAdapter(childFragmentManager, lifecycle)

        viewPager.adapter = adapter
        viewPager.setPageTransformer(MarginPageTransformer(20))

        if (viewModel.isWritingMode) {
            viewPager.setCurrentItem(PAGE_EDITING, false)
        }

        if (args.highlightId != NO_HIGHLIGHT_ID) {
            showCommentList(args.highlightId)
        }

        viewModel.msg.observe(viewLifecycleOwner) { showToast(it) }

        binding.fab.setOnClickListener {
            if (viewPager.currentItem == PAGE_VIEWING) {
                viewPager.currentItem = PAGE_EDITING
            } else {
                showPublishSheet()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showComment -> {
                showCommentList()
            }
            R.id.openInBrowser -> {
                // TODO: add draft post checking
                val customTabIntent = CustomTabsIntent.Builder().build()
                viewModel.post?.fullPath?.let { path ->
                    customTabIntent.launchUrl(requireContext(), Uri.parse(path))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCommentList(highlightId: Int? = null) {
        PostCommentPanel.newInstance(args.postId, highlightId)
            .show(childFragmentManager, PostCommentPanel.TAG)
    }

    private fun showPublishSheet() {
        PostPublishSheetFragment().show(childFragmentManager, PostPublishSheetFragment.TAG)
    }

    private fun setupBottomBarAndFab(position: Int) {
        val fab = binding.fab
        val bottomBar = binding.bottomBar

        // FIXME: very high rate of bottom bar menu inflate error

        when (position) {
            PAGE_VIEWING -> {
                fab.setImageResource(R.drawable.ic_baseline_edit_24)
                bottomBar.replaceMenu(viewingMenuRes)
                bottomBar.setOnMenuItemClickListener(viewingBottomMenuItemClickListener)
            }
            PAGE_EDITING -> {
                fab.setImageResource(R.drawable.ic_baseline_send_24)
                bottomBar.replaceMenu(editingMenuRes)
                bottomBar.setOnMenuItemClickListener(editingBottomMenuItemClickListener)
            }
        }
    }

    private fun showInsertAssetsPanel() {
        // this fm used by viewPager Adapter
        val viewPagerFm = childFragmentManager

        /**
         * see:
         * {@link FragmentStateAdapter#placeFragmentInViewHolder(androidx.viewpager2.adapter.FragmentViewHolder)}
         */
        val tag = "f$PAGE_EDITING"
        val editingFragment = viewPagerFm.findFragmentByTag(tag) as? PostEditingFragment
        // for setFragmentResult() to works fine between fragment
        editingFragment?.parentFragmentManager?.let {
            AddAssetFragment().show(it, AddAssetFragment.TAG)
        }
    }

    companion object {
        const val NO_HIGHLIGHT_ID = -1

        private const val viewingMenuRes = R.menu.post_viewing_bottom
        private const val editingMenuRes = R.menu.post_editing_bottom
    }
}

class PostFragmentStateAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment = when (position) {
        PAGE_VIEWING -> PostPreviewFragment()
        PAGE_EDITING -> PostEditingFragment()
        else -> PostPreviewFragment()
    }

    override fun getItemCount(): Int = 2
}