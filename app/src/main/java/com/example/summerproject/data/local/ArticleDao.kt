package com.example.summerproject.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article where LOWER(englishTitle) like '%' || LOWER(:query) || '%' ")
    fun getArticles(query: String): Flow<List<Article>>

    @Query("SELECT * FROM article where id = :id")
    suspend fun getArticleById(id: Int): Article?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article:Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM article WHERE year = :year ORDER BY englishTitle ASC")
    suspend fun getArticlesByYear(year: String): List<Article>?
}