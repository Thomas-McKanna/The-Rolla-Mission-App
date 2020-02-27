package com.project.therollamissionapp.util

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.project.therollamissionapp.data.Result
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

    @JvmStatic
    @BindingAdapter("uploading")
    fun uploadStatus(view: ProgressBar, result: Result<Unit>?) {
        if (result != null && result is Result.Loading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}