package me.ikvarxt.halo.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import me.ikvarxt.halo.databinding.ActivityLoginBinding
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val sp: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            "test",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            val domain = binding.siteAddress.text.toString()
            val key = binding.apiAccessKey.text.toString()

            if (TextUtils.isEmpty(domain) || TextUtils.isEmpty(key)) {
                Toast.makeText(
                    this,
                    "complete domain and key text",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Constants.domain = domain
                Constants.key = key

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}