package com.example.summerproject.presentation.SplashScreen_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var state by mutableStateOf<Int>(0)

    init {
        viewModelScope.launch {
            delay(1000L)
            state = if (auth.currentUser != null) {
                2
            }else{
                1
            }
        }

    }
}