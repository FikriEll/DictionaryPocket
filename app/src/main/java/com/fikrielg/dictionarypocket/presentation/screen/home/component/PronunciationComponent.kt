package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikrielg.dictionarypocket.ui.theme.SkyBlue
import com.fikrielg.dictionarypocket.ui.theme.montserrat


@Composable
fun PronunciationComponent(
    word: String,
    phonetic: String
) {
    Column {
        Text(
            text = word,
            fontSize = 24.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = phonetic,
                fontSize = 16.sp,
                fontFamily = montserrat,
                color = SkyBlue,
                fontWeight = FontWeight.Bold
            )
        }
    }
}