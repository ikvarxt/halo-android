package me.ikvarxt.halo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.ActivityMainBinding
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.network.infra.Resource
import me.ikvarxt.halo.network.infra.Status

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostsListAdapter
    private val viewModel by viewModels<PostsListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNonNull()

        adapter = PostsListAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.postsList.observe(this) {
            if (it.status == Status.SUCCESS) {
                adapter.submitList(it?.data)
                binding.recyclerView.visibility = View.VISIBLE
                binding.loading.visibility= View.GONE
            } else {
                binding.loading.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                if (it.status == Status.ERROR) {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkNonNull(): Boolean {
        return if (TextUtils.isEmpty(Constants.domain) && TextUtils.isEmpty(Constants.key)) {
            Toast.makeText(this, "Constants is null", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}