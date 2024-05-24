package com.fikrielg.dictionarypocket.presentation.screen.home

import StackedSnackbarAnimation
import StackedSnackbarDuration
import StackedSnackbarHost
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.data.kotpref.DictionaryPocketPref
import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.fikrielg.dictionarypocket.data.source.remote.model.Meaning
import com.fikrielg.dictionarypocket.data.source.remote.model.Phonetic
import com.fikrielg.dictionarypocket.presentation.component.DialogMessage
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketCustomDialog
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketUserAvatar
import com.fikrielg.dictionarypocket.presentation.screen.destinations.BookmarkScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.ProfileScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.TranslateScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.home.component.DefinitionsEmptyComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.DefinitionsLoadingComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.HistoryItem
import com.fikrielg.dictionarypocket.presentation.screen.home.component.MeaningKbbiItem
import com.fikrielg.dictionarypocket.presentation.screen.home.component.MeaningsKbbiEmptyComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.MeaningsKbbiLoadingComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.PartsOfSpeechDefinitionsItem
import com.fikrielg.dictionarypocket.presentation.screen.home.component.PronunciationItem
import com.fikrielg.dictionarypocket.presentation.screen.home.component.SearchTextFieldComponent
import com.fikrielg.dictionarypocket.ui.theme.montserrat
import com.fikrielg.dictionarypocket.util.GlobalState
import com.fikrielg.dictionarypocket.util.UiEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import rememberStackedSnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()

    val stackedSnackbarHostState = rememberStackedSnackbarHostState(
        animation = StackedSnackbarAnimation.Slide
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowSuccesSnackbar -> {
                    scope.launch {
                        stackedSnackbarHostState.showSuccessSnackbar(
                            event.message,
                            duration = StackedSnackbarDuration.Short
                        )
                    }
                }

                is UiEvents.ShowErrorSnackbar -> {
                    scope.launch {
                        stackedSnackbarHostState.showErrorSnackbar(
                            event.message,
                            duration = StackedSnackbarDuration.Short
                        )
                    }
                }

                is UiEvents.ShowInfoSnackbar -> {
                    scope.launch {
                        stackedSnackbarHostState.showInfoSnackbar(
                            event.message,
                            duration = StackedSnackbarDuration.Short
                        )
                    }
                }

                else -> {}
            }
        }
    }

    val definitionsState = viewModel.definitionsState.collectAsState().value
    val meaningsKbbiState = viewModel.meaningsKbbiState.collectAsState().value
    val typedWord = viewModel.typedWord.value
    val definitions =
        if (definitionsState.definition?.isNotEmpty() == true) definitionsState.definition[0].meanings
            ?: emptyList()
        else emptyList()
    val historyList = viewModel.historyState.collectAsStateWithLifecycle().value
    val context = LocalContext.current


    Scaffold(
        snackbarHost = { StackedSnackbarHost(hostState = stackedSnackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
                title = {
                    val titleHome = if (!GlobalState.isKBBIPocket) {
                        buildAnnotatedString {
                            append("Dictionary Pocket")
                        }
                    } else {
                        buildAnnotatedString {
                            append("KBBI Pocket ")
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("BETA")
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = titleHome,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = modifier.width(10.dp))
                        IconButton(
                            modifier = modifier.size(20.dp),
                            onClick = {
                                    DictionaryPocketPref.isKBBIPocketMode =
                                        !DictionaryPocketPref.isKBBIPocketMode
                                    GlobalState.isKBBIPocket = DictionaryPocketPref.isKBBIPocketMode
                            }) {
                            Icon(
                                imageVector = Icons.Default.Autorenew,
                                contentDescription = "Change to KBBI Pocket",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                actions = {
                    if (!GlobalState.isKBBIPocket) {
                        IconButton(onClick = { navigator.navigate(BookmarkScreenDestination) }) {
                            Icon(
                                imageVector = Icons.Default.Bookmarks,
                                contentDescription = "Bookmark",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                navigationIcon = {
                    DictionaryPocketUserAvatar(
                        fullName = AuthPref.username,
                        size = 36.dp,
                        onClickToProfile = {
                            navigator.navigate(ProfileScreenDestination)
                        })
                }
            )
        }
    ) { paddingValues ->
        HomeContent(
            definitionsState = definitionsState,
            meaningsKbbiState = meaningsKbbiState,
            typedWord = typedWord,
            setWordToBeSearched = { word ->
                viewModel.setTypedWord(typedWord = word)
            },
            searchWord = {
                if (!GlobalState.isKBBIPocket) viewModel.getDefinition() else viewModel.getMeaningsKbbi()
            },
            meanings = definitions,
            paddingValues = paddingValues,
            historyList = historyList,
            onSwipeToDelete = viewModel::deleteHistory,
            onSwipeToAddBookmark = { history ->
                viewModel.addBookmark(
                    Bookmark(
                        userId = AuthPref.id,
                        word = history.word ?: "",
                    )
                )
            },
            onClickToListen = { text ->
                viewModel.textToSpeech(context, text)
            },
            onClickToAddBookmark = { word ->
                viewModel.addBookmark(
                    Bookmark(
                        userId = AuthPref.id,
                        word = word,
                    )
                )
            },
            onClickToGTranslate = { word ->
                navigator.navigate(TranslateScreenDestination(word))
            },
        )
    }

}


@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    definitionsState: DefinitionsState,
    meaningsKbbiState: MeaningsKbbiState,
    typedWord: String,
    setWordToBeSearched: (String) -> Unit,
    searchWord: () -> Unit,
    meanings: List<Meaning>,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    historyList: List<History>,
    onSwipeToDelete: (History) -> Unit,
    onSwipeToAddBookmark: (History) -> Unit,
    onClickToListen: (String) -> Unit,
    onClickToAddBookmark: (String) -> Unit,
    onClickToGTranslate: (String) -> Unit,
) {

    var isSearchEmpty by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {


        Column(modifier = modifier.padding(vertical = 6.dp, horizontal = 20.dp)) {

            Text(
                text = if (!GlobalState.isKBBIPocket) "Hello ${AuthPref.username}!👋" else "Hai ${AuthPref.username}!👋",
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if (!GlobalState.isKBBIPocket) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = modifier.height(6.dp))
            Text(
                text = if (!GlobalState.isKBBIPocket) "What meaning do you want to know today?" else "Apa arti yang ingin kamu ketahui hari ini?",
                fontFamily = montserrat,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = modifier.height(12.dp))
            SearchTextFieldComponent(
                typedWord = typedWord,
                setWordToBeSearched = {
                    isSearchEmpty = it.isEmpty()
                    setWordToBeSearched(it)
                },
                searchWord = searchWord,
            )
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 14.dp, start = 14.dp, end = 14.dp)) {
            if (isSearchEmpty && !GlobalState.isKBBIPocket) {
                item {
                    Text(
                        text = "History",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            fontFamily = montserrat,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                    )
                }
                items(historyList) { history ->
                    val delete = SwipeAction(
                        onSwipe = {
                            onSwipeToDelete(history)
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
                    val bookmark = SwipeAction(
                        onSwipe = {
                            onSwipeToAddBookmark(history)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = "Bookmark History",
                                modifier = Modifier.padding(16.dp),
                                tint = Color.White
                            )
                        }, background = Color(0xFF50B384).copy(alpha = 0.7f)
                    )
                    SwipeableActionsBox(
                        modifier = Modifier,
                        swipeThreshold = 100.dp,
                        endActions = listOf(delete),
                        startActions = listOf(bookmark)
                    ) {
                        HistoryItem(
                            history = history,
                            phonetic = getNonNullPhonetic(phonetics = history.phonetics),
                            onClickToListen = onClickToListen,
                            onClickToGTranslate = onClickToGTranslate
                        )
                    }
                }
            }

            if (historyList.isEmpty() && isSearchEmpty && !GlobalState.isKBBIPocket) {
                item {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "You haven't searched any words yet",
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                fontFamily = montserrat,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                }
            }

            if (!GlobalState.isKBBIPocket && definitionsState.isLoading && definitionsState.definition.isNullOrEmpty()) {
                item {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        DefinitionsLoadingComponent(
                            isLoading = definitionsState.isLoading
                        )

                        DefinitionsEmptyComponent(
                            isLoading = definitionsState.isLoading,
                            definition = definitionsState.definition,
                        )
                    }
                }
            }

            if (GlobalState.isKBBIPocket && meaningsKbbiState.isLoading) {
                item {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MeaningsKbbiLoadingComponent(
                            isLoading = meaningsKbbiState.isLoading
                        )

                        MeaningsKbbiEmptyComponent(
                            isLoading = meaningsKbbiState.isLoading,
                            meanings = meaningsKbbiState.meanings,
                        )
                    }
                }
            }

            if (GlobalState.isKBBIPocket && !meaningsKbbiState.isLoading && meaningsKbbiState.meanings != null) {
                item {
                    MeaningKbbiItem(
                        kbbi = meaningsKbbiState.meanings
                    )
                }
            }

            if (!GlobalState.isKBBIPocket && !definitionsState.isLoading && !definitionsState.definition.isNullOrEmpty() && !isSearchEmpty) {
                item {
                    Spacer(
                        modifier = Modifier.height(15.dp)
                    )

                    val phonetic = getNonNullPhonetic(definitionsState.definition[0].phonetics)

                    PronunciationItem(
                        word = definitionsState.definition[0].word ?: "",
                        phonetic = phonetic,
                        onClickToListen = onClickToListen,
                        onClickToAddBookmark = onClickToAddBookmark,
                        onClickToGTranslate = onClickToGTranslate,
                        isBookmarkScreen = false
                    )
                }

                items(meanings) { meaning ->
                    Spacer(
                        modifier = modifier.height(10.dp)
                    )

                    PartsOfSpeechDefinitionsItem(
                        partsOfSpeech = meaning.partOfSpeech ?: "",
                        definitions = meaning.definitions ?: emptyList()
                    )
                }
            }
        }
    }
}

@Composable
fun getNonNullPhonetic(
    phonetics: List<Phonetic>?,
    index: Int = 0
): String {
    return if (phonetics != null && index < phonetics.size) {
        val currentPhonetic = phonetics[index].text
        if (currentPhonetic.isNullOrEmpty()) {
            getNonNullPhonetic(phonetics, index + 1)
        } else {
            currentPhonetic
        }
    } else {
        ""
    }
}

