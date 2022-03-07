package me.ikvarxt.halo.extentions

import android.view.View

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