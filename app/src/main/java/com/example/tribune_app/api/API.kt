package com.example.tribune_app.api

import com.example.tribune_app.dto.AttachmentModel
import com.example.tribune_app.dto.CreatePostRequest
import com.example.tribune_app.dto.PostModel
import com.example.tribune_app.dto.PushRequestParamsDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

data class AuthRequestParams(val username: String, val password: String)

data class Token(val token: String)

data class RegistrationRequestParams(val username: String, val password: String)

interface API {
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>

    @POST("api/v1/token")
    suspend fun registerPushToken(@Body params: PushRequestParamsDto): Response<Void>

    @POST("/api/v1/posts")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<Void>

    @GET("api/v1/posts/recent")
    suspend fun getRecentPosts(): Response<List<PostModel>>

    @GET("api/v1/posts/after/{id}")
    suspend fun getPostsAfter(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/before/{id}")
    suspend fun getPostsBefore(@Path("id") id: Long): Response<List<PostModel>>

    @POST("api/v1/posts/like/{id}")
    suspend fun likedByMe(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts/dislike/{id}")
    suspend fun dislikeByMe(@Path("id") id: Long): Response<PostModel>

    @Multipart
    @POST("api/v1/media")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<AttachmentModel>
}