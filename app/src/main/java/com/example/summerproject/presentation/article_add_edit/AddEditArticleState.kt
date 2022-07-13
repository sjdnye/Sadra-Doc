package com.example.summerproject.presentation.article_add_edit

data class AddEditArticleState(
    val author_name_1: String = "",
    val author_family_1: String = "",
    val author_name_2: String = "",
    val author_family_2: String = "",
    val author_name_3: String = "",
    val author_family_3: String = "",
    val persianTitle: String = "",
    val englishTitle: String = "",
    val placeOfPrinting: String = "",
    val year: String = "",
    val vol: String = "",
    val No: String = "",
    val institute: String = "",
    val content: String = ""
)
