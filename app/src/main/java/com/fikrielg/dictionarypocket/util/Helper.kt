package com.fikrielg.dictionarypocket.util

import android.util.Patterns

fun String.emailChecked() = !Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.capitalizeFirstLetter(): String {
    return this?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: ""
}



