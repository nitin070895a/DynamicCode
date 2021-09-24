package com.example

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

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