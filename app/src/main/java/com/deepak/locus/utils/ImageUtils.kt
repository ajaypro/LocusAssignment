package com.deepak.locus.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Save Bitmap to file
 *
 * @param context Context
 * @param bitmap Bitmap to save
 * @return file path
 * @throws IOException method throws IOException if error occurs while saving bitmap to file
 */

fun saveBitMap(bitmap: Bitmap, context: Context): String {
    val mediaFile = getOutputMediaFile(context)
    try {
    FileOutputStream(mediaFile).apply {
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
        flush()
        close()
    }
    } catch (exception: IOException){
        exception.stackTrace
    }
    return mediaFile.absolutePath
}

/**
 * Creates Files object to save
 *
 * @param context Context
 * @return File to save
 */
@Suppress("DEPRECATION")
fun getOutputMediaFile(context: Context): File {
//    val fileDir = File(Environment.getExternalStorageDirectory(), "Locus")
//    return if(fileDir.isDirectory){
//        File(fileDir.path + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg")
//
//    } else {
//        fileDir.mkdirs()
//        addPicToGallery(context, fileDir)
//        getOutputMediaFile(context)
//    }

    val file =  File.createTempFile(
        ("IMG_${SimpleDateFormat("MM-dd-_HH-mm-ss", Locale.getDefault()).format(Date())}"),
        ".jpg",
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))

    addPicToGallery(context, file)

    return file
}

/**
 * Add pic to Gallery if not automatically fetched
 *
 * @param context Context
 * @param f File instance
 */

fun addPicToGallery(context: Context, file: File) {
    Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE").apply {
        Uri.fromFile(file).apply { data = this }
        context.sendBroadcast(this)
    }

}



