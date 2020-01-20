package com.example.tribune_app.utils

import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide

const val AUTHENTICATED_SHARED_KEY = "authenticated_shared_key"
const val API_SHARED_FILE = "API_shared_file"
const val BASE_URL = //BuildConfig.SERVER_API_URL
//    "https://tribune-app.herokuapp.com/"
    "http://10.0.2.2:9999"

const val REQUEST_IMAGE_CAPTURE = 1

private const val BITMAP_KEY = "data"
val Intent.bitmap: Bitmap?
    get() = extras?.get(BITMAP_KEY) as? Bitmap?

const val USER_ID_DEFAULT_VALUE = 0L
private const val USER_ID_KEY = "userId"
var Intent.userId
    get() = getLongExtra(USER_ID_KEY, USER_ID_DEFAULT_VALUE)
    set(value) {
        putExtra(USER_ID_KEY, value)
    }

const val POST_ID_DEFAULT_VALUE = 0L
private const val POST_ID_KEY = "postId"
var Intent.postId
    get() = getLongExtra(POST_ID_KEY, POST_ID_DEFAULT_VALUE)
    set(value) {
        putExtra(POST_ID_KEY, value)
    }

fun loadImage(photoImg: ImageView, url: String) {
    Glide.with(photoImg.context)
        .load(url)
        .into(photoImg)
}