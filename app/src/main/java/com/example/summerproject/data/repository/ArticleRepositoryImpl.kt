package com.example.summerproject.data.repository

import com.example.summerproject.data.local.Article
import com.example.summerproject.data.local.ArticleDao
import com.example.summerproject.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class ArticleRepositoryImpl(
    private val dao: ArticleDao
) : ArticleRepository {
    override fun getArticles(query: String): Flow<List<Article>> {
        return dao.getArticles(query)
    }

    override suspend fun getArticleById(id: Int): Article? {
        return dao.getArticleById(id)
    }

    override suspend fun insertArticle(article: Article) {
        dao.insertArticle(article)
    }

    override suspend fun deleteArticle(article: Article) {
        dao.deleteArticle(article)
    }

    override suspend fun getArticlesByYear(year: String): List<Article>? {
       return dao.getArticlesByYear(year)
    }
}