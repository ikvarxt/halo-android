package me.ikvarxt.halo.ui.halosettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.constant.HaloSettingsConstants.General
import me.ikvarxt.halo.entites.HaloOption
import me.ikvarxt.halo.repository.HaloSettingsRepository
import javax.inject.Inject

@HiltViewModel
class HaloSettingsViewModel @Inject constructor(
    private val repository: HaloSettingsRepository
) : ViewModel() {

    private val _options = repository.options
    val options = mutableMapOf<String, String>()

    private val _optionsLiveData = MutableLiveData<Map<String, String>>()
    val optionsLiveData: LiveData<Map<String, String>> = _optionsLiveData

    init {
        viewModelScope.launch {
            repository.listAllOptions()

            _options.collectLatest { list ->
                options.clear()
                list.map { option ->
                    options.put(option.key, option.value)
                }
                _optionsLiveData.value = options
            }
        }
    }

    val blogTitle get() = getStringOption(General.BLOG_TITLE)
    val blogUrl get() = getStringOption(General.BLOG_URL)
    val logo get() = getStringOption(General.BLOG_LOGO)
    val favicon get() = getStringOption(General.FAVICON)
    val footer get() = getStringOption(General.FOOTER)

    fun saveOption(key: String, newValue: String) {
        val option = options[key]?.let { HaloOption(key, it) } ?: return
        saveOption(option, newValue)
    }

    fun saveOption(option: HaloOption, newValue: String) {
        viewModelScope.launch {
            val newOption = HaloOption(option.key, newValue)
            repository.saveOption(newOption)
        }
    }

    private fun getStringOption(key: String): String {
        return options.getOrDefault(key, "")
    }

}