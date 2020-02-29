package com.project.therollamissionapp.util

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.project.therollamissionapp.data.Result
import java.io.File

object MiscBindingAdapters {
    @JvmStatic
    @BindingAdapter("previewPatronImage")
    fun previewPatronImage(view: ImageView, path: String?) {
        path?.apply {
            Glide.with(view.context)
                .load(File(this))
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("loadPatronImage")
    fun loadPatronImage(view: ImageView, id: String?) {
        id?.apply {
            val prefix = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val path = String.format("%s/%s.jpg", prefix, id)
            Glide.with(view.context)
                .load(File(path))
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("loading")
    fun loading(view: ProgressBar, result: Result<Unit>?) {
        if (result != null && result is Result.Loading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}