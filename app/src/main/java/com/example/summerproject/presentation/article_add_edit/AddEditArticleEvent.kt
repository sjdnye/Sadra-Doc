package com.example.summerproject.presentation.article_add_edit

sealed class AddEditArticleEvent{
    data class ChangeAuthorName1(val name:String): AddEditArticleEvent()
    data class ChangeAuthorName2(val name:String): AddEditArticleEvent()
    data class ChangeAuthorName3(val name:String): AddEditArticleEvent()
    data class ChangeAuthorFamily1(val name:String): AddEditArticleEvent()
    data class ChangeAuthorFamily2(val name:String): AddEditArticleEvent()
    data class ChangeAuthorFamily3(val name:String): AddEditArticleEvent()
    data class ChangeAuthorAffiliation1(val affiliation: String): AddEditArticleEvent()
    data class ChangeAuthorAffiliation2(val affiliation: String): AddEditArticleEvent()
    data class ChangeAuthorAffiliation3(val affiliation: String): AddEditArticleEvent()
    data class ChangeArticleType(val type: String): AddEditArticleEvent()
    data class ChangeArticleTitle(val title : String): AddEditArticleEvent()
    data class ChangePersianTitle(val name:String): AddEditArticleEvent()
    data class ChangeEnglishTitle(val name:String): AddEditArticleEvent()
    data class ChangePlaceOfPrinting(val name:String): AddEditArticleEvent()
    data class ChangeInstitute(val institute:String): AddEditArticleEvent()
    data class ChangeContent(val content:String): AddEditArticleEvent()
    data class ChangeVOL(val number:String): AddEditArticleEvent()
    data class ChangeNO(val Number:String): AddEditArticleEvent()
    data class ChangeYear(val Number:String): AddEditArticleEvent()
    object SaveArticle : AddEditArticleEvent()

}