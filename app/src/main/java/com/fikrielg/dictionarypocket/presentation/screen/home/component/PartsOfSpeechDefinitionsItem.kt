package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
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
import com.fikrielg.dictionarypocket.data.source.remote.model.Definition
import com.fikrielg.dictionarypocket.ui.theme.montserrat


@Composable
fun PartsOfSpeechDefinitionsItem(
    modifier: Modifier = Modifier,
    partsOfSpeech: String,
    definitions: List<Definition>?
) {
    Column(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        PartsOfSpeechItem(
            headerText = partsOfSpeech,
            size = definitions?.size ?: 0,
            color = MaterialTheme.colorScheme.primary
        )

        definitions?.forEachIndexed { index, meaning ->
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
                        append(meaning.definition ?: "")
                    }
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .semantics { contentDescription = "definition" }
            )
        }
    }
}