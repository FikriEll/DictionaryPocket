package com.fikrielg.dictionarypocket.util

sealed class UiEvents {
    data class ShowSuccesSnackbar(val message: String) : UiEvents()
    data class ShowErrorSnackbar(val message: String) : UiEvents()
    data class ShowInfoSnackbar(val message: String) : UiEvents()
}