package com.example.consignmentProject.presentation.authentication_screen.login_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.consignmentProject.presentation.authentication_screen.AuthenticationViewModel
import com.example.consignmentProject.presentation.destinations.ConsignmentScreenDestination

import com.example.consignmentProject.presentation.export_consignments.component.ButtonGradiant
import com.example.consignmentProject.ui.theme.LightBlue300
import com.example.consignmentProject.ui.theme.LightBlue800
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(route = "/LoginScreen")
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true) {
        viewModel.loginEventFlow.collectLatest { event ->
            when (event) {
                is AuthenticationViewModel.AuthenticationUi.NavigateToMainScreen -> {
                    navigator.navigate(ConsignmentScreenDestination()) {
                        popUpTo("/LoginScreen") { inclusive = true }
                        popUpTo("/MainAuthenticationScreen") { inclusive = true }
                    }
                }
                is AuthenticationViewModel.AuthenticationUi.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    backgroundColor = MaterialTheme.colors.primary,
                    snackbarData = data
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {},
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to main panel",
                        Modifier
                            .clickable {
                                navigator.popBackStack()
                            }
                    )
                },
                contentColor = MaterialTheme.colors.primary,
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.loginLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .align(Start),
                    text = "Login",
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Enter your information to continue",
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    modifier = Modifier,
                    shape = CircleShape,
                    label = {
                        Text(text = "Email")
                    },
                    placeholder = {
                        Text(text = "Enter email")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "enter email")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    modifier = Modifier,
                    shape = CircleShape,
                    label = {
                        Text(text = "Password")
                    },
                    placeholder = {
                        Text(text = "Enter password")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "enter password"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                ButtonGradiant(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = "Login",
                    textColor = MaterialTheme.colors.onPrimary,
                    gradiant = Brush.horizontalGradient(
                        colors = listOf(
                            LightBlue800,
                            LightBlue300
                        )
                    ),
                    onCLick = {
                        viewModel.loginUser(password = password, email = email)
                    }
                )
            }
        }
    }
}