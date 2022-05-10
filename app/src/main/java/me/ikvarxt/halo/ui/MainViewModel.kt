package me.ikvarxt.halo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.repository.UserInfoRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val infoRepository: UserInfoRepository
) : ViewModel() {

    private val _infoLiveData = MutableLiveData<UserProfile>()
    val info: LiveData<UserProfile> = _infoLiveData

    init {
        viewModelScope.launch {
            infoRepository.getUserProfile()?.let { _infoLiveData.value = it }
        }
    }
}