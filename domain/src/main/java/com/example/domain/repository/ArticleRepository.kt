package com.example.domain.repository

import com.example.domain.entity.ArticleResponse

interface ArticleRepository {
    suspend fun getArticlesFromRemote(): ArticleResponse
}