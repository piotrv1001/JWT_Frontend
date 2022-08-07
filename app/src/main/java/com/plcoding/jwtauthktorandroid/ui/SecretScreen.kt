package com.plcoding.jwtauthktorandroid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.plcoding.jwtauthktorandroid.ui.destinations.AuthScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect

@Composable
@Destination
fun SecretScreen(
    navigator: DestinationsNavigator,
    viewModel: SecretViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.secretResults.collect { result ->
            when(result) {
                "signOut" -> {
                    navigator.navigate(AuthScreenDestination)
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "You're authenticated!")
        Text(text = "Your ID is ${viewModel.userId}")
        Button(onClick = {
            viewModel.onEvent(SecretUiEvent.SignOut)
        }) {
            Text(text = "Log out")
        }
    }
}