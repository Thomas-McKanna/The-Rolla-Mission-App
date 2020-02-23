package com.project.therollamissionapp.util

import androidx.databinding.BindingAdapter
import com.github.gcacace.signaturepad.utils.SignaturePadBindingAdapter
import com.github.gcacace.signaturepad.views.SignaturePad

object SignaturePadBindingAdapter {
    @JvmStatic
    @BindingAdapter("onStartSigning")
    fun setOnSignedListener(
        view: SignaturePad?,
        onStartSigningListener: SignaturePadBindingAdapter.OnStartSigningListener?
    ) {
        SignaturePadBindingAdapter.setOnSignedListener(
            view,
            onStartSigningListener,
            null,
            null
        )
    }

    @JvmStatic
    @BindingAdapter("onSigned")
    fun setOnSignedListener(
        view: SignaturePad?,
        onSignedListener: SignaturePadBindingAdapter.OnSignedListener?
    ) {
        SignaturePadBindingAdapter.setOnSignedListener(
            view,
            null,
            onSignedListener,
            null
        )
    }

    @JvmStatic
    @BindingAdapter("onClear")
    fun setOnSignedListener(
        view: SignaturePad?,
        onClearListener: SignaturePadBindingAdapter.OnClearListener?
    ) {
        SignaturePadBindingAdapter.setOnSignedListener(
            view,
            null,
            null,
            onClearListener
        )
    }

    @JvmStatic
    @BindingAdapter(value = ["onStartSigning", "onSigned", "onClear"], requireAll = false)
    fun setOnSignedListener(
        view: SignaturePad,
        onStartSigningListener: SignaturePadBindingAdapter.OnStartSigningListener?,
        onSignedListener: SignaturePadBindingAdapter.OnSignedListener?,
        onClearListener: SignaturePadBindingAdapter.OnClearListener?
    ) {
        view.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                onStartSigningListener?.onStartSigning()
            }

            override fun onSigned() {
                onSignedListener?.onSigned()
            }

            override fun onClear() {
                onClearListener?.onClear()
            }
        })
    }

    interface OnStartSigningListener {
        fun onStartSigning()
    }

    interface OnSignedListener {
        fun onSigned()
    }

    interface OnClearListener {
        fun onClear()
    }
}