package com.example.tribune_app.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LogoutInterceptor(private val logout: () -> Unit) : Interceptor {

    private companion object {
        const val UNAUTHORIZED_CODE = 401
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response

        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            throw e
        }

        return if (response.code() == UNAUTHORIZED_CODE) {
            logout()
            response
        } else {
            response
        }
    }
}