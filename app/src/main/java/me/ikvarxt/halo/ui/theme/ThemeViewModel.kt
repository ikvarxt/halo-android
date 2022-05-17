package me.ikvarxt.halo.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.repository.ThemeRepository
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repository: ThemeRepository
) : ViewModel() {

    private val _themes = MutableLiveData<NetworkResult<List<HaloTheme>>>()
    val themes: LiveData<NetworkResult<List<HaloTheme>>> = _themes

    init {
        viewModelScope.launch {
            _themes.value = repository.getAllThemes()
        }
    }

    fun activateTheme(theme: HaloTheme) {
        viewModelScope.launch {
            repository.activateTheme(theme.id)
            // FIXME: simple refresh view data
            _themes.value = repository.getAllThemes()
        }
    }
}