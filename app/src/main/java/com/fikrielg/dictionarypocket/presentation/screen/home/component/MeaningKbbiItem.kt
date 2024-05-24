package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikrielg.dictionarypocket.data.source.remote.model.KbbiResponse
import com.fikrielg.dictionarypocket.ui.theme.montserrat


@Composable
fun MeaningKbbiItem(
    modifier: Modifier = Modifier,
    kbbi: KbbiResponse?
) {
    Column(modifier = modifier.padding(10.dp)) {
        Column {
            Text(
                text = kbbi?.lema?.replace(".","")?.replace("\\d".toRegex(), "") ?: "",
                fontSize = 24.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = kbbi?.lema?.replace("\\d".toRegex(), "") ?: "",
                fontSize = 16.sp,
                fontFamily = montserrat,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        kbbi?.arti?.forEachIndexed { index, meaning ->
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("${index + 1}. ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append(meaning)
                    }
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .semantics { contentDescription = "definition" }
            )
        }
    }
}