package com.example.tribune_app.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.tribune_app.R

fun ImageView.loadImage(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun getToken(ctx: Context) =
    ctx.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
        .getString(AUTHENTICATED_SHARED_KEY, null)

fun howLongAgo(context: Context, seconds: Int): String {
    val minutes: Int = seconds / 60
    if (seconds / 60 == 0) {
        return context.resources.getString(R.string.less_minute)
    } else if (minutes < 60) {
        return "$minutes ${getSuffixForMinutes(context, minutes)}"
    }

    val hours: Int = minutes / 60
    return when {
        hours < 24 -> "$hours ${getSuffixForHours(context, hours)}"
        hours < 24 * 30 -> "$hours ${getSuffixForDays(context, hours)}"
        hours == 365 * 24 -> context.resources.getString(R.string.year_ago)
        hours < 365 * 24 -> context.resources.getString(R.string.few_months)
        else -> context.resources.getString(R.string.more_year_ago)
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