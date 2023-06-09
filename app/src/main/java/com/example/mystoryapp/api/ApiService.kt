package com.example.mystoryapp.api

import com.example.mystoryapp.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    fun stories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<StoriesResponse>

    @GET("stories")
    fun stories(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun uploadStorie(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<RegisterResponse>
    @GET("stories/{id}")
    fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>
}