package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.LoginRequest
import com.maritimo.control.data.remote.dto.LoginResponse
import com.maritimo.control.data.remote.dto.LogoutRequest
import com.maritimo.control.data.remote.dto.RegisterRequest
import com.maritimo.control.data.remote.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/logout/")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>

    @POST("api/auth/token/refresh/")
    suspend fun refreshToken(@Body request: Map<String, String>): Response<LoginResponse>
}
