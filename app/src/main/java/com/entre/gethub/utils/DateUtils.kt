package com.entre.gethub.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateDeadlineDays(minDeadline: String, maxDeadline: String): Long {
        val formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy")
        val minDate = LocalDate.parse(minDeadline, formatter)
        val maxDate = LocalDate.parse(maxDeadline, formatter)

        return ChronoUnit.DAYS.between(minDate, maxDate)
    }

    // Format date to (28-10-2024)
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatPostDate(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

        val parsedDate = ZonedDateTime.parse(date, inputFormatter)

        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        return parsedDate.format(outputFormatter)
    }
}