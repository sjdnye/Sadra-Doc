package com.example.summerproject.presentation.article_add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.use_case.ArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditArticleViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private var currentArticleId: Int? = null

    var state by mutableStateOf(AddEditArticleState())
        private set


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Article>("article")?.let { article ->
            currentArticleId = article.id
            state = state.copy(author_name_1 = article.authorName_1)
            state = state.copy(author_family_1 = article.authorFamily_1)
            state = state.copy(author_name_2 = article.authorName_2 )
            state = state.copy(author_family_2 = article.authorFamily_2 )
            state = state.copy(author_name_3 = article.authorName_3 )
            state = state.copy(author_family_3 = article.authorFamily_3 )
            state = state.copy(persianTitle = article.persianTitle)
            state = state.copy(englishTitle = article.englishTitle)
            state = state.copy(placeOfPrinting = article.placeOfPrinting)
            state = state.copy(vol = article.vol)
            state = state.copy(No = article.No)
            state = state.copy(institute = article.institute ?: "")
            state = state.copy(content = article.content)
            state = state.copy(year = article.year)
        }
    }


    fun onEvent(event: AddEditArticleEvent) {
        when (event) {
            is AddEditArticleEvent.ChangeAuthorName1 -> {
                state = state.copy(author_name_1 = event.name)
            }
            is AddEditArticleEvent.ChangeAuthorFamily1 -> {
                state = state.copy(author_family_1 = event.name)
            }
            is AddEditArticleEvent.ChangeAuthorName2 -> {
                state = state.copy(author_name_2 = event.name)
            }
            is AddEditArticleEvent.ChangeAuthorFamily2 -> {
                state = state.copy(author_family_2 = event.name)
            }
            is AddEditArticleEvent.ChangeAuthorName3 -> {
                state = state.copy(author_name_3 = event.name)
            }
            is AddEditArticleEvent.ChangeAuthorFamily3 -> {
                state = state.copy(author_family_3 = event.name)
            }
            is AddEditArticleEvent.ChangePersianTitle -> {
                state = state.copy(persianTitle = event.name)
            }
            is AddEditArticleEvent.ChangeEnglishTitle -> {
                state = state.copy(englishTitle = event.name)
            }
            is AddEditArticleEvent.ChangeInstitute -> {
                state = state.copy(institute = event.institute)
            }
            is AddEditArticleEvent.ChangePlaceOfPrinting -> {
                state = state.copy(placeOfPrinting = event.name)
            }
            is AddEditArticleEvent.ChangeVOL -> {
                state = state.copy(vol = event.number)
            }
            is AddEditArticleEvent.ChangeNO -> {
                state = state.copy(No = event.Number)
            }
            is AddEditArticleEvent.ChangeContent -> {
                state = state.copy(content = event.content)
            }
            is AddEditArticleEvent.ChangeYear -> {
                state = state.copy(year = event.Number)
            }
            is AddEditArticleEvent.SaveArticle -> {
                if (state.persianTitle.isBlank() or state.englishTitle.isBlank()
                    or state.author_name_1.isBlank() or state.author_family_1.isBlank()
                ){
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("Please fill the parameters correctly"))
                    }
                }else{
                    viewModelScope.launch {
                        articleUseCase.insertArticleUseCase(
                            Article(
                                id = currentArticleId,
                                authorName_1 = state.author_name_1,
                                authorFamily_1 = state.author_family_1,
                                authorName_2 = state.author_name_2,
                                authorFamily_2 = state.author_family_2,
                                authorName_3 = state.author_name_3,
                                authorFamily_3 = state.author_family_3,
                                persianTitle = state.persianTitle,
                                englishTitle = state.englishTitle,
                                content = state.content,
                                institute = state.institute,
                                articleAddTimeToDatabase = System.currentTimeMillis(),
                                placeOfPrinting = state.placeOfPrinting,
                                year = state.year,
                                vol = state.vol,
                                No = state.No
                            )
                        )

                        _eventFlow.emit(UiEvent.SaveNote)
                    }
                }
            }


        }
    }


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}