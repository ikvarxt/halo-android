package me.ikvarxt.halo.extentions

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.updatePadding
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import me.ikvarxt.halo.R
import rikka.insets.WindowInsetsHelper
import java.util.concurrent.Executors

fun EditText.editWithMarkdown(editor: MarkwonEditor) {
    val executors = Executors.newCachedThreadPool()
    val textWatcher = MarkwonEditorTextWatcher.withPreRender(editor, executors, this)
    addTextChangedListener(textWatcher)
}

fun View.applyAppbarMargin() {
    val topMargin = resources.getDimension(R.dimen.app_bar_margin).toInt()
    updatePadding(left = paddingLeft, top = topMargin, right = paddingRight, bottom = paddingBottom)
    val fitTopWindowsInsets = 0x30
    WindowInsetsHelper.attach(this, true, fitTopWindowsInsets, 0, 0)
}

fun SwitchCompat.setCheckedState(checked: Boolean) {
    isChecked = checked
    jumpDrawablesToCurrentState()
}