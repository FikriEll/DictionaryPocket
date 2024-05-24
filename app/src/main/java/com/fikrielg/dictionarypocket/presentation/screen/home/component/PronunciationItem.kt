package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikrielg.dictionarypocket.ui.theme.montserrat


@Composable
fun PronunciationItem(
    word: String,
    phonetic: String,
    onClickToListen: (String) -> Unit,
    onClickToAddBookmark: (String) -> Unit,
    onClickToGTranslate: (String) -> Unit,
    isBookmarkScreen: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        Column {
            Text(
                text = word,
                fontSize = 24.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = phonetic,
                fontSize = 16.sp,
                fontFamily = montserrat,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = modifier.width(10.dp))
        IconButton(onClick = { onClickToListen(word) }) {
            Icon(
                imageVector = Icons.Default.VolumeUp, contentDescription = "Listen",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = modifier.width(6.dp))
        IconButton(onClick = { onClickToGTranslate(word) }) {
            Icon(
                imageVector = Icons.Default.GTranslate,
                contentDescription = "Translate with GTranslate",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = modifier.width(6.dp))

        if (!isBookmarkScreen) {
            IconButton(onClick = { onClickToAddBookmark(word) }) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Add Bookmark",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

