package com.example.summerproject.presentation.articles

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.use_case.ArticleUseCase
import com.example.summerproject.domain.utils.ArticleOrder
import com.example.summerproject.domain.utils.OrderType
import com.example.summerproject.presentation.article_add_edit.AddEditArticleViewModel
import com.example.summerproject.utils.ConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase,
    private val firestore: FirebaseFirestore,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private var connectionStatus by mutableStateOf<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)

    var state by mutableStateOf(ArticleState())
        private set

    var showAlertDialog by mutableStateOf(false)

    private var recentlyArticleDeleted: Article? = null

    private var getArticlesJob: Job? = null

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = MutableSharedFlow<ArticleScreenUi>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {

        viewModelScope.launch {
            checkInternetConnection()
        }
        getArticles(state.searchQuery, ArticleOrder.Date(OrderType.Ascending))
    }

    private suspend fun checkInternetConnection() {
        connectivityObserver.observe().collectLatest { status ->
            connectionStatus = status
        }
    }

    fun onEvent(event: ArticleEvent) {
        when (event) {
            is ArticleEvent.Order -> {
                if (state.articleOrder::class == event.articleOrder::class &&
                    state.articleOrder.orderType == event.articleOrder.orderType
                ) {
                    return
                }
                state = state.copy(articleOrder = event.articleOrder)
                getArticles(state.searchQuery, state.articleOrder)
            }
            is ArticleEvent.DeleteNote -> {
                if (connectionStatus != ConnectivityObserver.Status.Available) {
                    viewModelScope.launch {
                        _eventFlow.emit(ArticleScreenUi.ShowMessage("No internet connection"))
                        return@launch
                    }
                } else {
                    deleteArticleFromFireStore(event.article)
                }
            }
            is ArticleEvent.SearchArticle -> {
                state = state.copy(searchQuery = event.query)
                getArticles(state.searchQuery, state.articleOrder)
            }

            is ArticleEvent.RestoreNote -> {
                if (connectionStatus != ConnectivityObserver.Status.Available) {
                    viewModelScope.launch {
                        _eventFlow.emit(ArticleScreenUi.ShowMessage("No internet connection"))
                        return@launch
                    }
                } else {
                    restoreArticleToFireStore()
                }
            }
            is ArticleEvent.ToggleOrderSection -> {
                state = state.copy(isOrderSectionVisible = !state.isOrderSectionVisible)
            }

            else -> {}
        }
    }

    private fun restoreArticleToFireStore() {
        recentlyArticleDeleted?.let {
            isLoading = true
            firestore.collection("articles").document(recentlyArticleDeleted!!.fireStoreId!!)
                .set(recentlyArticleDeleted!!).addOnCompleteListener {
                    if (it.isSuccessful && it.isComplete) {
                        viewModelScope.launch {
                            articleUseCase.insertArticleUseCase(
                                recentlyArticleDeleted ?: return@launch
                            )
                            recentlyArticleDeleted = null
                        }
                    } else {
                        viewModelScope.launch {
                            _eventFlow.emit(ArticleScreenUi.ShowMessage(it.exception.toString()))
                        }
                    }
                    isLoading = false
                }
        }
    }

    private fun deleteArticleFromFireStore(article: Article) {
        isLoading = true
        firestore.collection("articles").document(article.fireStoreId!!).delete()
            .addOnCompleteListener {
                if (it.isComplete && it.isSuccessful) {
                    viewModelScope.launch {
                        articleUseCase.deleteArticleUseCase(article)
                        recentlyArticleDeleted = article
                        _eventFlow.emit(ArticleScreenUi.DeleteArticleCompleted)
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(ArticleScreenUi.ShowMessage(it.exception.toString()))
                    }
                }
                isLoading = false
            }

    }


    private fun getArticles(query: String = state.searchQuery, articleOrder: ArticleOrder) {
        getArticlesJob?.cancel()
        getArticlesJob = articleUseCase.getArticlesUseCase(query, articleOrder).onEach { articles ->
            state = state.copy(
                articles = articles,
                articleOrder = articleOrder
            )
        }
            .launchIn(viewModelScope)
    }

    sealed class ArticleScreenUi() {
        object DeleteArticleCompleted : ArticleScreenUi()
        data class ShowMessage(val message: String) : ArticleScreenUi()
    }

}