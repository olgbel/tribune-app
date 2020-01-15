package com.example.tribune_app

import android.graphics.Bitmap
import com.example.tribune_app.api.API
import com.example.tribune_app.api.AuthRequestParams
import com.example.tribune_app.api.RegistrationRequestParams
import com.example.tribune_app.api.interceptor.InjectAuthTokenInterceptor
import com.example.tribune_app.dto.AttachmentModel
import com.example.tribune_app.dto.CreatePostRequest
import com.example.tribune_app.utils.BASE_URL
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.ByteArrayOutputStream

object Repository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        API = retrofit.create()
    }


    private var API: API =
        retrofit.create(com.example.tribune_app.api.API::class.java)


    suspend fun authenticate(login: String, password: String) = API.authenticate(
        AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
        API.register(
            RegistrationRequestParams(
                login,
                password
            )
        )

    suspend fun getRecentPosts() = API.getRecentPosts()

    suspend fun getPostsAfter(id: Long) = API.getPostsAfter(id)

    suspend fun getPostsBefore(id: Long) = API.getPostsBefore(id)

    suspend fun likedByMe(id: Long) = API.likedByMe(id)

    suspend fun dislikedByMe(id: Long) = API.dislikeByMe(id)

    suspend fun upload(bitmap: Bitmap): Response<AttachmentModel> {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle =
            RequestBody.create(MediaType.parse("image/jpeg"), bos.toByteArray())
        val body =
            MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        println("body: $body")
        return API.uploadImage(body)
    }

    suspend fun createPost(content: String, linkURL: String?, attachment: AttachmentModel) =
        API.createPost(
            CreatePostRequest(
                content = content,
                linkURL = linkURL,
                attachment = attachment
            )
        )

    suspend fun getCurrentUser() = API.getCurrentUser()

    suspend fun getReactionsById(postId: Long) = API.getReactionsById(postId)

    suspend fun getPostsByUserId(userId: Long) = API.getPostsByUserId(userId)
}