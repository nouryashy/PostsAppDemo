package com.example.postsappdemo.di

import com.example.data.remote.ApiServices
import com.example.data.repository.ArticleRepositoryImp
import com.example.domain.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideRepository(apiServices: ApiServices): ArticleRepository {
        return ArticleRepositoryImp(apiServices)
    }
}