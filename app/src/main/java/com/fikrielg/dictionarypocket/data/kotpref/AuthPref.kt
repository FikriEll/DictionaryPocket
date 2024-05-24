package com.fikrielg.dictionarypocket.data.kotpref

import com.chibatching.kotpref.KotprefModel

object AuthPref : KotprefModel() {
    var id by stringPref("")
    var username by stringPref("")
    var isSignInDone by booleanPref(false)
}