package com.fikrielg.dictionarypocket.presentation.screen.auth

import StackedSnackbarDuration
import StackedSnackbarHost
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fikrielg.dictionarypocket.R
import com.fikrielg.dictionarypocket.presentation.screen.auth.component.AuthTextFieldComponent
import com.fikrielg.dictionarypocket.presentation.screen.destinations.HomeScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.SignInScreenDestination
import com.fikrielg.dictionarypocket.presentation.screen.destinations.SignUpScreenDestination
import com.fikrielg.dictionarypocket.util.emailChecked
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rmaprojects.apirequeststate.ResponseState
import rememberStackedSnackbarHostState

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val signUpState = viewModel.state.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    var isEmailError by remember { mutableStateOf(false) }

    val stackedSnackbarHostState = rememberStackedSnackbarHostState(
        animation = StackedSnackbarAnimation.Slide
    )

    LaunchedEffect(signUpState.value) {
        when (val state = signUpState.value) {
            is ResponseState.Loading -> {

            }

            is ResponseState.Success -> {
                stackedSnackbarHostState.showSuccessSnackbar(
                    title = "Sign-in Successful!",
                    duration = StackedSnackbarDuration.Short
                )
                navigator.navigate(HomeScreenDestination)
            }

            is ResponseState.Error -> {
                stackedSnackbarHostState.showErrorSnackbar(
                    title = "Sign-in Error!",
                    description = state.message,
                    duration = StackedSnackbarDuration.Long
                )
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { StackedSnackbarHost(hostState = stackedSnackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {

            val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current

            Image(painter = painterResource(id = R.drawable.sign_up_rafiki), contentDescription = null)

            Text(
                text = "Let's create an account",
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                modifier = Modifier.clickable {
                    navigator.navigate(SignUpScreenDestination)
                }
            )

            Spacer(modifier = modifier.height(20.dp))

            AuthTextFieldComponent(
                onValueChange = { username = it },
                value = username,
                placeholder = "Username",
                leadingIcon = Icons.Default.Person,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            )

            AuthTextFieldComponent(
                onValueChange = {
                    email = it
                    isEmailError = it.emailChecked()
                },
                value = email,
                placeholder = "Email",
                leadingIcon = Icons.Default.Email,
                isError = isEmailError,
                supportingText = if (isEmailError) "Email is not valid" else "",
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )

            AuthTextFieldComponent(
                onValueChange = { password = it },
                value = password,
                placeholder = "Password",
                leadingIcon = Icons.Default.Password,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide_password",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        localSoftwareKeyboardController?.hide()
                        viewModel.onSignUp(username, email, password)
                    }
                )
            )



            Button(modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
                onClick = {
                    localSoftwareKeyboardController?.hide()
                    viewModel.onSignUp(username, email, password)
                }) {
                Text("Sign up", color = MaterialTheme.colorScheme.surface)
            }

            Spacer(modifier = modifier.height(10.dp))

            Row {
                Text(
                    text = "Have an account?",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                   fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        navigator.navigate(SignInScreenDestination)
                    }
                )
            }
        }
    }
}

