package com.fikrielg.dictionarypocket.presentation.screen.auth.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fikrielg.dictionarypocket.ui.theme.SkyBlue
import com.fikrielg.dictionarypocket.ui.theme.montserrat

@Composable
fun AuthTextFieldComponent(
    onValueChange: (String) -> Unit,
    value: String,
    placeholder: String,
    leadingIcon: ImageVector,
    isError: Boolean = false,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    supportingText: String = "",
    trailingIcon: @Composable () -> Unit = {},
) {

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "AuthTextField" },
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = montserrat,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            trailingIcon()
        },
        shape = RoundedCornerShape(30.dp),
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        isError = isError,
        supportingText = { Text(text = supportingText) }
    )
}