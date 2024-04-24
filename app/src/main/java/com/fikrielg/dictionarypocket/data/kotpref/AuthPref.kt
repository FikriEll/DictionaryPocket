package com.fikrielg.dictionarypocket.data.kotpref

import com.chibatching.kotpref.KotprefModel

object AuthPref : KotprefModel() {
    var username by stringPref("")
}