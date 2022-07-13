package com.example.summerproject.domain.use_case

import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.repository.ArticleRepository
import com.example.summerproject.domain.utils.ArticleOrder
import com.example.summerproject.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(query:String,articleOrder: ArticleOrder = ArticleOrder.Date(OrderType.Descending)): Flow<List<Article>> {
        return repository.getArticles(query).map { articles ->
            when(articleOrder.orderType){
                is OrderType.Ascending ->{
                    when(articleOrder){
                        is ArticleOrder.Title -> articles.sortedBy { it.englishTitle.lowercase() }
                        is ArticleOrder.Date -> articles.sortedBy { it.year }
                    }
                }
                is OrderType.Descending ->{
                    when(articleOrder){
                        is ArticleOrder.Title -> articles.sortedByDescending { it.englishTitle.lowercase() }
                        is ArticleOrder.Date -> articles.sortedByDescending { it.year }
                    }
                }
            }
        }

    }
}