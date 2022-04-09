package me.ikvarxt.halo.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.databinding.ActivityLoginBinding
import me.ikvarxt.halo.network.AdminApiService
import me.ikvarxt.halo.ui.MainActivity
import okhttp3.HttpUrl
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var accountManager: AccountManager

    @Inject
    lateinit var apiService: AdminApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (accountManager.checkValidation()) {
            dropIntoMainActivity()
            return
        }

        binding.siteAddress.apply {
            addTextChangedListener {
                val input = it.toString()
                if (input.isNotBlank()) {
                    try {
                        HttpUrl.parse(input)!!.host()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        error = "Please input correct https address"
                    }
                    if (input.startsWith("http://")) {
                        error = "Please input https:// schema"
                    }
                } else {
                    error = "Site Address can not be empty"
                }
            }
        }

        binding.login.setOnClickListener {
            val domain = binding.siteAddress.text.toString()
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (domain.isBlank() && username.isBlank() && password.isBlank()) {
                Toast.makeText(
                    this,
                    "Something wrong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                accountManager.saveDomain(domain)

                accountManager.login(apiService, username, password).observe(this) {
                    if (it) {
                        dropIntoMainActivity()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun dropIntoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}