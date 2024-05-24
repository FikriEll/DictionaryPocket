package com.fikrielg.dictionarypocket.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrielg.dictionarypocket.data.repository.AuthenticationRepository
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.rmaprojects.apirequeststate.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val repository: DictionaryRepository
) :
    ViewModel() {

    private val _editUsernameState = MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle)
    val editUsernameState = _editUsernameState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ResponseState.Idle
    )

    private val _signOutstate = MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle)
    val signOutState = _signOutstate.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ResponseState.Idle
    )


    fun onEditUsername(username: String) {
        viewModelScope.launch {
            _editUsernameState.emitAll(authenticationRepository.updateUsername(username))
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            repository.deleteAllHistory()
            _signOutstate.emitAll(authenticationRepository.signOut())
        }
    }

}