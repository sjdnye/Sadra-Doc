package com.example.summerproject.presentation.articles

import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.utils.ArticleOrder
import com.example.summerproject.domain.utils.OrderType

data class ArticleState(
    val searchQuery: String = "",
    val articles: List<Article> = emptyList(),
    val articleOrder: ArticleOrder = ArticleOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)