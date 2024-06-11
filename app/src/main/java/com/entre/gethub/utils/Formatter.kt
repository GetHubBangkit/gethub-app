package com.entre.gethub.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object Formatter {
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

    // Format rupiah currency
    fun formatRupiah(value: Int): String {
        val localeID = Locale("id", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(value)
    }

    fun maskPhoneNumbers(text: String): String {
        // Regex untuk mendeteksi nomor telepon
        val phoneRegex = "\\b(?:\\+?\\d{1,3})?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}\\b".toRegex()

        // Mengganti setiap nomor telepon yang cocok dengan simbol "*"
        return phoneRegex.replace(text) { matchResult ->
            "*".repeat(matchResult.value.length)
        }
    }

}