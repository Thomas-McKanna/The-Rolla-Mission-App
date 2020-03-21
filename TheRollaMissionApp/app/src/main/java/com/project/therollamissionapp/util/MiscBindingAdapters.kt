package com.project.therollamissionapp.util

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.Result
import kotlinx.android.synthetic.main.fragment_registration.view.*
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