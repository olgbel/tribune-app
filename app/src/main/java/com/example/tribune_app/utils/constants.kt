package com.example.tribune_app.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

const val AUTHENTICATED_SHARED_KEY = "authenticated_shared_key"
const val API_SHARED_FILE = "API_shared_file"
const val BASE_URL = //BuildConfig.SERVER_API_URL
"https://tribune-app.herokuapp.com/"
//    "http://10.0.2.2:9999"

const val REQUEST_IMAGE_CAPTURE = 1


fun loadImage(photoImg: ImageView, url: String) {
    Glide.with(photoImg.context)
        .load(url)
        .into(photoImg)
}