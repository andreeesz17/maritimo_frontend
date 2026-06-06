package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.CapitanDto
import com.maritimo.control.data.remote.dto.CapitanResponse
import retrofit2.Response
import retrofit2.http.*

interface CapitanApi {
    @GET("api/capitanes/")
    suspend fun getCapitanes(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("search") search: String? = null
    ): Response<CapitanResponse>

    @GET("api/capitanes/{id}/")
    suspend fun getCapitanById(@Path("id") id: Int): Response<CapitanDto>

    @POST("api/capitanes/")
    suspend fun createCapitan(@Body capitan: CapitanDto): Response<CapitanDto>

    @PUT("api/capitanes/{id}/")
    suspend fun updateCapitan(@Path("id") id: Int, @Body capitan: CapitanDto): Response<CapitanDto>

    @DELETE("api/capitanes/{id}/")
    suspend fun deleteCapitan(@Path("id") id: Int): Response<Unit>
}
