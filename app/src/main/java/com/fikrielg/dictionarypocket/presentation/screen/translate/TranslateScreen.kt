package com.fikrielg.dictionarypocket.presentation.screen.translate

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketAppBar
import com.fikrielg.dictionarypocket.util.capitalizeFirstLetter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("SetJavaScriptEnabled")
@Destination
@Composable
fun TranslateScreen(
    word:String,
    navigator: DestinationsNavigator
) {
  Scaffold(
      topBar = {
          DictionaryPocketAppBar(currentDestinationTitle = "Translation of ${word.capitalizeFirstLetter()}", navigateUp = { navigator.popBackStack() })
      }
  ) { paddingValues ->
      AndroidView(
          modifier = Modifier.padding(paddingValues),
          factory = { context ->
              return@AndroidView WebView(context).apply {
                  settings.javaScriptEnabled = true
                  webViewClient = WebViewClient()

                  settings.loadWithOverviewMode = true
                  settings.useWideViewPort = true
                  settings.setSupportZoom(false)
              }
          },
          update = {
              it.loadUrl("https://translate.google.co.id/?hl=id&sl=en&tl=id&text=${word}%0A&op=translate")
          }
      )
  }
}