package com.example.data.repository

import com.example.data.remote.ApiServices
import com.example.domain.entity.ArticleResponse
import com.example.domain.repository.ArticleRepository

class ArticleRepositoryImp(private val apiServices: ApiServices) : ArticleRepository {
    override suspend fun getArticlesFromRemote(): ArticleResponse = apiServices.getArticles()
}