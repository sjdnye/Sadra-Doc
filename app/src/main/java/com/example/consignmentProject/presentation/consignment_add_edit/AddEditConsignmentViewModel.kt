package com.example.consignmentProject.presentation.consignment_add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.use_case.ConsignmentUseCase
import com.example.consignmentProject.utils.ConnectivityObserver
import com.example.utils.CONSIGNMENT_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditConsignmentViewModel @Inject constructor(
    private val consignmentUseCase: ConsignmentUseCase,
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val connectivityObserver: ConnectivityObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var connectionStatus by mutableStateOf<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)

    private var currentConsignmentId: Int? = null

    var state by mutableStateOf(AddEditConsignmentState())
        private set

    var isLoading by mutableStateOf(false)
        private set


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            checkInternetConnection()
        }
        savedStateHandle.get<Consignment>("consignment")?.let { consignment ->
            currentConsignmentId = consignment.id
            state = state.copy(title = consignment.title)
            state = state.copy(weight = consignment.weight.toString())
            state = state.copy(cost = consignment.cost.toString())
            state = state.copy(documentNumber = consignment.documentNumber.toString())
            state = state.copy(year = consignment.year.toString())
            state = state.copy(month = consignment.month.toString())
            state = state.copy(day = consignment.day.toString())
            state = state.copy(fireStoreId = consignment.firestoreId)
        }
    }

    private suspend fun checkInternetConnection() {
        connectivityObserver.observe().collectLatest { status ->
            connectionStatus = status
        }
    }


    fun onEvent(event: AddEditConsignmentEvent) {
        when (event) {
            is AddEditConsignmentEvent.ChangeTitle -> {
                state = state.copy(title = event.title)
            }
            is AddEditConsignmentEvent.ChangeWeight -> {
                state = state.copy(weight = event.weight)
            }
            is AddEditConsignmentEvent.ChangeCost -> {
                state = state.copy(cost = event.cost)
            }
            is AddEditConsignmentEvent.ChangeDocumentNumber -> {
                state = state.copy(documentNumber = event.documentNumber)
            }
            is AddEditConsignmentEvent.ChangeYear -> {
                state = state.copy(year = event.year)
            }
            is AddEditConsignmentEvent.ChangeMonth -> {
                state = state.copy(month = event.month)
            }
            is AddEditConsignmentEvent.ChangeDay -> {
                state = state.copy(day = event.day)
            }

            is AddEditConsignmentEvent.SaveConsignment -> {
                if (connectionStatus != ConnectivityObserver.Status.Available) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("No internet connection"))
                        return@launch
                    }
                } else if (state.title.isBlank() or state.weight.isBlank()
                    or state.cost.isBlank() or state.documentNumber.isBlank()
                    or state.year.isBlank() or state.month.isBlank()
                    or state.day.isBlank()
                ) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("Please fill the required parameters"))
                    }

                } else {
                    try {
                        val month = state.month.toInt()
                        val day = state.day.toInt()
                        if (month > 12 || month < 1 || day > 31 || day < 1){
                            throw Exception("Month or day is not in valid range!!")
                        }
                        val consignment = Consignment(
                            id = currentConsignmentId,
                            title = state.title,
                            weight = state.weight.toDoubleOrNull(),
                            cost = state.cost.toLong(),
                            documentNumber = state.documentNumber.toLong(),
                            consignmentAddTimeToDatabase = System.currentTimeMillis(),
                            year = state.year.toInt(),
                            month = state.month.toInt(),
                            day = state.day.toInt(),
                            firestoreId = state.fireStoreId,
                            publisherId = firebaseAuth.uid!!
                        )
                        if (state.fireStoreId == null) {
                            addToFireStoreDatabase(consignment)
                        } else {
                            updateConsignmentInFireStore(consignment)
                        }
                    } catch (e: Exception) {
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.ShowSnackBar(e.message.toString()))
                        }
                    }
                }
            }
        }
    }

    private fun updateConsignmentInFireStore(consignment: Consignment) {
        isLoading = true
        val dbCollection = fireStore.collection(CONSIGNMENT_COLLECTION)
        consignment.firestoreId?.let { fireStoreId ->
            dbCollection.document(fireStoreId).set(consignment).addOnCompleteListener {
                if (it.isSuccessful && it.isComplete) {
                    viewModelScope.launch {
                        consignmentUseCase.insertConsignmentUseCase(consignment)
                        _eventFlow.emit(UiEvent.SaveConsignment)
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

    private fun addToFireStoreDatabase(consignment: Consignment) {
        isLoading = true
        val dbCollection = fireStore.collection(CONSIGNMENT_COLLECTION)
        val tempId = dbCollection.document().id
        consignment.firestoreId = tempId
        dbCollection.document(tempId).set(consignment).addOnCompleteListener {
            if (it.isSuccessful && it.isComplete) {
                viewModelScope.launch {
                    consignmentUseCase.insertConsignmentUseCase(consignment)
                    _eventFlow.emit(UiEvent.SaveConsignment)
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
        object SaveConsignment : UiEvent()
    }
}