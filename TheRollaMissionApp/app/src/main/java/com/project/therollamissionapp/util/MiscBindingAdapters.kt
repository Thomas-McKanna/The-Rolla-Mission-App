package com.project.therollamissionapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File


object MiscBindingAdapters {
    @JvmStatic
    @BindingAdapter("previewImage")
    fun loadImage(view: ImageView, path: String?) {
        path?.apply {
            Glide.with(view.context)
                .load(File(this))
                .into(view)
        }
    }
}