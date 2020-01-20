package com.example.tribune_app.utils

import android.content.Context
import com.example.tribune_app.R

fun howLongAgo(context: Context, seconds: Int): String {
    println("seconds: $seconds")
    val minutes: Int = seconds / 60
    println("minutes: $minutes")
    if (seconds / 60 == 0) {
        println("1")
        println("${R.string.less_minute.toString()}")
        return R.string.less_minute.toString()
    } else if (minutes < 60) {
        println("1")
        return "$minutes ${getSuffixForMinutes(context, minutes)}"
    }

    val hours: Int = minutes / 60
    return when {
        hours < 24 -> "${hours} ${getSuffixForHours(context, hours)}"
        hours < 24 * 30 -> "${hours} ${getSuffixForDays(context, hours)}"
        hours == 365 * 24 -> R.string.year_ago.toString()
        hours < 365 * 24 -> R.string.few_months.toString()
        else -> R.string.more_year_ago.toString()
    }
}

fun getSuffixForMinutes(context: Context, minutes: Int) =
    context.resources.getQuantityString(R.plurals.minutes_plurals, minutes, minutes)


fun getSuffixForHours(context: Context, hours: Int) =
    context.resources.getQuantityString(R.plurals.hours_plurals, hours, hours)

fun getSuffixForDays(context: Context, hours: Int) = context.resources.getQuantityString(
    R.plurals.days_plurals,
    hours, hours
)