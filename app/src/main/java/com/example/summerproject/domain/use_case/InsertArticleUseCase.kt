package com.example.summerproject.domain.use_case

import com.example.summerproject.data.local.Article
import com.example.summerproject.data.local.InvalidInputException
import com.example.summerproject.domain.repository.ArticleRepository

class InsertArticleUseCase(
    private val repository: ArticleRepository
) {

    @Throws(InvalidInputException::class)
    suspend operator fun invoke(article:Article){
        repository.insertArticle(article)
    }
}