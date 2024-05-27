package com.entre.gethub.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
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
}