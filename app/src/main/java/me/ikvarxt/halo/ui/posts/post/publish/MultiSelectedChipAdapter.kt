package me.ikvarxt.halo.ui.posts.post.publish

import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import me.ikvarxt.halo.R

data class SelectedItem(
    val id: Int,
    val name: String,
    val color: Int? = null,
    val isAddBtn: Boolean = false
) {
    var isSelected: Boolean = false
}

class MultiSelectedChipAdapter(
    private val type: MultiAddType,
    private val listener: Listener,
) {
    private val list = mutableListOf<SelectedItem>()

    val selectedItem: List<Int>
        get() = list.filter { it.isSelected }.map { it.id }

    fun submitList(data: List<SelectedItem>, preSelectedList: List<Int>, chipGroup: ChipGroup) {
        if (data != list) {
            list.clear()
            list.addAll(data)

            refreshChips(preSelectedList, chipGroup)
        }
    }

    private fun refreshChips(preSelectedList: List<Int>, group: ChipGroup) {
        group.removeAllViews()

        list.map { item ->
            item.isSelected = preSelectedList.contains(item.id)

            Chip(group.context).apply {
                text = item.name
                id = item.id
                isCheckable = true
                isChecked = item.isSelected
                chipBackgroundColor = item.color?.let { it -> ColorStateList.valueOf(it) }
                checkedIcon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_check_circle_24,
                    context.theme
                )
                setOnCheckedChangeListener { _, isChecked ->
                    selectItem(item, isChecked)
                }
            }
        }.forEach { group.addView(it) }

        val addChip = Chip(group.context).apply {
            text = group.context.getString(R.string.add)

            chipIcon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_baseline_add_24,
                context.theme
            )
            setOnClickListener { listener.addNewItem(type) }
        }
        group.addView(addChip)
    }

    private fun selectItem(item: SelectedItem, isSelect: Boolean) {
        list.firstOrNull { it.id == item.id }?.isSelected = isSelect
    }

    interface Listener {
        fun addNewItem(type: MultiAddType)
    }

    enum class MultiAddType {
        ADD_TAG,
        ADD_CATEGORY
    }
}