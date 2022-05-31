package me.ikvarxt.halo.ui.custompages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.DialogEditJournalBinding
import me.ikvarxt.halo.databinding.FragmentCustomPagesBinding
import me.ikvarxt.halo.entites.HaloJournalType
import me.ikvarxt.halo.ui.MainActivity
import me.ikvarxt.halo.ui.custompages.journal.JournalsFragment

private const val PAGE_JOURNAL = 0
private const val PAGE_LINKS = 1
private const val PAGE_PHOTOS = 2

@AndroidEntryPoint
class CustomPagesFragment : Fragment() {

    private lateinit var binding: FragmentCustomPagesBinding
    private val viewModel by viewModels<CustomPagesViewModel>()
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCustomPagesBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.viewPager
        viewPager.adapter = CustomPagesFragmentAdapter(childFragmentManager, lifecycle)
        viewPager.offscreenPageLimit = 1

        setupTabLayout()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    PAGE_JOURNAL -> {

                    }
                    PAGE_LINKS -> {

                    }
                    PAGE_PHOTOS -> {

                    }
                    else -> {}
                }
            }
        })

        binding.fab.setOnClickListener {
            when (binding.viewPager.currentItem) {
                PAGE_JOURNAL -> createJournal()
                PAGE_LINKS -> createLinks()
                PAGE_PHOTOS -> uploadPhotos()
                else -> {}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        tabLayout.isVisible = true
    }

    private fun createJournal() {
        val context = requireContext()
        val binding = DialogEditJournalBinding.inflate(LayoutInflater.from(context), null, false)
        binding.content.requestFocus()

        MaterialAlertDialogBuilder(context)
            .setTitle("New Journal")
            .setView(binding.root)
            .setPositiveButton(R.string.publish) { _, _ ->
                val content = binding.content.text.toString().trim()
                val type = if (binding.journalTypeSwitch.isChecked) {
                    HaloJournalType.INTIMATE
                } else HaloJournalType.PUBLIC
                viewModel.createJournal(content, type)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun createLinks() {

    }

    private fun uploadPhotos() {

    }

    private fun setupTabLayout() {
        val appbar = (activity as? MainActivity)?.appbar ?: return
        tabLayout = TabLayout(requireContext()).apply {
            tabGravity = TabLayout.GRAVITY_FILL
        }
        val layoutParams = AppBarLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        appbar.addView(tabLayout, layoutParams)
        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                PAGE_JOURNAL -> "Journals"
                PAGE_LINKS -> "Friends"
                PAGE_PHOTOS -> "Photos"
                else -> "Journals"
            }
        }.attach()
    }

    override fun onStop() {
        super.onStop()
        tabLayout.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()

        val appbar = (activity as? MainActivity)?.appbar ?: return
        appbar.removeView(tabLayout)
    }
}

class CustomPagesFragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        PAGE_JOURNAL -> JournalsFragment()

        else -> JournalsFragment()
    }

}