package me.ikvarxt.halo.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.*
import androidx.core.view.isVisible

/**
 * from ZhangHai
 */
class AutoGoneTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)

        isVisible = !text.isNullOrEmpty()
    }
}