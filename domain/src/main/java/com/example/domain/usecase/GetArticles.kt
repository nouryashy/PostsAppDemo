package com.example.domain.usecase

import com.example.domain.repository.ArticleRepository

class GetArticles(private val articleRepository: ArticleRepository) {
    suspend operator fun invoke() = articleRepository.getArticlesFromRemote()
}