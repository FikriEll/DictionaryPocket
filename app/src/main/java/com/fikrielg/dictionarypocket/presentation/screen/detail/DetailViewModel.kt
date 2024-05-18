package com.fikrielg.dictionarypocket.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.fikrielg.dictionarypocket.presentation.screen.home.UiState
import com.fikrielg.dictionarypocket.util.UiEvents
import com.rmaprojects.apirequeststate.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    fun getDefinition(word: String) {
        _uiState.value =
            uiState.value.copy(
                isLoading = true
            )

        viewModelScope.launch {
            repository.getDefinition(word).collect { response ->
                when (response) {
                    is ResponseState.Error -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            definition = emptyList()
                        )

                        _eventFlow.emit(
                            UiEvents.ShowSnackbar(
                                message = response.message ?: "Something went wrong!"
                            )
                        )
                    }

                    is ResponseState.Success -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            definition = response.data
                        )
                    }
                    else -> {
                        uiState
                    }
                }
            }
        }
    }
}