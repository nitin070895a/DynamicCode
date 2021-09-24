package com.example

import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val BUF_SIZE = 8 * 1024 // Copy buffer size

/**
 * Returns the current time in HH:mm:ss.SSS format
 */
fun getTime(): String? {
    return SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
}

/**
 * Show [msg] in snack bar over [activity]
 */
fun showSnackBar(activity: Activity, msg: String) {
    Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
}

/**
 * Downloads a dex file from an [inputStream] and stores it with [fileName] in app_dex folder
 *
 * @param context The context of the activity
 * @return The path where the dex is downloaded
 */
fun downloadDex(context: Context, inputStream: InputStream, fileName: String): String? {

    var path: String? = null

    try {
        val dexFile = File(context.getDir("dex", Context.MODE_PRIVATE), fileName)
        val outputStream = BufferedOutputStream(FileOutputStream(dexFile))

        val buffer = ByteArray(BUF_SIZE)
        var len: Int
        while (inputStream.read(buffer, 0, BUF_SIZE).also { len = it } > 0) {
            outputStream.write(buffer, 0, len)
        }

        outputStream.close()

        path = dexFile.absolutePath

    } catch (e: IOException) {
        e.printStackTrace()
    }

    return path
}