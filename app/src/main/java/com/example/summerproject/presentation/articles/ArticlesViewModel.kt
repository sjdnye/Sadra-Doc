package com.example.summerproject.presentation.articles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.use_case.ArticleUseCase
import com.example.summerproject.domain.utils.ArticleOrder
import com.example.summerproject.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase
) : ViewModel() {

    var state by mutableStateOf(ArticleState())
        private set

    private var recentlyArticleDeleted: Article? = null

    private var getArticlesJob: Job? = null


    init {
        getArticles(state.searchQuery, ArticleOrder.Date(OrderType.Ascending))
    }



    fun onEvent(event: ArticleEvent){
        when(event){
            is ArticleEvent.Order -> {
                if (state.articleOrder::class == event.articleOrder::class &&
                    state.articleOrder.orderType == event.articleOrder.orderType
                ) {
                    return
                }
                state = state.copy(articleOrder = event.articleOrder)
                getArticles(state.searchQuery,state.articleOrder)
            }
            is ArticleEvent.DeleteNote -> {
                viewModelScope.launch {
                    articleUseCase.deleteArticleUseCase(event.article)
                    recentlyArticleDeleted = event.article
                }

            }
            is ArticleEvent.SearchArticle -> {
                state = state.copy(searchQuery = event.query)
                getArticles(state.searchQuery, state.articleOrder)
            }

            is ArticleEvent.RestoreNote -> {
                viewModelScope.launch {
                    articleUseCase.insertArticleUseCase(recentlyArticleDeleted?: return@launch)
                    recentlyArticleDeleted = null
                }
            }
            is ArticleEvent.ToggleOrderSection -> {
                state = state.copy(isOrderSectionVisible = !state.isOrderSectionVisible)
            }
        }
    }


    private fun getArticles(query: String = state.searchQuery, articleOrder: ArticleOrder){
        getArticlesJob?.cancel()
        getArticlesJob = articleUseCase.getArticlesUseCase(query, articleOrder).onEach{ articles ->
            state = state.copy(
                articles = articles,
                articleOrder = articleOrder
            )
        }
            .launchIn(viewModelScope)
    }

}