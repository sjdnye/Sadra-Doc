package com.example.summerproject.domain.repository

import com.example.summerproject.data.local.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getArticles(query: String): Flow<List<Article>>

    suspend fun getArticleById(id: Int): Article?

    suspend fun insertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

}