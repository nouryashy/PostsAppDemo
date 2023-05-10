package com.example.postsappdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ArticleResponse
import com.example.domain.usecase.GetArticles
import com.example.postsappdemo.state.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val getArticlesUseCase: GetArticles) :
    ViewModel() {
    val _articles: MutableLiveData<ArticleResponse?> = MutableLiveData()
    val articles: LiveData<ArticleResponse?> = _articles
    val screenState = MutableLiveData<MainScreenState>()
    private val defaultError = "Something went wrong. Please try again later."
    fun getArticles() {
        viewModelScope.launch {
            if (screenState.value is MainScreenState.Success) return@launch
            screenState.value = MainScreenState.Loading
            viewModelScope.launch {
                delay(1000)
                try {
                    _articles.value = getArticlesUseCase()
                    screenState.value = MainScreenState.Success(_articles)
                } catch (e: Exception) {
                    screenState.value = MainScreenState.Error(e.message ?: defaultError)
                }
            }
        }
    }
}