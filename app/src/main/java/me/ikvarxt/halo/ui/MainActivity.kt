package me.ikvarxt.halo.ui

import android.os.Bundle
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.ActivityMainBinding
import me.ikvarxt.halo.entites.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        supportFragmentManager.commit { add<MainMainFragment>(android.R.id.content) }

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)


        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}