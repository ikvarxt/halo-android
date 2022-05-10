package me.ikvarxt.halo.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import me.ikvarxt.halo.R

@BindingAdapter(value = ["imageUrl", "error"], requireAll = false)
fun ImageView.bindImageWithUrl(url: String?, error: Drawable?) {
    var glide = Glide.with(this)
        .load(url)

    glide = if (error != null) {
        glide.error(error)
    } else {
        val errorDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_error_24,
            context.theme
        )
        glide.error(errorDrawable)
    }

    glide.into(this)
}