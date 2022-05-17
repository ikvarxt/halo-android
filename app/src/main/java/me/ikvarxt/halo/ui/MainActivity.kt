package me.ikvarxt.halo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.MainDirections
import me.ikvarxt.halo.R
import me.ikvarxt.halo.arch.BaseActivity
import me.ikvarxt.halo.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel>()

    val toolbar: Toolbar
        get() = binding.toolbar

    private var isRootFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        // TODO: current workaround for trigger ViewModel instantiation
        viewModel.info

        navController.addOnDestinationChangedListener { navController, destination, _ ->
            isRootFragment = when (destination.id) {
                R.id.postsListFragment,
                R.id.commentsFragment,
                R.id.assetsFragment,
                R.id.dashboardFragment -> true
                else -> false
            }

            requestNavigationHidden(!isRootFragment)

            binding.bottomNav.menu.forEach { item ->
                if (item.itemId == destination.id) {
                    item.isChecked = true
                }
            }

            binding.toolbar.title = destination.label
            supportActionBar?.setDisplayHomeAsUpEnabled(!isRootFragment)
            binding.toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }

        supportActionBar?.title = navController.currentDestination?.label

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            getPage(menuItem.itemId)?.let { navController.navigate(it) }
            true
        }
        binding.bottomNav.setOnItemReselectedListener { /* do nothing */ }
    }

    private fun getPage(destinationId: Int): NavDirections? = when (destinationId) {
        R.id.postsListFragment -> MainDirections.actionPostsListFragment()
        R.id.commentsFragment -> MainDirections.actionCommentsFragment()
        R.id.assetsFragment -> MainDirections.actionAssetsFragment()
        R.id.dashboardFragment -> MainDirections.actionDashboardFragment()
        else -> null
    }

    private fun requestNavigationHidden(hide: Boolean = true, requiresAnimation: Boolean = true) {
        val bottomView = binding.bottomNav
        if (requiresAnimation) {
            bottomView.isVisible = true
            bottomView.isHidden = hide
        } else {
            bottomView.isGone = hide
        }
    }
}