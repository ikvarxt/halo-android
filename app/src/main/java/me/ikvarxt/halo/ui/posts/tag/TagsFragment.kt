package me.ikvarxt.halo.ui.posts.tag

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.DialogEditingTagBinding
import me.ikvarxt.halo.databinding.FragmentTagsBinding
import me.ikvarxt.halo.entites.PostTag
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class TagsFragment : Fragment(), TagsAdapter.Listener {

    private lateinit var binding: FragmentTagsBinding
    private lateinit var adapter: TagsAdapter
    private val viewModel by viewModels<TagsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTagsBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = TagsAdapter(this).also {
            adapter = it
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.tags.collect {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun showTag(tag: PostTag) {
        val context = requireContext()
        val binding = DialogEditingTagBinding.inflate(LayoutInflater.from(context), null, false)
        binding.tag = tag

        val click = DialogInterface.OnClickListener { _, _ ->
            val newTag = tag.copy(
                name = binding.tagName.text.toString(),
                slug = binding.tagSlug.text.toString()
            )
            viewModel.updateTag(newTag)
        }

        MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setTitle(R.string.edit_tag)
            .setPositiveButton(R.string.save, click)
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}