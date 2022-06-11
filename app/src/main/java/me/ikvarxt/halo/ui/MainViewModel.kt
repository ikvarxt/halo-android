package me.ikvarxt.halo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.ikvarxt.halo.constant.HaloOptions
import me.ikvarxt.halo.entites.UserProfile
import me.ikvarxt.halo.repository.HaloSettingsRepository
import me.ikvarxt.halo.repository.UserInfoRepository
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val infoRepository: UserInfoRepository,
    private val settingsRepository: HaloSettingsRepository
) : ViewModel() {

    private val _infoLiveData = MutableLiveData<UserProfile>()
    val infoLiveData: LiveData<UserProfile> = _infoLiveData

    val profile = infoRepository.userProfile

    val blogUrl = settingsRepository.options.map { options ->
        options.firstOrNull { it.key == HaloOptions.BLOG_URL }
    }

    val info: UserProfile?
        get() = _infoLiveData.value

    init {
        viewModelScope.launch {
            infoRepository.getUserProfile()?.let {
                _infoLiveData.value = it
            }
            Log.i(TAG, "profile get: $info")
            settingsRepository.listAllOptions()
        }
    }

    fun updateDescription(new: String) {
        val profile = info ?: return

        viewModelScope.launch {
            infoRepository.updateProfile(
                username = profile.username,
                nickname = profile.nickname,
                email = profile.email,
                avatar = profile.avatarUrl,
                description = new,
            )
        }

    }
}