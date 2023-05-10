package com.example.data.repository

import com.example.data.remote.ApiServices
import com.example.domain.entity.ArticleResponse
import com.example.domain.repository.ArticleRepository

class ArticleRepositoryImp(private val apiServices: ApiServices) : ArticleRepository {
    private val country: String = "us"
    private val apiKey: String = "8c911bed784e46de80b7fb6cd80ea232"
    override suspend fun getArticlesFromRemote(): ArticleResponse =
        apiServices.getArticles(country, apiKey)
}