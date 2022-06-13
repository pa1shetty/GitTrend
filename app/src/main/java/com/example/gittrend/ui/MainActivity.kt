package com.example.gittrend.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gittrend.R
import com.example.gittrend.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: TrendingRepoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        observer()
    }

    private fun observer() {
        binding.srlRepo.setOnRefreshListener {
            viewModel.getTrendingList()
        }
        lifecycleScope.launch(Dispatchers.Main) {
            launch(Dispatchers.Main) {
                viewModel.trendingReposListTemp.collect {
                    if (it.isEmpty()) {
                        binding.rvRepo.visibility = View.INVISIBLE
                        binding.tvMessage.visibility = View.VISIBLE
                        if (viewModel.searchString.isEmpty()) {
                            binding.svRepo.visibility = View.GONE
                        }
                    } else {
                        binding.rvRepo.visibility = View.VISIBLE
                        binding.tvMessage.visibility = View.INVISIBLE
                        binding.svRepo.visibility = View.VISIBLE
                        adapter.submitList(it)
                    }
                }
            }
            launch(Dispatchers.Main) {
                viewModel.isLoading.collect {
                    binding.srlRepo.isRefreshing = it
                }
            }
            launch(Dispatchers.Main) {
                viewModel.error.collect {
                    Snackbar
                        .make(binding.root, it, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.retry)) {
                            viewModel.getTrendingList()
                        }.show()
                }
            }
        }

        binding.svRepo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return false
            }

        })
    }

    private fun initRecyclerView() {
        binding.rvRepo.layoutManager = LinearLayoutManager(this)
        binding.rvRepo.adapter = adapter
        adapter.onItemClick = { repoItem ->
            viewModel.setChecked(repoItem.rank, !repoItem.isChecked)
        }
    }
}