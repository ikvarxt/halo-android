package me.ikvarxt.halo.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.ikvarxt.halo.ui.dashboard.DashboardFragment
import me.ikvarxt.halo.ui.posts.PostsListFragment

class MainFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardFragment()
            1 -> PostsListFragment()
            else -> DashboardFragment()
        }
    }
}