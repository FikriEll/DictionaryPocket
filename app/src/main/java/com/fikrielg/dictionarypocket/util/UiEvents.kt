package com.fikrielg.dictionarypocket.util

sealed class UiEvents {
    data class ShowSnackbar(val message: String) : UiEvents()
}