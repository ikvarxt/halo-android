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
import me.ikvarxt.halo.ui.theme.setting.Item
import me.ikvarxt.halo.ui.theme.setting.OptionItem
import me.ikvarxt.halo.ui.theme.setting.TitleItem
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repository: ThemeRepository
) : ViewModel() {

    private val _themes = MutableLiveData<NetworkResult<List<HaloTheme>>>()
    val themes: LiveData<NetworkResult<List<HaloTheme>>> = _themes

    private val _themeConfigs = MutableLiveData<List<Item>>()
    val themeConfigs: LiveData<List<Item>> = _themeConfigs

    // theme setting real value, with key and value
    private val _themeSettings = MutableLiveData<Map<String, String>>()
    val themeSettings: LiveData<Map<String, String>> = _themeSettings

//    val themeConfigurations = repository.fetchActivatedThemeConfig().map { it ->
//
//
//    }

    init {
        viewModelScope.launch {
            _themes.value = repository.getAllThemes()

            repository.fetchActivatedThemeConfig().collect { list ->
                if (list == null) {
                    return@collect
                }

                val resList = mutableListOf<Item>()
                list.forEach { group ->
                    resList.add(TitleItem(group.label))
                    val options = group.items.map { OptionItem(item = it) }
                    resList.addAll(options)
                }
                _themeConfigs.value = resList
            }

            val settings = repository.listsActivatedThemeSettings()
            val hashMap = mutableMapOf<String, String>()
            settings.map {
                hashMap[it.key] = it.value
            }
            _themeSettings.value = hashMap
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