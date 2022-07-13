package com.example.summerproject.domain.use_case

import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.repository.ArticleRepository

class DeleteArticleUseCase(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(article:Article){
        repository.deleteArticle(article)
    }
}