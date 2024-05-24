package com.fikrielg.dictionarypocket.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.data.kotpref.DictionaryPocketPref

object GlobalState {
    var isOnBoardingDone by mutableStateOf(DictionaryPocketPref.isOnBoardingDone)
    var isKBBIPocket by mutableStateOf(DictionaryPocketPref.isKBBIPocketMode)
    var isSignInDone by mutableStateOf(AuthPref.isSignInDone)
}