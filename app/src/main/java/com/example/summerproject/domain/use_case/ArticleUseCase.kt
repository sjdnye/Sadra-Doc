package com.example.summerproject.domain.use_case

data class ArticleUseCase(
    val deleteArticleUseCase: DeleteArticleUseCase,
    val insertArticleUseCase: InsertArticleUseCase,
    val getArticlesUseCase: GetArticlesUseCase,
    val getArticleByIdUseCase: GetArticleByIdUseCase
)