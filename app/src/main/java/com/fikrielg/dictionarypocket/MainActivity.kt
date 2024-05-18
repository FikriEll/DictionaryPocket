package com.fikrielg.dictionarypocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.presentation.screen.NavGraphs
import com.fikrielg.dictionarypocket.presentation.screen.destinations.HomeScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.SignInScreenDestination
import com.fikrielg.dictionarypocket.ui.theme.DictionaryPocketTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryPocketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root, startRoute = if(! AuthPref.isLogin) SignInScreenDestination else HomeScreenDestination)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DictionaryPocketTheme {
        Greeting("Android")
    }
}