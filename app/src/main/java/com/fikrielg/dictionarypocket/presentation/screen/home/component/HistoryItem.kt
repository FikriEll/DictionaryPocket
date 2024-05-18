package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fikrielg.dictionarypocket.data.source.local.entities.History

@Composable
fun HistoryItem(
    history: History,
    onClickToListen: (String) -> Unit,
    onClickToGTranslate: (String) -> Unit,
    phonetic: String?,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .animateContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PronunciationItem(
                word = history.word ?: "",
                phonetic = phonetic ?: "",
                onClickToListen = onClickToListen,
                onClickToAddBookmark = {},
                onClickToGTranslate = onClickToGTranslate
            )
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .animateContentSize()
            ) {
                history.meanings?.forEachIndexed { index, meaning ->
                    Spacer(modifier = Modifier.height(10.dp))
                    PartsOfSpeechDefinitionsItem(
                        partsOfSpeech = meaning.partOfSpeech ?: "",
                        definitions = meaning.definitions ?: emptyList()
                    )
                }
            }
        }
    }
}

