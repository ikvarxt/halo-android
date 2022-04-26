package me.ikvarxt.halo.ui

import android.os.Bundle
import androidx.core.view.forEach
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        supportFragmentManager.commit { add<MainMainFragment>(android.R.id.content) }

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)


        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}