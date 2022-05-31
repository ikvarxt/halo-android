package me.ikvarxt.halo.ui.posts.category

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.DialogEditingCategoryBinding
import me.ikvarxt.halo.databinding.FragmentCategoriesBinding
import me.ikvarxt.halo.entites.PostCategory
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CategoriesListAdapter.Listener {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var adapter: CategoriesListAdapter
    private val viewModel by viewModels<CategoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCategoriesBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CategoriesListAdapter(this).also {
            binding.recyclerView.adapter = it
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.categories.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun editCategory(category: PostCategory) {
        val context = requireContext()
        val binding = DialogEditingCategoryBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        binding.category = category
        val confirmAction = DialogInterface.OnClickListener { dialog, which ->
            val name = binding.name.text.toString().trim()
            val slug = binding.slug.text.toString().trim()
            viewModel.updateCategory(category, name, slug)
        }
        val deleteAction = DialogInterface.OnClickListener { _, _ ->
            viewModel.delete(category)
        }
        MaterialAlertDialogBuilder(context)
            .setTitle("Editing Category")
            .setView(binding.root)
            .setPositiveButton(R.string.save, confirmAction)
            .setNegativeButton(R.string.cancel, null)
            .setNeutralButton(R.string.delete, deleteAction)
            .show()
    }
}