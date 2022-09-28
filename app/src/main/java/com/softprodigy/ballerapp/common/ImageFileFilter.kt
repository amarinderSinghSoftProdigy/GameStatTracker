package com.softprodigy.ballerapp.common

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val cursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
    cursor.moveToFirst()
    val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    return cursor.getString(0)
}

@Throws(IOException::class)
suspend fun getFileFromUriWithoutCompress(context: Context, uri: Uri): File? {
    val destinationFilename =
        File(context.filesDir.path + File.separatorChar.toString() + queryName(context, uri))
    try {
        context.contentResolver.openInputStream(uri).use { ins ->
            if (ins != null) {
                createFileFromStream(
                    ins,
                    destinationFilename
                )
            }
        }
    } catch (ex: Exception) {
        Timber.e("Save File", ex.message.toString())
        ex.printStackTrace()
    }

    val size = Integer.parseInt((destinationFilename.length()/1024).toString())
    Timber.i("Filesize not compressed--> $size")

    return destinationFilename
}
@Throws(IOException::class)
suspend fun getFileFromUri(context: Context, uri: Uri): File? {
    val destinationFilename =
        File(context.filesDir.path + File.separatorChar.toString() + queryName(context, uri))
    try {
        context.contentResolver.openInputStream(uri).use { ins ->
            if (ins != null) {
                createFileFromStream(
                    ins,
                    destinationFilename
                )
            }
        }
    } catch (ex: Exception) {
        Timber.e("Save File", ex.message.toString())
        ex.printStackTrace()
    }

    val size = Integer.parseInt((destinationFilename.length()/1024).toString())
    Timber.i("Filesize not compressed--> $size")

    return Compressor.compress(context, destinationFilename) {
        quality(80)
        size(2_097_152) // 2 MB
    }
}

fun createFileFromStream(ins: InputStream, destination: File?) {
    try {
        FileOutputStream(destination).use { os ->
            val buffer = ByteArray(4096)
            var length: Int
            while (ins.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
            os.flush()
        }
    } catch (ex: Exception) {
        Timber.e("Save File", ex.message.toString())
        ex.printStackTrace()
    }
}

private fun queryName(context: Context, uri: Uri): String {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}