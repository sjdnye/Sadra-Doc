package com.example.summerproject.presentation.articles

import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.utils.ArticleOrder

sealed class ArticleEvent {
    data class Order(val articleOrder: ArticleOrder) : ArticleEvent()
    data class DeleteNote(val article:Article): ArticleEvent()
    data class SearchArticle(val query: String): ArticleEvent()
    object RestoreNote : ArticleEvent()
    object ToggleOrderSection: ArticleEvent()
    object LogOut: ArticleEvent()
}