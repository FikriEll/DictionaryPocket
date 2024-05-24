package com.fikrielg.dictionarypocket.data.kotpref

import com.chibatching.kotpref.KotprefModel

object DictionaryPocketPref : KotprefModel() {
    var isOnBoardingDone by booleanPref(false)
    var isKBBIPocketMode by booleanPref(false)
}