package me.ikvarxt.halo.ui.theme.setting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import me.ikvarxt.halo.databinding.ViewThemeOptionStringBinding
import me.ikvarxt.halo.databinding.ViewThemeOptionSwitchBinding
import me.ikvarxt.halo.databinding.ViewThemeSettingTitleBinding
import me.ikvarxt.halo.entites.ThemeConfigDataType
import me.ikvarxt.halo.entites.ThemeConfiguration

private const val TYPE_TITLE = 0
private const val TYPE_OPTION_STRING = 1
private const val TYPE_OPTION_SWITCH = 2

class ThemeSettingsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<ThemeSettingsAdapter.ViewHolder>() {

    private val list = mutableListOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TYPE_TITLE -> {
                val binding = ViewThemeSettingTitleBinding.inflate(inflater, parent, false)
                return TitleViewHolder(binding)
            }
            TYPE_OPTION_STRING -> {
                val binding = ViewThemeOptionStringBinding.inflate(inflater, parent, false)
                return OptionStringViewHolder(binding)
            }
            TYPE_OPTION_SWITCH -> {
                val binding = ViewThemeOptionSwitchBinding.inflate(inflater, parent, false)
                return OptionSwitchViewHolder(binding)
            }
            else -> throw IllegalArgumentException("could not find $viewType ViewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]

        when (item) {
            is TitleItem ->
                return TYPE_TITLE
            is OptionItem -> {
                val realItem = item.item
                when (realItem.dataType) {
                    ThemeConfigDataType.BOOL -> {
                        return TYPE_OPTION_SWITCH
                    }
                    ThemeConfigDataType.STRING -> {
                        return TYPE_OPTION_STRING
                    }
                }
            }
        }
        return TYPE_TITLE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        when (item) {
            is TitleItem -> {
                (holder as TitleViewHolder).binding.titleText.text = item.label
            }
            is OptionItem -> {
                val realItem = item.item
                when (realItem.dataType) {
                    ThemeConfigDataType.BOOL -> {
                        val binding = (holder as OptionSwitchViewHolder).binding
                        binding.switchOption.text = item.label
                        binding.switchOption.isChecked = if (realItem.value != null) {
                            realItem.value?.toBooleanStrictOrNull() == true
                        } else {
                            realItem.defaultValue.toBooleanStrictOrNull() == true
                        }
                        binding.switchOption.setOnCheckedChangeListener { _, isChecked ->
                            listener.updateSetting(item, isChecked.toString())
                        }
                    }
                    ThemeConfigDataType.STRING -> {
                        val binding = holder.binding as ViewThemeOptionStringBinding
                        binding.inputLayout.hint = item.label
                        binding.inputEdit.setText(realItem.defaultValue)
                        if (realItem.value != null) {
                            binding.inputEdit.setText(realItem.value)
                        } else {
                            binding.inputEdit.setText(realItem.defaultValue)
                        }
                        binding.inputEdit.doAfterTextChanged {
                            val value = binding.inputEdit.text.toString().trim()
                            listener.updateSetting(item, value)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<Item>) {
        if (list != data) {
            list.clear()
            list.addAll(data)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setupValues(data: Map<String, String>) {
        if (list.isEmpty()) {
            return
        }

        list.filterIsInstance<OptionItem>().map { item ->
            val realItem = item.item
            if (data.containsKey(realItem.id)) {
                realItem.value = data[realItem.id]
            }
        }

        notifyDataSetChanged()
    }

    open class ViewHolder(
        open val binding: ViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    open class OptionStringViewHolder(
        override val binding: ViewThemeOptionStringBinding
    ) : ViewHolder(binding)

//    class OptionSelectViewHolder()

//    class OptionAttachmentViewHolder() : OptionStringViewHolder()

    class OptionSwitchViewHolder(
        override val binding: ViewThemeOptionSwitchBinding
    ) : ViewHolder(binding)

    class TitleViewHolder(
        override val binding: ViewThemeSettingTitleBinding
    ) : ViewHolder(binding)

    interface Listener {
        fun updateSetting(optionItem: OptionItem, value: String)
    }
}

open class Item(open val label: String)

data class TitleItem(val title: String) : Item(title)

data class OptionItem(val item: ThemeConfiguration) : Item(item.label)