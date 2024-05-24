package com.fikrielg.dictionarypocket.presentation.screen.detail

import StackedSnackbarAnimation
import StackedSnackbarHost
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketAppBar
import com.fikrielg.dictionarypocket.presentation.screen.destinations.TranslateScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.home.HomeViewModel
import com.fikrielg.dictionarypocket.presentation.screen.home.component.DefinitionsEmptyComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.DefinitionsLoadingComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.PartsOfSpeechDefinitionsItem
import com.fikrielg.dictionarypocket.presentation.screen.home.component.PronunciationItem
import com.fikrielg.dictionarypocket.presentation.screen.home.getNonNullPhonetic
import com.fikrielg.dictionarypocket.util.UiEvents
import com.fikrielg.dictionarypocket.util.capitalizeFirstLetter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rememberStackedSnackbarHostState

@Composable
@Destination
fun DetailScreen(
    word: String,
    viewModel: DetailViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = true) {
        viewModel.getDefinition(word)
    }

    val scope = rememberCoroutineScope()
    val stackedSnackbarHostState = rememberStackedSnackbarHostState(
        animation = StackedSnackbarAnimation.Slide
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowErrorSnackbar -> {
                    scope.launch { stackedSnackbarHostState.showInfoSnackbar(event.message) }
                }

                else -> {}
            }
        }
    }

    val detailState = viewModel.detailState.collectAsState().value
    val context = LocalContext.current
    val definitions =
        if (detailState.definition?.isNotEmpty() == true) detailState.definition[0].meanings
            ?: emptyList()
        else emptyList()

    Scaffold(
        topBar = {
            DictionaryPocketAppBar(
                currentDestinationTitle = "${word.capitalizeFirstLetter()}'s Definition",
                navigateUp = { navigator.popBackStack() })
        },
        snackbarHost = { StackedSnackbarHost(hostState = stackedSnackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
        ) {

            LazyColumn(contentPadding = PaddingValues(bottom = 14.dp, start = 10.dp, end = 10.dp)) {
                if (detailState.isLoading && detailState.definition.isNullOrEmpty()) {
                    item {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            DefinitionsLoadingComponent(
                                isLoading = detailState.isLoading
                            )

                            DefinitionsEmptyComponent(
                                isLoading = detailState.isLoading,
                                definition = detailState.definition
                            )
                        }
                    }
                }
                if (!detailState.isLoading && !detailState.definition.isNullOrEmpty()) {
                    item {
                        Spacer(
                            modifier = Modifier.height(15.dp)
                        )

                        val phonetic = getNonNullPhonetic(detailState.definition[0].phonetics)

                        PronunciationItem(
                            word = detailState.definition[0].word ?: "",
                            phonetic = phonetic,
                            onClickToListen = {
                                homeViewModel.textToSpeech(context, word)
                            },
                            onClickToAddBookmark = {},
                            onClickToGTranslate = { word ->
                                navigator.navigate(TranslateScreenDestination(word))
                            }
                        )
                    }
                    items(definitions) { meaning ->
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
}