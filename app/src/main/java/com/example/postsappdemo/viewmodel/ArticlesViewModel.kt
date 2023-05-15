package com.example.postsappdemo.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Article
import com.example.domain.entity.ArticleResponse
import com.example.domain.usecase.GetArticles
import com.example.postsappdemo.state.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
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


    var articleResponse: ArticleResponse? = null
    var newsPage = 1

    val readNews: LiveData<List<Article>> = getArticlesUseCase.articlesFromLocal().asLiveData()

    fun insertRecipes(articles: List<Article>) {
        viewModelScope.launch(Dispatchers.IO) {
            getArticlesUseCase.insertArticle(articles)
        }
    }

    fun getArticlesFromRemote() {
        viewModelScope.launch {
            if (screenState.value is MainScreenState.Success) return@launch
            screenState.value = MainScreenState.Loading
            viewModelScope.launch {
                delay(1000)
                try {
                    _articles.value = getArticlesUseCase.articlesFromRemote()
                    newsPage++
                    if (articleResponse == null) {
                        articleResponse == _articles.value
                    } else {
                        val oldArticles: ArrayList<Article> =
                            articleResponse!!.articles as ArrayList
                        val newArticles = _articles.value!!.articles
                        oldArticles.addAll(newArticles)
                    }
                    screenState.value =
                        MainScreenState.Success(articleResponse as MutableLiveData<ArticleResponse?>)
                } catch (e: Exception) {
                    screenState.value = MainScreenState.Error(e.message ?: defaultError)
                }
            }
        }
    }


}