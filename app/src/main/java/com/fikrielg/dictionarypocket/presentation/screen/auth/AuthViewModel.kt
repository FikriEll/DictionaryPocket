package com.fikrielg.dictionarypocket.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrielg.dictionarypocket.data.repository.AuthenticationRepository
import com.rmaprojects.apirequeststate.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle)
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ResponseState.Idle
    )

    fun onSignUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _state.emitAll(repository.signUp(username, email, password))
        }
    }

    fun onSignIn(email: String, password: String) {
        viewModelScope.launch {
            _state.emitAll(repository.signIn(email, password))
        }
    }

}
