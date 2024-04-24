package com.fikrielg.dictionarypocket.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fikrielg.dictionarypocket.data.source.remote.Meaning
import com.fikrielg.dictionarypocket.presentation.screen.home.component.EmptyComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.LoadingComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.PartsOfSpeechDefinitionsComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.PronunciationComponent
import com.fikrielg.dictionarypocket.presentation.screen.home.component.SearchTextFieldComponent
import com.fikrielg.dictionarypocket.ui.theme.SkyBlue
import com.fikrielg.dictionarypocket.ui.theme.montserrat
import com.fikrielg.dictionarypocket.util.UiEvents
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowSnackbar -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }

                else -> {}
            }
        }
    }

    val homeUiState = viewModel.homeUiState.collectAsState().value

    val typedWord = viewModel.typedWord.value

    val definitions = if (homeUiState.definition?.isNotEmpty() == true) homeUiState.definition[0].meanings
            ?: emptyList()
        else emptyList()


        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Dictionary",
                            fontSize = 16.sp,
                            fontFamily = montserrat,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = SkyBlue
                    )
                )
            }
        ) { paddingValues ->

            HomeContent(
                homeUiState = homeUiState,

                typedWord = typedWord,

                setWordToBeSearched = { word ->
                    viewModel.setTypedWord(typedWord = word)
                },

                searchWord = {
                    viewModel.getDefinition()
                },

                meanings = definitions,

                paddingValues = paddingValues
            )
        }

}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    typedWord: String,
    setWordToBeSearched: (String) -> Unit,
    searchWord: () -> Unit,
    meanings: List<Meaning>,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        LazyColumn(contentPadding = PaddingValues(14.dp)) {
            item {
                SearchTextFieldComponent(
                    typedWord = typedWord,
                    setWordToBeSearched = setWordToBeSearched,
                    searchWord = searchWord
                )
            }

            if (homeUiState.isLoading || homeUiState.definition?.isEmpty() == true) {
                item {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingComponent(
                            isLoading = homeUiState.isLoading
                        )

                        EmptyComponent(
                            isLoading = homeUiState.isLoading,
                            definition = homeUiState.definition
                        )
                    }
                }
            }

            if (!homeUiState.isLoading && !homeUiState.definition.isNullOrEmpty()) {
                item {
                    Spacer(
                        modifier = Modifier.height(15.dp)
                    )

                    val phonetic = if ((homeUiState.definition[0].phonetics?.size ?: 0) >= 3) {
                        homeUiState.definition[0].phonetics?.get(2)?.text ?: ""
                    } else {
                        homeUiState.definition[0].phonetic ?: ""
                    }

                    PronunciationComponent(
                        word = homeUiState.definition[0].word ?: "",
                        phonetic = phonetic
                    )
                }

                items(meanings) { meaning ->
                    Spacer(
                        modifier = modifier.height(10.dp)
                    )

                    PartsOfSpeechDefinitionsComponent(
                        partsOfSpeech = meaning.partOfSpeech ?: "",
                        definitions = meaning.definitions ?: emptyList()
                    )
                }
            }
        }
    }
}