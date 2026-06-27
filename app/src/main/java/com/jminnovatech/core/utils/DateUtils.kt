package com.jminnovatech.core.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatServerDate(date: String?): String {

    if (date.isNullOrEmpty()) return "-"

    return try {

        val input = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            Locale.getDefault()
        )

        input.timeZone = TimeZone.getTimeZone("UTC")

        val output = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        output.format(input.parse(date)!!)

    } catch (e: Exception) {

        date
    }
}