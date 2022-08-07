package com.plcoding.jwtauthktorandroid.auth

import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthAPI,
    private val prefs: SharedPreferences
): AuthRepository {

    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        return try {
            api.signUp(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            signIn(username, password)
        } catch(e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.HttpError()
            }
        } catch(e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.signIn(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .putString("userId", response.userId)
                .apply()
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.HttpError()
            }
        } catch(e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.HttpError()
            }
        } catch(e: Exception) {
            AuthResult.UnknownError()
        }
    }
}