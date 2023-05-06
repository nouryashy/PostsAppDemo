package com.example.data.remote

import com.example.domain.entity.ArticleResponse
import retrofit2.http.GET

interface ApiServices {
    @GET("top-headlines")
    suspend fun getArticles(): ArticleResponse
}