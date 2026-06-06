package com.maritimo.control.domain.repository

import com.maritimo.control.domain.model.LoggedUser
import com.maritimo.control.data.local.TokenDataStore

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoggedUser>
    suspend fun register(username: String, email: String, password: String, password2: String): Result<LoggedUser>
    suspend fun logout(): Result<Unit>
    suspend fun getStoredUser(): TokenDataStore.UserSnapshot?
    suspend fun isLoggedIn(): Boolean
}
