package com.example.tribune_app

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import com.example.tribune_app.api.interceptor.LogoutInterceptor
import com.example.tribune_app.utils.API_SHARED_FILE
import com.example.tribune_app.utils.AUTHENTICATED_SHARED_KEY

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Repository.logoutInterceptor = LogoutInterceptor(::logout)
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)

        if (sharedPreferences.contains(AUTHENTICATED_SHARED_KEY).not()) {
            return
        }

        sharedPreferences.edit {
            remove(AUTHENTICATED_SHARED_KEY)
        }

        startActivity(
            Intent(this, MainActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        )
    }
}