package com.fikrielg.dictionarypocket.presentation.screen.onboarding


import androidx.annotation.DrawableRes
import com.fikrielg.dictionarypocket.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.hello_rafiki,
        title = "Welcome to Dictionary Pocket",
        description = "Looking up the definition of a word has never been easier like this!"
    )

    object Second : OnBoardingPage(
        image = R.drawable.simpleui,
        title = "Simple.",
        description = "Makes it simple and practical to look up the definition of a word."
    )

    object Third : OnBoardingPage(
        image = R.drawable.switchkbbi,
        title = "KBBI Unleashed",
        description = "Now you can switch and look up the meaning of words from the dictionaries and KBBI easily."
    )
}