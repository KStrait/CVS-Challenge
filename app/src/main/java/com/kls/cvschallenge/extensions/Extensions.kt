package com.kls.cvschallenge.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatDate(): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date: Date = format.parse(this) ?: return "Invalid date"
        val outputFormat = SimpleDateFormat("MM-dd-yyyy h:mm:ss a", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}