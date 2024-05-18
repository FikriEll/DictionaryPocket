package com.fikrielg.dictionarypocket.presentation.screen.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.rmaprojects.apirequeststate.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: DictionaryRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<ResponseState<List<Bookmark>>>(
        ResponseState.Idle
    )

    val uiState = _uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ResponseState.Loading
    )

    private val _deleteBookmarkState = MutableStateFlow<ResponseState<Boolean>>(
        ResponseState.Idle
    )

    val deleteBookmarkState = _deleteBookmarkState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ResponseState.Loading
    )

    private val _deleteAllBookmarkState = MutableStateFlow<ResponseState<Boolean>>(
        ResponseState.Idle
    )

    val deleteAllBookmarkState = _deleteAllBookmarkState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ResponseState.Loading
    )


    fun connectToRealtime() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(ResponseState.Loading)
            try {
                repository.getBookmarkList()
                    .onSuccess { flow ->
                        flow.onEach {
                            _uiState.emit(ResponseState.Success(it))
                        }.collect()
                    }
                    .onFailure {
                        _uiState.emit(ResponseState.Error(it.message.toString()))
                    }
            } catch (e: Exception){
                Log.d("Get Bookmark List Error", e.message.toString())
            }
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch() {
            _deleteBookmarkState.emitAll(
                repository.deleteBookmark(id)
            )
        }
    }

    fun deleteAllBookmark() {
        viewModelScope.launch() {
            _deleteAllBookmarkState.emitAll(
                repository.deleteAllBookmark()
            )
        }
    }

    fun leaveRealtimeChannel() = viewModelScope.launch {
        repository.unsubscribeChannel()
    }


}


