package com.example.postsappdemo.state

import androidx.lifecycle.MutableLiveData
import com.example.domain.entity.ArticleResponse

sealed class MainScreenState {
    data class Success(val data: MutableLiveData<ArticleResponse?>) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
    object Loading : MainScreenState()
}
