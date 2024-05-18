package com.fikrielg.dictionarypocket.presentation.screen.bookmark

import StackedSnackbarDuration
import StackedSnackbarHost
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketAppBar
import com.fikrielg.dictionarypocket.presentation.screen.bookmark.component.BookmarkItem
import com.fikrielg.dictionarypocket.presentation.screen.destinations.DetailScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.HomeScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.ProfileScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.SignInScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
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
    val deleteAllBookmarkState by viewModel.deleteBookmarkState.collectAsStateWithLifecycle()

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
                        LazyColumn() {
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
                                    }, background = Color.Red.copy(alpha = 0.5f),
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

            if (showAlertDialog) {
                AlertDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.DeleteSweep,
                            contentDescription = null
                        )
                    },
                    title = { Text(text = "Delete All Bookmark") },
                    text = {
                        Text(
                            text = "Are you sure you want to delete all bookmark?",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onDismissRequest = { showAlertDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteAllBookmark()
                            }) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showAlertDialog = false
                            }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }

        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.leaveRealtimeChannel() }
    }
}