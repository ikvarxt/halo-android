package me.ikvarxt.halo.extentions

import android.view.View
import android.widget.EditText
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.concurrent.Executors

var View.layoutInStatusBar: Boolean
    get() = systemUiVisibility.hasBits(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    set(value) {
        systemUiVisibility = if (value) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else {
            systemUiVisibility andInv View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

var View.layoutInNavigation: Boolean
    get() = systemUiVisibility.hasBits(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    set(value) {
        systemUiVisibility = if (value) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else {
            systemUiVisibility andInv View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }

fun EditText.editWithMarkdown(editor: MarkwonEditor) {
    val executors = Executors.newCachedThreadPool()
    val textWatcher = MarkwonEditorTextWatcher.withPreRender(editor, executors, this)
    addTextChangedListener(textWatcher)
}