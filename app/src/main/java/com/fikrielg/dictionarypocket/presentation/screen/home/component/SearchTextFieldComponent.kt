package com.fikrielg.dictionarypocket.presentation.screen.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fikrielg.dictionarypocket.ui.theme.montserrat
import com.fikrielg.dictionarypocket.util.GlobalState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldComponent(
    setWordToBeSearched: (String) -> Unit,
    searchWord: () -> Unit,
    typedWord: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = typedWord,
        onValueChange = { wordEntered ->
            setWordToBeSearched(wordEntered)
        },
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "SearchTextField" },
        placeholder = {
            Text(text = if(!GlobalState.isKBBIPocket) "Let's find your word" else "Mari cari kata kamu", fontFamily = montserrat, color = MaterialTheme.colorScheme.onSurface)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            if (typedWord.isNotEmpty()) {
                IconButton(onClick = {
                    setWordToBeSearched("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = Color(0xFFEBE7E7),
            focusedIndicatorColor = if(!GlobalState.isKBBIPocket) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        ),
        shape = RoundedCornerShape(30.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                searchWord()
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )
}