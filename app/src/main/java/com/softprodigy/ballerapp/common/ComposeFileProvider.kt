package com.softprodigy.ballerapp.common

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.softprodigy.ballerapp.R
import java.io.File


class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            if (!directory.exists())
                directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            if (!file.exists()) {
                file.mkdirs()
            }
            val authority = context.packageName + ".provider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}