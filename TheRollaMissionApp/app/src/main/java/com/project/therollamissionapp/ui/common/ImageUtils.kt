package com.project.therollamissionapp.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException

object ImageUtils {
    fun takeImage(fragment: Fragment, id: String, requestCode: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(fragment.requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(fragment, id)
                } catch (ex: IOException) {
                    // TODO
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        fragment.requireContext(),
                        "com.project.therollamissionapp.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(takePictureIntent, requestCode)
                }
            }
        }
    }

    fun getPatronHeadshotPath(context: Context, patronId: String): String {
        val prefix = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
        return String.format("%s/%s.jpg", prefix, patronId)
    }

    @Throws(IOException::class)
    private fun createImageFile(fragment: Fragment, id: String): File {
        val storageDir: File? = fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, "${id}.jpg")
    }
}