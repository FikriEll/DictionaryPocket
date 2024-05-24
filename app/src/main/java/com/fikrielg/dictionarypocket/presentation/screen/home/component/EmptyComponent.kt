package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fikrielg.dictionarypocket.R
import com.fikrielg.dictionarypocket.data.source.remote.model.DictionaryResponse
import com.fikrielg.dictionarypocket.data.source.remote.model.KbbiResponse
import com.fikrielg.dictionarypocket.ui.theme.montserrat


@Composable
fun DefinitionsEmptyComponent(
    isLoading: Boolean,
    definition: List<DictionaryResponse>? = null,
) {

    if (!isLoading && definition.isNullOrEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmptyAnimation()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sorry the definition wasn't found...",
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun MeaningsKbbiEmptyComponent(
    isLoading: Boolean,
    meanings: KbbiResponse? = null,
) {

    if (!isLoading && meanings == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmptyAnimation()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Arti tidak ditemukan...",
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun EmptyAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))

    LottieAnimation(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .semantics { contentDescription = "Empty Animation" },
        iterations = LottieConstants.IterateForever,
        composition = composition,
    )
}