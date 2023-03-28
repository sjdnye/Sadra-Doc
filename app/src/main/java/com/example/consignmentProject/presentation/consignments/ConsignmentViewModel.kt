package com.example.consignmentProject.presentation.consignments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.domain.use_case.ConsignmentUseCase
import com.example.utils.CONSIGNMENT_COLLECTION
import com.example.consignmentProject.domain.utils.ConsignmentOrder
import com.example.consignmentProject.utils.ConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConsignmentViewModel @Inject constructor(
    private val consignmentUseCase: ConsignmentUseCase,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val connectivityObserver: ConnectivityObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var connectionStatus by mutableStateOf<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)

    private var publisherId: String? = null

    var refreshState by mutableStateOf(false)

    var state by mutableStateOf(ConsignmentState())
        private set

    var showAlertDialog by mutableStateOf(false)

    private var recentlyConsignmentDeleted: Consignment? = null

    private var getConsignmentJob: Job? = null

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = MutableSharedFlow<ConsignmentScreenUi>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        viewModelScope.launch {
            publisherId = firebaseAuth.uid
            checkInternetConnection()
        }
        savedStateHandle.get<Boolean>("isNewUser")?.let { isNewUser ->
            if (isNewUser) {
                viewModelScope.launch {
                    deleteAllConsignments()
                }
                refreshItemsFromServer()
                getConsignments(state.searchQuery, state.consignmentOrder)
            } else {
                getConsignments(state.searchQuery, state.consignmentOrder)
            }
        }
    }

    private suspend fun checkInternetConnection() {
        connectivityObserver.observe().collectLatest { status ->
            connectionStatus = status
        }
    }

    fun onEvent(event: ConsignmentEvent) {
        when (event) {
            is ConsignmentEvent.Order -> {
                if (state.consignmentOrder::class == event.consignmentOrder::class &&
                    state.consignmentOrder.orderType == event.consignmentOrder.orderType
                ) {
                    return
                }
                state = state.copy(consignmentOrder = event.consignmentOrder)
                getConsignments(state.searchQuery, state.consignmentOrder)
            }
            is ConsignmentEvent.DeleteConsignment -> {
                if (connectionStatus != ConnectivityObserver.Status.Available) {
                    viewModelScope.launch {
                        _eventFlow.emit(ConsignmentScreenUi.ShowMessage("No internet connection"))
                        return@launch
                    }
                } else {
                    deleteConsignmentFromFireStore(event.consignment)
                }
            }
            is ConsignmentEvent.SearchConsignment -> {
                state = state.copy(searchQuery = event.query)
                getConsignments(state.searchQuery, state.consignmentOrder)
            }

            is ConsignmentEvent.RestoreConsignment -> {
                if (connectionStatus != ConnectivityObserver.Status.Available) {
                    viewModelScope.launch {
                        _eventFlow.emit(ConsignmentScreenUi.ShowMessage("No internet connection"))
                        return@launch
                    }
                } else {
                    restoreConsignmentToFireStore()
                }
            }
            is ConsignmentEvent.ToggleOrderSection -> {
                state = state.copy(isOrderSectionVisible = !state.isOrderSectionVisible)
            }
            is ConsignmentEvent.Refresh -> {
                refreshItemsFromServer()
            }

            else -> {}
        }
    }

    private fun restoreConsignmentToFireStore() {
        recentlyConsignmentDeleted?.let {
            isLoading = true
            viewModelScope.launch(Dispatchers.IO) {
                firestore.collection(CONSIGNMENT_COLLECTION)
                    .document(recentlyConsignmentDeleted!!.firestoreId!!)
                    .set(recentlyConsignmentDeleted!!).addOnCompleteListener {
                        if (it.isSuccessful && it.isComplete) {
                            viewModelScope.launch {
                                consignmentUseCase.insertConsignmentUseCase(
                                    recentlyConsignmentDeleted ?: return@launch
                                )
                                recentlyConsignmentDeleted = null
                            }
                        } else {
                            viewModelScope.launch(Dispatchers.Main) {
                                _eventFlow.emit(ConsignmentScreenUi.ShowMessage(it.exception.toString()))
                            }
                        }
                        isLoading = false
                    }
            }
        }
    }

    private fun deleteConsignmentFromFireStore(consignment: Consignment) {
        isLoading = true
        firestore.collection(CONSIGNMENT_COLLECTION).document(consignment.firestoreId!!).delete()
            .addOnCompleteListener {
                if (it.isComplete && it.isSuccessful) {
                    viewModelScope.launch {
                        consignmentUseCase.deleteConsignmentUseCase(consignment)
                        recentlyConsignmentDeleted = consignment
                        _eventFlow.emit(ConsignmentScreenUi.DeleteConsignmentCompleted)
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(ConsignmentScreenUi.ShowMessage(it.exception.toString()))
                    }
                }
                isLoading = false
            }
    }

    private fun refreshItemsFromServer() {
        viewModelScope.launch {
            try {
                refreshState = true
                require(publisherId != null)
                val querySnapshot = firestore.collection(CONSIGNMENT_COLLECTION)
                    .whereEqualTo("publisherId", publisherId)
                    .get()
                    .await()
                val result = querySnapshot.toObjects<Consignment>() ?: emptyList()

                deleteAllConsignments()
                addAllConsignments(consignments = result)

                refreshState = false

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    refreshState = false
                    _eventFlow.emit(ConsignmentScreenUi.ShowMessage(e.message.toString()))
                }
            }
        }
    }

    private suspend fun deleteAllConsignments() {
        consignmentUseCase.deleteAllConsignmentsUseCase()
    }

    private suspend fun addAllConsignments(consignments: List<Consignment>) {
        consignmentUseCase.addAllConsignmentsUseCase(consignments = consignments)
    }


    private fun getConsignments(
        query: String = state.searchQuery,
        consignmentOrder: ConsignmentOrder
    ) {
        getConsignmentJob?.cancel()
        getConsignmentJob = consignmentUseCase.getConsignmentUseCase(query, consignmentOrder)
            .onEach { consignments ->
                state = state.copy(
                    consignments = consignments,
                    consignmentOrder = consignmentOrder
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class ConsignmentScreenUi() {
        object DeleteConsignmentCompleted : ConsignmentScreenUi()
        data class ShowMessage(val message: String) : ConsignmentScreenUi()
    }
}