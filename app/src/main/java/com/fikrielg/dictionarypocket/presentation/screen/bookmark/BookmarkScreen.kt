package com.fikrielg.dictionarypocket.presentation.screen.bookmark

import StackedSnackbarAnimation
import StackedSnackbarDuration
import StackedSnackbarHost
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fikrielg.dictionarypocket.presentation.component.DialogMessage
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketAppBar
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketCustomDialog
import com.fikrielg.dictionarypocket.presentation.screen.bookmark.component.BookmarkItem
import com.fikrielg.dictionarypocket.presentation.screen.destinations.DetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rmaprojects.apirequeststate.ResponseState
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import rememberStackedSnackbarHostState

@SuppressLint("CoroutineCreationDuringComposition")
@Destination
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteBookmarkState by viewModel.deleteBookmarkState.collectAsStateWithLifecycle()
    val deleteAllBookmarkState by viewModel.deleteAllBookmarkState.collectAsStateWithLifecycle()

    val stackedSnackbarHostState = rememberStackedSnackbarHostState(
        animation = StackedSnackbarAnimation.Slide
    )

    var showAlertDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.connectToRealtime()
    }

    LaunchedEffect(deleteBookmarkState) {
        when (val state = deleteBookmarkState) {
            is ResponseState.Loading -> {

            }
            is ResponseState.Success -> {
                stackedSnackbarHostState.showSuccessSnackbar(
                    title = "Bookmark Deleted Successfully!",
                    duration = StackedSnackbarDuration.Short
                )
            }
            is ResponseState.Error -> {
                stackedSnackbarHostState.showErrorSnackbar(
                    title = "Error Deleted Bookmark!",
                    description = state.message,
                    duration = StackedSnackbarDuration.Long
                )
            }
            else -> Unit
        }
    }

    LaunchedEffect(deleteAllBookmarkState) {
        when (val state = deleteAllBookmarkState) {
            is ResponseState.Loading -> {

            }
            is ResponseState.Success -> {
                stackedSnackbarHostState.showSuccessSnackbar(
                    title = "All Bookmark Deleted Successfully!",
                    duration = StackedSnackbarDuration.Short
                )
            }
            is ResponseState.Error -> {
                stackedSnackbarHostState.showErrorSnackbar(
                    title = "Error Deleted All Bookmark!",
                    description = state.message,
                    duration = StackedSnackbarDuration.Long
                )
            }
            else -> Unit
        }
    }


    Scaffold(
        snackbarHost = { StackedSnackbarHost(hostState = stackedSnackbarHostState) },
        topBar = {
            DictionaryPocketAppBar(
                currentDestinationTitle = "Bookmark",
                navigateUp = { navigator.popBackStack() },
                actions = {
                    val bookmarkList = (uiState as? ResponseState.Success)?.data
                    if (bookmarkList?.isNotEmpty() == true){
                        IconButton(onClick = {
                            showAlertDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Delete All Bookmark",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            uiState.DisplayResult(
                onLoading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                onSuccess = { bookmarkList ->
                    if (bookmarkList.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Bookmark belum ditambahkan")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            items(bookmarkList) { bookmark ->
                                val delete = SwipeAction(
                                    onSwipe = {
                                        viewModel.deleteBookmark(bookmark.id.toString())
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete History",
                                            modifier = Modifier.padding(16.dp),
                                            tint = Color.White
                                        )
                                    },
                                    background = Color.Red.copy(alpha = 0.5f),
                                    isUndo = true
                                )
                                SwipeableActionsBox(
                                    modifier = Modifier,
                                    swipeThreshold = 100.dp,
                                    endActions = listOf(delete),
                                ) {
                                    BookmarkItem(
                                        word = bookmark.word,
                                        onClickToDetail = {
                                            navigator.navigate(
                                                DetailScreenDestination(word = bookmark.word)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                onError = { message ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(message)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.connectToRealtime() }) {
                            Text(text = "Retry?")
                        }
                    }
                }
            )

            DictionaryPocketCustomDialog(
                showDialog = showAlertDialog,
                onDismissRequest = { showAlertDialog = false }
            ) {
                DialogMessage(
                    onDismissRequest = { showAlertDialog = false },
                    title = "Delete All Bookmark",
                    message = "Are you sure you want to delete all bookmark?",
                    dismissText = "Cancel",
                    confirmText = "Confirm",
                    icon = Icons.Default.DeleteSweep,
                    onConfirmRequest = {
                        showAlertDialog = false
                        viewModel.deleteAllBookmark()
                    },
                )
            }

        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.leaveRealtimeChannel() }
    }
}