package me.ikvarxt.halo.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.FragmentThemeBinding
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.ui.theme.list.ThemesListFragment
import me.ikvarxt.halo.ui.theme.setting.ThemeSettingFragment

private const val PAGE_THEMES_LIST = 0
private const val PAGE_THEME_SETTING = 1
private const val PAGE_MENUS = 2

@AndroidEntryPoint
class ThemeFragment : Fragment(), ThemeListAdapter.Listener {

    private lateinit var binding: FragmentThemeBinding
    private lateinit var adapter: ThemeFragmentStateAdapter
    private val viewModel by viewModels<ThemeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentThemeBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ThemeFragmentStateAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                PAGE_THEMES_LIST -> "Themes"
                PAGE_THEME_SETTING -> "Theme Setting"
                else -> "Themes"
            }
        }.attach()
    }

    override fun selectActivate(theme: HaloTheme) {
        viewModel.activateTheme(theme)
    }
}

class ThemeFragmentStateAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment = when (position) {
        PAGE_THEMES_LIST -> ThemesListFragment()
        PAGE_THEME_SETTING -> ThemeSettingFragment()
        else -> ThemesListFragment()
    }

    override fun getItemCount(): Int = 2
}
