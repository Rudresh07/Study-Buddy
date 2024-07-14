package com.example.studybuddy.utils

import androidx.compose.ui.graphics.Color.Companion.Yellow
import com.example.studybuddy.ui.theme.Green
import com.example.studybuddy.ui.theme.Orange
import com.example.studybuddy.ui.theme.Red
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class Priority(val title: String, val color:androidx.compose.ui.graphics.Color, val value: Int) {
    LOW(title = "Low", color = Green, value = 1),
    MEDIUM(title = "Medium", color = Orange, value = 2),
    HIGH(title = "High", color = Red, value = 3);


    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value }?:MEDIUM
    }


}


fun Long?.changeMillisToDateString():String{
    val date:LocalDate = this?.let {
        Instant
            .ofEpochMilli(it)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()

    }?: LocalDate.now()

    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}