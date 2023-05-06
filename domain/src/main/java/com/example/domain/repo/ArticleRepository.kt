package com.example.domain.repo

import com.example.domain.entity.ArticleResponse

interface ArticleRepository {
    suspend fun getArticlesFromRemote(): ArticleResponse
}