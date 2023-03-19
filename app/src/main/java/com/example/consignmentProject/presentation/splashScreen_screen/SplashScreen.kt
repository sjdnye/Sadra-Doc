package com.example.consignmentProject.presentation.splashScreen_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.consignmentProject.R
import com.example.consignmentProject.presentation.destinations.ConsignmentScreenDestination
import com.example.consignmentProject.presentation.destinations.MainAuthenticationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true, route = "/SplashScreen")
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {

    if (viewModel.state == 1) {
        navigator.navigate(MainAuthenticationScreenDestination()) {
            popUpTo("/SplashScreen") { inclusive = true }
        }
    } else if (viewModel.state == 2) {
        navigator.navigate(ConsignmentScreenDestination()){
            popUpTo("/SplashScreen") { inclusive = true }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(
            painter = painterResource(id = R.drawable.itrc_edit),
            contentDescription = "splash screen image",
            modifier = Modifier.size(200.dp)
        )
    }

}