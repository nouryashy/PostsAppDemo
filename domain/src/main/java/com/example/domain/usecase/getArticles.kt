package com.example.domain.usecase

import com.example.domain.repo.ArticleRepository

class getArticles(private val articleRepository: ArticleRepository) {
    suspend operator fun invoke() = articleRepository.getArticlesFromRemote()
}