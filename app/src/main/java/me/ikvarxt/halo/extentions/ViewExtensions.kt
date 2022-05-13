package me.ikvarxt.halo.extentions

import android.widget.EditText
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.concurrent.Executors

fun EditText.editWithMarkdown(editor: MarkwonEditor) {
    val executors = Executors.newCachedThreadPool()
    val textWatcher = MarkwonEditorTextWatcher.withPreRender(editor, executors, this)
    addTextChangedListener(textWatcher)
}