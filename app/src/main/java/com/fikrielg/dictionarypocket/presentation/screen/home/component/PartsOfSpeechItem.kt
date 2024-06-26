package com.fikrielg.dictionarypocket.presentation.screen.home.component


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikrielg.dictionarypocket.ui.theme.montserrat


@Composable
fun PartsOfSpeechItem(
    headerText: String,
    size: Int,
    color: Color
) {
    Button(
        onClick = {},
        elevation = ButtonDefaults.buttonElevation(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.semantics { contentDescription = "PartsOfSpeech" }
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = montserrat,
                        color = MaterialTheme.colorScheme.background
                    )
                ) {
                    append("$headerText ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserrat,
                        color = MaterialTheme.colorScheme.background
                    )
                ) {
                    append("($size)")
                }
            }
        )
    }
}