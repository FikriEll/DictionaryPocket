package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fikrielg.dictionarypocket.data.source.local.entities.History

@Composable
fun HistoryComponent(
    history: History,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .padding(10.dp)
            .background(color = Color(0xFFEBE7E7))
            .clip(shape = RoundedCornerShape(5.dp))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PronunciationComponent(word = history.word!!, phonetic = history.phonetic!!)
        IconButton(onClick = { onClick() }) {
           Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


