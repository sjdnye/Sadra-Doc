package com.example.summerproject.presentation.article_add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.use_case.ArticleUseCase
import com.example.summerproject.utils.ConnectivityObserver
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditArticleViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase,
    private val fireStore: FirebaseFirestore,
    private val connectivityObserver: ConnectivityObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var connectionStatus by mutableStateOf<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)

    private var currentArticleId: Int? = null

    var state by mutableStateOf(AddEditArticleState())
        private set

    var isLoading by mutableStateOf(false)
        private set


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            checkInternetConnection()
        }
        savedStateHandle.get<Article>("article")?.let { article ->
            currentArticleId = article.id
            state = state.copy(author_name_1 = article.authorName_1)
            state = state.copy(author_family_1 = article.authorFamily_1)
            state = state.copy(author_name_2 = article.authorName_2)
            state = state.copy(author_family_2 = article.authorFamily_2)
            state = state.copy(author_name_3 = article.authorName_3)
            state = state.copy(author_family_3 = article.authorFamily_3)
            state = state.copy(persianTitle = article.persianTitle)
            state = state.copy(englishTitle = article.englishTitle)
            state = state.copy(placeOfPrinting = article.placeOfPrinting)
            state = state.copy(vol = article.vol)
            state = state.copy(No = article.No)
            state = state.copy(institute = article.institute ?: "")
            state = state.copy(content = article.content)
            state = state.copy(year = article.year)
            state = state.copy(author_affiliation_1 = article.authorAffiliation_1)
            state = state.copy(author_affiliation_2 = article.authorAffiliation_2)
            state = state.copy(author_affiliation_3 = article.authorAffiliation_3)
            state = state.copy(articleTitle = article.articleTitle)
            state = state.copy(articleType = article.articleType)
            state = state.copy(fireStoreId = article.fireStoreId)
        }
    }

    private suspend fun checkInternetConnection() {
        connectivityObserver.observe().collectLatest { status ->
            connectionStatus = status
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
            is AddEditArticleEvent.ChangeAuthorAffiliation1 -> {
                state = state.copy(author_affiliation_1 = event.affiliation)
            }
            is AddEditArticleEvent.ChangeAuthorAffiliation2 -> {
                state = state.copy(author_affiliation_2 = event.affiliation)
            }
            is AddEditArticleEvent.ChangeAuthorAffiliation3 -> {
                state = state.copy(author_affiliation_3 = event.affiliation)
            }
            is AddEditArticleEvent.ChangeArticleTitle -> {
                state = state.copy(articleTitle = event.title)
            }
            is AddEditArticleEvent.ChangeArticleType -> {
                state = state.copy(articleType = event.type)
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
                if (connectionStatus != ConnectivityObserver.Status.Available){
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("No internet connection"))
                        return@launch
                    }
                }
                else if (state.persianTitle.isBlank() or state.englishTitle.isBlank()
                    or state.author_name_1.isBlank() or state.author_family_1.isBlank()
                    or state.articleTitle.isBlank() or state.articleType.isBlank()
                    or state.institute.isBlank()
                ) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("Please fill the required parameters"))
                    }
                } else if (state.year.isBlank()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("Please input the year"))
                    }
                } else {
                    val article = Article(
                        id = currentArticleId,
                        authorName_1 = state.author_name_1,
                        authorFamily_1 = state.author_family_1,
                        authorName_2 = state.author_name_2,
                        authorFamily_2 = state.author_family_2,
                        authorName_3 = state.author_name_3,
                        authorFamily_3 = state.author_family_3,
                        authorAffiliation_1 = state.author_affiliation_1,
                        authorAffiliation_2 = state.author_affiliation_2,
                        authorAffiliation_3 = state.author_affiliation_3,
                        articleTitle = state.articleTitle,
                        articleType = state.articleType,
                        persianTitle = state.persianTitle,
                        englishTitle = state.englishTitle,
                        content = state.content,
                        institute = state.institute,
                        articleAddTimeToDatabase = System.currentTimeMillis(),
                        placeOfPrinting = state.placeOfPrinting,
                        year = state.year,
                        vol = state.vol,
                        No = state.No,
                        fireStoreId = state.fireStoreId
                    )
                    if (state.fireStoreId == null) {
                        addToFireStoreDatabase(article)
                    } else {
                        updateArticleInFireStore(article)
                    }
                }
            }
        }
    }

    private fun updateArticleInFireStore(article: Article) {
        isLoading = true
        val dbCollection = fireStore.collection("articles")
        article.fireStoreId?.let { fireStoreId ->
            dbCollection.document(fireStoreId).set(article).addOnCompleteListener {
                if (it.isSuccessful && it.isComplete) {
                    viewModelScope.launch {
                        articleUseCase.insertArticleUseCase(article)
                        _eventFlow.emit(UiEvent.SaveNote)
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar(it.exception.toString()))
                    }
                }
                isLoading = false
            }
        }
    }

    private fun addToFireStoreDatabase(article: Article) {
        isLoading = true
        val dbCollection = fireStore.collection("articles")
        val tempId = dbCollection.document().id
        article.fireStoreId = tempId
        dbCollection.document(tempId).set(article).addOnCompleteListener {
            if (it.isSuccessful && it.isComplete) {
                viewModelScope.launch {
                    articleUseCase.insertArticleUseCase(article)
                    _eventFlow.emit(UiEvent.SaveNote)
                }
            } else {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowSnackBar(it.exception.toString()))
                }
            }
            isLoading = false
        }

    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}