package com.project.therollamissionapp.util

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.ui.common.ImageUtils.getPatronHeadshotPath
import java.io.File

object MiscBindingAdapters {
    @JvmStatic
    @BindingAdapter("previewPatronImage")
    fun previewPatronImage(view: ImageView, path: String?) {
        path?.apply {
            Glide.with(view.context)
                .load(File(this))
                // Always update image (do not cache)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("loadPatronImage")
    fun loadPatronImage(view: ImageView, id: String?) {
        id?.apply {
            val path = getPatronHeadshotPath(view.context, id)
            Glide.with(view.context)
                .load(File(path))
                .signature(ObjectKey(System.currentTimeMillis().toString()))
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