package com.example.mochilar.utils

import java.text.SimpleDateFormat
import java.util.*

fun isEndDateAfterStartDate(start: String, end: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startParsed = sdf.parse(start)
        val endParsed = sdf.parse(end)
        endParsed != null && startParsed != null && !endParsed.before(startParsed)
    } catch (e: Exception) {
        false
    }
}