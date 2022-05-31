package me.ikvarxt.halo.ui.custompages.journal

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.DialogEditJournalBinding
import me.ikvarxt.halo.databinding.FragmentJournalsBinding
import me.ikvarxt.halo.entites.HaloJournal
import me.ikvarxt.halo.entites.HaloJournalType
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import javax.inject.Inject

@AndroidEntryPoint
class JournalsFragment : Fragment(), JournalsListAdapter.Listener {

    @Inject
    lateinit var markwon: Markwon

    private lateinit var binding: FragmentJournalsBinding
    private lateinit var adapter: JournalsListAdapter
    private val viewModel by viewModels<JournalsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentJournalsBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = JournalsListAdapter(this, markwon).also {
            binding.recyclerView.adapter = it
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.journals.collectLatest {
                    adapter.submitList(it)
                }
            }
        }

    }

    override fun editJournal(journal: HaloJournal) {
        val context = requireContext()
        val binding = DialogEditJournalBinding.inflate(LayoutInflater.from(context), null, false)
        binding.journal = journal

        val positiveAction = DialogInterface.OnClickListener { _, _ ->
            val content = binding.content.text.toString().trim()
            val type = if (binding.journalTypeSwitch.isChecked) {
                HaloJournalType.INTIMATE
            } else HaloJournalType.PUBLIC
            viewModel.updateJournal(journal, content, type)
        }
        val deleteAction = DialogInterface.OnClickListener { _, _ ->
            viewModel.deleteJournal(journal)
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Edit Journal")
            .setView(binding.root)
            .setPositiveButton(R.string.save, positiveAction)
            .setNegativeButton(R.string.cancel, null)
            .setNeutralButton(R.string.delete, deleteAction)
            .show()
    }
}