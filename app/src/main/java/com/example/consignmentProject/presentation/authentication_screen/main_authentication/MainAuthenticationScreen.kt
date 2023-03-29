package com.example.consignmentProject.presentation.authentication_screen.main_authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.consignmentProject.R
import com.example.consignmentProject.presentation.authentication_screen.AuthenticationViewModel
import com.example.consignmentProject.presentation.destinations.LoginScreenDestination
import com.example.consignmentProject.presentation.destinations.RegisterScreenDestination

import com.example.consignmentProject.ui.theme.LightBlue300
import com.example.consignmentProject.ui.theme.LightBlue800
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(route = "/MainAuthenticationScreen")
@Composable
fun MainAuthenticationScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthenticationViewModel = hiltViewModel(),
    auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(if (isSystemInDarkTheme()) R.drawable.splash_night else R.drawable.splash_light),
            contentDescription = "main background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(alignment = TopCenter)
                .size(200.dp)
                .padding(top = 50.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = "Sadra Document",
                modifier = Modifier.align(CenterHorizontally),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navigator.navigate(LoginScreenDestination())
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    shape = CircleShape,

                    ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        LightBlue800,
                                        LightBlue300
                                    )
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Login",
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navigator.navigate(RegisterScreenDestination())
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    shape = CircleShape,

                    ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        LightBlue800,
                                        LightBlue300
                                    )
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Register",
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
        }
    }
}

