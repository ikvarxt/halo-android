package me.ikvarxt.halo.ui.login

import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.arch.BaseActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit { add<LoginFragment>(android.R.id.content) }
    }
}