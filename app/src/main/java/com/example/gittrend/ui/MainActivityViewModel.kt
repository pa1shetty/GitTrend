package com.example.gittrend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gittrend.model.trending.TrendingListItem
import com.example.gittrend.repository.GithubRepository
import com.example.gittrend.util.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
) : ViewModel() {
    init {
        getTrendingList()
    }


    private var _trendingReposList: ArrayList<TrendingListItem> = ArrayList()
    private val _trendingReposListTemp = MutableStateFlow(ArrayList<TrendingListItem>())
    var trendingReposListTemp: StateFlow<ArrayList<TrendingListItem>> = _trendingReposListTemp
    private val _error = MutableSharedFlow<String>(replay = 0)
    val error: SharedFlow<String> = _error
    var isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var searchString = ""

    fun getTrendingList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.emit(true)
                _trendingReposList = githubRepository.getTrendingList()
                search(searchString)
            } catch (e: NoInternetException) {
                _error.emit("No Internet Connection!")
            } catch (e: Exception) {
                _error.emit("Something Went Wrong!")
            } finally {
                isLoading.emit(false)
            }
        }
    }

    fun search(text: String = "") {
        searchString = text
        viewModelScope.launch(Dispatchers.Default) {
            _trendingReposListTemp.value =
                _trendingReposList.filter { trendingRepo ->
                    trendingRepo.repositoryName.contains(searchString)
                } as ArrayList<TrendingListItem>

        }
    }

    fun setChecked(rank: Int?, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            _trendingReposList.find { it.rank == rank }?.isChecked = isChecked
            _trendingReposListTemp.value.find { it.rank == rank }?.isChecked = isChecked
        }
    }

}