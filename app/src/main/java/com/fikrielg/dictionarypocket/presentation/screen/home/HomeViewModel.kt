package com.fikrielg.dictionarypocket.presentation.screen.home

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.fikrielg.dictionarypocket.data.repository.KbbiRepository
import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.model.DictionaryResponse
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.fikrielg.dictionarypocket.data.source.remote.model.KbbiResponse
import com.fikrielg.dictionarypocket.util.UiEvents
import com.rmaprojects.apirequeststate.ResponseState
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
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DictionaryRepository,
    private val kkbiRepository: KbbiRepository
) :
    ViewModel() {

    private val _definitionsState = MutableStateFlow(DefinitionsState())
    val definitionsState: StateFlow<DefinitionsState> = _definitionsState.asStateFlow()

    private val _meaningsKbbiState = MutableStateFlow(MeaningsKbbiState())
    val meaningsKbbiState: StateFlow<MeaningsKbbiState> = _meaningsKbbiState.asStateFlow()

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


    fun deleteHistory(history: History) {
        viewModelScope.launch {
            try {
                _eventFlow.emit(
                    UiEvents.ShowSuccesSnackbar(
                        message = "Successful deleting from history"
                    )
                )
                repository.deleteHistory(history)

            } catch (e: Exception) {
                Log.e("Error Delete History", "Error deleting history", e)
            }
        }
    }

    private var textToSpeech: TextToSpeech? = null


    fun getDefinition() {
        _definitionsState.value =
            definitionsState.value.copy(
                isLoading = true
            )

        val word = typedWord.value

        if (word.isNotEmpty()) {
            viewModelScope.launch {
                repository.getDefinition(word = word).collect { response ->
                    when (response) {
                        is ResponseState.Error -> {
                            _definitionsState.value = definitionsState.value.copy(
                                isLoading = false,
                                definition = emptyList()
                            )

                            _eventFlow.emit(
                                UiEvents.ShowErrorSnackbar(
                                    message = response.message ?: "Something went wrong!"
                                )
                            )
                        }

                        is ResponseState.Success -> {
                            _definitionsState.value = definitionsState.value.copy(
                                isLoading = false,
                                definition = response.data
                            )
                            viewModelScope.launch {
                                val history = response.data[0]
                                repository.addHistory(
                                    History(
                                        id = null,
                                        meanings = history.meanings,
                                        origin = history.origin,
                                        phonetic = history.phonetic,
                                        phonetics = history.phonetics,
                                        word = history.word
                                    )
                                )
                            }
                        }

                        else -> {
                            definitionsState
                        }
                    }
                }
            }
        } else {
            showErrorMessage()
        }
    }

    fun getMeaningsKbbi() {
        _meaningsKbbiState.value =
            meaningsKbbiState.value.copy(
                isLoading = true
            )

        val word = typedWord.value

        if (word.isNotEmpty()) {
            viewModelScope.launch {
                kkbiRepository.getMeaningsKbbi(kata = word).collect { response ->
                    when (response) {
                        is ResponseState.Error -> {
                            _meaningsKbbiState.value = meaningsKbbiState.value.copy(
                                isLoading = false,
                                meanings = null
                            )

                            _eventFlow.emit(
                                UiEvents.ShowErrorSnackbar(
                                    message = response.message ?: "Something went wrong!"
                                )
                            )
                        }

                        is ResponseState.Success -> {
                            _meaningsKbbiState.value = meaningsKbbiState.value.copy(
                                isLoading = false,
                                meanings = response.data
                            )
                        }

                        else -> {
                            definitionsState
                        }
                    }
                }
            }
        } else {
            showErrorMessage()
        }
    }

    fun addBookmark(bookmarks: Bookmark) {
        viewModelScope.launch {
            try {
                repository.addBookmark(bookmarks).collect {
                    when (it) {
                        is ResponseState.Success -> {
                            Log.d("Success Add Bookmarks", "Successful adding to bookmarks")
                            _eventFlow.emit(
                                UiEvents.ShowSuccesSnackbar(
                                    message = "Successful adding to bookmarks"
                                )
                            )
                        }
                        is ResponseState.Error -> {
                            _eventFlow.emit(
                                UiEvents.ShowInfoSnackbar(
                                    message = "The word is already in the bookmarks"
                                )
                            )                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                Log.d("Error Add Bookmark", "Error adding bookmark", e)
                _eventFlow.emit(
                    UiEvents.ShowErrorSnackbar(
                        message = e.message.toString()
                    )
                )
            }
        }
    }

    fun textToSpeech(context: Context, text: String) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                textToSpeech?.setSpeechRate(1.0f)
                textToSpeech?.speak(
                    text,
                    TextToSpeech.QUEUE_ADD,
                    null,
                    null
                )
            }
        }
    }


    private fun showErrorMessage() {
        viewModelScope.launch {
            _definitionsState.value =
                definitionsState.value.copy(
                    isLoading = false
                )

            _eventFlow.emit(
                UiEvents.ShowInfoSnackbar(
                    message = "Please enter a word"
                )
            )
        }
    }


}


data class DefinitionsState(
    val definition: List<DictionaryResponse>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class MeaningsKbbiState(
    val meanings: KbbiResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)


