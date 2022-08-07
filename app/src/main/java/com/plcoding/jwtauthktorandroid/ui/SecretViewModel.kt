package com.plcoding.jwtauthktorandroid.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.jwtauthktorandroid.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecretViewModel @Inject constructor(
    private val prefs: SharedPreferences
): ViewModel() {

    private val resultChannel = Channel<String>()
    val secretResults = resultChannel.receiveAsFlow()
    val userId = prefs.getString("userId", "ERROR")

    fun onEvent(event: SecretUiEvent) {
        when(event) {
            is SecretUiEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            prefs.edit()
                .remove("jwt")
                .apply()
            resultChannel.send("signOut")
        }
    }
}