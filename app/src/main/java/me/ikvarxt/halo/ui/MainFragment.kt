package me.ikvarxt.halo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)

        binding.apply {
            viewPager.apply {
                isUserInputEnabled = false
                adapter = MainFragmentStateAdapter(activity)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        bottomNav.menu.getItem(position).isChecked = true
                    }
                })
            }
            bottomNav.apply {
                setOnItemSelectedListener {
                    fun performClickNavigationItem(index: Int) {
                        if (viewPager.currentItem != index) {
                            if (!viewPager.isFakeDragging) {
                                viewPager.currentItem = index
                            }
                        }
                    }

                    when (it.itemId) {
                        R.id.dashboard -> performClickNavigationItem(0)
                        R.id.postsList -> performClickNavigationItem(1)
                    }
                    true
                }
            }


        }
    }
}