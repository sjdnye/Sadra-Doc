package com.example.summerproject.presentation.authentication_screen

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var registerLoading by mutableStateOf(false)
    private val _registerEventFlow = MutableSharedFlow<AuthenticationUi>()
    val registerEventFlow = _registerEventFlow.asSharedFlow()

    var loginLoading by mutableStateOf(false)
    private val _loginEventFlow = MutableSharedFlow<AuthenticationUi>()
    val loginEventFlow = _loginEventFlow.asSharedFlow()

    private val _mainAuthentication = MutableSharedFlow<AuthenticationUi>()
    val mainAuthentication = _mainAuthentication.asSharedFlow()


    fun createNewAccount(password: String, confirmPassWord: String, email: String) {
        if (password.isNotBlank() && confirmPassWord.isNotBlank() &&
            password.trim() == confirmPassWord.trim() &&
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        ) {
            registerLoading = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.isComplete) {
                        viewModelScope.launch {
                            _registerEventFlow.emit(AuthenticationUi.NavigateToMainScreen)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        _registerEventFlow.emit(AuthenticationUi.ShowMessage(it.exception.toString()))
                    }
                }
                registerLoading = false
            }
        } else {
            viewModelScope.launch {
                _registerEventFlow.emit(AuthenticationUi.ShowMessage("Enter password or email correctly!!"))
            }
        }
    }

    fun loginUser(password: String, email: String) {
        if (password.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        ) {
            loginLoading = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.isComplete) {
                        viewModelScope.launch {
                            _loginEventFlow.emit(AuthenticationUi.NavigateToMainScreen)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        _loginEventFlow.emit(AuthenticationUi.ShowMessage(it.exception.toString()))
                    }
                }
                loginLoading = false
            }
        } else {
            viewModelScope.launch {
                _loginEventFlow.emit(AuthenticationUi.ShowMessage("Enter password or email correctly!!"))
            }
        }
    }

    fun onStart() {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                _mainAuthentication.emit(AuthenticationUi.NavigateToMainScreen)
            }
        }
    }

    fun onStop() {

    }

    init {

    }


    sealed class AuthenticationUi {
        object NavigateToMainScreen : AuthenticationUi()
        data class ShowMessage(val message: String) : AuthenticationUi()
    }
}