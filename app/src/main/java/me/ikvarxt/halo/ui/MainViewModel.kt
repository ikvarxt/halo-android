package me.ikvarxt.halo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.repository.UserInfoRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val infoRepository: UserInfoRepository
) : ViewModel() {

    lateinit var info: UserProfile
        private set

    init {
        viewModelScope.launch {
            when (val result = infoRepository.getUserProfile()) {
                is NetworkResult.Success -> info = result.data
                is NetworkResult.Failure -> {}
            }
        }
    }
}