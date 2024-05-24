package com.fikrielg.dictionarypocket.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fikrielg.dictionarypocket.ui.theme.montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryPocketAppBar(
    currentDestinationTitle: String,
    navigateUp: () -> Unit,
    isHomeScreen: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = currentDestinationTitle,
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            if (!isHomeScreen) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                leadingIcon?.invoke()
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),

        actions = { actions() },

        )
}