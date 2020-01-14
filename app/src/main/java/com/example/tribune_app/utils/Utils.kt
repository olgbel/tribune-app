package com.example.tribune_app.utils

fun howLongAgo(seconds: Int) : String{

    val minutes: Double = seconds.toDouble() / 60
    if (seconds / 60 == 0){
        return "менее минуты назад"
    }
    else if (minutes < 60){
        return "$minutes ${getSuffixForMinutes(minutes)} назад"
    }

    val hours: Double = minutes / 60
    return when {
        hours == 1.0 -> "час назад"
        hours < 24 -> "${hours.toInt()} ${getSuffixForHours(hours)} назад"
        hours == 24.0 -> "один день назад"
        hours < 24 * 30 -> "несколько дней назад"
        hours == 365.0 * 24 -> "год назад"
        hours < 365 * 24 -> "несколько месяцев назад"
        else -> "более года назад"
    }
}

fun getSuffixForMinutes(minutes: Double) : String {

    return when (minutes.toInt()){
        1, 21, 31, 41, 51 -> "минуту"
        2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54 -> "минуты"
        else -> "минут"
    }}

fun getSuffixForHours(hours: Double): String{
    return when (hours.toInt()){
        21 -> "час"
        2, 3, 4, 22, 23 -> "часа"
        else -> "часов"
    }
}