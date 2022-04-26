package me.ikvarxt.halo.entites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rikka.insets.WindowInsetsHelper
import rikka.layoutinflater.view.LayoutInflaterFactory

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 = LayoutInflaterFactory(delegate).addOnViewCreatedListener(
            WindowInsetsHelper.LISTENER
        )
        super.onCreate(savedInstanceState)
    }
}