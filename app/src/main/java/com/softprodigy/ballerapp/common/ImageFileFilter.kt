package com.softprodigy.ballerapp.common

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

fun getRealPathFromURI(context: Context, uri: Uri): String? {
   val cursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
   cursor.moveToFirst()
   val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
   return cursor.getString(0)
}