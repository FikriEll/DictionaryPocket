package com.fikrielg.dictionarypocket.presentation.screen.profile

import StackedSnackbarDuration
import StackedSnackbarHost
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketAppBar
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketCustomDialog
import com.fikrielg.dictionarypocket.presentation.component.DictionaryPocketUserAvatar
import com.fikrielg.dictionarypocket.presentation.component.DialogMessage
import com.fikrielg.dictionarypocket.presentation.screen.destinations.HomeScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.ProfileScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.SignInScreenDestination
import com.fikrielg.dictionarypocket.ui.theme.montserrat
import com.fikrielg.dictionarypocket.util.GlobalState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.rmaprojects.apirequeststate.ResponseState
import rememberStackedSnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {

    val editUsernameState = viewModel.editUsernameState.collectAsStateWithLifecycle()
    val signOutState = viewModel.signOutState.collectAsStateWithLifecycle()
    var username by remember { mutableStateOf(AuthPref.username) }
    var isEditEnabled by remember { mutableStateOf(false) }

    var showAlertDialog by remember {
        mutableStateOf(false)
    }

    val stackedSnackbarHostState = rememberStackedSnackbarHostState()

    val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(editUsernameState.value) {
        when (val state = editUsernameState.value) {
            is ResponseState.Loading -> {
            }

            is ResponseState.Success -> {
                stackedSnackbarHostState.showSuccessSnackbar("Successful Editing Username")
            }

            is ResponseState.Error -> {
                stackedSnackbarHostState.showErrorSnackbar(
                    title = "Oops.. there was an error",
                    description = state.message,
                    duration = StackedSnackbarDuration.Long
                )
            }

            else -> Unit
        }
    }

    LaunchedEffect(signOutState.value) {
        when (val state = editUsernameState.value) {
            is ResponseState.Loading -> {}

            is ResponseState.Success -> {
                stackedSnackbarHostState.showSuccessSnackbar("Logged Out Successfully")
            }

            is ResponseState.Error -> {
                stackedSnackbarHostState.showErrorSnackbar(
                    title = "Oops.. there was an error",
                    description = state.message,
                    duration = StackedSnackbarDuration.Long
                )
            }

            else -> Unit
        }
    }



    Scaffold(
        snackbarHost = { StackedSnackbarHost(hostState = stackedSnackbarHostState) },
        topBar = {
            DictionaryPocketAppBar(
                currentDestinationTitle = "Profile",
                navigateUp = { navigator.popBackStack() },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            val focusManager = LocalFocusManager.current

            DictionaryPocketUserAvatar(fullName = AuthPref.username, size = 100.dp)

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { newUsername ->
                    username = newUsername
                    isEditEnabled = username != AuthPref.username
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "UsernameTextField" },
                placeholder = {
                    Text(text = username, fontFamily = montserrat)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = Color(0xFFEBE7E7),
                    focusedIndicatorColor = if (!GlobalState.isKBBIPocket) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                ),
                shape = RoundedCornerShape(30.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(modifier = modifier
                .fillMaxWidth(),
                enabled = isEditEnabled,
                onClick = {
                    localSoftwareKeyboardController?.hide()
                    viewModel.onEditUsername(username)
                }) {
                Text("Change Username")
            }

            Spacer(modifier = Modifier.height(6.dp))


            Box(
                modifier = modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                Color(0xFF922121),
                            )
                        )
                    )
                    .clickable(onClick = {
                        showAlertDialog = true
                    })
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "", tint = MaterialTheme.colorScheme.surface)
                    Spacer(modifier = modifier.width(6.dp))
                    Text("Sign Out", color = MaterialTheme.colorScheme.surface)
                }
            }


            DictionaryPocketCustomDialog(
                showDialog = showAlertDialog,
                onDismissRequest = { showAlertDialog = false }
            ) {
                DialogMessage(
                    onDismissRequest = { showAlertDialog = false },
                    title = "Sign Out",
                    message = "Are you sure you want to sign up for this account?",
                    dismissText = "Cancel",
                    confirmText = "Confirm",
                    icon = Icons.Default.Logout,
                    onConfirmRequest = {
                        showAlertDialog = false
                        viewModel.onLogout()
                    },
                )
            }
        }
    }
}