package com.fikrielg.dictionarypocket.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikrielg.dictionarypocket.ui.theme.montserrat
import com.fikrielg.dictionarypocket.util.GlobalState

@Composable
fun DictionaryPocketUserAvatar(
    fullName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    onClickToProfile: () -> Unit = {}
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    brush = if (!GlobalState.isKBBIPocket) Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            Color(0xFF1E6296),
                        )
                    ) else Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            Color(0xFFE08F8F),
                        )
                    )
                )
                .clickable {
                    onClickToProfile()
                },
            contentAlignment = Alignment.Center
        ) {
            val initials = remember(fullName) {
                val names = fullName.split(" ")
                if (names.size >= 2) {
                    names.joinToString(separator = "") { it.first().toString() }.uppercase()
                } else {
                    fullName.take(1).uppercase()
                }
            }
            val fontSize = size.value / 2.5
            Text(
                text = initials,
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = with(LocalDensity.current) { fontSize.sp }),
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
@Preview
fun DictionaryPocketUserAvatarPreview() {
    DictionaryPocketUserAvatar(fullName = "Fikri el")
}