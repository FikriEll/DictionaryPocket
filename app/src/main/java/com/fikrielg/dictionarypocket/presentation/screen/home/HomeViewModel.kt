package com.fikrielg.dictionarypocket.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.DictionaryResponseModel
import com.fikrielg.dictionarypocket.util.Resource
import com.fikrielg.dictionarypocket.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DictionaryRepository) :
    ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _typedWord = mutableStateOf("")
    val typedWord: State<String> = _typedWord
    fun setTypedWord(typedWord: String) {
        _typedWord.value = typedWord
    }

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    val historyState = repository.getHistoryList().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500),
        emptyList()
    )

    fun deleteHistory(history: History){
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }


    fun getDefinition() {
        _homeUiState.value =
            homeUiState.value.copy(
                isLoading = true
            )

        val word = typedWord.value

        if (word.isNotEmpty()) {
            viewModelScope.launch {
                repository.getDefinition(word = word).collect { response ->
                    when (response) {
                        is Resource.Error -> {
                            _homeUiState.value = homeUiState.value.copy(
                                isLoading = false,
                                definition = emptyList()
                            )

                            _eventFlow.emit(
                                UiEvents.ShowSnackbar(
                                    message = response.message ?: "Something went wrong!"
                                )
                            )
                        }
                        is Resource.Success -> {
                            _homeUiState.value = homeUiState.value.copy(
                                isLoading = false,
                                definition = response.data
                            )
                            viewModelScope.launch {
                                repository.addHistory(
                                    History(
                                        id = null,
                                        meanings = response.data?.get(0)?.meanings,
                                        origin = response.data?.get(0)?.origin,
                                        phonetic  = response.data?.get(0)?.phonetic,
                                        phonetics = response.data?.get(0)?.phonetics,
                                        word = response.data?.get(0)?.word
                                    )
                                )
                            }
                        }
                        else -> {
                            homeUiState
                        }
                    }
                }
            }
        } else {
            showErrorMessage()
        }
    }

    private fun showErrorMessage() {
        viewModelScope.launch {
            _homeUiState.value =
                homeUiState.value.copy(
                    isLoading = false
                )

            _eventFlow.emit(
                UiEvents.ShowSnackbar(
                    message = "Please enter a word"
                )
            )
        }
    }
}


data class HomeUiState(
    val definition: List<DictionaryResponseModel>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

